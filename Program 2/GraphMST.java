
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

// Minimum Spanning Tree Class
public class GraphMST {
	private int[] rank, parent;
	private int max_num_vertices;
	private List<Integer> adjList[];
	private List<Double> adjListWeight[];
	private List<MyEdge> edgeList;
	private int num_vertices;
	HashMap<String, Integer> hmap = new HashMap<String, Integer>();
	HashMap<Integer, String> reverseMap = new HashMap<Integer, String>();
	PriorityQueue<MyEdge> queue = new PriorityQueue<MyEdge>();

	public GraphMST(int max_num_vertices) {
		this.max_num_vertices = max_num_vertices;
		adjList = new List[max_num_vertices];
		adjListWeight = new List[max_num_vertices];
		edgeList = new ArrayList<>();
		rank = new int[max_num_vertices];
		parent = new int[max_num_vertices];
		makeSet();

		for (int i = 0; i < max_num_vertices; i++) {
			adjList[i] = new ArrayList<Integer>();
			adjListWeight[i] = new ArrayList<Double>();
		}
	}

	void makeSet() {
		for (int i = 0; i < num_vertices; i++)
			parent[i] = i;
	}

	int find(int x) {
		if (parent[x] != x)
			parent[x] = find(parent[x]);
		return parent[x];
	}

	void union(int x, int y) {
		int xNode = find(x), yNode = find(y);
		if (xNode == yNode)
			return;
		if (rank[xNode] < rank[yNode])
			parent[xNode] = yNode;
		else if (rank[yNode] < rank[xNode])
			parent[yNode] = xNode;

		else {
			parent[yNode] = xNode;
			rank[xNode] = rank[xNode] + 1;
		}
	}

	public void addEdge(String start_vertex_key, String end_vertex_key, double edge_weight) {
		int from = hmap.get(start_vertex_key);
		int to = hmap.get(end_vertex_key);
		adjList[from].add(to);
		adjListWeight[from].add(edge_weight);
		edgeList.add(new MyEdge(start_vertex_key, end_vertex_key, (int) edge_weight));
	}

	public <T> void addVertex(T item) {
		hmap.put((String) item, num_vertices++);
		reverseMap.put(num_vertices - 1, (String) item);
	}
        // Added Prim's Algorithm
	List<?> prim() {
		int[] dist, pred;
		boolean[] visited;
		int next = -1;
		int prev = 0;
		dist = new int[max_num_vertices];
		pred = new int[max_num_vertices];
		visited = new boolean[max_num_vertices];
		ArrayList<MyEdge> prim = new ArrayList<>();

		for (int i = 0; i < dist.length; i++) {
			dist[i] = Integer.MAX_VALUE;
		}
		dist[0] = 0;

		for (int k = 0; k < dist.length; k++) {
			int x = Integer.MAX_VALUE;
			int y = -1; // graph not connected, or no unvisited vertices

			for (int j = 0; j < dist.length; j++) {
				if (!visited[j] && dist[j] < x) {
					y = j;
					x = dist[j];
				}
			}

			next = y;
			visited[next] = true;
			if (next != prev) {
				double weight = 0;
				for (int j = 0; j < adjList[prev].size(); j++) {
					if (adjList[prev].get(j) == next) {
						weight = adjListWeight[prev].get(j);
					}
				}
				prim.add(new MyEdge(reverseMap.get(prev), reverseMap.get(next), (int) weight));
			}
			if (next != -1)
				for (int j = 0; j < adjList[next].size(); j++) {
					int v = adjList[next].get(j);
					double d = adjListWeight[next].get(j);
					if (dist[v] > d) {
						dist[v] = (int) d;
						pred[v] = next;
					}
				}
			prev = next;
		}
		System.out.println("Prim:");
		for (MyEdge res : prim) {
			System.out.println("(" + res.src + ", " + res.dest + ", " + res.weight + ")");
		}
		return prim;
	}

	List<?> kruskal() {
		makeSet();
		Collections.sort(edgeList);
		ArrayList<MyEdge> result = new ArrayList<MyEdge>();

		for (int i = 0; i < edgeList.size(); i++) {
			int x = find(hmap.get(edgeList.get(i).src));
			int y = find(hmap.get(edgeList.get(i).dest));

			if (x != y) {
				union(x, y);
				result.add(edgeList.get(i));
			}
		}
		System.out.println("Kruskals:");
		for (MyEdge res : result) {
			System.out.println("(" + res.src + ", " + res.dest + ", " + res.weight + ")");
		}
		return result;
	}

	List<?> shortestPath(String start_vertex_key, String end_vertex_key) {
		Set<Integer> settled = new HashSet<Integer>();
		PriorityQueue<MyNode> priorityQueue = new PriorityQueue<>();
		int distances[] = new int[max_num_vertices];
		int start = hmap.get(start_vertex_key);
		int evaluationNode;

		for (int i = 0; i < max_num_vertices; i++)
			distances[i] = Integer.MAX_VALUE;
		priorityQueue.add(new MyNode(start, 0));
		distances[start] = 0;

		while (!priorityQueue.isEmpty()) {
			evaluationNode = priorityQueue.remove().node;
			settled.add(evaluationNode);
			double edgeDistance = -1;
			double newDistance = -1;
			for (int destinationNode = 0; destinationNode < adjList[evaluationNode].size(); destinationNode++) {
				if (!settled.contains(destinationNode)) {
					if (adjList[evaluationNode].get(destinationNode) != Integer.MAX_VALUE) {
						edgeDistance = adjListWeight[evaluationNode].get(destinationNode);
						newDistance = distances[evaluationNode] + edgeDistance;
						if (newDistance < distances[destinationNode]) {
							distances[destinationNode] = (int)newDistance;
						}
						priorityQueue.add(new MyNode(destinationNode, distances[destinationNode]));
					}
				}
			}
		}
		System.out.println("Dijsktra from: " + start_vertex_key + " " + end_vertex_key );
		System.out.println(distances[hmap.get(end_vertex_key)]);
		return null;
	}

	public static void main(String[] args) throws IOException {
		String preffix = "./src/files/";
		String line = null;
		int num_vertices = 0;

		FileReader fileReader = new FileReader(preffix + args[0]);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((line = bufferedReader.readLine()) != null)
			num_vertices++;
		bufferedReader.close();

		GraphMST g = new GraphMST(num_vertices);

		fileReader = new FileReader(preffix + args[0]);
		bufferedReader = new BufferedReader(fileReader);

		while ((line = bufferedReader.readLine()) != null) {
			g.addVertex(line);
		}

		bufferedReader.close();

		fileReader = new FileReader(preffix + args[1]);
		bufferedReader = new BufferedReader(fileReader);

		while ((line = bufferedReader.readLine()) != null) {
			String[] split = line.split(" ");
			g.addEdge(split[0], split[1], Double.parseDouble(split[2]));
		}

		bufferedReader.close();
		g.kruskal();
		g.prim();
		g.shortestPath(args[2], args[3]);
	}

}
