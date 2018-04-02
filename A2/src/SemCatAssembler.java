import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SemCatAssembler extends Thread{
	private Random rn = new Random();
	private AtomicInteger count = new AtomicInteger(250);
	private Body body;
	private Head head;
	private int i =0;
	private long idleTime = 0;
	@Override
	public void run() {
		while(count.getAndDecrement() > 0) {
			if(rn.nextInt(10) < 5) {
				head = getHead();
				body = getBody();
			}else {
				body = getBody();
				head = getHead();
			}
			
			//Attach legs and head which takes 10-20ms
			try {
				Thread.sleep(rn.nextInt(11) + 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
			//System.out.println("Cat produced: " + i);
		}
		
		//Once we've finished 250 cats, signal the other threads to stop.
		//System.out.println("Finished Cats");
		q2.produce.set(false);
		
	}
	
	private Head getHead() {
		long start,end;
		Head head;
		start = System.currentTimeMillis();
		head = q2.semfullHeadBin.consume();
		end = System.currentTimeMillis();
		idleTime += (end - start);
		return head;
	}
	
	private Body getBody() {
		long start,end;
		Body body;
		start = System.currentTimeMillis();
		body = q2.semfullLowerBin.consume();
		end = System.currentTimeMillis();
		idleTime += (end - start);
		return body;
	}

	public long getIdleTime() {
		return idleTime;
	}
}
