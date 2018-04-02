import java.util.Random;

public class HeadAssemblerWhiskers extends Thread{
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
				synchronized(q2.fullHeadBin) {
					q2.fullHeadBin.add(head);
					q2.fullHeadBin.notify();
				}
			}else { //Else need to add eyes.
				synchronized(q2.eyelessBin) {
					q2.eyelessBin.add(head);
					q2.eyelessBin.notify();
				}
			}
		}
		//System.out.println("Stopped Whisker Assembler");
	}
	
//	private Head getHead() {
//		int p = rn.nextInt(10);
//		if (p < 5) {
//			synchronized(q2.headBin) {
//				return new Head();
//			}
//		}else {
//			synchronized(q2.whiskerlessBin) {
//				while(q2.whiskerlessBin.size() == 0) {
//					try {
//						q2.whiskerlessBin.wait();
//					}catch(InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				return q2.whiskerlessBin.remove(q2.whiskerlessBin.size()-1);
//			}
//		}
//	}
	
	private Head getHead() {
		long start,end;
		start = System.currentTimeMillis();
		synchronized(q2.whiskerlessBin){
			if(q2.whiskerlessBin.size() > 0) {
				end = System.currentTimeMillis();
				idleTime += (end - start);
				return q2.whiskerlessBin.remove(q2.whiskerlessBin.size()-1);
			}
		}
		
		synchronized(q2.headBin) {
			end = System.currentTimeMillis();
			idleTime += (end - start);
			return new Head();
		}
	}
	
	public long getIdleTime() {
		return idleTime;
	}
}
