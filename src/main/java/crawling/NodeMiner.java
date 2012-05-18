package crawling;

import java.io.IOException;

import com.google.api.services.plus.Plus;

public class NodeMiner implements Runnable {

	private Node node;
	private Plus plus;

	public NodeMiner(Node node, Plus plus) {
		this.node = node;
		this.plus = plus;
	}

	@Override
	public void run() {
		try {
			node.crawl(plus);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
