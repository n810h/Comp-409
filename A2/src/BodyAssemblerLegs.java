import java.util.Random;

/**
 * Body is acquired, with or without tail, and 4 legs are attached to body.
 * Need 2 forelegs, 2 hindlegs.
 * Takes 30-50ms.
 * */
public class BodyAssemblerLegs extends Thread{
	private Random rn = new Random();
	private Leg foreleg;
	private Leg hindleg;
	private Body body;
	private long idleTime = 0;
	
	@Override
	public void run() {
		int p;
		while(q2.produce.get()) {
			body = getBody();
			//More randomization.
			if(rn.nextInt(10) < 5) {
				hindleg = getHindlegs();
				foreleg = getForelegs();
			}else {
				hindleg = getForelegs();
				foreleg = getHindlegs();
			}
			body.addForeleg(foreleg);
			body.addHindleg(hindleg);
			
			//Once legs added, can add to body to it's bin.
			try {
				Thread.sleep(rn.nextInt(21)+30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(body.hasTail) {//if lower body completed add it to full body.
				synchronized(q2.fullLowerBin) {
					q2.fullLowerBin.add(body);
					q2.fullLowerBin.notify();
				}
			}else { //Else need to add a tail.
				synchronized(q2.taillessBin) {
					q2.taillessBin.add(body);
					q2.taillessBin.notify();
				}
			}
		}
		
		//System.out.println("Stopped Body(leg) assembler");
	}
	
	private Body getBody() {
		long start, end;
		//First check if there's any body without legs, just tail
		start = System.currentTimeMillis();
		synchronized(q2.leglessBin) {
			if(q2.leglessBin.size() > 0) {
				end = System.currentTimeMillis();
				idleTime += (end - start);
				return q2.leglessBin.remove(q2.leglessBin.size()-1);
			}
		}
		//If not, just get a new body.
		synchronized(q2.bodyBin) {
			end = System.currentTimeMillis();
			idleTime += (end - start);
			return new Body();
		}
	}
	
	private Leg getHindlegs() {
		long start, end;
		synchronized(q2.hindlegBin) {
			//avoid spurious wakeups
			start = System.currentTimeMillis();
			while(q2.hindlegBin.size() == 0) {
				try {
					q2.hindlegBin.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			end = System.currentTimeMillis();
			idleTime += (end - start);
			return q2.hindlegBin.remove(q2.hindlegBin.size()-1);
		}
	}
	
	private Leg getForelegs() {
		long start,end;
		synchronized(q2.forelegBin) {
			//avoid spurious wakeups
			start = System.currentTimeMillis();
			while(q2.forelegBin.size() == 0) {
				try {
					q2.forelegBin.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			end = System.currentTimeMillis();
			idleTime += (end - start);
			return q2.forelegBin.remove(q2.forelegBin.size()-1);
		}
	}
	
	public long getIdleTime() {
		return idleTime;
	}
}
