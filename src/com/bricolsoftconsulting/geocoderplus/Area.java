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

public class Area
{
	// Members
	Position mNorthEast;
	Position mSouthWest;
	
	// Constructors
	public Area()
	{
	}
	
	public Area(Position northEast, Position southWest)
	{
		mNorthEast = northEast;
		mSouthWest = southWest;
	}
	
	// Getters and setters
	public Position getNorthEast()
	{
		return mNorthEast;
	}

	public void setNorthEast(Position northEast)
	{
		mNorthEast = northEast;
	}

	public Position getSouthWest()
	{
		return mSouthWest;
	}

	public void setSouthWest(Position southWest)
	{
		mSouthWest = southWest;
	}
	
	public double getLatitudeSpan()
	{
		double maxLatitude = mNorthEast.getLatitude();
		double minLatitude = mSouthWest.getLatitude();
		return maxLatitude - minLatitude;
	}
	
	public double getLongitudeSpan()
	{
		double maxLongitude = mNorthEast.getLongitude();
		double minLongitude = mSouthWest.getLongitude();
		return (maxLongitude - minLongitude);
	}
	
	public int getLatitudeSpanE6()
	{
		return (int)((getLatitudeSpan())*1E6);
	}
	
	public int getLongitudeSpanE6()
	{
		return (int)((getLongitudeSpan()) * 1E6);
	}
}