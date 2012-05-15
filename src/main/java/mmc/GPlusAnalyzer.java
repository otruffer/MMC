package mmc;

import java.io.IOException;

import javax.management.RuntimeErrorException;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.Comments;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Comment;
import com.google.api.services.plus.model.Person;

public class GPlusAnalyzer {

	private Plus plus;
	private Person me;
	private int likes;
	private int posts;

	private boolean crawled;

	public GPlusAnalyzer(Plus plus) throws IOException {
		this.plus = plus;
		me = plus.people().get("me").execute();
		this.crawled = false;
	}

	public void crawl() throws IOException {
		ActivityFeed feed = plus.activities().list(me.getId(), "public")
				.execute();

		int posts = 0, likes = 0;
		for (Activity activity : feed.getItems()) {
			posts++;
			likes += activity.getPlusObject().getPlusoners().size();
		}

		this.likes = likes;
		this.posts = posts;

		this.crawled = true;
	}

	private void ensureCrawled() {
		if (!crawled) {
			try {
				this.crawl();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public int getLikes() {
		ensureCrawled();

		return this.likes;
	}

	public int getPosts() {
		ensureCrawled();

		return this.posts;
	}

}
