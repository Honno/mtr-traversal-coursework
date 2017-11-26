package graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Node<T, E> implements Comparable {
	private T content;
	private Set<Edge<T, E>> edges;

	public Node() {
		content = null;
		edges = new HashSet<Edge<T, E>>();
	}

	public Node(T content) {
		this.content = content;
		edges = new HashSet<Edge<T, E>>();
	}

	public Node(T content, Edge<T, E> edge) {
		this.content = content;
		edges = new HashSet<Edge<T, E>>();
		edges.add(edge);
	}

	public Node(T content, Set<Edge<T, E>> edges) {
		this.content = content;
		this.edges = edges;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public void setEdges(Set<Edge<T, E>> edges) {
		this.edges = edges;
	}

	public T getContent() {
		return content;
	}

	public Set<Edge<T, E>> getEdges() {
		return edges;
	}

	public boolean addEdge(Edge<T, E> edge) {
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

	public String toStringEdges() throws ClassCastException {
		try {
			StringBuffer sb = new StringBuffer();
			String contentString = (String) content;
			sb.append(contentString);
			if (edges.size() != 0) {
				sb.append(": ");
				Iterator<Edge<T, E>> itr = edges.iterator();
				while (itr.hasNext()) {
					sb.append(itr.next().toString());
					if (itr.hasNext()) {
						sb.append(", ");
					}
				}
			}
			return sb.toString();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
