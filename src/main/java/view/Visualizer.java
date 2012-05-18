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
		tag.add(makeGPlusLogo());
		tag.add(makeTitle("You:"));
		new NodeVisualizer(user).renderInto(tag);
		tag.add(Tag.br());
		tag.add(makeTitle("Your friends:"));
		for (Node node : user.getPlusOners()) {
			new NodeVisualizer(node).renderInto(tag);
		}
	}

	private Tag makeGPlusLogo() {
		Tag img = new Tag("img", "src=https://ssl.gstatic.com/s2/oz/images/google-logo-plus-0fbe8f0119f4a902429a5991af5db563.png");
		img.addAttribute(new Attribute("class", "gPlusLogo"));
		return img;
	}

	private Tag makeTitle(String string) {
		Tag title = new Tag("h1");
		title.add(string);
		return title;
	}

	public String getHtml() {
		return new HtmlDocument(html).toString();
	}
}
