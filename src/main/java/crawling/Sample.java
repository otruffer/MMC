package crawling;
/*
 * Copyright (c) 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */


import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusRequest;
import com.google.api.services.plus.model.*;

import java.io.IOException;
import java.util.logging.Logger;

import util.Auth;
import util.Util;

/**
 * @author Yaniv Inbar
 * @author Tony Aiuto
 * @author Will Norris
 * @author Jenny Murphy
 */
public class Sample {
  private static final Logger log = Logger.getLogger(Sample.class.getName());

  private static Plus plus;
  private static Plus unauthenticatedPlus;

  public static void main(String[] args) throws IOException {

    try {
      setupTransport();

      getProfile();
      listActivities();
      getActivity();
    } catch (HttpResponseException e) {
      log.severe(e.getResponse().parseAsString());
      throw e;
    }
  }

  /**
   * Setup the transport for our API calls.
   * @throws java.io.IOException when the transport cannot be created
   */
  private static void setupTransport() throws IOException {
    // Here's an example of an unauthenticated Plus object. In cases where you
    // do not need to use the /me/ path segment to discover the current user's
    // ID, you can skip the OAuth flow with this code.
    unauthenticatedPlus = Plus.builder(Util.TRANSPORT, Util.JSON_FACTORY)
        // When we do not specify access tokens, we must specify our API key instead
        // We do this using a JsonHttpRequestInitializer
        .setJsonHttpRequestInitializer(new JsonHttpRequestInitializer() {
          @Override
          public void initialize(JsonHttpRequest jsonHttpRequest) throws IOException {
            PlusRequest plusRequest = (PlusRequest) jsonHttpRequest;
            plusRequest.setKey(Auth.GOOGLE_API_KEY);
          }
        }).build();

    // If, however, you need to use OAuth to identify the current user you must
    // create the Plus object differently. Most programs will need only one
    // of these since you can use an authenticated Plus object for any call.
    Auth.authorize();
    GoogleAccessProtectedResource requestInitializer =
        new GoogleAccessProtectedResource(
            Auth.getAccessToken(),
            Util.TRANSPORT,
            Util.JSON_FACTORY,
            Auth.CLIENT_ID,
            Auth.CLIENT_SECRET,
            Auth.getRefreshToken());
    plus = Plus.builder(Util.TRANSPORT, Util.JSON_FACTORY)
        .setHttpRequestInitializer(requestInitializer).build();
  }

  /**
   * List the public activities for the authenticated user
   *
   * @throws IOException if unable to call API
   */
  private static void listActivities() throws IOException {
    header("Search Activities for wolff");

    // Fetch the first page of activities
    Plus.Activities.Search listActivities = plus.activities().search();
    listActivities.setQuery("me");
    listActivities.setMaxResults(3L);

    ActivityFeed feed;
    try {
      feed = listActivities.execute();
    } catch (HttpResponseException e) {
      log.severe(Util.extractError(e));
      throw e;
    }
    // Keep track of the page number in case we're listing activities
    // for a user with thousands of activities. We'll limit ourselves
    // to 5 pages
    int currentPageNumber = 0;
    while (feed != null && feed.getItems() != null && currentPageNumber < 1) {
      currentPageNumber++;
      
      System.out.println();
      System.out.println("~~~~~~~~~~~~~~~~~~ page "+currentPageNumber+" of activities ~~~~~~~~~~~~~~~~~~");
      System.out.println();

      for (Activity activity : feed.getItems()) {

        show(activity);
        System.out.println();
        System.out.println("------------------------------------------------------");
        System.out.println();
      }

      // Fetch the next page
      System.out.println("next token: " + feed.getNextPageToken());
      listActivities.setPageToken(feed.getNextPageToken());
      feed = listActivities.execute();
    }
  }

  /**
   * Get the most recent activity for the authenticated user.
   *
   * @throws IOException if unable to call API
   */
  private static void getActivity() throws IOException {
    // A known public activity ID
    String activityId = "z12gtjhq3qn2xxl2o224exwiqruvtda0i";

    // We do not need to be authenticated to fetch this activity
    header("Get an explicit public activity by ID");
    try {
      Activity activity = unauthenticatedPlus.activities().get(activityId).execute();
      show(activity);
    } catch (HttpResponseException e) {
      log.severe(Util.extractError(e));
      throw e;
    }
  }

  /**
   * Get the profile for the authenticated user.
   *
   * @throws IOException if unable to call API
   */
  private static void getProfile() throws IOException {
    header("Get my Google+ profile");
    try {
      Person profile = plus.people().get("me").execute();
      show(profile);
    } catch (HttpResponseException e) {
      log.severe(Util.extractError(e));
      throw e;
    }
  }

  /**
   * Print the specified person on the command line.
   *
   * @param person the person to show
   */
  private static void show(Person person) {
    System.out.println("id: " + person.getId());
    System.out.println("name: " + person.getDisplayName());
    System.out.println("image url: " + person.getImage().getUrl());
    System.out.println("profile url: " + person.getUrl());
  }

  /**
   * Print the specified activity on the command line.
   *
   * @param activity the activity to show
   */
  private static void show(Activity activity) {
    System.out.println("id: " + activity.getId());
    System.out.println("url: " + activity.getUrl());
    System.out.println("content: " + activity.getPlusObject().getContent());
    System.out.println("plus1selflink: " + activity.getPlusObject().getPlusoners().getSelfLink());

  }

  private static void header(String name) {
    System.out.println();
    System.out.println("============== " + name + " ==============");
    System.out.println();
  }
}
