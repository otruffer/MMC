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
		body.add(makeAverage());
		Tag mainWrapper = new Tag("div", "class=mainWrapper");
		showPlusStatistics(mainWrapper);
		body.add(mainWrapper);
		return body;
	}

	private Tag makeAverage() {
		Tag average = new Tag("div", "class=average");
		average.add(averageItem("+1s received",
				roundOn2(computeAverageReceived())));
		average.add(averageItem("+1s sent", roundOn2(computeAverageSent())));
		average.add(averageItem("ratio", roundOn2(computeAverageRatio())));
		average.add(averageItem("karma", roundOn2(computeAverageKarma())));
		return average;
	}

	private float computeAverageReceived() {
		return (float) computeSumReceived() / (user.getPlusOners().size() + 1);
	}

	private float computeAverageSent() {
		return (float) computeSumSent() / (user.getPlusOners().size() + 1);
	}

	private int computeSumReceived() {
		int received = user.receivedPlusOnes();
		for (Node node : user.getPlusOners()) {
			received += node.receivedPlusOnes();
		}
		return received;
	}

	private int computeSumSent() {
		int sent = user.getAllSentPlusOnes();
		for (Node node : user.getPlusOners()) {
			sent += node.getAllSentPlusOnes();
		}
		return sent;
	}

	private float computeAverageRatio() {
		int received = computeSumReceived();
		int sent = computeSumSent();

		if (sent == 0)
			return 0;

		return (float) received / sent;
	}

	private float computeAverageKarma() {
		float ratio = computeAverageRatio();
		if (ratio <= 0)
			return 0;
		return 1 / ratio;
	}

	private Tag averageItem(String key, Object value) {
		Tag span = new Tag("span", "class=averageItem");
		span.add("Average " + key + ": <b>" + value + "</b>");
		return span;
	}

	/**
	 * Round a given float to another with a maximum of 2 digits after comma.
	 * 
	 * @return rounded float
	 */
	private float roundOn2(float f) {
		return (float) Math.round(100 * f) / 100;
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
		Tag img = new Tag(
				"img",
				"src=https://ssl.gstatic.com/s2/oz/images/google-logo-plus-0fbe8f0119f4a902429a5991af5db563.png");
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
