package view;

import htmlgen.Tag;

public class HtmlDocument {

	String doctype = "<!DOCTYPE html>";
	Tag html;

	public HtmlDocument(Tag html) {
		this.html = html;
	}

	@Override
	public String toString() {
		return doctype + "\n" + html;
	}

}
