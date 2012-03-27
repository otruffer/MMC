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

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.auth.oauth2.draft10.AuthorizationRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAuthorizationRequestUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.util.Scanner;

/**
 * Implements OAuth authentication.
 *
 * @author Yaniv Inbar
 * @author Will Norris
 * @author Jenny Murphy
 */
public class Auth {
  /**
   * OAuth client ID.
   */
  public static String CLIENT_ID = Util.config.getProperty("oauth_client_id").trim();

  /**
   * OAuth client secret.
   */
  public static String CLIENT_SECRET = Util.config.getProperty("oauth_client_secret").trim();

  /**
   * OAuth client secret.
   */
  public static String GOOGLE_API_KEY = Util.config.getProperty("google_api_key").trim();

  /**
   * OAuth redirect URI.
   */
  private static String REDIRECT_URI = Util.config.getProperty("oauth_redirect_uri").trim();

  /**
   * Space separated list of OAuth scopes.
   */
  private static String SCOPES = Util.config.getProperty("oauth_scopes").trim();

  private static AccessTokenResponse accessTokenResponse;

  public static String getRefreshToken() {
    return accessTokenResponse.refreshToken;
  }

  public static String getAccessToken() {
    return accessTokenResponse.accessToken;
  }

  /**
   * Send the user through the OAuth flow to authorize the application and update
   * the refresh and auth tokens.
   *
   * @throws IOException unable to complete OAuth flow
   */
  public static void authorize() throws IOException {
    if ("".equals(CLIENT_ID)) {
      System.err.println("Please specify your OAuth Client ID in src/main/resources/config.properties.");
      System.exit(1);
    }

    // build the authorization URL
    GoogleAuthorizationRequestUrl authorizeUrl = new GoogleAuthorizationRequestUrl(
            CLIENT_ID,
            REDIRECT_URI,
            SCOPES
    );
    authorizeUrl.redirectUri = REDIRECT_URI;
    authorizeUrl.scope = SCOPES;
    // Setting offline access type and forced approval prompt will guarantee
    // that we always have an OAuth refresh token
    authorizeUrl.setAccessType("offline");
    authorizeUrl.setApprovalPrompt("force");

    String authorizationUrl = authorizeUrl.build();

    // launch in browser
    System.out.println("Attempting to open a web browser to start the OAuth2 flow");
    Util.openBrowser(authorizationUrl);

    // request code from user
    System.out.print("Once you authorize please enter the code here: ");
    String code = new Scanner(System.in).nextLine();

    // Exchange code for an access token
    Auth.accessTokenResponse = new GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant(
            new NetHttpTransport(),
            new GsonFactory(),
            CLIENT_ID,
            CLIENT_SECRET,
            code,
            REDIRECT_URI
    ).execute();
  }
}
