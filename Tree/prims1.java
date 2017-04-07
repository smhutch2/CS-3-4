import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.Objects;

public class prims1{
	public static void main(String args[]){
		Graph graph = new Graph(800);
		
		graph.createGraph("bingraph800-");
		
		int sum = graph.primSolve(0);
		
		//prints out tree
		for(int i = 0; i < graph.tree.vertices.length;i++) {
			for(int j = 0; j < graph.tree.vertices[i].connections.size(); j++) {
				System.out.println("vertex: "+i+"\tconnection: "+graph.tree.vertices[i].connections.get(j).connection+"\t\tweight: "+graph.tree.vertices[i].connections.get(j).weight);
			}
		}
		System.out.println("prims sum "+sum);
	}
}

class Graph{
	Vertex vertices[]; //vertices of graph
	Graph tree; //the minimally spanning tree, run primSolve to fill
	int size;
	
	//Constructor, only size is necessary
	Graph(int size){
		this.size = size;
		vertices = new Vertex[size];
	}
	
	//fills graph
	void createGraph(String filename){
		int buff[] = new int[5500];
		int length;
				
		try(DataInputStream din = new DataInputStream(new FileInputStream(filename)))
		{
			for(int i = 0; din.available() > 0;i++){
				int conn1 = din.readInt();
				int conn2 = din.readInt();
				int weight = din.readInt();

				addVertex(conn1,weight,conn2);
			} 
		}
		catch(IOException exc)
		{
			System.out.println("error opening file");
		}	
	}

	//adds a vertex to the graph
	void addVertex(int conn, int weight, int conn2){
		if(Objects.isNull(vertices[conn])) vertices[conn] = new Vertex();
		vertices[conn].addConnection(weight, conn2);
		
		if(Objects.isNull(vertices[conn2])) vertices[conn2] = new Vertex();
		vertices[conn2].addConnection(weight, conn);
	}

	//solves prim and fills tree graph
	int primSolve(int nextIndex){
		int sum = 0; 	//running total
		tree = new Graph(size); 	//constructs tree
		Graph tempGraph = new Graph(size); 	//constructs temporary graph
		ArrayList<Integer> visited = new ArrayList(); 	//list of visited vertices
		Vertex vTemp = new Vertex();	//temporary vertex
		int wIndex = 0;		//the index that works
		
		//loops through all vertices
		for(int i = 0; i < vertices.length; i++){
			tempGraph.vertices[nextIndex] = vertices[nextIndex]; //sets the tempgraph
			visited.add(nextIndex);	
			
			Edge least = new Edge(100,-1);
			for(int j = 0; j < visited.size(); j++){
				Vertex temp = tempGraph.vertices[visited.get(j)];
				for(int k = 0; k < temp.connections.size(); k++){
					Edge etemp = temp.connections.get(k);
					if(etemp.weight < least.weight && visited.indexOf(etemp.connection) == -1){
						least = etemp;
						wIndex = j;
					} 
				}
			}
			
			if(least.connection == -1) break; //when it gets to the final one
			tree.addVertex(wIndex, least.weight, least.connection);
			nextIndex = least.connection;
			sum += least.weight;
			
		}
		return sum;
	}	
}

//makes up a graph, made up of edges
class Vertex{
	ArrayList<Edge> connections = new ArrayList();
	
	void addConnection(int weight, int connection){
		connections.add(new Edge(weight, connection));
	}
}

//edges, assigned to vertices, has a weight and next connection
class Edge{
	int weight;
	int connection;
	
	Edge(int weight, int connection){
		this.weight = weight;
		this.connection = connection;
	}
}



