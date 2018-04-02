import java.util.Random;

public class SemHeadAssemblerWhiskers extends Thread{
	private Random rn = new Random();
	private Head head;
	private long idleTime = 0;
	@Override
	public void run() {
		while(q2.produce.get()) {
			head = getHead();
			//Once whiskers added, can add to body to it's bin. sleep for 20-60ms
			try {
				Thread.sleep(rn.nextInt(41)+20);
				head.hasWhiskers = true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(head.hasEyes) {//if head is completed add it to full head.
				q2.semfullHeadBin.produce(head);
			}else { //Else need to add eyes.
				q2.semeyelessBin.produce(head);
			}
		}
		//System.out.println("Stopped Whisker Assembler");
	}
	
	private Head getHead() {
		long start,end;
		//Check if a body with tail exists.
		Head head = q2.semwhiskerlessBin.consumeIfExists();
		if(head != null) {
			return head;
		}
		//Get body from infinite bins
		else {
			return new Head();
		}
	}
	
	public long getIdleTime() {
		return idleTime;
	}
}
