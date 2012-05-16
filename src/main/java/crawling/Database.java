package crawling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import util.Util;

@SuppressWarnings("serial")
public class Database implements Serializable{
	List<Network> networks;
	
	
	public Database(){
		this.networks = new LinkedList<Network>();
	}
	public void load(String filename) {
		Database data = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;

		Util.write("reading object..");
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			data = (Database) in.readObject();
			this.networks = data.getNetworks();
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
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
	
	/**
	 * 
	 * @return returns a list of network states sorted by timestamp
	 */
	public List<Network> getNetworks(){
		return networks;
	}
	
	public void addNetwork(Network net){
		networks.add(net);
		Collections.sort(networks, new NetworkTimeComparator());
	}
}
