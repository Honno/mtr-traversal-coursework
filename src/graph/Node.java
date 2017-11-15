package graph;

import java.util.Set;

public class Node<T> {
	private T content;
	private Set<Edge<T>> edges;

	public Node() {
		content = null;
		edges = null;
	}
	
	public Node(T content) {
		this.content = content;
		edges = null;
	}
	
	public Node(T content, Set<Edge<T>> edges) {
		this.content = content;
		this.edges = edges;
	}
	
	public void setContent(T content) {
		this.content = content;
	}
	
	public void setEdges(Set<Edge<T>> edges) {
		this.edges = edges;
	}
	
	public T getContent() {
		return content;
	}
	
	public Set<Edge<T>> getEdges() {
		return edges;
	}
}
