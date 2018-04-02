package q1;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue implements Queue<BNode> {
	private AtomicReference<LFNode> head;
	private AtomicReference<LFNode> tail;
	
	public LockFreeQueue() {
		LFNode sentinal = new LFNode(null);
		head = new AtomicReference<LFNode>(sentinal);
		tail = new AtomicReference<LFNode>(sentinal);
	}
	
	@Override
	public void enqueue(BNode bnode) {
		LFNode node = new LFNode(bnode);
		while(true) {
			LFNode last = tail.get();
			LFNode next = last.next.get();
			if(last == tail.get()) {
				if(next == null) {
					if(last.next.compareAndSet(next, node)) {
						tail.compareAndSet(last, node);
						bnode.eTime = System.currentTimeMillis();
						return;
					}
				} else {
					tail.compareAndSet(last, next);
				}
			}
		}
	}

	@Override
	public BNode dequeue() {
		while(true) {
			LFNode first = head.get();
			LFNode last = tail.get();
			LFNode next = first.next.get();
			if(first == head.get()) {
				if(first == last) {
					if(next == null) {
						return null;
					}
					tail.compareAndSet(last, next);
				} else {
					BNode value = next.node;
					if(head.compareAndSet(first, next)) {
						value.dTime = System.currentTimeMillis();
						return value;
					}
				}
			}
		}
	}

	private class LFNode {
		public BNode node;
		public AtomicReference<LFNode> next;
		
		public LFNode(BNode _node) {
			node = _node;
			next = new AtomicReference<LFNode>(null);
		}
	}
}
