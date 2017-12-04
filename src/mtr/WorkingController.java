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

	private Map<String, String[]> lineMap;
	private Map<String, Node<String, String>> nodesMap;

	/**
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
		lineMap = new HashMap<String, String[]>();
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			while ((line = br.readLine()) != null) {

				String[] lineElements = line.split(",");								//split line information by commas
				lineMap.put(lineElements[0], Arrays.copyOfRange(lineElements, 1,		//add the line name and associated terminals
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
		nodesMap = new HashMap<String, Node<String, String>>();

		for (Entry<String, String[]> pair : lineMap.entrySet()) {
			String line = pair.getKey();
			String[] terminals = pair.getValue();

			Iterator<String> itr = Arrays.asList(terminals).iterator();

			boolean first = true;

			Node<String, String> prevNode = null;

			while (itr.hasNext()) {
				String terminal = itr.next();										//retrieve the next terminal
				Node<String, String> node;											
				if (nodesMap.containsKey(terminal)) {								//check if terminal already exists
					node = nodesMap.get(terminal);									//retrieve the terminal
				} else {
					node = new Node<String, String>(terminal);						//create a new terminal
				}
				if (first) {														//check if it is the first iteration
					first = false;													//allow connections to be made
				} else {
					Edge<String, String> edge = new Edge<String, String>(			//create a new terminal link
							prevNode, node, line);
					prevNode.addEdge(edge);											//add the terminal link to two terminals
					node.addEdge(edge);
				}
				nodesMap.put(terminal, node);										//add the terminal name and terminal to the map
				prevNode = node;													//store the previously modified terminal
			}
		}
	}

	@Override
	public String listAllTermini() {
		StringBuffer sb = new StringBuffer();			
		Iterator<String> itr = nodesMap.keySet().iterator();						//iterate through the terminals
		while (itr.hasNext()) {											
			sb.append(itr.next());													//append the next terminal
			if (itr.hasNext()) {
				sb.append(", ");
			}
		}
		return sb.toString();														//return the terminal list
	}

	
	@Override
	public String listStationsInLine(String line) {
		try {
			String[] terminals = lineMap.get(line);									//retrieve the target line
			if (terminals != null) {
				return String.join(", ", terminals);								//concatenate and return the terminals in the line
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
