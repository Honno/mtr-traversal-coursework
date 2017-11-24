package graph;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
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
	
	public List<Edge<T,E>> bfs(Node<T,E> start, Node<T,E> end) throws NoSuchElementException {
		Queue<Node<T,E>> toSearch = new PriorityQueue<Node<T,E>>();
		Queue<Node<T,E>> searched = new PriorityQueue<Node<T,E>>();
		
		Map<Node<T,E>, List<Edge<T,E>>> pathToNodes = new HashMap<Node<T,E>, List<Edge<T,E>>>();
		
		pathToNodes.put(start, new ArrayList<Edge<T,E>>());
		toSearch.add(start);
		
		while(!toSearch.isEmpty()) {
			Node<T,E> parent = toSearch.remove();
			if(parent.equals(end)) {
				// construct path
			}
			for(Edge<T,E> edge : parent.getEdges()) {
				Node<T,E> connectedNode = edge.getNode(parent);
				if(searched.contains(connectedNode)) {
					continue;
				}
				if(!toSearch.contains(connectedNode)) {
					List<Edge<T,E>> pathToConnectedNode = new ArrayList<Edge<T,E>>(pathToNodes.get(parent));
					pathToConnectedNode.add(edge);
					pathToNodes.put(connectedNode, pathToConnectedNode);
					toSearch.add(connectedNode);
				}
			}
			searched.add(parent);
		}
		
		List<Edge<T,E>> path =  pathToNodes.get(end);
		if(path != null) {
			return path;
		} else {
			throw new NoSuchElementException();
		}
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
