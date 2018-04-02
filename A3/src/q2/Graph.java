package q2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Graph {
	public Vertex[] vertices;
	private static int idCounter;
	
	public Graph(int n, int e) {
		vertices = new Vertex[n];
		createRandomGraph(n,e);
		idCounter = 0;
	}
	
	public void Print() {
		for(int i = 0 ; i < vertices.length; i++) {
			System.out.println("Vertex: " + vertices[i].id+ ", color: " + vertices[i].color + "\nEdges: ");
			for(int j = 0; j < vertices[i].adjacencyList.size(); j++) {
				System.out.println("("+vertices[i].id+","+vertices[i].adjacencyList.get(j).id+")");
			}
		}
	}
	
	/**
	 * Creates random graph by choosing random edges to create.
	 * */
	private void createRandomGraph(int n, int e) {
		Random rn = new Random();
		Vertex v1,v2;
		int edgesAdded = 0;
		int idx1, idx2;
		
		//Create vertices
		for(int i = 0; i < n; i++)
			vertices[i] = new Vertex();
		
		//Add edges
		if(e > n*(n-1)/2) e = n*(n-1)/2; //Can't have more than |V|(|V|-1)/2 edges in undirected graph.
		while(edgesAdded < e) {
			idx1 = rn.nextInt(n);
			idx2 = (idx1 + rn.nextInt(n-1) + 1) %n;
			v1 = vertices[idx1];
			v2 = vertices[idx2];
			if(v1.addEdge(v2)) edgesAdded++;
		}
	}
	
	public long getMaxDegree() {
		long max = 0;
		for(Vertex v : vertices) {
			max = Math.max(v.adjacencyList.size(), max);
		}
		return max;
	}
	
	public long getMaxColor() {
		long maxc = 1;
		for(Vertex v : vertices) {
			maxc = Math.max(v.color, maxc);
		}
		return maxc;
	}
	
	public boolean existsConflicts() {
		for(Vertex v : vertices) {
			if(v.isConflit()) return true;
		}
		return false;
	}
	
	public class Vertex{
		public int id;
		public volatile int color;
		public ArrayList<Vertex> adjacencyList;
		
		public Vertex() {
			id = idCounter++;
			adjacencyList = new ArrayList<Vertex>();
		}
		
		public boolean addEdge(Vertex v) {
			if(v == this) return false;
			if(!adjacencyList.contains(v)) {
				adjacencyList.add(v);
				v.addEdge(this);
				return true;
			}
			else
				return false;
		}
		
		/**
		 * Sets color to smallest available.
		 * */
		public void setColor() {
			this.color = this.smallestUnusedColor();
		}
		
		/**
		 * Go through the adjacency list and look for a matching color
		 * */
		public boolean isConflit() {
			for(Vertex v : adjacencyList) {
				if(v.color == this.color)
					return true;
			}
			return false;
		}
		
		private int smallestUnusedColor() {
			
			int min = 1;
			ArrayList<Integer> sorted = new ArrayList<>();
			for(Vertex v : adjacencyList) {
				sorted.add(v.color);
			}
			Collections.sort(sorted);
			for(int color : sorted) {
				if(color == min)
					min++;
			}
			return min;
		}
		
		/**
		 * Didn't end up needing this.
		 * */
		private ArrayList<Vertex> sortedOnColor(){
			ArrayList<Vertex> sorted = new ArrayList<>(adjacencyList);
			sorted.sort(new Comparator<Vertex>() {
				@Override
				public int compare(Vertex o1, Vertex o2) {
					if(o1.color < o2.color)
						return -1;
					else if(o1.color > o2.color)
						return 1;
					else 
						return 0;
				}
				
			});
			return sorted;
		}
	}
}

