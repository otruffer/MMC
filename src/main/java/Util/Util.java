package util;
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

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Strings;

import java.awt.*;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Assorted utility methods.
 * 
 * @author Will Norris
 * @author Jenny Murphy
 */
public class Util {
  public static final boolean DEBUG = false;
  public static final Properties config = getConfig();
  public static final JsonFactory JSON_FACTORY = new GsonFactory();
  public static final HttpTransport TRANSPORT = new NetHttpTransport();
  public static final boolean SILENT = false;

  public static void write(String s){
    if(!SILENT){
      System.out.println(s);
    }
  }
  /**
   * Load the configuration file for this application.
   * 
   * @return application configuration properties
   */
  static Properties getConfig() {
    InputStream input = Util.class.getResourceAsStream("/config.properties");
    Properties config = new Properties();
    try {
      config.load(input);
    } catch (IOException e) {
      System.err.println("Unable to load config file: config.properties");
      System.exit(1);
    }
    return config;
  }

  /**
   * Try to load the specified URL in the user's browser. If unable to launch a browser, prompt the
   * user to open the URL directly.
   * 
   * @param uri URL to open
   */
  public static void openBrowser(URI uri) {
    boolean browsed = false;

    if (Desktop.isDesktopSupported()) {
      Desktop desktop = Desktop.getDesktop();
      if (desktop.isSupported(Action.BROWSE)) {
        try {
          desktop.browse(uri);
          browsed = true;
        } catch (IOException e) {
          // sometimes BROWSE appears to be supported but isn't
        }
      }
    }

    if (!browsed) {
      try {
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + uri);
        browsed = true;
      } catch (IOException e) {
        // rundll32 only works on Windows
      }
    }

    if (!browsed) {
      try {
        String browser = "google-chrome";
        Runtime.getRuntime().exec(new String[] {browser, uri.toString()});
      } catch (IOException e) {
        // Google Chrome does not appear to be installed
      }
    }

    if (!browsed) {
      System.out.println("Please open the following URL to continue: " + uri);
      System.out.println("");
    }
  }

  /**
   * Try to load the specified URL in the user's browser. If unable to launch a browser, prompt the
   * user to open the URL directly.
   * 
   * @param str String representation of URL to open
   */
  public static void openBrowser(String str) {
    openBrowser(URI.create(str));
  }

  /**
   * Extract the request error details from a HttpResponseException
   * @param e An HttpResponseException that contains error details
   * @return The String representation of all errors that caused the
   *     HttpResponseException
   * @throws IOException when the response cannot be parsed or stringified
   */
  public static String extractError(HttpResponseException e) throws IOException {
    if (!Json.CONTENT_TYPE.equals(e.getResponse().getContentType())) {
      return e.getResponse().parseAsString();
    }

    GoogleJsonError errorResponse =
            GoogleJsonError.parse(JSON_FACTORY, e.getResponse());
    StringBuilder errorReportBuilder = new StringBuilder();

    errorReportBuilder.append(errorResponse.code);
    errorReportBuilder.append(" Error: ");
    errorReportBuilder.append(errorResponse.message);

    for (GoogleJsonError.ErrorInfo error : errorResponse.errors) {
      errorReportBuilder.append(JSON_FACTORY.toString(error));
      errorReportBuilder.append(Strings.LINE_SEPARATOR);
    }
    return errorReportBuilder.toString();
  }
}
