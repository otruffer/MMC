package crawling;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import util.Util;

import com.google.api.services.plus.Plus;

/**
 * repersents a network in GPLus with crawl possibilities.
 * 
 * @author otruffer
 * 
 */
public class Network implements Serializable {
	Map<String, Node> network;
	Date timestamp;
	String center;
	int depdth;

	public Network() {
		this.network = new HashMap<String, Node>();
	}

	public void put(Node node) {
		if (!network.containsKey(node.getId()))
			this.network.put(node.getId(), node);
	}

	public Node get(String id) {
		return this.network.get(id);
	}

	public void crawl(String nodeId, int depdth, Plus plus) throws IOException {
		if (!network.containsKey(nodeId))
			return;
		Node node = network.get(nodeId);
		node.crawl(plus);
		List<Node> nextNodes = new LinkedList<Node>();
		for (String s : node.getPlusOnerIds()) {
			this.put(new Node(s));
			nextNodes.add(network.get(s));
		}
		for (Node n : nextNodes) {
			n.crawl(plus);
		}
		if (depdth != 0)
			for (Node n : nextNodes)
				crawl(n.getId(), depdth - 1, plus);

		node.setPlusOners(this.getPlusSenders(nodeId));
		this.timestamp = new Date();
		this.center = nodeId;
	}

	private List<Node> getPlusSenders(String nodeId) {
		Node node = network.get(nodeId);
		List<Node> nextNodes = new LinkedList<Node>();
		for (String s : node.getPlusOnerIds()) {
			Node n = network.get(s);
			nextNodes.add(n);
		}
		return nextNodes;
	}

	public int getSentPlus(String nodeId) {
		return network.get(nodeId).getAllSentPlusOnes();
	}

	public int getReceivedPlus(String nodeId) {
		Node node = network.get(nodeId);
		return node.receivedPlusOnes();
	}

	public float getRatio(String nodeId) {
		if (getReceivedPlus(nodeId) == 0)
			return 0;
		return getSentPlus(nodeId) / getReceivedPlus(nodeId);
	}

	public void write() {
		Util.write("-------------------------------");
		for (String key : network.keySet()) {
			network.get(key).write();
			Util.write("-------------------------------");
		}
	}

	public Map<String, Node> getNetwork() {
		return network;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public int getDepdth() {
		return depdth;
	}

	public void setDepdth(int depdth) {
		this.depdth = depdth;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
