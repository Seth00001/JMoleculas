package engine_2_0;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gui.Paintable;

public class HexGridHelper implements Paintable {
	public HexGrid grid;
	public int currentlyPaintedPlane;
	public double r;
	public boolean running;
	public int ax, ay, az, a2, a3, ox, oy, oz;
	public long step;

	public double concentration = 0.00145;// 0.00129688
	public double modifier = 0.8;
	public int stepCount = 16000;

	public HexGridHelper() {
		currentlyPaintedPlane = 0;
		r = 10;
	}

	public HexGridHelper(HexGrid h) {
		this();
		grid = h;
		running = false;
		afterSetGrid();
	}

	public void afterSetGrid() {
		ax = 10;
		ay = 6;
		az = 10;

		a2 = 3;

		Point p = grid.toSpaceCoordinates(grid.dimX, grid.dimY, grid.dimZ);
		Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
		grid.setP0(5);

		r = (byte) (((s.height * ax - 100) / p.y) > (s.width * ax / p.x) ? s.width * ax / p.x
				: (s.height * ax - 100) / p.y);
		r = r / 2;
		step = 0;

		int zMax = 0, zMin = grid.dimX, yMax = 0, yMin = grid.dimY;
		double kk;

		System.out.println(zMin);
		System.out.println(zMax);

		grid.x0 = 0;
		grid.xA = grid.dimX;

		grid.y0 = 0;
		grid.yA = grid.dimY;

		grid.z0 = 0;
		grid.zA = grid.dimZ;
	}

	public void saveGrid() {
		synchronized (grid) {
			try {
				File f = new File("Backups\\" + step + " Backup ");
				f.createNewFile();
				FileOutputStream stream = new FileOutputStream(f);
				ObjectOutputStream writer = new ObjectOutputStream(stream);
				writer.writeObject(grid);
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void loadGrid(String path) {
		synchronized (this) {
			try {
				File f = new File(path);
				FileInputStream stream = new FileInputStream(f);
				ObjectInputStream reader = new ObjectInputStream(stream);
				grid = (HexGrid) reader.readObject();
				reader.close();
				afterSetGrid();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Loading Completed!");
		}

	}

	public void runOneRawThread() {
		running = true;
		new Thread(new Runnable() {
			public void run() {
				int dif = 0;

//				int volume = grid.getVacansys();

				while (running) {

//					dif = (int) ((volume * concentration * modifier) - grid.calculateAtoms());
//					grid.populateTopLevels(dif);
//					System.out.println(grid.coordinates.size() + "  |  " + dif);

					for (int j = 0; j < stepCount; j++) {
						synchronized (grid) {
							for (int i = 0; i < grid.coordinates.size(); i++) {
								grid.jump(grid.coordinates.get(i), i);
							}
							step++;
						}
					}

				}
			}
		}).start();
	}

	public void runMany() {
		running = true;

		/*
		 * 
		 * 
		 * 
		 * */
	}

	public void pauseCalculations() {
		running = false;
	}

	public void saveForUser(String path) {

	}

	public void saveForVMD(String path) throws IOException {
		File file = new File("Prints\\" + path + "VMDReview.pdb");

		file.createNewFile();

		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		int x, y, z;

		synchronized (grid) {
			for (int k = 0; k < grid.dimZ; k++) {
				for (int j = 0; j < grid.dimY; j++) {
					for (int i = 0; i < grid.dimX; i++) {
						if (grid.volume[i][j][k] && grid.getNeirbourgsCount(i, j, k) > 0) {

							// System.out.println(i + " | " + j + " | " + k);

							x = i;
							y = j;
							z = k;

							
//							x = ax * i;
//							y = ay * j;
//							z = az * k;

//							if (k % 2 == 0) {
//
//								if (!grid.isCross(i, j, k)) {
//									x = x - a2;
//								}
//							} else {
//								if (grid.isCross(i, j, k)) {
//									x = x - a2;
//								}
//							}
//
//							if (!grid.isCross(i, j, k)) {
//								z = z - a2;
//							}

							if (grid.isCross(i, j, k)) {
								writer.write("ATOM    100  N   VAL A  25     " + (form(x)) + " " + (form(y)) + " "
										+ (form(z)) + "  1.00 12.00      A1   C   ");
							} else {
								writer.write("ATOM    100  B   VAL A  25     " + (form(x)) + " " + (form(y)) + " "
										+ (form(z)) + "  1.00 12.00      A1   C   ");
							}

							writer.newLine();
						}
					}
				}
			}
		}

		writer.flush();
	}

	public void saveAsDatFile(String s) throws Exception {
		synchronized (grid) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Dats\\" + s + ".dat")));

			for (Point p : grid.coordinates) {
				Point point = grid.toSpaceCoordinates(p.x, p.y, p.z);

				writer.write(point.x + " " + point.y + " " + point.z);
				writer.newLine();
			}
			writer.flush();
			writer.close();
		}
	}

	public String form(int number) {
		String s = Double.toString(number / 10.0);

		while (s.indexOf(".") < 3) {
			s = "0" + s;
		}

		while (s.length() < 7) {
			s += "0";
		}

		return s;
	}

	public void setTemperature(double d) {
		grid.setP0(d);
	}

	public double getTemperature() {
		return grid.p0;
	}

	@Override
	public void paint(Graphics2D g) {
		Point p;
		Toolkit kit = Toolkit.getDefaultToolkit();

		p = grid.toSpaceCoordinates(grid.dimX, grid.dimY, grid.dimZ);

		double cx, cy;
		cx = (kit.getScreenSize().getWidth() - 10) / p.x;
		cy = (kit.getScreenSize().getHeight() - 90) / p.y;

		if (cx > cy) {
			cx = cy;
		}

		int offsetX = (int) (p.x / grid.dimY * cx / 2);
		if (offsetX < 1) {
			offsetX = 1;
		}

		g.setColor(Color.BLUE);
		g.drawRect(0, 0, (int) (p.x * cx), (int) (p.y * cx));

		for (int i = 0; i < grid.dimX; i++) {
			for (int j = 0; j < grid.dimY; j++) {
				if (grid.volume[i][j][currentlyPaintedPlane]) {
					g.setColor(Color.BLUE);
				} else {
					continue;
					// g.setColor(Color.LIGHT_GRAY);
				}
				p = grid.toSpaceCoordinates(i, j, currentlyPaintedPlane);
				g.fillRect((int) (p.x * cx + offsetX), (int) (p.y * cx), offsetX, offsetX);

			}
		}
	}

	public void beginFinalization() {
		running = false;
	}

	public void getGridFromDatFile(String path) {
		File f = new File(path);

		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(f));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Point parseCoordString(String s) {
		Point p = new Point();

		return (p);
	}

}
