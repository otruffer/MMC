package crawling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import util.Util;

import com.google.api.services.plus.Plus;

@SuppressWarnings("serial")
public class Network implements Serializable {
	Map<String, Node> network;

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
		for (String s : node.getPlusOners()) {
			this.put(new Node(s));
			nextNodes.add(network.get(s));
		}
		for (Node n : nextNodes) {
			n.crawl(plus);
		}
		if (depdth != 0)
			for (Node n : nextNodes)
				crawl(n.getId(), depdth - 1, plus);

		node.setPlusReceivers(this.getPlusReceivers(nodeId));
	}

	private List<Node> getPlusReceivers(String nodeId) {
		Node node = network.get(nodeId);
		List<Node> nextNodes = new LinkedList<Node>();
		for (String s : node.getPlusOners())
			nextNodes.add(network.get(s));
		return nextNodes;
	}

	public int getSentPlus(String nodeId) {
		List<Node> nextNodes = this.getPlusReceivers(nodeId);

		int count = 0;
		for (Node n : nextNodes)
			count += n.getPlus(nodeId);
		return count;
	}

	public int getReceivedPlus(String nodeId) {
		Node node = network.get(nodeId);
		return node.getAllSentPlusOnes();
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

	public void serialize(String filename) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void load(String filename) {
		Network net = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;

		Util.write("reading object..");
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			net = (Network) in.readObject();
			this.network = net.getNetwork();
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	public Map<String, Node> getNetwork() {
		return network;
	}
}
