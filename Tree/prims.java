import java.io.*;
import java.util.*;
import java.lang.*;

public class prims{
	public static void main(String args[]){
		Graph graph = new Graph();
		graph.createGraph("bingraph800-");
		
		Graph test = new Graph();
		test.addVertex(new Vertex(0), 3, 1);
		test.addVertex(new Vertex(0), 2, 2);
		test.addVertex(new Vertex(0), 4, 3);
		test.addVertex(new Vertex(1), 5, 2);
		test.addVertex(new Vertex(1), 3, 5);
		test.addVertex(new Vertex(1), 2, 6);
		test.addVertex(new Vertex(2), 4, 7);
		test.addVertex(new Vertex(2), 5, 8);
		
		test.primSolve(0);
		//Graph test1 = test.consolidate();
		Graph test1 = test.tree;
		for(int i = 0; i < test1.vertices.size();i++) for(int j = 0; j < test1.vertices.get(i).connections.size(); j++)	System.out.println("vertex: "+test1.vertices.get(i).index+"\tconnection: "+test1.vertices.get(i).connections.get(j).connection+"\t\tweight: "+test1.vertices.get(i).connections.get(j).weight);
		//for(int i = 0; i < graph.vertices.size();i++) for(int j = 0; j < graph.vertices.get(i).connections.size(); j++)	System.out.println("vertex: "+graph.vertices.get(i).index+"\tconnection: "+graph.vertices.get(i).connections.get(j).connection+"\t\tweight: "+graph.vertices.get(i).connections.get(j).weight);
		//System.out.println(graph.checkUsed(new Vertex(4)));
		//graph.primSolve(0);
		//for(int i = 0; i < graph.tree.vertices.size();i++) for(int j = 0; j < graph.tree.vertices.get(i).connections.size(); j++)	System.out.println("vertex: "+graph.tree.vertices.get(i).index+"\tconnection: "+graph.tree.vertices.get(i).connections.get(j).connection+"\t\tweight: "+graph.tree.vertices.get(i).connections.get(j).weight);
		//int sum = 0;
		//for(int i = 0; i <  graph.tree.vertices.size();i++) for(int j = 0; j < graph.tree.vertices.get(i).connections.size(); j++) sum += graph.tree.vertices.get(i).connections.get(j).weight;
		//System.out.println("sum: "+sum);
		//Vertex vert = new Vertex(5);
		//System.out.println("checking: "+graph.tree.checkUsed(vert));
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
		Graph tempGraph = new Graph();
		int nextIndex = startI;
		Vertex temp;
		Vertex next;
		int sum = 0;
		for(int i = 0; i < vertices.size(); i++){
			temp = find(nextIndex);
			tempGraph.vertices.add(find(nextIndex));

			for(int k = 0; k < tempGraph.vertices.size(); k++){
				Vertex vTemp = tempGraph.vertices.get(k);
				Edge last = new Edge(100,-1);
				
				for(int j = 0; j < vTemp.connections.size(); j++){
					Edge eTemp = vTemp.connections.get(j); //the working edge

					if(tempGraph.find(eTemp.connection).index == -1){ 
						if(eTemp.weight <= last.weight){
							nextIndex = eTemp.connection;
							sum += eTemp.weight;
						} 
						last = eTemp;
					}	
				}
				if(nextIndex == -1) System.out.println("failure");
			}
			//System.out.println("next: "+nextIndex);
		}
		System.out.println("sum "+sum);
		tree=tempGraph;
	}
	
	Graph consolidate(){
		Graph fnl = new Graph();
		fnl.vertices.add(new Vertex(vertices.get(0).index));
		
		for(int i = 0; i < vertices.size()-1; i++){
		
			Vertex temp = vertices.get(i);
			Vertex next = vertices.get(i+1);
			System.out.println("here2");
			
			for(int j = 0; j < temp.connections.size(); j++){
				
				Edge eTemp = temp.connections.get(j);
				if(eTemp.connection == next.index){
					System.out.println("here1");
					fnl.addVertex(new Vertex(temp.index), eTemp.weight, next.index);
				} 

			}
		}
		return fnl;
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

