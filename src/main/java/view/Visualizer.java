package view;

import util.FileUtil;
import crawling.Node;
import htmlgen.Attribute;
import htmlgen.Tag;

public class Visualizer {

	private Tag html;
	private Node user;

	private final int MAX_SCALE_SIZE = 200;

	public Visualizer(Node user) {
		this.user = user;
		html = new Tag("html");
		html.add(makeHead());
		html.add(makeBody());
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
		createReceivePlusStats(tag);
		tag.add(Tag.br());
		createSendPlusStats(tag);
	}

	private void createReceivePlusStats(Tag tag) {
		Tag received = new Tag("div", "class=\"stat receiveStat\"");
		received.add("+1s received: " + user.receivedPlusOnes());
		makeScaleInto(received, user.receivedPlusOnes());
		tag.add(received);
	}

	private void createSendPlusStats(Tag tag) {
		Tag sent = new Tag("div", "class=\"stat sendStat\"");
		sent.add("+1s sent: " + user.getAllSentPlusOnes());
		makeScaleInto(sent, user.getAllSentPlusOnes());
		tag.add(sent);
	}

	private void makeScaleInto(Tag sent, int number) {
		Tag scale = new Tag("div", "class=scale");
		scale.addAttribute(new Attribute("style", "padding:2pt 0;width:"
				+ number + "pt"));
		sent.add(scale);
	}

	public String getHtml() {
		HtmlDocument document = new HtmlDocument(html);
		return document.toString();
	}

}
