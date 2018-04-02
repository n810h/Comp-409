import java.util.Random;

public class SemBodyAssemblerTail extends Thread{
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
				q2.semfullLowerBin.produce(body);
			}else { //Else need to add a tail.
				q2.semleglessBin.produce(body);
			}
		}
		//System.out.println("Stopped Body(Tail) assembler");
	}
	
	private Body getBody() {
		//Check if a body with tail exists.
		Body body = q2.semtailessBin.consumeIfExists();
		if(body != null)
			return body;
		//Get body from infinite bins
		else {
			return new Body();
		}
	}
	
	public long getIdleTime() {
		return idleTime;
	}
}
