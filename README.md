Geocoder Plus
=============

What is it?
-----------
GeocoderPlus is a replacement for the built-in Android Geocoder library. Just like the original, GeocoderPlus geocodes locations -- you enter location names and it returns latitude and longitude coordinates. Unlike the original, GeocoderPlus actually gives you viewport information so you can zoom maps correctly to display entire locations on the screen -- whether those locations are countries, states, cities or buildings.

GeocoderPlus does not include reverse geocoding. You can use the built-in Android Geocoder for reverse geocoding -- no complaints about that functionality.

Installation
------------
GeocoderPlus is provided as a JAR library. To add GeocoderPlus to your Android project in Eclipse, follow these steps:

1. Create a `lib` directory in your project and copy the `GeocoderPlus.jar` file there.
1. Right click on your project in Eclipse and select `Properties`. This will open the `Properties` window.
1. Select `Java Build Path` from the collapsable options tree on the left. This will show the build path options on the right.
1. Select the `Libraries` tab.
1. Click the `Add JARs...` button, browse to the lib directory in Step 1 and select the `GeocoderPlus.jar` file.

Usage
-----

### Geocoding

To geocode an address, instantiate a new `com.bricolsoftconsulting.geocoderplus.Geocoder` object, and then call the following function:

    List<Address> getFromLocationName(String locationName)

The function will return a list of `Address` objects. You can get each result address properly formatted using `Address.getFormattedAddress()`. You can also get the latitude and longitude of an address using `Address.getLatitude()` and `Address.getLongitude()`. Finally, you can get the viewport using `Address.getViewPort()`. There are also additional properties to retrieve each individual address component.

A complete example showing how to use the Geocoder to display locations on a map at the proper zoom factor is provided in the separate `GeocoderPlusExample` repository at https://github.com/bricolsoftconsulting/GeocoderPlusExample/.

### Localization

Localization helps you customize the location results based on a particular language and / or region.

#### Language Localization

GeocoderPlus uses the language in your default Android locale for geocoding. You can manually change the locale either at instantiation via the constructor `Geocoder(Locale locale)` or after instantiation using the setter `Geocoder.setLocale(Locale locale)`. Make sure you do this before calling the geocoding function.

#### Region Biasing

GeocoderPlus supports region biasing, which gives priority to certain results based on your location. For example, if you are in the US and have region biasing enabled, searching for `Rome` will give priority to `Rome, GA` and `Rome, NY` at the expense of `Rome, Italy` which will appear further down in the results.

To enable regions biasing, use the setter `Geocoder.setUseRegionBias()`.

Copyright
---------
Copyright 2011 Bricolsoft Consulting

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.