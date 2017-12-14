package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a node in a graph data structure.
 * 
 * @param <C>
 *            type of object used to represent content of node
 * @param <W>
 *            type of object used to represent weight of node's edges
 */
public class Node<C, W> {
	private C content;
	private Set<Edge<C, W>> edges;

	/**
	 * Construct an empty node.
	 */
	public Node() {
		content = null;
		edges = new HashSet<Edge<C, W>>();
	}

	/**
	 * Construct a node with content only.
	 * 
	 * @param content
	 *            the content of the node
	 */
	public Node(C content) {
		this.content = content;
		edges = new HashSet<Edge<C, W>>();
	}

	/**
	 * Construct a node with a single edge.
	 * 
	 * @param content
	 *            the content of the node
	 * @param edge
	 *            an edge to add to the node
	 */
	public Node(C content, Edge<C, W> edge) {
		this.content = content;
		edges = new HashSet<Edge<C, W>>();
		edges.add(edge);
	}

	/**
	 * Construct a Node with multiple edges.
	 * 
	 * @param content
	 *            the content of the node
	 * @param edges
	 *            multiple edges to add to the node
	 */
	public Node(C content, Set<Edge<C, W>> edges) {
		this.content = content;
		this.edges = edges;
	}

	/**
	 * @param content
	 *            new content of the node
	 */
	public void setContent(C content) {
		this.content = content;
	}

	/**
	 * @param edges
	 *            multiple edges the node has
	 */
	public void setEdges(Set<Edge<C, W>> edges) {
		this.edges = edges;
	}

	/**
	 * @return the content of the node
	 */
	public C getContent() {
		return content;
	}

	/**
	 * @return all the edges the node has
	 */
	public Set<Edge<C, W>> getEdges() {
		return edges;
	}

	/**
	 * Add an edge to the node.
	 * 
	 * @param edge
	 *            an edge to add to the node
	 * @return if the node has been added successfully added
	 */
	public boolean addEdge(Edge<C, W> edge) {
		if (edges.contains(edge)) {
			return false;
		} else {
			edges.add(edge);
			return true;
		}
	}

	/*
	 * @throws ClassCastException in the case of the node containing content of
	 * a non-String type
	 */
	@Override
	public String toString() throws ClassCastException {
		return (String) getContent();
	}

	/**
	 * Finds a path between start and end nodes.
	 * 
	 * @param start
	 *            the starting node
	 * @param end
	 *            the ending node
	 * @return a path between start and end nodes
	 * @throws NoSuchElementException
	 *             when there is no path between start and end nodes
	 */
	public static <C, W> List<Edge<C, W>> bfs(Node<C, W> start, Node<C, W> end) throws NoSuchElementException {
		// initialises a queue that stores nodes to search
		Queue<Node<C, W>> toSearch = new ConcurrentLinkedQueue<Node<C, W>>();
		// initialises a set that stores nodes already searched
		Set<Node<C, W>> searched = new HashSet<Node<C, W>>();

		// initialises a map that stores paths to nodes from the start node
		Map<Node<C, W>, List<Edge<C, W>>> pathToNodes = new HashMap<Node<C, W>, List<Edge<C, W>>>();
		// declare path from start to end nodes
		List<Edge<C, W>> path = null;

		// put the path to the start node from the start node as empty and adds start node as the first node to search
		pathToNodes.put(start, new ArrayList<Edge<C, W>>());
		toSearch.add(start);

		// keep searching for path between start and end nodes while searchable
		// nodes exist
		while (!toSearch.isEmpty()) {
			Node<C, W> parent = toSearch.remove();

			if (!parent.equals(end)) {
				for (Edge<C, W> edge : parent.getEdges()) {
					Node<C, W> child = edge.getNode(parent);

					// if child has already been searched, skip finding the child's path
					if (searched.contains(child)) {
						continue;
					}

					// check if child is not to be searched now
					if (!toSearch.contains(child)) {
						// store path to parent node and add path between parent and child nodes
						List<Edge<C, W>> pathTochild = new ArrayList<Edge<C, W>>(pathToNodes.get(parent));
						pathTochild.add(edge);
						// store path to child node from start node
						pathToNodes.put(child, pathTochild);
						
						// add child to queue of nodes to be searched
						toSearch.add(child);
					}
				}

				// add parent node to set of nodes already searched
				searched.add(parent);
			} else {
				// remove all nodes from to be searched queue
				toSearch.clear();

				// retrieve path to end node
				path = pathToNodes.get(end);
			}
		}

		// check if path exists
		if (path != null) {
			return path;
		} else {
			throw new NoSuchElementException("Path between nodes do not exist");
		}
	}
}
