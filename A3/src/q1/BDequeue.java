package q1;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BDequeue extends Thread {
	int counter;
	LinkedList<BNode> operations;
	Queue<BNode> queue;
	boolean test;
	
	public BDequeue(Queue<BNode> _queue, int _counter, boolean _test) {
		queue = _queue;
		counter = _counter;
		operations = new LinkedList<BNode>();
		test = _test;
	}
	public BDequeue(Queue<BNode> _queue, int _counter) {
		queue = _queue;
		counter = _counter;
		operations = null;
	}
	@Override
	public void run() {
		if(test)
			test();
		else
			trackOperations();
	}
	
	private void trackOperations() {
		BNode node;
		while(counter > 0) {// Don't decrement unless we know we've got a Node.
			node = queue.dequeue();
			if(node != null) {
				counter--;
				operations.add(node);
			}
			else { // queue is empty, so sleep and try again.
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void test() {
		BNode node;
		while(counter > 0) {// Don't decrement unless we know we've got a Node.
			node = queue.dequeue();
			if(node != null) {
				counter--;
			}
			else { // queue is empty, so sleep and try again.
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
