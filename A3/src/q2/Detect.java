package q2;

import java.util.ArrayList;

import q2.Graph.Vertex;

public class Detect implements Runnable {
	ArrayList<Vertex> conflicts;
	
	public Detect(ArrayList<Vertex> conflicts) {
		this.conflicts = conflicts;
	}
	@Override
	public void run() {
		for(Vertex v : conflicts) {
			if(v.isConflit())
				Driver.conflicts[v.id].set(true);
			else
				Driver.conflicts[v.id].set(false);
		}
	}

}
