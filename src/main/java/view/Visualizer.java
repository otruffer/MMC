package view;

import htmlgen.Tag;
import mmc.GPlusAnalyzer;

public class Visualizer {

	private GPlusAnalyzer analyzer;
	private Tag html;

	public Visualizer(GPlusAnalyzer analyzer) {
		this.analyzer = analyzer;
		html = new Tag("html");
		html.add(makeHead());
		html.add(makeBody());
	}

	private Object makeHead() {
		Tag head = new Tag("head");
		// TODO Include some stylesheet here
		return head;
	}

	private Object makeBody() {
		Tag body = new Tag("body");
		body.add("Likes: " + analyzer.getLikes() + "\n");
		body.add("Posts: " + analyzer.getPosts());
		return body;
	}

	public String getHtml() {
		HtmlDocument document = new HtmlDocument(html);
		return document.toString();
	}

}
