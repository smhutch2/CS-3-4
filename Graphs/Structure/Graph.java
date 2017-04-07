package Structure;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.Objects;


public class Graph{
	public Vertex vertices[]; //vertices of graph
	public Graph tree; //the minimally spanning tree, run primSolve to fill
	public int size;
	
	//Constructor, only size is necessary
	public Graph(int size){
		this.size = size;
		vertices = new Vertex[size];
	}
	
	//fills graph
	public void createGraph(String filename){
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
	public void addVertex(int conn, int weight, int conn2){
		if(Objects.isNull(vertices[conn])) vertices[conn] = new Vertex();
		vertices[conn].addConnection(weight, conn2);
		
		if(Objects.isNull(vertices[conn2])) vertices[conn2] = new Vertex();
		vertices[conn2].addConnection(weight, conn);
	}

	//solves prim and fills tree graph
	public int primSolve(int nextIndex){
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

	//does breadfirst search
	public/* int[]*/ void breadthFirst(int start, int goal){
		ArrayList<Vertex> Current = new ArrayList();	//current working list
		Current.add(vertices[start]);					//adds start vertex
		ArrayList<Vertex> Next = new ArrayList();		//next working list
		
		ArrayList<Integer> checked = new ArrayList();	//records vertices that have been checked
		checked.add(start);
		int distance[] = new int[size];					//records the distances from the start
		distance[start] = 0;
		int count = 0;									//records distance
		boolean there = false;							//return true when it is there
		
		while(!there){
			count++;
			
			for(int i = 0; i < Current.size(); i++){	//goes through current vertices
				Vertex temp = Current.get(i);			//gets working Vertex
				
				for(int j = 0; j < temp.connections.size(); j++){	//goes through current connections
					int index = temp.connections.get(j).connection;
					
					if(checked.indexOf(index) == -1){	//makes sure its not repeating
						Next.add(vertices[index]);		//adds vertex to next
						distance[index] = count;		//sets distance
						checked.add(index);
						
						System.out.println("index:\t"+index+"\tdistance:\t"+count);
						
						if(index == goal){				//if it sees the goal
							there = true;
							System.out.println("here");
							break;
						}
					}
				}
				if(there == true) break;
			}
			
			System.out.println("list reset");
			Current = Next;								//makes current next
			Next = new ArrayList();						//resets next
		}
		
		
	}
}
