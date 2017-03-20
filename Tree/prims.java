import java.io.*;
import java.util.*;
import java.lang.*;

public class prims{
	public static void main(String args[]){
		Graph graph = new Graph();
		graph.createGraph("bingraph800-");
		//for(int i = 0; i < graph.vertices.size();i++) for(int j = 0; j < graph.vertices.get(i).connections.size(); j++)	System.out.println("vertex: "+graph.vertices.get(i).index+"\tconnection: "+graph.vertices.get(i).connections.get(j).connection+"\t\tweight: "+graph.vertices.get(i).connections.get(j).weight);
		//System.out.println(graph.checkUsed(new Vertex(4)));
		graph.primSolve(0);
		//for(int i = 0; i < graph.tree.vertices.size();i++) for(int j = 0; j < graph.tree.vertices.get(i).connections.size(); j++)	System.out.println("vertex: "+graph.tree.vertices.get(i).index+"\tconnection: "+graph.tree.vertices.get(i).connections.get(j).connection+"\t\tweight: "+graph.tree.vertices.get(i).connections.get(j).weight);		
	}
}

class Graph{

	ArrayList<Vertex> vertices = new ArrayList();
	Graph tree;
	
	void createGraph(String filename){
		int buff[] = new int[5500];
		int length;
				
		try(DataInputStream din = new DataInputStream(new FileInputStream(filename)))
		{
			for(int i = 0; din.available() > 0;i++){
				int conn1 = din.readInt();
				int conn2 = din.readInt();
				int weight = din.readInt();
//				din.readInt();
	//			System.out.println("vertex: "+conn1+"\tconnection: "+conn2+"\t\tweight: "+weight);
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
	
	Vertex find(int index){
		for(int i = 0; i < vertices.size(); i++){
			Vertex temp = vertices.get(i);
			if(index == temp.index) return temp;
		}
		return new Vertex(-1); 
	}
	
	void primSolve(int startI){
		tree = new Graph();
		int nextIndex = startI;
		Vertex temp;
		for(int i = 0; i < vertices.size(); i++){
			temp = vertices.get(nextIndex); //the working vertex
			tree.vertices.add(vertices.get(nextIndex)); //adds vertex to graph
			Edge least = temp.connections.get(0); //the working edge of the vertex
			//System.out.println(nextIndex);
			System.out.println(temp.connections.size());
			for(int j = 0; j < temp.connections.size(); j++){
				Edge eTemp = temp.connections.get(j); //the working edge
				System.out.println(eTemp.connection+"\t"+least.connection);
				if(eTemp.weight < least.weight && tree.checkUsed(temp) == -1){
					least = eTemp; //the least weight edge
					
				} 
			}
			
			nextIndex = least.connection;
		}
	}
/* 	
	boolean checkCycle(Graph G, int Vertex){
		if(int i = 0; i < vertices.size(); i++){
			
		}
	}
	
	boolean recurse(Vertex current, Graph tree){
		
		for(int i = 0; i < current.connections.size())
	} */
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

