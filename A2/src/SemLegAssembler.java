import java.util.Random;

public class SemLegAssembler extends Thread{
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
			q2.semhindlegBin.produce(leg);
		}else {
			q2.semforelegBin.produce(leg);
		}
	}
	
	private int getToe() {
		synchronized(q2.toeBin) {
			return (rn.nextInt(10) < 4 ? 4 : 5);
		}
	}
}
