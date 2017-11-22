package graph;

import java.util.HashSet;
import java.util.Set;

public class Node<T,E> {
	private T content;
    private Set<Edge<T,E>> edges;
 
    public Node() {
        content = null;
        edges = new HashSet<Edge<T,E>>();
    }
   
    public Node(T content) {
        this.content = content;
        edges = new HashSet<Edge<T,E>>();
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
    	if(edges.contains(edge)) {
			return false;
		} else {
    		edges.add(edge);
    		return true;
    	}
    }
    
    public String toString() throws ClassCastException {
    	try {
    		String contentString = (String) content;
    		return contentString;
    	} catch(ClassCastException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
}
