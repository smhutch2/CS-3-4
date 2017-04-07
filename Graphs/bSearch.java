//Runs breadth first search

import Structure.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.Objects;

public class bSearch{
	public static void main(String args[]){
		//Graph graph = new Graph(800);
		//graph.createGraph("bingraph800-");
		Graph graph = new Graph(13);
		graph.addVertex(1,1,2);
		graph.addVertex(1,1,3);
		graph.addVertex(1,1,4);
		graph.addVertex(2,1,5);		
		graph.addVertex(2,1,6);
		graph.addVertex(4,1,7);
		graph.addVertex(4,1,8);
		graph.addVertex(5,1,9);
		graph.addVertex(5,1,10);
		graph.addVertex(7,1,11);		
		graph.addVertex(7,1,12);

		
		graph.breadthFirst(1,9);
	}
}