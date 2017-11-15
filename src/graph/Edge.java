package graph;

public class Edge<T> {
	private Node<T> a, b;
	private String weight;

	public Node<T>[] getNodes() {
		Node<T>[] nodes = (Node<T>[]) new Object[2];
		nodes[0] = a;
		nodes[1] = b;
		return nodes;
	}
	
	public String getWeight() {
		return weight;
	}
}
