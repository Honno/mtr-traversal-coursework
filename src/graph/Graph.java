package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Graph<T, E> {
	private Map<T, Node<T, E>> nodesMap; // Key is the content of the node, for
											// efficient traversal

	public Graph() {
		nodesMap = null;
	}

	public Graph(Set<Node<T, E>> nodes) {
		// TBC
		nodesMap = new HashMap<T, Node<T, E>>();
		for (Node<T, E> node : nodes) {
			nodesMap.put(node.getContent(), node);
		}
	}

	public Graph(Map<T, Node<T, E>> nodesMap) {
		this.nodesMap = nodesMap;
	}

	public int size() {
		return nodesMap.size();
	}
	
	public Map<T, Node<T, E>> getNodesMap() {
		return nodesMap;
	}
	
	public Set<Node<T, E>> getNodes() {
		return new HashSet<Node<T,E>>(nodesMap.values());
	}
	
	public Node<T, E> getNode(T content) {
		return nodesMap.get(content);
	}

	@SuppressWarnings("unchecked")
	public String toString() throws ClassCastException {
		try {
			StringBuffer sb = new StringBuffer();
			Set<Node<String, String>> nodes = new HashSet<Node<String, String>>();
			for (Node<T, E> node : nodesMap.values()) {
				nodes.add((Node<String, String>) node);
			}
			Iterator<Node<String, String>> itr = nodes.iterator();
			while (itr.hasNext()) {
				sb.append(itr.next().toString());
				if (itr.hasNext()) {
					sb.append("\n");
				}
			}
			return sb.toString();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return null;
	}
}
