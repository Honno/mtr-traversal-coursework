package graph;

import java.util.NoSuchElementException;

public class Edge<C, W> {
	private Node<C, W> a, b;
	private W weight;

	public Edge(Node<C, W> a, Node<C, W> b, W weight) {
		this.a = a;
		this.b = b;
		this.weight = weight;
	}

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

	@SuppressWarnings("unchecked")
	public Node<C, W>[] getNodes() {
		Node<C, W>[] nodes = (Node<C, W>[]) new Object[2];
		nodes[0] = a;
		nodes[1] = b;
		return nodes;
	}

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
