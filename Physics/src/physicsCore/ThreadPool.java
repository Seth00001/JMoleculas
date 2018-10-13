package physicsCore;

import java.util.ArrayList;

public class ThreadPool {
	private ArrayList<HoldedThread> threadList;
	
	
	public ThreadPool() {
		threadList = new ArrayList<HoldedThread>();
		for(int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
			threadList.add(new HoldedThread());
		}
	}
	
	public void add(int number, Runnable task) {
		if(number >= threadList.size() || number < 0) {
		}
		else {
			threadList.get(number).queue.add(task);
		}
	}
	
	public void add(Runnable task) {
		int number = 0;
		for(int i = 0; i < threadList.size(); i++) {
			if(threadList.get(number).queue.size() > threadList.get(i).queue.size()) {
				number = i;
			}
		}
		add(number, task);
	}

	public int getThreadCount() {
		return(threadList.size());
	}
	
	public synchronized void setThreadCount(int count) {
		if(count > threadList.size()) {
			for(int i = 0; i < count - threadList.size(); i++) {
				threadList.add(new HoldedThread());
			}	
		}
		else {
			for(int i = count; i >= threadList.size(); i++) {
				threadList.get(i).finalize();
				threadList.remove(i);
			}
		}	
	}

	@Override
	public synchronized void finalize() {
		for(HoldedThread t : threadList) {
			t.finalize();
		}
	}

}
