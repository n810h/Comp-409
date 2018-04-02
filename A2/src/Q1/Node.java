package Q1;

import java.util.ArrayList;

public class Node {
	public volatile float x;
	public volatile float y;
	public Node prev;
	public Node next;
	public int elem;
	
	public Node(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void addNode(Node node) {
		this.next = node;
		node.prev = this;
		elem = this.elem + 1;
	}
	
	public static ArrayList<Float> getXs(){
		ArrayList<Float> xs = new ArrayList<Float>();
		Node curr = star.polygon.next;
		xs.add(star.polygon.x);
		while(curr != star.polygon) {
			xs.add(curr.x);
			curr = curr.next;
		}
		
		return xs;
	}
	
	public static ArrayList<Float> getYs(){
		ArrayList<Float> ys = new ArrayList<Float>();
		Node curr = star.polygon.next;
		ys.add(star.polygon.y);
		while(curr != star.polygon) {
			ys.add(curr.y);
			curr = curr.next;
		}
		
		return ys;
	}
	
	public String toString() {
		return "(" + this.x + ","+this.y+")";
	}
}
