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
				+ makeStyleColor()));
		tag.add(statWrapper);
	}

	private void createNameField(Tag tag) {
		Tag div = new Tag("div", "class=name");
		div.add(user.getName());
		tag.add(div);
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

	private String makeStyleColor() {
		Color color = user.getColor();
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		// int alpha = color.getAlpha();
		return "rgb(" + red + "," + green + "," + blue + ")";
	}

	private void makeScaleInto(Tag tag, float number) {
		Tag scale = new Tag("div", "class=scale");
		scale.addAttribute(new Attribute("style", ";width:"
				+ number + "pt"));
		tag.add(scale);
	}

}
