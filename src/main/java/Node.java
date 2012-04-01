import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Util.Util;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.api.services.plus.model.Person;

/**
 * 
 * @author otruffer
 * 
 *         This class represents a node in the Plus1 Network. Note that it is empty if you didn't
 *         use crawl().
 */
public class Node {
  private String id;
  private Map<String, Integer> plusOners;
  private String name;
  private boolean crawled;

  public Node(String id) {
    this.id = id;
    this.crawled = false;
    this.plusOners = new HashMap<String, Integer>();
  }

  /**
   * Collects all plus1 this user receives for his activities.
   * 
   * @throws IOException
   */
  public void crawl(Plus plus) throws IOException {
    if (crawled) return;
    Person profile = plus.people().get(id).execute();
    this.name = profile.getNickname();
    Util.write("crawling for: " + id + "(" + this.name + ")");

    ActivityFeed feed = plus.activities().list(profile.getId(), "public").execute();

    for (Activity activity : feed.getItems()) {
      Util.write("PlusOners for activity: " + activity.getId());
      String aId = activity.getId();
      PeopleFeed people = plus.people().listByActivity(aId, "plusoners").execute();
      for (Person p : people.getItems()) {
        addPlusOne(p.getId());
        Util.write(p.getId() + "(" + p.getNickname() + ")");
      }
    }
    crawled = true;
  }

  private void addPlusOne(String id) {
    if (plusOners.containsKey(id))
      plusOners.put(id, plusOners.get(id).intValue() + 1);
    else
      plusOners.put(id, 1);
  }

  public String getId() {
    return this.id;
  }

  public List<String> getPlusOners() {
    return new LinkedList<String>(this.plusOners.keySet());
  }

  public int getPlus(String key) {
    int a;
    if(plusOners.get(key)!= null)
      a = plusOners.get(key).intValue();
    else
      a = 0;
    return a;
  }

  public int getAllPlus() {
    int count = 0;
    for (Integer i : plusOners.values())
      count = i + count;
    return count;
  }

  public void write() {
    Util.write("->"+ this.id);
    for(String key: plusOners.keySet())
    Util.write(key + " : " + plusOners.get(key));
  }
}
