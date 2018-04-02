import java.util.Random;

public class BodyAssemblerTail extends Thread {
	private Random rn = new Random();
	private Body body;
	private long idleTime = 0;
	@Override
	public void run() {
		while(q2.produce.get()) {
			body = getBody();
			
			//Once tail added, can add to body to it's bin.
			try {
				Thread.sleep(rn.nextInt(11)+10);
				body.hasTail = true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(body.hasLegs()) {//if lower body completed add it to full body.
				synchronized(q2.fullLowerBin) {
					q2.fullLowerBin.add(body);
					q2.fullLowerBin.notify();
				}
			}else { //Else need to add a tail.
				synchronized(q2.leglessBin) {
					q2.leglessBin.add(body);
					q2.leglessBin.notify();
				}
			}
		}
		//System.out.println("Stopped Body(Tail) assembler");
	}
	
	//Randomly get a new body, or wait until a body with legs has been created.
//	private Body getBody() {
//		int p = rn.nextInt(10);
//		if (p < 5) {
//			synchronized(q2.bodyBin) {
//				return new Body();
//			}
//		}else {
//			synchronized(q2.taillessBin) {
//				while(q2.taillessBin.size() == 0) {
//					try {
//						q2.taillessBin.wait();
//					}catch(InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				return q2.taillessBin.remove(q2.taillessBin.size()-1);
//			}
//		}
//	}
	
	private Body getBody() {
		long start,end;
		//Check if body with legs is made
		start = System.currentTimeMillis();
		synchronized(q2.taillessBin) {
			if(q2.taillessBin.size() > 0) {
				end = System.currentTimeMillis();
				idleTime += (end - start);
				return q2.taillessBin.remove(q2.taillessBin.size()-1);
			}
		}
		
		//Else just get new body
		synchronized(q2.bodyBin) {
			end = System.currentTimeMillis();
			idleTime += (end - start);
			return new Body();
		}
	}
	
	public long getIdleTime() {
		return idleTime;
	}
}
