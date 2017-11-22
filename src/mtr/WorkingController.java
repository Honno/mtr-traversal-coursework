package mtr;

import graph.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
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
					prevNode = node;
				}
			}
			graph = new Graph<String, String>(nodesMap);
			LOG.log(Level.INFO, graph.toString());
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String listStationsInLine(String line) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String listAllDirectlyConnectedLines(String line) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String showPathBetween(String stationA, String stationB) {
		// TODO Auto-generated method stub
		return null;
	}

}
