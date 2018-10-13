package physicsCore;

import java.util.concurrent.*;;

public class HoldedThread implements Runnable{

	public ConcurrentLinkedQueue<Runnable> queue;
	private Thread thisThread;
	private boolean finishing;
	
	
	public HoldedThread() {
		queue = new ConcurrentLinkedQueue<Runnable>();
		finishing = false;
		thisThread = new Thread(this);	
		thisThread.start();
		
	}

	public void run() {
		while(!finishing) {
			if(queue.isEmpty()) {

				try {
					Thread.currentThread().join(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Thread.currentThread().yield();
			}
			else {
				queue.poll().run();
			}
		}
	}

	public void finalize() {
		finishing = true;
	}

}
