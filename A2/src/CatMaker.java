
public class CatMaker {

	public CatMaker() {}
	
	public void startMonitorRobots() {
		//Create threads
		Thread toes1 = new Thread(new LegAssembler());
		Thread toes2 = new Thread(new LegAssembler());
		BodyAssemblerLegs legs1 = new BodyAssemblerLegs();
		BodyAssemblerLegs legs2 = new BodyAssemblerLegs();
		BodyAssemblerTail tail1 = new BodyAssemblerTail();
		BodyAssemblerTail tail2 = new BodyAssemblerTail();
		HeadAssemblerEyes eyes1 = new HeadAssemblerEyes();
		HeadAssemblerEyes eyes2 = new HeadAssemblerEyes();
		HeadAssemblerWhiskers whiskers1 = new HeadAssemblerWhiskers();
		HeadAssemblerWhiskers whiskers2 = new HeadAssemblerWhiskers();
		CatAssembler catMaker = new CatAssembler();
		
		//Start threads
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		toes1.start();toes2.start();
		legs1.start();legs2.start();
		tail1.start();tail2.start();
		eyes1.start();eyes2.start();
		whiskers1.start();whiskers2.start();
		catMaker.start();
		try {
			catMaker.join();
			toes1.join();toes2.join();
			legs1.join();legs2.join();
			tail1.join();tail2.join();
			eyes1.join();eyes2.join();
			whiskers1.join();whiskers2.join();
			endTime = System.currentTimeMillis();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Legs: " +(legs1.getIdleTime() + legs2.getIdleTime()));
		System.out.println("Tail: " + (tail1.getIdleTime() + tail2.getIdleTime()));
		System.out.println("Eyes: " + (eyes1.getIdleTime() + eyes2.getIdleTime()));
		System.out.println("Whiskers: " + (whiskers1.getIdleTime() + whiskers2.getIdleTime()));
		System.out.println("Cat: " + catMaker.getIdleTime());
		long idleTime = legs1.getIdleTime() + legs2.getIdleTime() + tail1.getIdleTime() + tail2.getIdleTime() + eyes1.getIdleTime() + 
						eyes2.getIdleTime() + whiskers1.getIdleTime() + whiskers2.getIdleTime() + catMaker.getIdleTime();
		
		System.out.println("Proportion spent idle(Monitor): " +  ((double)idleTime)/((double)(endTime - startTime)));
	
		q2.forelegBin.clear();
		q2.hindlegBin.clear();
		q2.leglessBin.clear();
		q2.taillessBin.clear();
		q2.eyelessBin.clear();
		q2.whiskerlessBin.clear();
		q2.fullHeadBin.clear();
		q2.fullLowerBin.clear();
		
		q2.produce.set(true);
	}
	
	public void startSemaphoreRobots() {
		//Create threads
				SemLegAssembler toes1 = new SemLegAssembler();
				SemLegAssembler toes2 = new SemLegAssembler();
				SemBodyAssemblerLegs legs1 = new SemBodyAssemblerLegs();
				SemBodyAssemblerLegs legs2 = new SemBodyAssemblerLegs();
				SemBodyAssemblerTail tail1 = new SemBodyAssemblerTail();
				SemBodyAssemblerTail tail2 = new SemBodyAssemblerTail();
				SemHeadAssemblerEyes eyes1 = new SemHeadAssemblerEyes();
				SemHeadAssemblerEyes eyes2 = new SemHeadAssemblerEyes();
				SemHeadAssemblerWhiskers whiskers1 = new SemHeadAssemblerWhiskers();
				SemHeadAssemblerWhiskers whiskers2 = new SemHeadAssemblerWhiskers();
				SemCatAssembler catMaker = new SemCatAssembler();
				
				//Start threads
				long startTime = System.currentTimeMillis();
				long endTime = 0;
				toes1.start();toes2.start();
				legs1.start();legs2.start();
				tail1.start();tail2.start();
				eyes1.start();eyes2.start();
				whiskers1.start();whiskers2.start();
				catMaker.start();
				try {
					catMaker.join();
					toes1.join();toes2.join();
					legs1.join();legs2.join();
					tail1.join();tail2.join();
					eyes1.join();eyes2.join();
					whiskers1.join();whiskers2.join();
					endTime = System.currentTimeMillis();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("Legs: " +(legs1.getIdleTime() + legs2.getIdleTime()));
				System.out.println("Tail: " + (tail1.getIdleTime() + tail2.getIdleTime()));
				System.out.println("Eyes: " + (eyes1.getIdleTime() + eyes2.getIdleTime()));
				System.out.println("Whiskers: " + (whiskers1.getIdleTime() + whiskers2.getIdleTime()));
				System.out.println("Cat: " + catMaker.getIdleTime());
				long idleTime = legs1.getIdleTime() + legs2.getIdleTime() + tail1.getIdleTime() + tail2.getIdleTime() + eyes1.getIdleTime() + 
								eyes2.getIdleTime() + whiskers1.getIdleTime() + whiskers2.getIdleTime() + catMaker.getIdleTime();
				
				System.out.println("Proportion spent idle(Semaphore): " +  ((double)idleTime)/((double)(endTime - startTime)));
			
				q2.forelegBin.clear();
				q2.hindlegBin.clear();
				q2.leglessBin.clear();
				q2.taillessBin.clear();
				q2.eyelessBin.clear();
				q2.whiskerlessBin.clear();
				q2.fullHeadBin.clear();
				q2.fullLowerBin.clear();
				q2.produce.set(true);
	}
}
