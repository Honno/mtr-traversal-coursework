package graph;

import java.util.Map;
import java.util.Set;

public class Graph<T> {
	private Map<T, Node<T>> nodesMap; // Key is the content of the node, for effecient traversal
	
	public Graph() {
		nodesMap = null;
	}
	
	public Graph(Set<Node<T>> nodes) {
		// TBC
		nodesMap = new Map<T, Node<T>>();
		for(Node<T> node: nodes) {
			
		}
	}
	
	public Graph(Map<T, Node<T>> nodesMap) {
		this.nodesMap = nodesMap;
	}
}
