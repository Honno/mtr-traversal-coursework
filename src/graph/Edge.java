package graph;

public class Edge<T, E> {
	private Node<T, E> a, b;
	private E weight;

	public Edge(Node<T, E> a, Node<T, E> b, E weight) {
		this.a = a;
		this.b = b;
		this.weight = weight;
	}

	public Node<T, E>[] getNodes() {
		@SuppressWarnings("unchecked")
		Node<T, E>[] nodes = (Node<T, E>[]) new Object[2];
		nodes[0] = a;
		nodes[1] = b;
		return nodes;
	}

	public E getWeight() {
		return weight;
	}

	public String toString() throws ClassCastException {
		try {
			return (String) a.getContent() + " <-> " + (String) b.getContent()
					+ " (" + (String) weight + ")";
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return null;
	}
}
