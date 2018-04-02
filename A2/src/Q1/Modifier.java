package Q1;

import java.util.Random;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicInteger;

public class Modifier implements Runnable{
	AtomicInteger count;
	Random rn = new Random();
	Node[] nodes;
	
	public Modifier(AtomicInteger count, Node[] nodes) {
		this.count = count;
		this.nodes = nodes;
	}
	@Override
	public void run() {
		Node first, second, third;
		while(count.getAndDecrement()>0) {
			//Pick random vertex.
			int v = rn.nextInt(6);
			
			//Define ordering of nodes to pick up.
			if(v == 0) {
				 third = nodes[5];
				 second = nodes[1];
				 first = nodes[0];
			}else if(v == 5) {
				third = nodes[5];
				second = nodes[4];
				first = nodes[0];
			}else {
				third = nodes[v+1];
				second = nodes[v];
				first = nodes[v-1];
			}
			
			//Lock nodes in order.
			synchronized(first) {
				synchronized(second) {
					synchronized(third) {
						modify(nodes[v]);
					}
				}
			}
		}
	}
	
	private void modify(Node mid) {
		float r1 = rn.nextFloat();
		float r2 = rn.nextFloat();
		float rx = randomX(mid, r1, r2);
		float ry = randomY(mid, r1, r2);
		mid.x = rx;
		mid.y = ry;
	}
	
	/**
	 * Get random x coord in triangle.
	 * */
	public static float randomX(Node rnode, float r1, float r2) {
		return (1 - sqrt(r1)) * rnode.prev.x + (sqrt(r1) * (1 - r2)) * rnode.x + (sqrt(r1) * r2) * rnode.next.x;
	}
	
	/**
	 * Get random y coord in triangle
	 * */
	public static float randomY(Node rnode, float r1, float r2) {
		return (1 - sqrt(r1)) * rnode.prev.y + (sqrt(r1) * (1 - r2)) * rnode.y + (sqrt(r1) * r2) * rnode.next.y;
	}
	
	public static float sqrt(float x) {
		return (float) Math.sqrt(x);
	}

}
