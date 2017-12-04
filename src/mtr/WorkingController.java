package mtr;

import graph.Edge;
import graph.Node;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkingController implements Controller {
	// key: terminal line, value: array of terminals in respective line
	private Map<String, String[]> lineMap;
	// key: terminal name, value: respective Node object of terminal
	private Map<String, Node<String, String>> nodesMap;

	/**
	 * Convert given CSV file to create the respective line map, and use that to create a nodes map.
	 * 
	 * @param path the path to the csv file
	 * @throws FileNotFoundException in case of file not existing
	 * @throws IOException in case of the file reading the file with insufficient permissions
	 */
	public WorkingController(String path) throws FileNotFoundException,
			IOException {
		generateLineMap(path);
		generateNodesMap();
	}

	/**
	 * Generates a HashMap that represents the MTR line's CSV with the terminal line and respective terminals.
	 * 
	 * @param path path of the file containing the line information
	 * @throws FileNotFoundException in case of file path not existing
	 * @throws IOException in the case of reading the file with insufficient permissions
	 */
	public void generateLineMap(String path)
			throws FileNotFoundException, IOException {
		// initialise map that stores terminal lines and respective terminals
		lineMap = new HashMap<String, String[]>();
		
		//iterate through each line in the csv
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				// split line elements by commas
				String[] lineElements = line.split(",");
				// add the line name (first element) and associated terminals (subsequent elements)
				lineMap.put(lineElements[0], Arrays.copyOfRange(lineElements, 1,		
						lineElements.length));
			}
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Generates a HashMap that represents the MTR terminals and their connections
	 * 
	 */
	public void generateNodesMap() {
		// initialise map that stores all the terminal nodes, with the respective terminal's name as key
		nodesMap = new HashMap<String, Node<String, String>>();

		// iterate through every terminal line
		for (Entry<String, String[]> pair : lineMap.entrySet()) {
			// retrieve terminal line
			String line = pair.getKey();
			// retrieve terminals
			String[] terminals = pair.getValue();

			// declare and initialise a variable to indicate if the current iteration is the first
			boolean first = true;

			// store the node found in the previous iteration to be used in creating an edge
			Node<String, String> prevNode = null;

			// iterate through the terminals
			Iterator<String> itr = Arrays.asList(terminals).iterator();
			while (itr.hasNext()) {
				// retrieve the next terminal
				String terminal = itr.next();
				
				// find already existing node of the terminal, or create a new one
				Node<String, String> node;
				if (nodesMap.containsKey(terminal)) {
					node = nodesMap.get(terminal);									
				} else {
					node = new Node<String, String>(terminal);						
				}
				
				// check if it is the first iteration 
				if (first) {
					// ignore creating an edge between a (non-existent) previous node
					// tell the subsequent iterations that the first iteration has occurred
					first = false;
				// create edge between current node and previous node
				} else {
					// create a new edge between two nodes
					Edge<String, String> edge = new Edge<String, String>(			
							prevNode, node, line);
					// add the generated edge to the two terminals
					prevNode.addEdge(edge);											
					node.addEdge(edge);
				}
				// add the terminal to the nodes map with the key as the terminal's name
				nodesMap.put(terminal, node);
				
				// store the previously modified terminal
				prevNode = node;													
			}
		}
	}

	@Override
	public String listAllTermini() {
		StringBuffer sb = new StringBuffer();
		
		// iterate through the terminals
		Iterator<String> itr = nodesMap.keySet().iterator();						
		while (itr.hasNext()) {											
			//append the next terminal to the string buffer
			sb.append(itr.next());
			
			// if there is another terminal to add in the next iteration, add a comma separator
			if (itr.hasNext()) {
				sb.append(", ");
			}
		}
		return sb.toString();														
	}

	
	@Override
	public String listStationsInLine(String line) {
		try {
			// retrieve the the terminals of the passed in terminal line
			String[] terminals = lineMap.get(line);
			
			// check if terminal line exists for error handling
			if (terminals != null) {
				// if the terminal line exists, concatenate and return the terminals in the line
				return String.join(", ", terminals);								
			} else {
				throw new NoSuchElementException("terminal line " + line
						+ " does not exist");
			}
		} catch (NoSuchElementException e) {
			return e.getMessage();
		}
	}

	
	@Override
	public String listAllDirectlyConnectedLines(String line) {
		try {
			String[] terminals = lineMap.get(line);											
			if (terminals != null) {
				List<Node<String, String>> nodes = new ArrayList<Node<String, String>>();
				for (String terminal : terminals) {
					nodes.add(nodesMap.get(terminal));
				}
				Set<String> lines = new HashSet<String>();
				for (Node<String, String> node : nodes) {
					Set<Edge<String, String>> edges = node.getEdges();
					for (Edge<String, String> edge : edges) {
						lines.add(edge.getWeight());
					}
				}

				lines.remove(line);

				return String.join(", ", lines);
			} else {
				throw new NoSuchElementException("terminal line " + line
						+ " does not exist");
			}
		} catch (NoSuchElementException e) {
			return e.getMessage();
		}
	}

	

	@SuppressWarnings("finally")
	@Override
	public String showPathBetween(String terminalA, String terminalB) {
		String output = new String();
		try {
			StringBuffer sb = new StringBuffer();
			Node<String, String> start = nodesMap.get(terminalA);
			Node<String, String> end = nodesMap.get(terminalB);
			boolean startIsNull = start == null;
			boolean endIsNull = end == null;
			if (startIsNull || endIsNull) {
				if (startIsNull && endIsNull) {
					throw new NoSuchElementException(
							"terminals provided do not exist");
				} else if (startIsNull) {
					throw new NoSuchElementException("terminal " + terminalA
							+ " does not exist");
				} else if (endIsNull) {
					throw new NoSuchElementException("terminal " + terminalB
							+ " does not exist");
				}
			} else {
				List<Edge<String, String>> path = bfs(start, end);
				Iterator<Edge<String, String>> itr = path.iterator();
				Node<String, String> prevNode = start;
				sb.append(start.toString() + " -> ");
				while (itr.hasNext()) {
					Edge<String, String> edge = itr.next();
					Node<String, String> node = edge.getNode(prevNode);
					sb.append(node.getContent() + " (" + edge.getWeight() + ")");
					prevNode = node;
					if (itr.hasNext()) {
						sb.append(" -> ");
					}
				}
				output = sb.toString();

			}
		} catch (NoSuchElementException e) {
			output = e.getMessage();
		} finally {
			return output;
		}
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 * @throws NoSuchElementException
	 */
	public List<Edge<String, String>> bfs(Node<String, String> start,
			Node<String, String> end) throws NoSuchElementException {
		Queue<Node<String, String>> toSearch = new ConcurrentLinkedQueue<Node<String, String>>();
		Queue<Node<String, String>> searched = new ConcurrentLinkedQueue<Node<String, String>>();

		Map<Node<String, String>, List<Edge<String, String>>> pathToNodes = new HashMap<Node<String, String>, List<Edge<String, String>>>();

		pathToNodes.put(start, new ArrayList<Edge<String, String>>());
		toSearch.add(start);

		while (!toSearch.isEmpty()) {

			Node<String, String> parent = toSearch.remove();
			if (!parent.equals(end)) {
				for (Edge<String, String> edge : parent.getEdges()) {
					Node<String, String> connectedNode = edge.getNode(parent);
					if (searched.contains(connectedNode)) {
						continue;
					}
					if (!toSearch.contains(connectedNode)) {
						List<Edge<String, String>> pathToConnectedNode = new ArrayList<Edge<String, String>>(
								pathToNodes.get(parent));
						pathToConnectedNode.add(edge);
						pathToNodes.put(connectedNode, pathToConnectedNode);
						toSearch.add(connectedNode);
					}
				}
				searched.add(parent);
			} else {
				toSearch.clear();
			}
		}

		List<Edge<String, String>> path = pathToNodes.get(end);

		if (path != null) {
			return path;
		} else {
			throw new NoSuchElementException("path between "
					+ start.getContent() + " and " + end.getContent()
					+ " does not exist");
		}
	}

}
