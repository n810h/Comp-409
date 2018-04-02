import java.util.Random;

public class LegAssembler implements Runnable {
	private Leg leg;
	private Random rn = new Random();
	
	public void run() {
		while(q2.produce.get()) {
			int toes = getToe();
			leg = new Leg(toes);
			
			try {
				Thread.sleep(rn.nextInt(11)+10);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			addLeg();
		}
	}
	
	private void addLeg() {
		if(leg.toes == 4) {
			synchronized(q2.hindlegBin) {
					q2.hindlegBin.add(leg);
					q2.hindlegBin.notify();
			}
		}else {
			synchronized(q2.forelegBin) {
				q2.forelegBin.add(leg);
				q2.forelegBin.notify();
			}
		}
	}
	
	private int getToe() {
		synchronized(q2.toeBin) {
			return (rn.nextInt(10) < 4 ? 4 : 5);
		}
	}
}
