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
	 * @param path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public WorkingController(String path) throws FileNotFoundException,
			IOException {
		try {
			lineMap = generateMap(path);
			nodesMap = new HashMap<String, Node<String, String>>();

			for (Entry<String, String[]> pair : lineMap.entrySet()) {
				String line = pair.getKey();
				String[] terminals = pair.getValue();

				Iterator<String> itr = Arrays.asList(terminals).iterator();

				boolean first = true;

				Node<String, String> prevNode = null;

				while (itr.hasNext()) {
					String terminal = itr.next();
					Node<String, String> node;
					if (nodesMap.containsKey(terminal)) {
						node = nodesMap.get(terminal);
					} else {
						node = new Node<String, String>(terminal);
					}
					if (first) {
						first = false;
					} else {
						Edge<String, String> edge = new Edge<String, String>(
								prevNode, node, line);
						prevNode.addEdge(edge);
						node.addEdge(edge);
					}
					nodesMap.put(terminal, node);
					prevNode = node;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("finally")
	public HashMap<String, String[]> generateMap(String path)
			throws FileNotFoundException, IOException {
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		String line = "";

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			while ((line = br.readLine()) != null) {

				String[] lineElements = line.split(",");
				map.put(lineElements[0], Arrays.copyOfRange(lineElements, 1,
						lineElements.length));
			}

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			return map;
		}
	}

	@Override
	public String listAllTermini() {
		StringBuffer sb = new StringBuffer();
		Iterator<String> itr = nodesMap.keySet().iterator();
		while (itr.hasNext()) {
			sb.append(itr.next());
			if (itr.hasNext()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	@Override
	public String listStationsInLine(String line) {
		try {
			String[] terminals = lineMap.get(line);
			if (terminals != null) {
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
		// 
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
