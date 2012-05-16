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
		int received = user.receivedPlusOnes();
		int sent = user.getAllSentPlusOnes();
		float scaleFactor = 0;
		if (sent > 0 || received > 0) {
			scaleFactor = (float) MAX_SCALE_SIZE / Math.max(received, sent);
		}

		Tag statWrapper = new Tag("div", "class=statWrapper");
		createReceivePlusStats(statWrapper, received, scaleFactor);
		createSendPlusStats(statWrapper, sent, scaleFactor);
		tag.add(statWrapper);
	}

	private void createReceivePlusStats(Tag tag, int num, float scaleFactor) {
		Tag received = new Tag("div", "class=\"stat receiveStat\"");
		received.add("+1s received: " + num);
		makeScaleInto(received, num * scaleFactor);
		tag.add(received);
	}

	private void createSendPlusStats(Tag tag, int num, float scaleFactor) {
		Tag sent = new Tag("div", "class=\"stat sendStat\"");
		sent.add("+1s sent: " + num);
		makeScaleInto(sent, num * scaleFactor);
		tag.add(sent);
	}

	private void makeScaleInto(Tag sent, float number) {
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
