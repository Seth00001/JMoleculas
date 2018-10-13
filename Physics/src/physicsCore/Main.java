package physicsCore;

public class Main {
	
	public static int done;
	
	public static void main(String[] args) {
		done = 0;
		
		ThreadPool pool = new ThreadPool();
		pool.setThreadCount(40);

		System.out.println(pool.getThreadCount());
		System.out.println("---------------------");

		pool.add(new AbstractTask1("1"));
		pool.add(new AbstractTask2("2"));
		pool.add(new AbstractTask3("3"));
		pool.add(new AbstractTask4("4"));
		pool.add(new AbstractTask1("5"));
		pool.add(new AbstractTask2("6"));
		pool.add(new AbstractTask3("7"));
		pool.add(new AbstractTask4("8"));
		pool.add(new AbstractTask1("9"));
		pool.add(new AbstractTask2("10"));
		pool.add(new AbstractTask3("11"));
		pool.add(new AbstractTask4("12"));
		pool.add(new AbstractTask1("13"));
		pool.add(new AbstractTask2("14"));
		pool.add(new AbstractTask3("15"));
		pool.add(new AbstractTask4("12"));
	}
}

class AbstractTask1 implements Runnable {

	public String message;

	public AbstractTask1(String s) {
		message = s;
	}

	public void run() {
		for (int i = 0; i < 1000; i++) {
			try {
				Thread.currentThread().sleep(1);
				System.out.println(message);
			} catch (InterruptedException e) {

			}
		}
	}
}

class AbstractTask2 implements Runnable {
	public String message;

	public AbstractTask2(String s) {
		message = s;
	}

	public void run() {
		for (int i = 0; i < 1000; i++) {
			try {
				Thread.currentThread().sleep(1);
				System.out.println(message);
			} catch (InterruptedException e) {

			}
		}
	}
}

class AbstractTask3 implements Runnable {

	public String message;

	public AbstractTask3(String s) {
		message = s;
	}

	public void run() {
		for (int i = 0; i < 1000; i++) {
			try {
				Thread.currentThread().sleep(1);
				System.out.println(message);
			} catch (InterruptedException e) {

			}
		}
	}
}

class AbstractTask4 implements Runnable {

	public String message;

	public AbstractTask4(String s) {
		message = s;
	}

	public void run() {
		for (int i = 0; i < 1000; i++) {
			try {
				Thread.currentThread().sleep(1);
				System.out.println(message);
			} catch (InterruptedException e) {

			}
		}
	}
}
