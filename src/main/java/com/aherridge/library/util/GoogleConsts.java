package com.aherridge.library.util;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Arrays;
import java.util.Collection;

public class GoogleConsts
{
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();
	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/userinfo.email", "https://www.googleapis.com/auth/userinfo.profile");
	public static String CLIENT_ID = "660130689297-luq4tp9b3a7mqhs8i0sntqcvsk7sbd9n.apps.googleusercontent.com";
	public static String CLIENT_SECRET = "bqzgqvU4qUiCEXBz_DJVuTnJ";
	public static String REDIRECT_URI = Path.Web.AUTH;
	public static String APP_NAME = "Open Library";
	public static String API_KEY = "AIzaSyAYNVYsaoN_1FR_KIedcbh9qiia_HaBvz8";
}
