package view;

import java.awt.Color;

import util.FileUtil;
import crawling.Node;
import htmlgen.Attribute;
import htmlgen.Tag;

public class NodeVisualizer {

	private Node user;

	private final int MAX_SCALE_SIZE = 200;

	public NodeVisualizer(Node user) {
		this.user = user;
	}

	public void renderInto(Tag tag) {
		int received = user.receivedPlusOnes();
		int sent = user.getAllSentPlusOnes();
		float scaleFactor = 0;
		if (sent > 0 || received > 0) {
			scaleFactor = (float) MAX_SCALE_SIZE / Math.max(received, sent);
		}

		Tag statWrapper = new Tag("div", "class=statWrapper");
		createNameField(statWrapper);
		createReceivePlusStats(statWrapper, received, scaleFactor);
		createSendPlusStats(statWrapper, sent, scaleFactor);
		statWrapper.addAttribute(new Attribute("style", "background-color:"
				+ makeStyleColor(user)));

		float ratio = sent == 0 ? 0 : (float) received / sent;
		createRatioStats(statWrapper, ratio);
		tag.add(statWrapper);
	}

	private void createNameField(Tag tag) {
		Tag div = new Tag("div", "class=name");
		Tag arrowLink = new Tag("a", "href=" + user.getProfileURL());
		arrowLink.addAttribute(new Attribute("target", "_blank"));
		arrowLink.addAttribute(new Attribute("class", "profileLink"));
		arrowLink.add("&#8605;");
		div.add(user.getName());
		div.add(arrowLink);
		tag.add(div);
	}

	private void createReceivePlusStats(Tag tag, int num, float scaleFactor) {
		Tag received = new Tag("div", "class=\"stat receiveStat\"");
		received.add("+1s received: <b>" + num + "</b>");
		makeScaleInto(received, num * scaleFactor, true);
		tag.add(received);
	}

	private void createSendPlusStats(Tag tag, int num, float scaleFactor) {
		Tag sent = new Tag("div", "class=\"stat sendStat\"");
		sent.add("+1s sent: <b>" + num + "</b>");
		makeScaleInto(sent, num * scaleFactor, false);
		tag.add(sent);
	}

	private void createRatioStats(Tag tag, float ratio) {
		Tag ratioStat = new Tag("div", "class=\"stat ratioStat\"");
		ratioStat.add("Ratio (received/sent): <b>" + round(ratio) + "</b>");
		ratioStat.add(Tag.br());
		ratioStat.add("Karma (sent/received): <b>"
				+ round(ratio == 0 ? 0 : 1 / ratio) + "</b>");
		tag.add(ratioStat);
	}

	/**
	 * Round a float to a maximum of 2 digits after comma.
	 * 
	 * @param f
	 * @return
	 */
	private float round(float f) {
		return (float) Math.round(f * 100) / 100;
	}

	private String makeStyleColor(Node node) {
		Color color = node.getColor();
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		// int alpha = color.getAlpha();
		return "rgb(" + red + "," + green + "," + blue + ")";
	}

	private void makeScaleInto(Tag tag, float number, boolean received) {
		Tag scale = new Tag("div", "class=scale");
		scale.addAttribute(new Attribute("style", "max-width:" + number + "pt"));
		for (Node node : user.getPlusOners()) {
			Tag scalePart = new Tag("div", "class=scalePart");
			float percentage;
			if (received) {
				percentage = (float) user.getPlusOnesFrom(node.getId()) * 100
						/ user.receivedPlusOnes();
			} else {
				percentage = (float) node.getPlusOnesFrom(user.getId()) * 100
						/ user.getAllSentPlusOnes();
			}
			scalePart.addAttribute(new Attribute("style", "width:" + percentage
					+ "%;background-color:" + makeStyleColor(node)));
			scale.add(scalePart);
		}
		tag.add(scale);
	}
}
