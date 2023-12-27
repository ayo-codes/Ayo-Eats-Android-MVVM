# Mobile App Development Module Assignment 2 
## AyoEats App 
> An App that allows you to record photos and details of meals you have at restaurants/locations 

## Features
+ Authentication
  + Authentication is managed using firebase authentication
  + A user can sign in or create an account using email and password
  + A user can also sign in or create an account with their google accounut via oauth
  + A user can also sign out of the app
  
+ Meal Locations
  + A user can add a meal location to their account
  + meal locations have the following details entered by the user
    + meal name
    + meal description
    + meal price
    + meal rating , which you can give out of 5
    + a photo of the meal from the user's gallery
  + Meal locations also store additional details
    + meal locations latitude and longitude details which is obtained from the users' device current location
    + meal locations also saves the address using reverse geocoding from the latitude and longitude

+ List of Meal Locations
  + A user can view their own list of meal locations that they have added
  + Using a toggle button on the toolbar, a user can also see other meal locations added by other users but can not edit or delete it
  + A user can swipe right to edit the meal location from the list of meal locations fragment
  + A user can swipe left to detail the meal location from the list of meal locations fragment
  + A user can also pull to refresh on the screen , to refresh the list

+ Meal Location Details
  + A user can view details of a meal location that they have previously added
  + A user can decide to update details or to delete the meal location

+ Meal Location Maps
  + A user can view locations of their previous meal locations on a map with some details when they click the mark
  + A user using a toggle button can view other meal locations added by other users

+ User Details
  + When a user signs in with their google account their details are added to the navigation header panel, which includes:
    + their profile photo from google
    + their email address
    + their first name and surname
  + A user can also change and update their profile photo , using a photo from their gallery
 
     
## External APIs 
+ Google Firebase Authentication for authentication
+ Google Firebase Realtime firebase for database information
+ Google Firebase storage for storage of images
+ Google Maps API to display maps
+ Google Places API to get information on places



## UML Class Diagram
+ Uml class diagram generated using a plugin with android studio IDE - UML generator
![][uml_diagram]

## UX/DX Approach
+ The app was built using the MVVM design pattern/architecture in terms of organising the code and in kotlin
+ The app utilised using recommended ux design guidelines from google, for example using material icons and implementing navigation drawers and routing

## Git Approach 
+ Used regular commits with tagged releases, whilst also using branches for development and releases

## Personal Statement
+ The work was done by myself with the assistance of the lecture notes and labs from the course

## External References 
+ Seekbar input - https://www.udemy.com/course/kotlin-masterclass-learn-kotlin-from-zero-to-advanced/learn/lecture/35375964#overview
+ Reverse Geocoding - https://www.geeksforgeeks.org/reverse-geocoding-in-android/

## Running the App 
+ To run the app you will need to use your own google-servicces.json file which can be downloaded from the firebase settings console. Ensure you have the relevant APIs enabled on your google cloud

## Demo 
you can view a demo of the app here - https://youtu.be/xkVUxy0hX_A

[uml_diagram]: ./app/src/main/res/drawable/Assignment_2.png