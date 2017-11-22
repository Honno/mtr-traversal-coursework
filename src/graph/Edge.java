package graph;

public class Edge<T,E> {
	private Node<T,E> a, b;
    private E weight;
    
    public Edge(Node<T,E> a, Node<T,E> b, E weight) {
    	this.a = a;
    	this.b = b;
    	this.weight = weight;
    }
 
    public Node<T,E>[] getNodes() {
        Node<T,E>[] nodes = (Node<T,E>[]) new Object[2];
        nodes[0] = a;
        nodes[1] = b;
        return nodes;
    }
   
    public E getWeight() {
        return weight;
    }
}
