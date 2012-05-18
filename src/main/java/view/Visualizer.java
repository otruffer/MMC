package view;

import htmlgen.Attribute;
import htmlgen.Tag;
import util.FileUtil;
import crawling.Node;

public class Visualizer {
	private Node user;
	private Tag html;

	public Visualizer(Node user) {
		this.user = user;
		this.html = new Tag("html");
		this.html.add(makeHead());
		this.html.add(makeBody());
	}

	private Tag makeHead() {
		Tag head = new Tag("head");
		Tag title = new Tag("title");
		title.add("Your Google +1 Statistics");
		head.add(title);
		head.add(makeStyle());
		return head;
	}

	private Object makeStyle() {
		Tag style = new Tag("style", "type=text/css");
		String css = FileUtil
				.readFile(this.getClass().getResource("style.css"));
		style.add(css);
		return style;
	}

	private Object makeBody() {
		Tag body = new Tag("body");
		showPlusStatistics(body);
		return body;
	}

	private void showPlusStatistics(Tag tag) {
		Tag statWrapper = new Tag("div", "class=statWrapper");
		new NodeVisualizer(user).renderInto(tag);
		for (Node node : user.getPlusOners()) {
			new NodeVisualizer(node).renderInto(tag);
		}
		tag.add(statWrapper);
	}

	public String getHtml() {
		return new HtmlDocument(html).toString();
	}
}
