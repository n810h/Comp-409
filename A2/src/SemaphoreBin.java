import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class SemaphoreBin<E> {
	Semaphore mutex = new Semaphore(1);
	Semaphore count = new Semaphore(0);
	ArrayList<E> bin = new ArrayList<E>();
	
	public void produce(E newObj) {
		try {
			mutex.acquire();
			bin.add(newObj);
			mutex.release();
			count.release();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public E consume() {
		E consumed = null;
		try {
			count.acquire();
			mutex.acquire();
			consumed = bin.remove(bin.size() -1);
			mutex.release();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		return consumed;
	}
	
	public E consumeIfExists() {
		E consumed = null;
		if(count.tryAcquire()) {
			try {
				mutex.acquire();
				consumed = bin.remove(bin.size()-1);
				mutex.release();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		return consumed;
	}
}
