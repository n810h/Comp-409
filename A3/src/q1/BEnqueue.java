package q1;

import java.util.concurrent.atomic.AtomicInteger;

public class BEnqueue implements Runnable {
	AtomicInteger idCounter;
	Queue<BNode> queue;
	
	public BEnqueue(Queue<BNode> _queue, AtomicInteger _idCounter) {
		queue = _queue;
		idCounter = _idCounter;
	}
	@Override
	public void run() {
		BNode node;
		while(!Thread.currentThread().isInterrupted()) { //Create new node, and enqueue.
			node = new BNode(idCounter.getAndIncrement());
			queue.enqueue(node);
			try {//After enqueing sleep for 10ms.
				Thread.sleep(10);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

}
