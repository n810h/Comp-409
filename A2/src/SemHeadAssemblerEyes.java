import java.util.Random;

public class SemHeadAssemblerEyes extends Thread{
	private Random rn = new Random();
	private Head head;
	private long idleTime = 0;
	
	@Override
	public void run() {
		while(q2.produce.get()) {
			head = getHead();

			//Once eyes added, can add to body to it's bin. sleep for 10-30ms
			try {
				Thread.sleep(rn.nextInt(21)+10);
				head.hasEyes = true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(head.hasWhiskers) {//if head is completed add it to full head.
				q2.semfullHeadBin.produce(head);
			}else { //Else need to add whiskers.
				q2.semwhiskerlessBin.produce(head);
			}
			
		}
		//System.out.println("Stopped Eye Assembler");
	}
	
	private Head getHead() {
		//Check if a body with tail exists.
		Head head = q2.semeyelessBin.consumeIfExists();
		if(head != null)
			return head;
		//Get body from infinite bins
		else {
			return new Head();
		}
	}	
	
	public long getIdleTime() {
		return idleTime;
	}
}
