/**
 *  Name: Dhairya Nishar
 *  CSC 2400 
 *  Section: 02
 *  
 *  Compile: javac Graph.java
 *  Run: java Graph romanian_cities.txt romanian_mileages.txt
 *  Run: java Graph top_sort_vertices.txt top_sort_edges.txt
 *  Using eclipse or any other IDE
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Graph {
 private int max_num_vertices;
 private int adjMatrix[][];
 private List<Integer> adjList[];
 private List<Double> adjListWeight[];
 private int num_vertices;
 HashMap<String, Integer> hmap = new HashMap<String, Integer>();
 HashMap<Integer, String> reverseMap = new HashMap<Integer, String>();
 private boolean isCyclic = false;

 /**
  * Constructor accepts max numbers of vertices which are allowed in graph.
  * @param max_num_vertices
  */
 public Graph(int max_num_vertices){ 
  this.max_num_vertices = max_num_vertices;
  adjMatrix = new int[max_num_vertices][max_num_vertices];
  adjList = new List[max_num_vertices];
  adjListWeight = new List[max_num_vertices];
  
  for (int i=0; i<max_num_vertices; i++){
   adjList[i] = new ArrayList<Integer>();
   adjListWeight[i] = new ArrayList<Double>();
  }
 }
 
 /**
  * Added a directed edge between two vertices.
  * @param start_vertex_key
  * @param end_vertex_key
  * @param edge_weight
  */
 public void addEdge(String start_vertex_key, String end_vertex_key, double edge_weight){
  int from = hmap.get(start_vertex_key);
  int to = hmap.get(end_vertex_key);
  adjList[from].add(to);
  adjListWeight[from].add(edge_weight);
  adjMatrix[from][to] = 1;
 }
 
 /**
  * Allowed items to be stored in the graph vertices.
  * @param item
  */
 public <T> void addVertex(T item){
  hmap.put((String) item, num_vertices++);
  reverseMap.put(num_vertices-1, (String)item);
 }
 
 /**
  * Performed Depth First Search
  * @param curr
  * @param list
  * @param visited
  */
 private void depthFirstSearch(int curr, List<String> list, boolean visited[]){
  visited[curr] = true;
  list.add(reverseMap.get(curr));
  
  for (int i=0; i<max_num_vertices; i++){
   if (i != curr && !visited[i] && adjMatrix[curr][i] > 0){
    depthFirstSearch(i, list, visited);
   }
  }
 }

 public List<?> dfs(){
  boolean visited[] = new boolean[max_num_vertices];
  List<String> list = new ArrayList<String>();
  for (int i=0; i<max_num_vertices; i++)
   if (!visited[i]){
    depthFirstSearch(i, list, visited);
   }
  return list;
 }
 
 /**
  * Performed Breath First Search
  * @param curr
  * @param list
  * @param visited
  */
 private void breadthFirstSearch(int curr, List<String> list, boolean visited[]){
  Queue <Integer> queue = new LinkedList<Integer>();
  queue.add(curr);
  visited[curr] = true;
  
  while (!queue.isEmpty()){
   int cur = queue.poll();
   list.add(reverseMap.get(cur));
   for (int i=0; i<adjList[cur].size(); i++)
    if (!visited[adjList[cur].get(i)]){
     visited[adjList[cur].get(i)] = true;
     queue.add(adjList[cur].get(i));
    }
  }
 }
 
 public List<?> bfs(){
  boolean visited[] = new boolean[max_num_vertices];
  List<String> list = new ArrayList<String>();

  for (int i=0; i<max_num_vertices; i++)
   if (!visited[i]){
    breadthFirstSearch(i, list, visited);
   }
  return list;
 }
 
 /**
  * Computed a Topological Sort using Depth First Search.
  * Returned null.
  * @param curr
  * @param list
  * @param visited
  */
 private void topologicalSort(int curr, List<String> list, int visited[]){
  visited[curr] = 1;
  
  for (int i=0; i<max_num_vertices; i++){
   if (i != curr && visited[i] == 0 && adjMatrix[curr][i] > 0){
    topologicalSort(i, list, visited);
   }
   else if (i != curr && adjMatrix[curr][i] > 0 && visited[i] == 1){
    isCyclic = true;
   }
  }
  
  visited[curr] = 2;
  list.add(reverseMap.get(curr));
 }
 
 public List<?> topsort(){
  List<String> list = new ArrayList<String>();
  isCyclic = false;
  int visited[] = new int[max_num_vertices];
  
  for (int i=0; i<max_num_vertices; i++){
   if (visited[i] == 0) topologicalSort(i, list, visited);
  }
  
  if (isCyclic) list = null;
  else {
   List temp = new ArrayList<String>();
   for (int i=max_num_vertices-1; i>=0; i--)
    temp.add(list.get(i)); 
    list = temp;
  }
  
  return list;
 }
 
 public static void main(String[] args) throws IOException {
  String preffix = "";
  String line = null;
  int num_vertices = 0;
  
  FileReader fileReader = new FileReader(preffix + args[0]);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while((line = bufferedReader.readLine()) != null) 
         num_vertices++;
        bufferedReader.close();  
        
        Graph g = new Graph(num_vertices);
 
        fileReader = new FileReader(preffix + args[0]);
        bufferedReader = new BufferedReader(fileReader);
        
        while((line = bufferedReader.readLine()) != null) {
            g.addVertex(line);
        }   

        bufferedReader.close();  
        
        fileReader = new FileReader(preffix + args[1]);
        bufferedReader = new BufferedReader(fileReader);

        while((line = bufferedReader.readLine()) != null) {
         String[] split = line.split(" ");
            g.addEdge(split[0], split[1], Double.parseDouble(split[2]));
        }   

        bufferedReader.close(); 
        System.out.println("DFS: " + g.dfs());
        System.out.println("BFS: " + g.bfs());
        System.out.println("TOPSORT: " + g.topsort());
 }
}
