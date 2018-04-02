package q2;

import java.util.ArrayList;

import q2.Graph.Vertex;

public class Assign implements Runnable {
	ArrayList<Vertex> conflicts;
	
	public Assign(ArrayList<Vertex> conflicts) {
		this.conflicts = conflicts;
	}
	@Override
	public void run() {
		for(Vertex v : conflicts) {
			v.setColor();
		}
	}
}
