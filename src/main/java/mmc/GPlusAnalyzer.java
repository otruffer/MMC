package mmc;

import java.io.IOException;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.Comments;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Comment;
import com.google.api.services.plus.model.Person;

public class GPlusAnalyzer {

  private Plus plus;
  private Person me;

  public GPlusAnalyzer(Plus plus) throws IOException {
    this.plus = plus;
    me = plus.people().get("me").execute();
  }

  public double likesPerPost() throws IOException {
    ActivityFeed feed = plus.activities().list(me.getId(), "public").execute();

    int posts = 0, likes = 0;
    for (Activity activity : feed.getItems()) {
      posts++;
      likes += activity.getPlusObject().getPlusoners().size();
    }

    if (posts == 0) {
      return -1;
    }

    return (double) likes / posts;
  }

}
