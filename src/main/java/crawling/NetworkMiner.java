package crawling;

import java.io.IOException;

import com.google.api.services.plus.Plus;

public class NetworkMiner implements Runnable {

	private Network network;
	private String userId;
	private int depth;
	private Plus plus;

	public NetworkMiner(Network network, String userId, int depth, Plus plus) {
		this.network = network;
		this.userId = userId;
		this.depth = depth;
		this.plus = plus;
	}

	@Override
	public void run() {
		try {
			network.crawl(userId, depth, plus);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
