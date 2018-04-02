import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class q2 {
	//Used to keep track of items produced. (In monitor solution)
	
	public static ArrayList<Leg> hindlegBin = new ArrayList<Leg>();
	public static ArrayList<Leg> forelegBin = new ArrayList<Leg>();
	public static ArrayList<Body> fullLowerBin = new ArrayList<Body>();
	public static ArrayList<Body> leglessBin = new ArrayList<Body>();
	public static ArrayList<Body> taillessBin = new ArrayList<Body>();
	public static ArrayList<Head> whiskerlessBin = new ArrayList<Head>();
	public static ArrayList<Head> eyelessBin = new ArrayList<Head>();
	public static ArrayList<Head> fullHeadBin = new ArrayList<Head>();
	
	//Used  for semaphore solution.
	public static SemaphoreBin<Leg> semhindlegBin = new SemaphoreBin<Leg>();
	public static SemaphoreBin<Leg> semforelegBin = new SemaphoreBin<Leg>();
	public static SemaphoreBin<Body> semfullLowerBin = new SemaphoreBin<Body>();
	public static SemaphoreBin<Body> semleglessBin = new SemaphoreBin<Body>();
	public static SemaphoreBin<Body> semtailessBin = new SemaphoreBin<Body>();
	public static SemaphoreBin<Head> semwhiskerlessBin = new SemaphoreBin<Head>();
	public static SemaphoreBin<Head> semeyelessBin = new SemaphoreBin<Head>();
	public static SemaphoreBin<Head> semfullHeadBin = new SemaphoreBin<Head>();
	
	//Used for infinte bins to restrict only one thread accessing them at a time.
	public static Object toeBin = new Object();
	public static Object bodyBin = new Object();
	public static Object legBin = new Object();
	public static Object headBin = new Object();
	public static Object eyeBin = new Object();
	public static Object whiskerBin = new Object();
	public static Object tailBin = new Object();
	
	//Stops threads
	public static AtomicBoolean produce = new AtomicBoolean(true);
	
	public static void main(String[] args) {
		CatMaker catAssembly = new CatMaker();
		catAssembly.startMonitorRobots();
		catAssembly.startSemaphoreRobots();
	}
}
