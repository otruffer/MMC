import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import util.Auth;
import util.Util;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.People;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.api.services.plus.model.Person;
import com.google.api.services.plus.model.PersonUrls;

public class Sandbox {
	public static final String WHAT = "113187650697340616399";
	public static final String FILENAME = "output.network";

	public static void main(String[] args) throws IOException {
		// scanAndWrite();
		read();
	}

	public static void read() throws IOException {
		Network net = new Network();

		Util.write("reading...");
		net.load(FILENAME);
		Util.write("read!");
		net.write();
		Util.write("" + net.getRatio(WHAT));
		Util.write("" + net.getReceivedPlus(WHAT));
		Util.write("" + net.getSendPlus(WHAT));
		Util.write("finished!");
	}

	public static void scanAndWrite() throws IOException {
		// Set up the HTTP transport and JSON factory
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		// Set up OAuth 2.0 access of protected resources
		// using the refresh and access tokens, automatically
		// refreshing the access token when it expires
		Auth.authorize();

		GoogleAccessProtectedResource requestInitializer = new GoogleAccessProtectedResource(
				Auth.getAccessToken(), httpTransport, jsonFactory,
				Auth.CLIENT_ID, Auth.CLIENT_SECRET, Auth.getRefreshToken());

		// Set up the main Google+ class
		Plus plus = Plus.builder(httpTransport, jsonFactory)
				.setHttpRequestInitializer(requestInitializer).build();

		Network net = new Network();
		net.put(new Node(WHAT));
		net.crawl(WHAT, 0, plus);
		net.write();
		Util.write("" + net.getRatio(WHAT));
		Util.write("" + net.getReceivedPlus(WHAT));
		Util.write("" + net.getSendPlus(WHAT));
		Util.write("serialzing network");
		net.serialize(FILENAME);
		Util.write("serialized!");
	}

	private static void show(Activity activity) {
		System.out.println("id: " + activity.getId());
		System.out.println("url: " + activity.getUrl());
		System.out.println("content: " + activity.getPlusObject().getContent());
		System.out.println("+1: "
				+ activity.getPlusObject().getPlusoners().getTotalItems());
	}

	private static void show(Person profile) {
		System.out.println("ID: " + profile.getId());
		System.out.println("Name: " + profile.getDisplayName());
		System.out.println("Image URL: " + profile.getImage().getUrl());
		System.out.println("Profile URL: " + profile.getUrl());
	}
}
