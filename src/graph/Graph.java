package graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Graph<T,E> {
	private Map<T, Node<T, E>> nodesMap; // Key is the content of the node, for effecient traversal
	   
    public Graph() {
        nodesMap = null;
    }
   
    public Graph(Set<Node<T, E>> nodes) {
        // TBC
        nodesMap = new HashMap<T, Node<T, E>>();
        for(Node<T,E> node: nodes) {
            nodesMap.put(node.getContent(), node);
        }
    }
   
    public Graph(Map<T, Node<T, E>> nodesMap) {
        this.nodesMap = nodesMap;
    }
    
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	for(Node<T, E> node: nodesMap.values()) {
    		sb.append(node.toString());
    		sb.append("");
    	}
    	return sb.toString();
    }
}
