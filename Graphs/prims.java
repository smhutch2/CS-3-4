//Runs prims algorithm on the graph

import Structure.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.Objects;

public class prims{
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