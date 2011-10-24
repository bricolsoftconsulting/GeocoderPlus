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

public class Position
{
	// Members
	double mLatitude;
	double mLongitude;
	
	// Getters and setters
	public double getLatitude()
	{
		return mLatitude;
	}

	public void setLatitude(double latitude)
	{
		this.mLatitude = latitude;
	}

	public double getLongitude()
	{
		return mLongitude;
	}

	public void setLongitude(double longitude)
	{
		this.mLongitude = longitude;
	}

	// Constructors
	public Position()
	{
		
	}
	
	public Position(double latitude, double longitude)
	{
		mLatitude = latitude;
		mLongitude = longitude;
	}
}