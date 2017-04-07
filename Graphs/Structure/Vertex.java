package Structure;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.Objects;

public class Vertex{
	public ArrayList<Edge> connections = new ArrayList();
	
	public void addConnection(int weight, int connection){
		connections.add(new Edge(weight, connection));
	}
}