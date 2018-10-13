package engine_2_0;

import java.time.temporal.JulianFields;
import java.util.ArrayList;

import gui.Window;

public class Main {
	
	static double coefA, coefB, coefC;
	
	public static String path = "Backups\\3155004 Backup";
	
	public static void main(String[] args) {
		
		int radius, height, dim;
		radius = 1000;
		height = 3000;
		
		//dim = (int) ( (height + 2 * radius * (Math.sqrt(3) - 1)) / Math.sqrt(3) / 10 ) + 10;
		
		//System.out.println(dim);
		dim = 100;
		
//		HexGrid grid = createGrid(6*dim, 10*dim, 6*dim);
		HexGrid grid = createSphere(200, 200, 200);
		HexGridHelper helper = new HexGridHelper(grid);
		
		
		
//		HexGridHelper helper = new HexGridHelper();
//		helper.loadGrid(path);
		
		System.out.println(helper.grid.lowerBoundary + "   " + helper.grid.higherBoundary);
		
		
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
	
	
	public static HexGrid createSphere(int dx, int dy, int dz) {
		HexGrid grid = new HexGrid(dx, dy, dz);
		
		int radius = 40;
		Point p = new Point(dx/2, dy/2, dz/2);
		
		for(int i = 0; i < dx; i++) {
			for(int j = 0; j < dy; j++) {
				for(int k = 0; k < dz; k++) {
					if((i - p.x)*(i - p.x) + (j - p.y)*(j - p.y) + (k - p.z)*(k - p.z) <= radius*radius) {
						grid.setPoint(i, j, k);
					}
				}
			}
		}
		
		
		
		return grid;
	}
	
	public static HexGrid createHexClusters(int dx, int dy, int dz) {
		HexGrid grid = new HexGrid(dx, dy, dz);
		
		int kx = 1;
		int ky = 1;
		int kz = dz/2 - 1;
		int radius = 5;
		int dist = 40;
		Point p;
		grid.radiusA = 2000;
		grid.radiusB = 2000;

		double a2 = dist * 0.3;
		double dix, diy, diz;
		Point dim = grid.toSpaceCoordinates(dx, dy, dz);
		
		ArrayList<DPoint> centers = new ArrayList<>();
		
		
		//collecting centers
		for(int i = 0; i < dim.x / dist; i++) {
			for(int j = 0; j < dim.y / dist; j++) {
				for(int k = 0; k < dim.z / dist; k++) {
					DPoint dp = new DPoint(i * dist, j * dist, k * dist);
					
					dp.y = dp.y * 0.6;
					
					if (k % 2 == 0) {
						
						if ((i + j + k) % 2 == 0) {
							dp.x = dp.x + a2;
							dp.z = dp.z - a2/2;
						}
						
//						if ((i + j + k) %2 != 0) {
//							dp.z = dp.z + a2;
//						}
						
					} else {
						
						if ((i + j + k) %2 == 0) {
							dp.x = dp.x + a2;
							dp.z = dp.z - a2/2;
						}
						
//						if ((i + j + k) %2 != 0) {
//							
//						}
					}

					
					System.out.println(i + "  " +  j + "  " +  k);
					
					centers.add(dp);
				}
			}
		}
		
		int counter = 0;
		
		for(DPoint cen : centers) {
			for(int i = 0; i < dx; i++) {
				for(int j = 0; j < dy; j++) {
					for(int k = 0; k < dz; k++) {
						Point tp = grid.toSpaceCoordinates(i, j, k); 
						
						if((tp.x - cen.x)*(tp.x - cen.x) + (tp.y - cen.y)*(tp.y - cen.y) + (tp.z - cen.z)*(tp.z - cen.z) <= radius*radius) 
						{
							grid.setPoint(i,  j,  k);
						}
						
					}
				}
			}
			
			System.out.println(counter + "  / " + centers.size());
			counter++;
		}
		
		
		return grid;
	}

	
	public static boolean getConditions(int i, int j, int k) {
		boolean res = true;
		
		//straight planes
		
		res = res && i > 25;
		res = res && i < 300;
		
		res = res && j > 25;
		res = res && j < 300;
		
		res = res && k > 25;
		res = res && k < 300;
		
		//once tilted planes:
		
//		res = res && i + j > 3;
//		res = res && i + j < 17;
//
//		res = res && i + k > 3;
//		res = res && i + k < 17;
//		
//		res = res && k + j > 3;
//		res = res && k + j < 17;

		//double tilted planes
		
//		res = res && i + i + k > 7;
//		res = res && i + i + k < 15;
		
		return res;
	}
	
	public static PointCont getTwistedCoord(int i, int j, int k) {
		PointCont p = new PointCont();		
		
		p.x = (int) Math.round(3*coefA*i + 5*coefA * j - coefC*k);
		p.y = (int) Math.round(- 10*coefB * i + 6*coefB * j);
		p.z = (int) Math.round(3*coefA*i + 5*coefA * j + coefC*k);
		
		return(p);
	}
	
	public static PointCont getTwistedCoord(Point p) {
		return(getTwistedCoord(p.x, p.y, p.z));
	}
			
	public static class PointCont {
		public double x, y, z;
		
		public PointCont() {
		}
		
		public PointCont(int i, int j, int k) {
			x = i;
			y = j;
			z = k;
		}
		
		public PointCont(Point p) {
			x = p.x;
			y = p.y;
			z = p.z;
		}
		
	}
}




