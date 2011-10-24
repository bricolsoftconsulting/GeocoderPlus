/*
Copyright 2011 Bricolsoft Consulting

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.bricolsoftconsulting.geocoderplus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bricolsoftconsulting.geocoderplus.util.http.HttpRetriever;

public class Geocoder
{
	// Constants
	public static final String URL_MAPS_GEOCODE = "http://maps.googleapis.com/maps/api/geocode/json";
	public static final String PARAM_SENSOR = "sensor";
	public static final String PARAM_ADDRESS = "address";
	public static final String PARAM_LANGUAGE = "language";
	public static final String PARAM_REGION = "region";
	public static final String QUERY_LIMIT_ERROR = "You exceeded the number of queries that the Google Maps API allows in a 24 hour period!";
	public static final String REQUEST_DENIED_ERROR = "The HTTP request was denied!";
	public static final String INVALID_REQUEST_ERROR = "The HTTP request was invalid!";
	public static final String PARAMETER_ERROR = "Location name cannot be null!";
	public static final String HTTP_ERROR = "Failed to retrieve JSON data over HTTP!";
	public static final String JSON_PARSE_ERROR = "Failed to parse JSON data!";
	
	// Members
	Locale mLocale;
	boolean mUseRegionBias = false;
	
	// Constructors
	public Geocoder()
	{
		mLocale = Locale.getDefault();
	}
	
	public Geocoder(Locale locale)
	{
		mLocale = locale;
	}
	
	// Getters and setters
	public Locale getLocale()
	{
		return mLocale;
	}

	public void setLocale(Locale locale)
	{
		this.mLocale = locale;
	}
	
	public boolean getUseRegionBias()
	{
		return mUseRegionBias;
	}

	public void setUseRegionBias(boolean useRegionBias)
	{
		this.mUseRegionBias = useRegionBias;
	}
	
	// Functions
	public List<Address> getFromLocationName(String locationName) throws IllegalArgumentException, IOException
	{
		return getFromLocationName(locationName, -1);
	}
	
	public List<Address> getFromLocationName (String locationName, int maxResults) throws IllegalArgumentException, IOException
	{
		// Declare
		List<Address> results = null;
		String jsonString = null;
		
		// Validate parameters
		if (locationName == null)
		{
			throw new IllegalArgumentException(PARAMETER_ERROR);
		}
		
		// Get the geocoding URL
		String url = getGeocodingUrl(locationName);
		
		// Get the URL content
		HttpRetriever retriever = new HttpRetriever();
		try
		{
			jsonString = retriever.retrieve(url);
		}
		catch (IOException e)
		{
			throw new IOException(HTTP_ERROR);
		}
		
		// Get the addresses
		results = getAddressesFromJSON(jsonString, maxResults);
		
		// Return
		return results;
	}
	
	private String getGeocodingUrl(String locationName)
	{
		// Declare
		String url;
		Vector<BasicNameValuePair> params;

		// Extract language from locale
		String language = mLocale.getLanguage();
		
		// Create params
		params = new Vector<BasicNameValuePair>();
		params.add(new BasicNameValuePair(PARAM_SENSOR, "true"));
		params.add(new BasicNameValuePair(PARAM_ADDRESS, locationName));
		if (language != null && language.length() > 0)
		{
			params.add(new BasicNameValuePair(PARAM_LANGUAGE, language));
		}
		if (mUseRegionBias)
		{
			String region = mLocale.getCountry();
			params.add(new BasicNameValuePair(PARAM_REGION, region));
		}
		
		// Create URL
    	String encodedParams = URLEncodedUtils.format(params, "UTF-8");
    	url = URL_MAPS_GEOCODE + "?" + encodedParams;

		// Return
		return url;		
	}
	
	private List<Address> getAddressesFromJSON(String jsonString, int maxResults) throws IOException
	{
		// Declare
		List<Address> results = null;
		
		// Check result
		if (jsonString != null && jsonString.length() > 0)
		{
			// Parse the JSON
			try
			{
				// Parse the string into an array
				JSONObject json = new JSONObject(jsonString);
				
				// Check for success
				if (isRequestSuccessful(json))
				{
					// Get the results
					JSONArray addresses = json.getJSONArray("results");
					
					// Get number of elements we should process
					int numAddresses = addresses.length();
					if ((maxResults != -1) && (maxResults < numAddresses)) numAddresses = maxResults;
					
					// Get the default locale
					Locale locale = Locale.getDefault();
					
					// Check for addreses
					if (numAddresses > 0)
					{
						// Initialize results
						results = new ArrayList<Address>();
					
						// Parse out each address
						for (int i=0; i<numAddresses; i++)
						{
							// Get the JSON object
							JSONObject address = addresses.getJSONObject(i);
							
							// Parse the address
							Address result = new Address(locale);
							result = getAddressFromJSON(address);
							
							// Add the new address result to results array
							results.add(result);
						}
					}
				}
			}
			catch (JSONException e)
			{
				// Throw exception
				throw new IOException(JSON_PARSE_ERROR);
			}
		}
		
		// Return
		return results;
	}
	
	private boolean isRequestSuccessful(JSONObject json) throws IOException
	{
		// Check status
		String status = getJSONStringField(json, "status");
		
		if (status.equals("OK"))
		{
			// No error and should continue
			return true;
		}
		else if (status.equals("ZERO_RESULTS"))
		{
			// No error and should not continue
			return false;
		}
		else if (status.equals("OVER_QUERY_LIMIT"))
		{
			// Error and should throw exception
			throw new IOException(QUERY_LIMIT_ERROR);
		}
		else if (status.equals("REQUEST_DENIED"))
		{
			// Error and should throw exception
			throw new IOException(REQUEST_DENIED_ERROR);
		}
		else if (status.equals("INVALID_REQUEST"))
		{
			// Error and should throw exception
			throw new IOException(INVALID_REQUEST_ERROR);
		}
		
		// Return
		return false;
	}
	
	private Address getAddressFromJSON(JSONObject address) throws IOException
	{
		// Declare
		Address result = new Address();
		
		// Fill in the address components
		populateAddressComponentsFromJSON(result, address);
		
		// Fill in the location fields
		populateAddressGeometryFromJSON(result, address);
		
		// Return
		return result;
	}
	
	private void populateAddressComponentsFromJSON(Address result, JSONObject address) throws IOException
	{
		// Declare
		JSONObject addressComponent;
		
		// Parse address_components array
		JSONArray addressComponents;
		try
		{
			// Create address components
			addressComponents = address.getJSONArray("address_components");
			
			// Formatted Address
			String formattedAddress = getJSONStringField(address, "formatted_address");
			result.setFormattedAddress(formattedAddress);
		
			// Street Number
			addressComponent = getAddressComponent(addressComponents, "street_number");
			String streetNumber = getJSONStringField(addressComponent, "long_name");
			result.setStreetNumber(streetNumber);
			
			// Premise
			addressComponent = getAddressComponent(addressComponents, "premise");
			String premise = getJSONStringField(addressComponent, "long_name");
			result.setPremise(premise);
			
			// Subpremise
			addressComponent = getAddressComponent(addressComponents, "subpremise");
			String subPremise = getJSONStringField(addressComponent, "long_name");
			result.setSubPremise(subPremise);
			
			// Floor
			addressComponent = getAddressComponent(addressComponents, "floor");
			String floor = getJSONStringField(addressComponent, "long_name");
			result.setFloor(floor);
			
			// Room
			addressComponent = getAddressComponent(addressComponents, "room");
			String room = getJSONStringField(addressComponent, "long_name");
			result.setRoom(room);
			
			// Route
			addressComponent = getAddressComponent(addressComponents, "route");
			String route = getJSONStringField(addressComponent, "long_name");
			result.setRoute(route);
			
			// Neighborhood
			addressComponent = getAddressComponent(addressComponents, "neighborhood");
			String neighborhood = getJSONStringField(addressComponent, "long_name");
			result.setNeighborhood(neighborhood);
			
			// Locality
			addressComponent = getAddressComponent(addressComponents, "locality");
			String locality = getJSONStringField(addressComponent, "long_name");
			result.setLocality(locality);
			
			// Sublocality
			addressComponent = getAddressComponent(addressComponents, "sublocality");
			String sublocality = getJSONStringField(addressComponent, "long_name");
			result.setSubLocality(sublocality);
			
			// Postal code
			addressComponent = getAddressComponent(addressComponents, "postal_code");
			String postalCode = getJSONStringField(addressComponent, "long_name");
			result.setPostalCode(postalCode);
			
			// Admin area
			addressComponent = getAddressComponent(addressComponents, "administrative_area_level_1");
			String adminArea = getJSONStringField(addressComponent, "long_name");
			result.setAdminArea(adminArea);
			
			// Subadmin area
			addressComponent = getAddressComponent(addressComponents, "administrative_area_level_2");
			String subAdminArea = getJSONStringField(addressComponent, "long_name");
			result.setSubAdminArea(subAdminArea);
			
			// Subadmin area #2
			addressComponent = getAddressComponent(addressComponents, "administrative_area_level_3");
			String subAdminArea2 = getJSONStringField(addressComponent, "long_name");
			result.setSubAdminArea2(subAdminArea2);
			
			// Country code
			addressComponent = getAddressComponent(addressComponents, "country");
			String countryCode = getJSONStringField(addressComponent, "short_name");
			result.setCountryCode(countryCode);
			
			// Country name
			addressComponent = getAddressComponent(addressComponents, "country");
			String countryName = getJSONStringField(addressComponent, "long_name");
			result.setCountryName(countryName);
			
		}
		catch (JSONException e)
		{
			// Throw exception
			throw new IOException(JSON_PARSE_ERROR);
		}
	}
	
	private void populateAddressGeometryFromJSON(Address result, JSONObject address) throws IOException
	{
		// Get the data objects
		try
		{
			// Get the geometry
			JSONObject geometry = address.getJSONObject("geometry");
			
			// Fill in the location fields
			populateAddressLocationFromJSON(result, geometry);
			
			// Fill in the viewport fields
			populateAddressViewPortFromJSON(result, geometry);
			
			// Fill in the bounds fields
			populateAddressBoundsFromJSON(result, geometry);
		}
		catch (JSONException e)
		{
			// Throw exception
			throw new IOException(JSON_PARSE_ERROR);
		}
	}
	
	private void populateAddressLocationFromJSON(Address result, JSONObject geometry) throws IOException
	{
		// Location
		try
		{
			JSONObject addressLocation = geometry.getJSONObject("location");
			
			// Location information
			Position position = getPositionFromJSON(addressLocation);
			if (position != null)
			{
				result.setLatitude(position.getLatitude());
				result.setLongitude(position.getLongitude());
			}
			
		}
		catch (JSONException e)
		{
			// Throw exception
			throw new IOException(JSON_PARSE_ERROR);
		}
	}
	
	private void populateAddressViewPortFromJSON(Address result, JSONObject geometry) throws IOException
	{
		// ViewPort
		try
		{
			// Get the data objects
			JSONObject addressViewPort = geometry.getJSONObject("viewport");
			
			// Add viewport information
			Area viewPort = getAreaFromJSON(addressViewPort);
			result.setViewPort(viewPort);
		}
		catch (JSONException e)
		{
			// Throw exception
			throw new IOException(JSON_PARSE_ERROR);
		}
	}
	
	private void populateAddressBoundsFromJSON(Address result, JSONObject geometry) throws IOException
	{
		// Bounds
		try
		{
			// Get the data objects
			JSONObject addressBounds = geometry.getJSONObject("bounds");
			
			// Add viewport information
			Area bounds = getAreaFromJSON(addressBounds);
			result.setViewPort(bounds);
		}
		catch (JSONException e)
		{
			// No exception thrown, since bounds info is optional
		}
	}
	
	private Area getAreaFromJSON(JSONObject json) throws IOException
	{		
		// Declare
		Area area = null;
		
		// Process
		try
		{
			// Extract data
			JSONObject ne = json.getJSONObject("northeast");
			JSONObject sw = json.getJSONObject("southwest");
			
			// Parse data
			Position northEast = getPositionFromJSON(ne);
			Position southWest = getPositionFromJSON(sw);
			
			// Create area
			area = new Area(northEast, southWest);
		}
		catch (JSONException e)
		{
			// Throw exception
			throw new IOException(JSON_PARSE_ERROR);
		}
		
		// Return
		return area;
	}
	
	private Position getPositionFromJSON(JSONObject json) throws IOException
	{
		// Declare
		double latitude = 0;
		double longitude = 0;
		Position position = null;
		
		// Extract data
		try
		{
			// Extract data
			latitude = json.getDouble("lat");
			longitude = json.getDouble("lng");
			
			// Create position
			position = new Position(latitude, longitude);
		}
		catch (JSONException e)
		{
			// Throw exception
			throw new IOException(JSON_PARSE_ERROR);
		}
		
		// Return
		return position;
	}
	
	private String getJSONStringField(JSONObject json, String field)
	{
		// Check parameters
		if (json == null || field == null) return null;
		
		// Locate field
		try
		{
			return json.getString(field);
		}
		catch (JSONException e)
		{
			return null;
		}
	}
	
	private JSONObject getAddressComponent(JSONArray componentArray, String componentType) throws IOException
	{
		// Go through array looking for specified type
		for (int i=0; i<componentArray.length(); i++)
		{
			// Declare
			JSONObject component;
			
			try
			{
				// Get address component
				component = componentArray.getJSONObject(i);
				
				// Get types array
				JSONArray componentTypes = component.getJSONArray("types");
				
				// Check if we have the correct type
				if (isTypeInTypeArray(componentType, componentTypes))
				{
					return component;
				}
			}
			catch (JSONException e)
			{
				// Throw exception
				throw new IOException(JSON_PARSE_ERROR);
			}
		}
		
		// Return
		return null;
	}
	
	private boolean isTypeInTypeArray(String type, JSONArray typeArray) throws IOException
	{
		// Parameter check
		if (type == null || typeArray == null) return false;
		
		// Check if we have the desired type in the types JSON array
		for (int i=0; i< typeArray.length(); i++)
		{
			try
			{
				if (typeArray.getString(i).equals(type))
				{
					return true;
				}
			}
			catch (JSONException e)
			{
				// Throw exception
				throw new IOException(JSON_PARSE_ERROR);
			}
		}
		
		// Return
		return false;
	}
}