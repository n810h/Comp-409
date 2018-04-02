import java.util.Random;

public class HeadAssemblerEyes extends Thread{
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
				synchronized(q2.fullHeadBin) {
					q2.fullHeadBin.add(head);
					q2.fullHeadBin.notify();
				}
			}else { //Else need to add whiskers.
				synchronized(q2.whiskerlessBin) {
					q2.whiskerlessBin.add(head);
					q2.whiskerlessBin.notify();
				}
			}
			
		}
		//System.out.println("Stopped Eye Assembler");
	}
	
//	private Head getHead() {
//		int p = rn.nextInt(10);
//		if (p < 5) {
//			synchronized(q2.headBin) {
//				return new Head();
//			}
//		}else {
//			synchronized(q2.eyelessBin) {
//				while(q2.eyelessBin.size() == 0) {
//					try {
//						q2.eyelessBin.wait();
//					}catch(InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				return q2.eyelessBin.remove(q2.eyelessBin.size()-1);
//			}
//		}
//	}	
	
	private Head getHead() {
		long start,end;
		start = System.currentTimeMillis();
		synchronized(q2.eyelessBin) {
			if(q2.eyelessBin.size() > 0) {
				end = System.currentTimeMillis();
				idleTime += (end - start);
				return q2.eyelessBin.remove(q2.eyelessBin.size()-1);
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
