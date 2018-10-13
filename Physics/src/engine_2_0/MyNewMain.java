package engine_2_0;

import java.time.temporal.JulianFields;
import java.util.ArrayList;

import gui.Window;

public class MyNewMain {
	
	 static double coefA, coefB, coefC;
	
	public static String path = "Backups\\3155004 Backup";
	
	public static void main(String[] args) {
		
		GridInitializer init = new GridInitializer();
		HexGridHelper helper = new HexGridHelper(init.grid);
		
		init.populateGrid();
		//init.drawClusters();
//		init.clusters = init.toSpaceCoord(init.grid, init.clusters);
		init.shift();
		init.trim();
		
		init.drawClusters();
		
		
		Window win = new Window();
		
		helper.currentlyPaintedPlane = 0;
		
		win.setHelper(helper);
		win.refreshPanel();
		
		
		
		//System.out.println(Runtime.getRuntime().totalMemory());
		
		
		new Thread(new Runnable() {
			public void run() {
				
				while(true) {
					try {
						Thread.currentThread().sleep(win.backupPeriod);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(helper.step);
				}
			}
		}).start();		
	
		//System.out.println(helper.grid.coordinates.size());
	
	}	
	
}




