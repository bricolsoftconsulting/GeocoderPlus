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

import java.util.Locale;

public class Address
{
	// Members
	Locale mLocale;
	String mStreetNumber;
	String mRoute;
	String mPremise;
	String mSubPremise;
	String mFloor;
	String mRoom;
	String mNeighborhood;
	String mLocality;
	String mSubLocality;
	String mAdminArea;
	String mSubAdminArea;
	String mSubAdminArea2;
	String mCountryName;
	String mCountryCode;
	String mPostalCode;
	String mFormattedAddress;
	Area mViewPort;
	Area mBounds;
	double mLatitude;
	double mLongitude;
	
	// Constructor
	public Address()
	{
		mLocale = Locale.getDefault();
	}
	
	public Address(Locale locale)
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
	
	public String getStreetNumber()
	{
		return mStreetNumber;
	}

	public void setStreetNumber(String streetNumber)
	{
		this.mStreetNumber = streetNumber;
	}

	public String getRoute()
	{
		return mRoute;
	}

	public void setRoute(String route)
	{
		this.mRoute = route;
	}
	
	public String getPremise()
	{
		return mPremise;
	}

	public void setPremise(String premise)
	{
		this.mPremise = premise;
	}

	public String getSubPremise()
	{
		return mSubPremise;
	}

	public void setSubPremise(String subPremise)
	{
		this.mSubPremise = subPremise;
	}
	
	public String getFloor()
	{
		return mFloor;
	}

	public void setFloor(String floor)
	{
		this.mFloor = floor;
	}

	public String getRoom()
	{
		return mRoom;
	}

	public void setRoom(String room)
	{
		this.mRoom = room;
	}

	public String getNeighborhood()
	{
		return mNeighborhood;
	}

	public void setNeighborhood(String neighborhood)
	{
		this.mNeighborhood = neighborhood;
	}

	public String getLocality()
	{
		return mLocality;
	}

	public void setLocality(String locality)
	{
		this.mLocality = locality;
	}

	public String getSubLocality()
	{
		return mSubLocality;
	}

	public void setSubLocality(String sublocality)
	{
		this.mSubLocality = sublocality;
	}

	public String getAdminArea()
	{
		return mAdminArea;
	}

	public void setAdminArea(String adminArea)
	{
		this.mAdminArea = adminArea;
	}

	public String getSubAdminArea()
	{
		return mSubAdminArea;
	}

	public void setSubAdminArea(String subAdminArea)
	{
		this.mSubAdminArea = subAdminArea;
	}
	
	public String getSubAdminArea2()
	{
		return mSubAdminArea2;
	}

	public void setSubAdminArea2(String subAdminArea2)
	{
		this.mSubAdminArea = subAdminArea2;
	}

	public String getCountryName()
	{
		return mCountryName;
	}

	public void setCountryName(String countryName)
	{
		this.mCountryName = countryName;
	}

	public String getCountryCode()
	{
		return mCountryCode;
	}

	public void setCountryCode(String countryCode)
	{
		this.mCountryCode = countryCode;
	}

	public String getPostalCode()
	{
		return mPostalCode;
	}

	public void setPostalCode(String postalCode)
	{
		this.mPostalCode = postalCode;
	}

	public String getFormattedAddress()
	{
		return mFormattedAddress;
	}

	public void setFormattedAddress(String formattedAddress)
	{
		this.mFormattedAddress = formattedAddress;
	}
	
	public Area getViewPort()
	{
		return mViewPort;
	}

	public void setViewPort(Area viewPort)
	{
		this.mViewPort = viewPort;
	}
	public Area getBounds()
	{
		return mBounds;
	}

	public void setBounds(Area bounds)
	{
		this.mBounds = bounds;
	}

	public double getLatitude()
	{
		return mLatitude;
	}
	
	public int getLatitudeE6()
	{
		return (int)(mLatitude * 1E6);
	}

	public void setLatitude(double latitude)
	{
		this.mLatitude = latitude;
	}

	public double getLongitude()
	{
		return mLongitude;
	}
	
	public int getLongitudeE6()
	{
		return (int)(mLongitude*1E6);
	}

	public void setLongitude(double longitude)
	{
		this.mLongitude = longitude;
	}
}