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

/**
 * An implementation of the Controller interface that meets the coursework requirements.
 */
public class WorkingController implements Controller {
	// line name: array of stations in respective line
	private Map<String, String[]> lineMap;
	// station name: Node object of respective station
	private Map<String, Node<String, String>> nodesMap;

	/**
	 * Convert given CSV file to create the respective line map, and use the line map to create a nodes map.
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
	 * Generates a HashMap that represents the MTR line's CSV with the station line and respective stations.
	 * 
	 * @param path path of the file containing the line information
	 * @throws FileNotFoundException in case of file path not existing
	 * @throws IOException in the case of reading the file with insufficient permissions
	 */
	public void generateLineMap(String path)
			throws FileNotFoundException, IOException {
		// initialise map that stores station lines and respective stations
		lineMap = new HashMap<String, String[]>();
		
		//iterate through each line in the csv
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] lineElements = line.split(",");
				
				// add the line name (first element) and associated stations (subsequent elements)
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
	 * Generates a HashMap that represents the MTR stations and their connections
	 */
	public void generateNodesMap() {
		// initialise map that stores all the station nodes, with the respective station's name as key
		nodesMap = new HashMap<String, Node<String, String>>();

		// iterate through every station line
		for (Entry<String, String[]> pair : lineMap.entrySet()) {
			// retrieve station line
			String line = pair.getKey();
			// retrieve stations
			String[] stations = pair.getValue();

			// declare and initialise a variable to indicate if the current iteration is the first
			boolean first = true;

			// store the node found in the previous iteration to be used in creating an edge
			Node<String, String> prevNode = null;

			// iterate through the stations
			Iterator<String> itr = Arrays.asList(stations).iterator();
			while (itr.hasNext()) {
				// retrieve the next station
				String station = itr.next();
				
				// retrieve already existing node of the station, or create a new one
				Node<String, String> node;
				if (nodesMap.containsKey(station)) {
					node = nodesMap.get(station);									
				} else {
					node = new Node<String, String>(station);						
				}
				
				// check if it is the first iteration 
				if (first) {
					// ignore creating an edge between a (non-existent) previous node and tell the subsequent iterations that the first iteration has occurred
					first = false;
				// create edge between current node and previous node
				} else {
					// create a new edge between two nodes
					Edge<String, String> edge = new Edge<String, String>(			
							prevNode, node, line);
					// add the generated edge to the two stations
					prevNode.addEdge(edge);											
					node.addEdge(edge);
				}
				// add the station to the nodes map with the key as the station's name
				nodesMap.put(station, node);
				
				// store the previously modified station
				prevNode = node;													
			}
		}
	}

	@Override
	public String listAllTermini() {
		StringBuffer sb = new StringBuffer();
		
		// iterate through every station line
		Iterator<Entry<String, String[]>> itr = lineMap.entrySet().iterator();
		while(itr.hasNext()) {
			Entry<String, String[]>  entry = itr.next();
			
			// add line to buffer
			sb.append(entry.getKey());
			sb.append(": ");
			
			// add line's respective stations to buffer
			String[] stations = entry.getValue();
			sb.append(stations[0]);
			sb.append(" <-> ");
			sb.append(stations[stations.length - 1]);
			
			if(itr.hasNext()) {
				sb.append("\n");
			}
		}
		
		return sb.toString();
	}

	
	@Override
	public String listStationsInLine(String line) {
		try {
			String[] stations = lineMap.get(line);
			
			// if the station line exists, concatenate and return the stations in the line
			if (stations != null) {
				return String.join(", ", stations);
			} else {
				throw new NoSuchElementException("Station line " + line
						+ " does not exist");
			}
		} catch (NoSuchElementException e) {
			return e.getMessage();
		}
	}

	
	@Override
	public String listAllDirectlyConnectedLines(String line) {
		try {
			String[] stations = lineMap.get(line);											
			if (stations != null) {
				List<Node<String, String>> nodes = new ArrayList<Node<String, String>>();
				for (String station : stations) {
					nodes.add(nodesMap.get(station));
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
				throw new NoSuchElementException("Station line " + line
						+ " does not exist");
			}
		} catch (NoSuchElementException e) {
			return e.getMessage();
		}
	}

	

	@SuppressWarnings("finally")
	@Override
	public String showPathBetween(String stationA, String stationB) {
		String output = new String();
		try {
			StringBuffer sb = new StringBuffer();
			Node<String, String> start = nodesMap.get(stationA);
			Node<String, String> end = nodesMap.get(stationB);
			boolean startIsNull = start == null;
			boolean endIsNull = end == null;
			if (startIsNull || endIsNull) {
				if (startIsNull && endIsNull) {
					throw new NoSuchElementException(
							"Both " + stationA + " and " + stationB + " stations do not exist");
				} else if (startIsNull) {
					throw new NoSuchElementException("Station " + stationA
							+ " does not exist");
				} else if (endIsNull) {
					throw new NoSuchElementException("Station " + stationB
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
		Set<Node<String, String>> searched = new HashSet<Node<String, String>>();

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
			throw new NoSuchElementException("Path between "
					+ start.getContent() + " and " + end.getContent()
					+ " does not exist");
		}
	}

}
