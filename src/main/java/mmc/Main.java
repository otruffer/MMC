package mmc;


import java.io.IOException;

import util.Auth;
import util.Util;


import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusRequest;

public class Main {

  private static Plus plus;
  private static Plus unauthenticatedPlus;

  
  /**
   * @param args
   */
  public static void main(String[] args) throws IOException {
    setupTransport();
    
    GPlusAnalyzer analyzer = new GPlusAnalyzer(plus);
    
    System.out.println("Average +1s per activity: "+analyzer.likesPerPost());
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

}
