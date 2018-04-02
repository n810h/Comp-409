import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class CatAssembler extends Thread{
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
		
		synchronized(q2.fullHeadBin) {
			start = System.currentTimeMillis();
			while(q2.fullHeadBin.size() == 0) {
				try {
					q2.fullHeadBin.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			end = System.currentTimeMillis();
			idleTime += (end - start);
			return q2.fullHeadBin.remove(q2.fullHeadBin.size()-1);
		}
	}
	
	private Body getBody() {
		long start,end;
		synchronized(q2.fullLowerBin) {
			start = System.currentTimeMillis();
			while(q2.fullLowerBin.size() == 0) {
				try {
					q2.fullLowerBin.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			end = System.currentTimeMillis();
			idleTime += (end - start);
			return q2.fullLowerBin.remove(q2.fullLowerBin.size()-1);
		}
	}

	public long getIdleTime() {
		return idleTime;
	}
}
