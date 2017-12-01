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
	 * Construct a Node object with no parameters, initialising empty content and edges as an empty set.
	 */
	public Node() {
		content = null;
		edges = new HashSet<Edge<C, W>>();
	}

	/**
	 * Construct a Node object with given parameter as the content, and initialise edges as an empty set.
	 * 
	 * @param content the content of the node
	 */
	public Node(C content) {
		this.content = content;
		edges = new HashSet<Edge<C, W>>();
	}

	
	/**
	 * Construct a Node object with parameters as the contents and edges. The edges attribute of the node is initialised as an empty set, then the passed edge is added to it.
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
	 * Construct a Node object with parameters as the contents and edges.
	 * 
	 * @param content the content of the node
	 * @param edges multiple edges to add to the node
	 */
	public Node(C content, Set<Edge<C, W>> edges) {
		this.content = content;
		this.edges = edges;
	}

	/**
	 * @param content the content of the node
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
	 * @return TODO set of all the edges the node has
	 */
	public Set<Edge<C, W>> getEdges() {
		return edges;
	}

	/**
	 * Adds an edge to the node if the edge does not exist in it already, telling the user whether the operation was successful.
	 * 
	 * @param edge an edge to add to the node
	 * @return TODO
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
		try {
			return (String) getContent();
		} catch (ClassCastException e) {
			throw new ClassCastException();
		}
	}
}
