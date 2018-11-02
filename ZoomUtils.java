package com.kss.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ZoomUtils {

    private static final String CLIENTID = "<Enter Client_Id here>";
    private static final String CLIENTSECRET = "<Enter Client_Secret here>";
    private static final String REDIRECTURL = "<Enter redirect URL>";

    public static String authorizationUrl() {
        return "https://zoom.us/oauth/authorize?response_type=code&client_id=" + CLIENTID + "&redirect_uri=" + REDIRECTURL;
    }

    // you will get this code in response URL after success of above process
    public static String generateToken(String code) throws Exception {
        String url = "https://api.zoom.us/oauth/token";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        String authString = CLIENTID + ":" + CLIENTSECRET;
        String authStringEnc = new BASE64Encoder().encode(authString.getBytes());
        List<NameValuePair> urlParameters = new ArrayList<>();
        post.setHeader("Accept", "application/json");
        post.setHeader("Authorization", "Basic " + authStringEnc);
        urlParameters.add(new BasicNameValuePair("code", code));
        urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
        urlParameters.add(new BasicNameValuePair("redirect_uri", REDIRECTURL));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        //Set response type
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            // Read Response
            result.append(line);
        }
        return result.toString();
    }

    public static String exchangeRefreshToken(String refreshToken) throws Exception {
        String url = "https://zoom.us/oauth/token";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        String authString = CLIENTID + ":" + CLIENTSECRET;
        String authStringEnc = new BASE64Encoder().encode(authString.getBytes());
        List<NameValuePair> urlParameters = new ArrayList<>();
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Authorization", "Basic " + authStringEnc);
        urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
        urlParameters.add(new BasicNameValuePair("refresh_token", refreshToken));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        //Set response type
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    public static String getUserDetails(String token) throws Exception {
        String url = "https://api.zoom.us/v2/users/me";
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        List<NameValuePair> urlParameters = new ArrayList<>();
        //post.setHeader("Authorization", "Bearer"+token);
        get.setHeader("Accept", "application/json");
        get.setHeader("Content-Type", "application/json");
        get.setHeader("Authorization", "Bearer " + token);
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    public static String getMeetingList(String token, String meetingType) throws Exception {
        token = "Bearer " + token;
        String url = "https://api.zoom.us/v2/users/me/meetings";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", token);
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("type", meetingType);
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public static String createMeeting(String token, String topic, String type, String start_time, String duration, String timezone, String password) throws Exception {
        String url = "https://api.zoom.us/v2/users/me/meetings";
        URL serverUrl = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Authorization", "Bearer " + token);
        String d = "{\"topic\": \"" + topic + "\",\"type\": \"" + type + "\",\"start_time\": \"" + start_time + "\",\"duration\": \"" + duration + "\",\"timezone\": \"" + timezone + "\",\"password\": \"" + password + "\"}";
        BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
        httpRequestBodyWriter.write(d);
        httpRequestBodyWriter.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        // int responseCode = urlConnection.getResponseCode();
        StringBuilder results = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            results.append(line);
        }
        urlConnection.disconnect();
        return results.toString();
    }

    public static String retriveMeeting(String token, String meetingId) throws Exception {
        String url = "https://api.zoom.us/v2/meetings/" + meetingId;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        List<NameValuePair> urlParameters = new ArrayList<>();
        get.setHeader("Content-Type", "application/json");
        get.setHeader("Authorization", "Bearer " + token);
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            // Read Response
            result.append(line);
        }
        System.out.println(result.toString());
        return result.toString();
    }

    public static String updateMeeting(String token, String meetingId, String topic, String type, String start_time, String duration, String timezone, String password) throws Exception {
        String url = "https://api.zoom.us/v2/meetings/" + meetingId;
        HttpClient client = new DefaultHttpClient();
        HttpPatch patch = new HttpPatch(url);
        // List<NameValuePair> urlParameters = new ArrayList<>();
        patch.setHeader("Content-Type", "application/json");
        patch.setHeader("Accept", "application/json");
        patch.setHeader("Authorization", "Bearer " + token);
        String d = "{\"topic\": \"" + topic + "\",\"type\": \"" + type + "\",\"start_time\": \"" + start_time + "\",\"duration\": \"" + duration + "\",\"timezone\": \"" + timezone + "\",\"password\": \"" + password + "\"}";
        patch.setEntity(new StringEntity(d));
        HttpResponse response = client.execute(patch);
        BufferedReader rd = new BufferedReader(new InputStreamReader(patch.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            // Read Response
            result.append(line);
        }
        System.out.println(result.toString());
        return result.toString();
    }

    public static String listOfWebinar(String token) throws Exception {
        String url = "https://api.zoom.us/v2/users/me/webinars";
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-Type", "application/json");
        get.setHeader("Authorization", "Bearer " + token);
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    public static String createWebinar(String exchangeToken , String topic, String type, String startTime, String duration, String timezone, String password) throws Exception {
        String url = "https://api.zoom.us/v2/users/me/webinars";
        URL serverUrl = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Authorization", "Bearer " + exchangeToken);
        String d = "{\"topic\": \"" + topic + "\",\"type\": \"" + type + "\",\"start_time\": \"" + startTime + "\",\"duration\": \"" + duration + "\",\"timezone\": \"" + timezone + "\",\"password\": \"" + password + "\"}";
        BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
        httpRequestBodyWriter.write(d);
        httpRequestBodyWriter.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder results = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            results.append(line);
        }
        urlConnection.disconnect();
        return results.toString();
    }

    public static String retriveWebinar(String token, String webinarId) throws Exception {
        String url = "https://api.zoom.us/v2/webinars/" + webinarId;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        List<NameValuePair> urlParameters = new ArrayList<>();
        get.setHeader("Content-Type", "application/json");
        get.setHeader("Authorization", "Bearer " + token);

        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            // Read Response
            result.append(line);
        }
        System.out.println(result.toString());
        return result.toString();
    }

    public static void main(String args[]) throws Exception {
        // please enter the values of below variables and uncommect method one by to test results
        String code = "";
        String token = "";

        String meetingId = "";
        String webinarId = "";
        String meetingType = "";
        String topic = "";
        String type = "2";
        String start_time = "";
        String duration = "30";
        String timezone = "Asia/Kolkata";
        String password = "123";
        String s = "";
        //String exchangeToken = "";
        String refreshtoken = "";

        s = authorizationUrl();
        //s=generateToken(code);
        //s=getUserDetails(token);
        //s = exchangeRefreshToken(refreshtoken);
        //s=getMeetingList(token,meetingType);
        //s=createMeeting(token,topic,type,start_time,duration,timezone,password);
        //s=retriveMeeting(token,meetingId);
        //s=updateMeeting(token,meetingId,topic,type,start_time,duration,timezone,password);
        //s=createWebinar(token,topic,type,start_time,duration,timezone,password);
        //s=listOfWebinar(token);
        //s=retriveWebinar(token,webinarId);
        //s = exchangeRefreshToken(exchangeToken);
        System.out.println("s:" + s);
    }
}
