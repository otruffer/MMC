package view;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

public class HtmlPrinter {

	public static void showHtml(String html) {
		openInBrowser(html);
	}

	private static void openInBrowser(String html) {
		try {
			File file = new File(System.getProperty("user.dir")
					+ "/tempfiles/output.html");
			new File(file.getParent()).mkdirs();
			FileWriter fw = new FileWriter(file.getPath());
			BufferedWriter buf = new BufferedWriter(fw);
			buf.write(html);
			buf.close();

			URI uri = file.toURI();
			Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
