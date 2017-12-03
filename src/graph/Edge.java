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
	 * Construct edge with two passed nodes and passed weight. 
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
	 * @param target node
	 * @return result node
	 * @throws NoSuchElementException in the case of node not existing
	 */
	public Node<C, W> getNode(Node<C, W> node) throws NoSuchElementException {
		try {
			if (a == node) {
				return b;
			} else if (b == node) {
				return a;
			} else {
				throw new NoSuchElementException("node does not exist in edge");
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		return null;
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

	public String toString() throws ClassCastException {
		try {
			return (String) a.getContent() + " <-> " + (String) b.getContent()
			+ " (" + (String) weight + ")";
		} catch (ClassCastException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * Retrieves the content of the connected node using a known node.
	 * 
	 * @param node the known node
	 * @return contents of the other node
	 * @throws ClassCastException in the case of the node not containing a non-string content 
	 * @throws NoSuchElementException in the case of node not existing
	 */
	public String toString(Node<C, W> node) throws ClassCastException,
	NoSuchElementException {
		try {
			if (a == node) {
				return (String) b.getContent();
			} else if (b == node) {
				return (String) a.getContent();
			} else {
				throw new NoSuchElementException("node does not exist in edge");
			}
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			return cce.getMessage();
		} catch (NoSuchElementException nsee) {
			nsee.printStackTrace();
			return nsee.getMessage();
		}
	}
}
