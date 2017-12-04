package graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a node in a graph data structure.
 *
 * @param <C> type of object used to represent content of node
 * @param <W> type of object used to represent weight of node's edges
 */
public class Node<C, W> {
	private C content;
	private Set<Edge<C, W>> edges;

	/**
	 * Construct an empty node.
	 * 
	 */
	public Node() {
		content = null;
		edges = new HashSet<Edge<C, W>>();
	}

	/**
	 * Construct a node with content only.
	 * 
	 * @param content the content of the node
	 */
	public Node(C content) {
		this.content = content;
		edges = new HashSet<Edge<C, W>>();
	}

	
	/**
	 * Construct a node with a single edge.
	 * 
	 * @param content the content of the node
	 * @param edge an edge to add to the node
	 */
	public Node(C content, Edge<C, W> edge) {
		this.content = content;
		edges = new HashSet<Edge<C, W>>();
		edges.add(edge);
	}

	/**
	 * Construct a Node with multiple edges
	 * 
	 * @param content the content of the node
	 * @param edges multiple edges to add to the node
	 */
	public Node(C content, Set<Edge<C, W>> edges) {
		this.content = content;
		this.edges = edges;
	}
	
	/**
	 * @param content new content of the node
	 */
	public void setContent(C content) {
		this.content = content;
	}

	/**
	 * @param edges multiple edges the node has
	 */
	public void setEdges(Set<Edge<C, W>> edges) {
		this.edges = edges;
	}
	
	/**
	 * @return the content of the node
	 */
	public C getContent() {
		return content;
	}

	/**
	 * @return all the edges the node has
	 */
	public Set<Edge<C, W>> getEdges() {
		return edges;
	}

	/**
	 * Add a unique edge to the node.
	 * 
	 * @param edge an edge to add to the node
	 * @return if the node has been added successfully added
	 */
	public boolean addEdge(Edge<C, W> edge) {
		if (edges.contains(edge)) {
			return false;
		} else {
			edges.add(edge);
			return true;
		}
	}
	
	public String toString() throws ClassCastException {
		return (String) getContent();
	}
}
