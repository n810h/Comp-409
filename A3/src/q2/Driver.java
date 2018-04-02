package q2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import q2.Graph.Vertex;

public class Driver {
	public static ArrayList<Vertex> conflictSet;
	public static Graph graph;
	public static AtomicBoolean[] conflicts;
	public static int t;
	public static int n;
	public static int e;
	
	public static void main(String[] args) {
		if(args.length < 3) {
			System.out.println("Not enugh arguements.");
			return;
		}
		n = Integer.parseInt(args[0]);
		e = Integer.parseInt(args[1]);
		t = Integer.parseInt(args[2]);
		assert(n > 3 && e > 0 && t > 0);
		
		long startTime, endTime;
		graph = new Graph(n,e);
		
		startTime = System.currentTimeMillis();
		color(graph);
		endTime = System.currentTimeMillis();
		System.out.println("Time taken to color: " + (endTime - startTime));
		System.out.println("Conflicts: " + graph.existsConflicts());
		System.out.println("Max degree: " + graph.getMaxDegree() + "\nMax Color: " + graph.getMaxColor());
		
		//test();
	}
	
	public static void test() {
		//n,e chosen so that graph generation is ~10s
		n = 500000;
		e = 15000000;
		long startTime, endTime;
		long avg = 0;
		graph = new Graph(n,e);
		System.out.println("Testing");
		for(int k = 2; k <=8; k+=2) {
			t = k;
			System.out.print("t=" + t + " takes :");
			for(int i = 0; i < 5; i++) {
				startTime = System.currentTimeMillis();
				color(graph);
				endTime = System.currentTimeMillis();
				avg += (endTime-startTime);
			}
			System.out.println((avg/5));
			avg = 0;
		}
	}
	
	public static void color(Graph graph) {
		initConflictSet();
		while(!isConflictEmpty()) {
			//Assign
			assign();
			
			//DetectConflits.
			detect();
		}
	}
	
	/**
	 * Partition conflicts among threads.
	 * Start some threads to assign color, then join.
	 * */
	private static void assign() {
		Thread[] threads = partitionThreads(true);
		
		//start
		for(int i = 0 ; i <t ; i++) {
			threads[i].start();
		}
		
		//join
		try {
			for(int i = 0; i < t; i++)
				threads[i].join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Partition Conflict detection among threads.
	 * Start some threads to detect conflicts and then join.
	 * */
	private static void detect() {
		Thread[] threads = partitionThreads(false);
		//start
		for(int i = 0 ; i <t ; i++) {
			threads[i].start();
		}
		
		//join
		try {
			for(int i = 0; i < t; i++)
				threads[i].join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static Thread[] partitionThreads(boolean assign) {
		Thread[] threads = new Thread[t];
		ArrayList<ArrayList<Vertex>> threadsConfs = new ArrayList<>(t);
		
		//initialize
		for(int i = 0 ; i < t; i++){
			threadsConfs.add(i, new ArrayList<Vertex>());
		}
		
		for(int i = 0 ; i < n; i++) {
			if(conflicts[i].get())
				threadsConfs.get(i%t).add(graph.vertices[i]);
		}
		
		for(int i = 0 ; i < t; i++) {
			if(assign)
				threads[i] = new Thread(new Assign(threadsConfs.get(i)));
			else
				threads[i] = new Thread(new Detect(threadsConfs.get(i)));
		}
		return threads;
	}
	
	/**
	 * Set conlfict set to all of the vertices.
	 * */
	private static void initConflictSet() {
		//Collections.addAll(conflictSet, graph.vertices);
		
		conflicts = new AtomicBoolean[n];
		for(int i = 0 ; i < n; i++) {
			conflicts[i] = new AtomicBoolean(true);
		}
	}
	
	private static boolean isConflictEmpty() {
		for(AtomicBoolean bool : conflicts) {
			if(bool.get()) return false;
		}
		return true;
	}
}
