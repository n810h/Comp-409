package q1;

import java.util.LinkedList;

public class BlockingQueue implements Queue<BNode> {
	//LinkedList<BNode> Queue = new LinkedList<BNode>();
	Object enqLock = new Object();
	Object deqLock = new Object();
	volatile Node head;
	volatile Node tail;
	
	public BlockingQueue() {
		head = new Node(null);
		tail = head;
	}
//	@Override
//	public BNode dequeue(){
//		synchronized(Queue) {
//			if(Queue.isEmpty()) return null;
//			BNode node = Queue.removeFirst();
//			node.dTime = System.currentTimeMillis();
//			return node;
//		}
//	}
//
//	@Override
//	public void enqueue(BNode node) {
//		synchronized(Queue) {
//			node.eTime = System.currentTimeMillis();
//			Queue.addLast(node);
//		}
//	}
	@Override
	public BNode dequeue(){
		synchronized(deqLock) {
			if(head.next == null) {
				return null;
			}
			BNode result = head.next.node;
			head = head.next;
			result.dTime = System.currentTimeMillis();
			return result;
		}
	}

	@Override
	public void enqueue(BNode bnode) {
		synchronized(enqLock) {
			Node node = new Node(bnode);
			bnode.eTime = System.currentTimeMillis();
			tail.next = node;
			tail = node;
		}
	}
	
	private class Node{
		public BNode node;
		public volatile Node next;
		
		public Node(BNode _node) {
			node = _node;
			next = null;
		}
	}

}
