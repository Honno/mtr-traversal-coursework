package graph;

import java.util.HashSet;
import java.util.Set;

public class Node<C, W> {
	private C content;
	private Set<Edge<C, W>> edges;

	public Node() {
		content = null;
		edges = new HashSet<Edge<C, W>>();
	}

	public Node(C content) {
		this.content = content;
		edges = new HashSet<Edge<C, W>>();
	}

	public Node(C content, Edge<C, W> edge) {
		this.content = content;
		edges = new HashSet<Edge<C, W>>();
		edges.add(edge);
	}

	public Node(C content, Set<Edge<C, W>> edges) {
		this.content = content;
		this.edges = edges;
	}

	public void setContent(C content) {
		this.content = content;
	}

	public void setEdges(Set<Edge<C, W>> edges) {
		this.edges = edges;
	}

	//returns the contents of the variable contents
	public C getContent() {
		return content;
	}

	public Set<Edge<C, W>> getEdges() {
		return edges;
	}

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
