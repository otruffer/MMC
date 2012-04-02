import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import util.Util;


import com.google.api.services.plus.Plus;


public class Network {
  Map<String, Node> network;

  public Network() {
    this.network = new HashMap<String, Node>();
  }
  
  public void put(Node node){
    if(!network.containsKey(node.getId()))
      this.network.put(node.getId(), node);
  }
  
  public Node get(String id){
    return this.network.get(id);
  }
  
  public void crawl(String nodeId, int depdth, Plus plus) throws IOException{
    if(!network.containsKey(nodeId))
      return;
    Node node = network.get(nodeId);
    node.crawl(plus);
    List<Node> nextNodes = new LinkedList<Node>();
    for (String s : node.getPlusOners()){
      this.put(new Node(s));
      nextNodes.add(network.get(s)); 
    }
    for(Node n : nextNodes){
      n.crawl(plus);
    }
    if(depdth!=0)
      for(Node n : nextNodes)
        crawl(n.getId(), depdth - 1, plus);
  }
  
  public float getSendPlus(String nodeId){
    Node node = network.get(nodeId);
    List<Node> nextNodes = new LinkedList<Node>();
    for (String s : node.getPlusOners()){
      //this.put(new Node(s));
      nextNodes.add(network.get(s)); 
    }
    float count = 0;
    for(Node n: nextNodes)
      count += n.getPlus(nodeId);
    return count;
  }
  
  public float getReceivedPlus(String nodeId){
    Node node = network.get(nodeId);
    return node.getAllPlus();
  }
  
  public float getRatio(String nodeId){
    if(getReceivedPlus(nodeId) == 0)
      return 0;
    return getSendPlus(nodeId) / getReceivedPlus(nodeId);
  }
  
  public void write(){
    Util.write("-------------------------------");
    for(String key : network.keySet()){
      network.get(key).write();
      Util.write("-------------------------------");
    }
  }
}
