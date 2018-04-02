package q1;

import java.rmi.server.Operation;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Driver {
	
	static AtomicInteger idCounter = new AtomicInteger(0);
	/**
	 * Takes three integers p,q,n>0. Launches p threads, that
	 * create and add items to a queue, and q threads that dequeue items.
	 * */
	public static void main(String[] args) {
		if(args.length < 3) {
			System.out.println("Need three inputs");
			return;
		}
		int p = Integer.parseInt(args[0]);
		int q = Integer.parseInt(args[1]);
		int n = Integer.parseInt(args[2]);
		if(p <= 0 || q <= 0 || n <= 0) {
			System.out.println("p, q, n must be positive");
			return;
		}
		
		//Question A and B. 
		Queue<BNode> lfqueue = new LockFreeQueue();
		Queue<BNode> blkqueue = new BlockingQueue();
		System.out.println("-------------Blocking Queue-------------");
		//Part a
		partAandB(blkqueue, p, q, n);
		System.out.println("----------------------------------------");
		System.out.println("------------Lock Free Queue-------------");
		//part b
		partAandB(lfqueue, p, q, n);
		System.out.println("----------------------------------------");
		
		//Question C. Uncomment this block to run part c.
		long avgTime = 0;
		n = 500; //Chosen time for good average.
		for(int type= 0 ; type < 2; type++) {
			for(int j = 0; j < 5; j++) {
				p = (int)Math.pow(2, j+1);	
				for(int i = 0; i < 10; i++) {
					avgTime += test((type == 0 ? new BlockingQueue() : new LockFreeQueue()),p,n);
				}
				avgTime /= 10;
				System.out.println((type == 0 ? "Blocking" : "Locking") + " Queue with p,q =" + p + " takes " + avgTime + "ms");
				avgTime = 0;
			}
		}
		
	}
	
	/**
	 * Ended up sorting a different way.
	 * */
	private static void order(LinkedList<BNode> operations) {
		Collections.sort(operations, new Comparator() {

	        public int compare(Object o1, Object o2) {
	        	long et1 = ((BNode) o1).eTime;
	        	long et2 = ((BNode) o2).eTime;
	        	if( et1 < et2) return -1;
	        	else if (et1 == et2) return 0;
	        	else return 1;
	    }});
	}

	/**
	 * Question A and B. If Blocking passed->A, if lock free passed -> B.
	 * */
	private static void partAandB(Queue<BNode> queue, int p, int q, int n) {
		LinkedList<BNode> deques = new LinkedList<BNode>();
		int count = n;
		Thread[] enquers = new Thread[p];
		BDequeue[] dequers = new BDequeue[q];
		
		//initialize threads
		for(int i = 0; i < p ; i++)
			enquers[i] = new Thread(new BEnqueue(queue, idCounter));
		for(int i = 0; i < q ; i++)
			dequers[i] = new BDequeue(queue, count, false);
		
		//start
		for(int i = 0; i < p ; i++)
			enquers[i].start();
		for(int i = 0; i < q ; i++)
			dequers[i].start();
		//Join
		try {
			for(int i = 0; i < q ; i++) {
				dequers[i].join();
				deques.addAll(dequers[i].operations);
			}
			
			for(int i = 0; i < p ; i++) {
				enquers[i].interrupt();
				enquers[i].join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Total: " + deques.size());
		Operations operations = new Operations(deques);
		for(Operations.Op op : operations.ops){
			System.out.println("ID: " + op.id + " " + op.type + " " + op.stamp);
		}
		
		//Reset values
		idCounter.set(0);
	}
	
	/**
	 * Executes the performance tests, i.e. no output.
	 * */
	private static long test(Queue<BNode> queue, int p, int n) {
		int count = n;
		Thread[] enquers = new Thread[p];
		Thread[] dequers = new Thread[p];
		long startTime, endTime;
		//initialize threads
		for(int i = 0; i < p ; i++) {
			enquers[i] = new Thread(new BEnqueue(queue, idCounter));
			dequers[i] = new BDequeue(queue,count,true);
		}
		//start
		startTime = System.currentTimeMillis();
		for(int i = 0; i < p ; i++) {
			enquers[i].start();
			dequers[i].start();
		}
		//Join
		try {
			for(int i = 0; i < p ; i++) {
				dequers[i].join();
			}
			for(int i = 0; i < p ; i++) {
				enquers[i].interrupt();
				enquers[i].join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		endTime = System.currentTimeMillis();
		
		//Reset values
		idCounter.set(0);
		
		return (endTime - startTime);
	}
}
