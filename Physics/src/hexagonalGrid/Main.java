package hexagonalGrid;

import java.util.Scanner;

public class Main {
	public static HexGrid grid;
	public static long steps;
	
	public static void main(String[] args) {
		grid = new HexGrid(60, 60, 60);
		steps = 0;
		Scanner s = new Scanner(System.in);
		
		Thread t = new Thread(new ThreadProvider());
		
		
		while(true) {
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			grid.HorsPlaneXY(30);	
		}
	}
	
	
	
	static class ThreadProvider implements Runnable{
		public ThreadProvider() {
			
		}
		public void run() {
			grid.p0 = 3.5;
	        grid.p1 = 0.05;
	        grid.p1 = Math.pow(0.4, grid.p0);

	        int n = 10000;


	        for (int i = 10; i < 50; i++)
	        {
	            for (int j = 10; j < 50; j++)
	            {
	                for (int k = 10; k < 50; k++)
	                {
	                    grid.placeOne(i, j, k);
	                }
	            }
	        }
	        
	        for (; ; )
	        {
	            for (int j = 0; j < n; j++)
	            {
	                grid.cycledMethod();
	                steps++;
	                grid.Shuffle();
	            }
	           // grid.CreateBackup();
	        }
		}

	}
	
	
}
