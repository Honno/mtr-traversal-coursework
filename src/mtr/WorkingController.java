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
import java.util.Set;

/**
 * An implementation of the Controller interface that meets the coursework
 * requirements.
 */
public class WorkingController implements Controller {
	// stores line name and array of stations in respective line
	private Map<String, String[]> lineMap;
	// stores station name and Node object of respective station
	private Map<String, Node<String, String>> nodesMap;

	/**
	 * Convert given CSV file to create the respective line map, and use the
	 * line map to create a nodes map.
	 * 
	 * @param path
	 *            the path to the csv file
	 * @throws FileNotFoundException
	 *             in case of file not existing
	 * @throws IOException
	 *             in case of the file reading the file with insufficient
	 *             permissions
	 */
	public WorkingController(String path) throws FileNotFoundException, IOException {
		lineMap = generateLineMap(path);
		nodesMap = generateNodesMap(lineMap);
	}

	/**
	 * Generates a HashMap that represents the MTR network with the station
	 * lines and respective stations.
	 * 
	 * @param path
	 *            path of the file containing the line information
	 * @throws FileNotFoundException
	 *             in case of file path not existing
	 * @throws IOException
	 *             in the case of reading the file with insufficient permissions
	 */
	public HashMap<String, String[]> generateLineMap(String path) throws FileNotFoundException, IOException {
		// initialise map that stores station lines and respective stations
		HashMap<String, String[]> lineMap = new HashMap<String, String[]>();

		// iterate through each line in the csv
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] lineElements = line.split(",");

				// add the line name (first element) and associated stations
				// (subsequent elements)
				lineMap.put(lineElements[0], Arrays.copyOfRange(lineElements, 1, lineElements.length));
			}
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return lineMap;
	}

	/**
	 * Generates a HashMap that represents the MTR network with the stations and
	 * all their immediate connections.
	 * 
	 * @param lineMap
	 *            the lines and their respective stations
	 * @return a map of the stations and their respective Node object
	 */
	public HashMap<String, Node<String, String>> generateNodesMap(Map<String, String[]> lineMap) {
		// initialise map that stores all the station nodes, with the respective
		// station's name as key
		HashMap<String, Node<String, String>> nodesMap = new HashMap<String, Node<String, String>>();

		// iterate through every station line
		for (Entry<String, String[]> pair : lineMap.entrySet()) {
			// retrieve station line
			String line = pair.getKey();
			// retrieve stations
			String[] stations = pair.getValue();

			// declare and initialise a variable to indicate if the current
			// iteration is the first
			boolean first = true;

			// store the node found in the previous iteration to be used in
			// creating an edge
			Node<String, String> prevNode = null;

			// iterate through the stations
			Iterator<String> itr = Arrays.asList(stations).iterator();
			while (itr.hasNext()) {
				// retrieve the next station
				String station = itr.next();

				// retrieve already existing node of the station, or create a
				// new one
				Node<String, String> node;
				if (nodesMap.containsKey(station)) {
					node = nodesMap.get(station);
				} else {
					node = new Node<String, String>(station);
				}

				// check if it is the first iteration
				if (first) {
					// ignore creating an edge between a (non-existent) previous
					// node and tell the subsequent iterations that the first
					// iteration has occurred
					first = false;
					// create edge between current node and previous node
				} else {
					// create a new edge between two nodes
					Edge<String, String> edge = new Edge<String, String>(prevNode, node, line);
					// add the generated edge to the two stations
					prevNode.addEdge(edge);
					node.addEdge(edge);
				}
				// add the station to the nodes map with the key as the
				// station's name
				nodesMap.put(station, node);

				// store the previously modified station
				prevNode = node;
			}
		}

		return nodesMap;
	}

	@Override
	public String listAllTermini() {
		StringBuilder sb = new StringBuilder();

		// iterate through every station line
		Iterator<Entry<String, String[]>> itr = lineMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, String[]> entry = itr.next();

			// add line to buffer
			sb.append(entry.getKey());
			sb.append(": ");

			// add line's respective stations to buffer
			String[] stations = entry.getValue();
			sb.append(stations[0]);
			sb.append(" <-> ");
			sb.append(stations[stations.length - 1]);

			if (itr.hasNext()) {
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	@Override
	public String listStationsInLine(String line) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(line);
			sb.append(": ");

			String[] stations = lineMap.get(line);

			// if the station line exists, concatenate and return the stations
			// in the line
			if (stations != null) {
				sb.append(String.join(" <-> ", stations));
				return sb.toString();
			} else {
				throw new NoSuchElementException("Station line " + line + " does not exist");
			}
		} catch (NoSuchElementException e) {
			return e.getMessage();
		}
	}

	@Override
	public String listAllDirectlyConnectedLines(String line) {
		try {
			String[] stations = lineMap.get(line);

			// if station line exists, list all the connected lines
			if (stations != null) {
				// find all station nodes in the line
				List<Node<String, String>> nodes = new ArrayList<Node<String, String>>();
				for (String station : stations) {
					nodes.add(nodesMap.get(station));
				}

				// initialise store of all connected lines
				Set<String> lines = new HashSet<String>();

				// iterate through every node in the line
				for (Node<String, String> node : nodes) {

					// iterate through every edge of the line
					Set<Edge<String, String>> edges = node.getEdges();
					for (Edge<String, String> edge : edges) {
						// store edge's weight (in this case the connection's
						// line)
						lines.add(edge.getWeight());
					}
				}

				// remove given line from store as it's useless information
				lines.remove(line);

				// concatenate and return the connected lines
				return String.join(", ", lines);
			} else {
				throw new NoSuchElementException("Station line " + line + " does not exist");
			}
		} catch (NoSuchElementException e) {
			return e.getMessage();
		}
	}

	@SuppressWarnings("finally")
	@Override
	public String showPathBetween(String stationA, String stationB) {
		String output = new String();
		StringBuilder sb = new StringBuilder();

		try {
			// retrieves the nodes respective to the users inputed station names
			Node<String, String> start = nodesMap.get(stationA);
			Node<String, String> end = nodesMap.get(stationB);

			// checks if given stations exist
			boolean startIsNull = start == null;
			boolean endIsNull = end == null;
			// if either of the given stations don't exist throw a suitable
			// exception
			if (startIsNull || endIsNull) {
				if (startIsNull && endIsNull) {
					throw new NoSuchElementException(
							"Both " + stationA + " and " + stationB + " stations do not exist");
				} else if (startIsNull) {
					throw new NoSuchElementException("Station " + stationA + " does not exist");
				} else if (endIsNull) {
					throw new NoSuchElementException("Station " + stationB + " does not exist");
				}
			} else {
				// find a path between start and end stations
				List<Edge<String, String>> path = Node.bfs(start, end);

				// store previous node of iteration, initialise with start node
				Node<String, String> prevNode = start;
				// start output with starting node
				sb.append(start.toString() + " -> ");

				// iterate through edges in path
				Iterator<Edge<String, String>> itr = path.iterator();
				while (itr.hasNext()) {
					// store next edge of the path
					Edge<String, String> edge = itr.next();
					// retrieve the other node of the edge by passing the known
					// previous node
					Node<String, String> node = edge.getNode(prevNode);

					// append connecting node to output
					sb.append(node.getContent());

					// if there are more edges to add, append a separator string
					// to output
					if (itr.hasNext()) {
						sb.append(" -> ");
					}

					// store current node as previous node
					prevNode = node;
				}

				output = sb.toString();
			}
		} catch (NoSuchElementException e) {
			output = e.getMessage();
		} finally {
			return output;
		}
	}

}