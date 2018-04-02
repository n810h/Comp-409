import java.util.Random;

public class SemBodyAssemblerLegs extends Thread {
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
				q2.semfullLowerBin.produce(body);
			}else { //Else need to add a tail.
				q2.semtailessBin.produce(body);
			}
		}
		
		//System.out.println("Stopped Body(leg) assembler");
	}
	
	private Body getBody() {
		//Check if a body with tail exists.
		Body body = q2.semleglessBin.consumeIfExists();
		if(body != null)
			return body;
		//Get body from infinite bins
		else {
			return new Body();
		}
	}
	
	private Leg getHindlegs() {
		long start, end;
		start = System.currentTimeMillis();
		Leg leg = q2.semhindlegBin.consume();
		end = System.currentTimeMillis();
		idleTime += (end - start);
		return leg;
	}
	
	private Leg getForelegs() {
		long start,end;
		start = System.currentTimeMillis();
		Leg leg = q2.semforelegBin.consume();
		end = System.currentTimeMillis();
		idleTime += (end-start);
		return leg;
	}
	
	public long getIdleTime() {
		return idleTime;
	}
}
