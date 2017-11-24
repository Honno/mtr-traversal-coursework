package mtr;

import graph.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

public class WorkingController implements Controller {

	private final static Logger LOG = Logger.getLogger(WorkingController.class
			.getName());

	private HashMap<String, String[]> map;
	private Graph<String, String> graph;

	public WorkingController(String path) throws IOException {
		map = generateMap(path);
		if (map != null) {
			Map<String, Node<String, String>> nodesMap = new HashMap<String, Node<String, String>>();

			for (Entry<String, String[]> pair : map.entrySet()) {
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
			graph = new Graph<String, String>(nodesMap);
			//LOG.info(graph.toString());
		} else {
			throw new IOException();
		}
	}

	public HashMap<String, String[]> generateMap(String path) {
		// HashMap to represent MTR, with the MTR line's name as key and the
		// respective terminis as the value
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		String line = "";

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			while ((line = br.readLine()) != null) {

				String[] lineElements = line.split(",");
				map.put(lineElements[0], Arrays.copyOfRange(lineElements, 1,
						lineElements.length));
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return map;
	}

	@Override
	public String listAllTermini() {
		StringBuffer sb = new StringBuffer();
		Iterator<Node<String,String>> itr = (Iterator<Node<String,String>>) graph.getNodes().iterator();
		while(itr.hasNext()) {
			sb.append((String) itr.next().getContent());
			if(itr.hasNext()) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	@Override
	public String listStationsInLine(String line) throws NullPointerException {
		try {
			String[] terminals = map.get(line);
			return String.join(", ", terminals);
		} catch (NullPointerException e) {
			//
		}
		return "Terminal line does not exist";
	}

	@Override
	public String listAllDirectlyConnectedLines(String station) {
		Node<String,String> node = graph.getNode(station);
		Iterator<Edge<String,String>> itr = (Iterator<Edge<String,String>>) node.getEdges().iterator();
		Set<String> lines = new HashSet<String>();
		while(itr.hasNext()) {
			lines.add((String) itr.next().getWeight());
		}
		return String.join("\n", lines);
	}

	@Override
	public String showPathBetween(String stationA, String stationB) {
		StringBuffer sb = new StringBuffer();
		
		try {
			Node<String,String> start = graph.getNode(stationA);
			List<Edge<String,String>> path = graph.bfs(start, graph.getNode(stationB));
			Iterator<Edge<String,String>> itr = path.iterator();
			Node<String,String> prevNode = start;
			while(itr.hasNext()) {
				Node<String,String>  node = itr.next().getNode(prevNode);
				sb.append(node.getContent());
				prevNode = node;
				if(itr.hasNext()) {
					sb.append(" -> ");
				}
			}
		} catch(NoSuchElementException e) {
			sb = new StringBuffer("Path does not exist");
		}
		
		return sb.toString();
	}

}
