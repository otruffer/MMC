import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import Util.Auth;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Person;
import com.google.api.services.plus.model.PersonUrls;


public class Sandbox {
  public static void main(String[] args) throws IOException{
 // Set up the HTTP transport and JSON factory
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();

    // Set up OAuth 2.0 access of protected resources
    // using the refresh and access tokens, automatically
    // refreshing the access token when it expires
    Auth.authorize();
    
    GoogleAccessProtectedResource requestInitializer =
        new GoogleAccessProtectedResource(Auth.getAccessToken(), httpTransport,
        jsonFactory, Auth.CLIENT_ID, Auth.CLIENT_SECRET, Auth.getRefreshToken());

    // Set up the main Google+ class
    Plus plus = Plus.builder(httpTransport, jsonFactory)
        .setHttpRequestInitializer(requestInitializer)
        .build();

    // Make a request to access your profile and display it to console
    Person profile = plus.people().get("me").execute();
    System.out.println("ID: " + profile.getId());
    System.out.println("Name: " + profile.getDisplayName());
    System.out.println("Image URL: " + profile.getImage().getUrl());
    System.out.println("Profile URL: " + profile.getUrl());
    
     ActivityFeed feed = plus.activities().list(profile.getId(), "public").execute();
     for (Activity activity : feed.getItems()) {
       show(activity);
       System.out.println();
       System.out.println("------------------------------------------------------");
       System.out.println();
     }
  }
  private static void show(Activity activity) {
    System.out.println("id: " + activity.getId());
    System.out.println("url: " + activity.getUrl());
    System.out.println("content: " + activity.getPlusObject().getContent());
    System.out.println("+1: " + activity.getPlusObject().getPlusoners().getTotalItems());
    Collection<Object> val = activity.getPlusObject().getPlusoners().values();
    Set<String> keys = activity.getPlusObject().getPlusoners().keySet();
    for(Object o: val){
      System.out.println(o.toString());
    }
    for(String s: keys){
      System.out.println(s);
    }
  }
}
