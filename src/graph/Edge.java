package graph;

import java.util.NoSuchElementException;

/**
 * Represents an edge in a graph data structure.
 * 
 * @param <C> type of object used to represent content of node
 * @param <W> type of object used to represent weight of node's edges
 */
public class Edge<C, W> {
	private Node<C, W> a, b;
	private W weight;

	
	/**
	 * Construct an edge with nodes and a weight. 
	 * 
	 * @param a the first node of the edge
	 * @param b	the second node of the edge
	 * @param weight the weight of the edge
	 */
	public Edge(Node<C, W> a, Node<C, W> b, W weight) {
		this.a = a;
		this.b = b;
		this.weight = weight;
	}

	/**
	 * Takes in a target node that should exist in the edge, and returns the other node in the edge.
	 * 
	 * TODO
	 * above seems way too long 
	 * 
	 * Retrieve a node's corresponding node
	 * Retrieve a node's connecting node
	 * 
	 * @param node the target node
	 * @return the other node in the edge
	 * @throws NoSuchElementException in the case of node given not existing in edge
	 */
	public Node<C, W> getNode(Node<C, W> node) throws NoSuchElementException {
		if (a == node) {
			return b;
		} else if (b == node) {
			return a;
		} else {
			throw new NoSuchElementException("node does not exist in edge");
		}
	}

	/**
	 * @return the two nodes connected to the edge
	 */
	@SuppressWarnings("unchecked")
	public Node<C, W>[] getNodes() {
		Node<C, W>[] nodes = (Node<C, W>[]) new Object[2];
		nodes[0] = a;
		nodes[1] = b;
		return nodes;
	}

	/**
	 * @return the weight of the edge
	 */
	public W getWeight() {
		return weight;
	}

	/*
	 * @throws ClassCastException in the case of the node not containing a non-string content
	 */
	public String toString() throws ClassCastException {
		return (String) a.getContent() + " <-> " + (String) b.getContent()
		+ " (" + (String) weight + ")";
	}

	/**
	 * Retrieves a String representation of a node's connected node
	 * 
	 * @param node the target node
	 * @return the other node's content in the edge, casted as a String
	 * @throws ClassCastException in the case of the node not containing a non-String content 
	 * @throws NoSuchElementException in the case of node given not existing in edge
	 */
	public String toString(Node<C, W> node) throws ClassCastException,
	NoSuchElementException {
		if (a == node) {
			return (String) b.getContent();
		} else if (b == node) {
			return (String) a.getContent();
		} else {
			throw new NoSuchElementException("node does not exist in edge");
		}
	}
}
