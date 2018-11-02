# integration-with-zoom
Integration with Zoom Meetings ans Webinars using java

# Getting started
The Zoom API allows developers to safely and securely access information from Zoom. You can use this API to build private services or public applications on Zoom marketplace. If you want to make a private service or application please visit http://developer.zoom.us/me/ to get your key. If you plan on making a public application, please visit https://marketplace.zoom.us/ to register your app.

## Prerequisites
* Java IDE (Ex: IntelliJ Idea)
* Java 8
* Stripe Account

## Create Zoom App
You need to create app on zoom to access REST APIs from Zoom
* Visit https://marketplace.zoom.us/develop/create to create new app
* After createing app you will get Client Id and Client Secret
* Enter Redirect URL for OAuth to your domain or localhost
* After completeing app information please select scopes from dropdowm
And you are ready to make api calls now.

## Create methods to make API calls
###### I have written code using Java to make REST API calls
> please see ZoomUtils.java

In ZoomUtils.java methods are arranged by this flow:
1. Create Authorization URL
2. Generate token from given auth_code 
3. Exchange refresh token to get new access token
4. Get User details 
5. Get meeting list from Zoom
6. Create new Meeting in Zoom
7. Retrive particular meeting by Id
8. Update meeting by meetingId
9. Get List of webinars from Zoom
10. Create new webinar on Zoom
11. Retrive particular webinar by webinarId

###### If you need any other integrations for Zoom or find any mistakes or bugs, please feel free to contact me at samritkamlesh@gmail.com

