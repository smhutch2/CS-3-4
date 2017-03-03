import java.io.*;
import java.util.*;
import java.lang.*;

public class prims{
	public static void main(String args[]){
		Graph graph = new Graph();
		graph.createGraph("bingraph800-");
		for(int i = 0; i < graph.vertices.size();i++)	System.out.println(graph.vertices.get(i).index);
		graph.vertices.add(new Vertex(5));
		graph.vertices.add(new Vertex(2));
		graph.vertices.add(new Vertex(3));
		//System.out.println(graph.checkUsed(new Vertex(4)));
	}
}

class Graph{

	ArrayList<Vertex> vertices = new ArrayList();
	
	void createGraph(String filename){
		int buff[] = new int[5500];
		int length;
				
		try(DataInputStream din = new DataInputStream(new FileInputStream(filename)))
		{
			for(int i = 0; din.available() > 0;i++){
				int conn1 = din.readInt();
				int conn2 = din.readInt();
				int weight = din.readInt();
				din.readInt();
				Vertex vert1 = new Vertex(conn1);
				Vertex vert2 = new Vertex(conn2);
				addVertex(vert1,weight,conn2);
				addVertex(vert2,weight,conn1);
			} 
		}
		catch(IOException exc)
		{
			System.out.println("error opening file");
		}	
	}

	int checkUsed(Vertex conn){
		for(int i = 0; i < vertices.size(); i++){
			if(conn.index == vertices.get(i).index) return i;
		}
		return -1;
	}
	
	void addVertex(Vertex conn, int weight, int conn2){
		int used = checkUsed(conn);
		if(used == -1){
			conn.addConnection(weight, conn2);
			vertices.add(conn);
		} 
		else vertices.get(used).addConnection(weight,conn2);
		
	}
}

class Vertex{
	ArrayList<Edge> connections = new ArrayList();
	int index;
	
	Vertex(int index){
		this.index = index;
	}
	
	void addConnection(int weight, int connection){
		connections.add(new Edge(weight, connection));
	}
	
}

class Edge{
	int weight;
	int connection;
	
	Edge(int weight, int connection){
		this.weight = weight;
		this.connection = connection;
	}
}

/*class Edge{
	Node node[] = new Node[2];
	int weight;
	
	Edges(int node1, int node2, int weight){
		node[0].add(node1);
		node[1].add(node2);
		this.weight = weight;
	}
}*/

