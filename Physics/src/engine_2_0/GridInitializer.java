package engine_2_0;

import java.util.ArrayList;

public class GridInitializer {

	public HexGrid grid;
	public double radius, distance, driftDist, shrinkCoef, trimCoef;

	ArrayList<Cluster> clusters;

	public GridInitializer() {
		grid = new HexGrid(100);
		clusters = new ArrayList<Cluster>();
		radius = 8;
		distance = 0.5;
		driftDist = radius * 0.25;
		trimCoef = 0.9;
	}

	public void populateGrid() {
		double twoRad = 2 * radius;
		double twoRadAndDist = twoRad + distance;
		int nx = (int) (grid.dimX / twoRadAndDist); // count of clusters in every direction
		int ny = (int) (grid.dimY / twoRadAndDist); 
		int nz = (int) (grid.dimZ / twoRadAndDist);

		for (int i = 0; i < nx; i++) {
			for (int j = 0; j < ny; j++) {
				for (int k = 0; k < nz; k++) {
					clusters.add(new Cluster(new Point(
							(int) (i * twoRadAndDist + twoRad),
							(int) (j * twoRadAndDist + twoRad), 
							(int) (k * twoRadAndDist + twoRad)), radius));
				}
			}
		}

		for (Cluster c : clusters) {
			System.out.println(c.cen.x + "  " + c.cen.y + "  " + c.cen.z);
		}
	}

	public void shift() {
		DPoint vector = new DPoint();

		for (Cluster c : clusters) {
			vector.x = (1000 * Math.random());
			vector.y = (1000 * Math.random());
			vector.z = (1000 * Math.random());
			// System.out.println(vector.x+" "+vector.y+" "+vector.z);

			vector.normalize();

			// System.out.println(vector.x+" "+vector.y+" "+vector.z);

			c.cen.x += (int) (vector.x * Math.random() * driftDist);
			c.cen.y += (int) (vector.y * Math.random() * driftDist);
			c.cen.z += (int) (vector.z * Math.random() * driftDist);

			System.out.println(c.cen.x + "  " + c.cen.y + "  " + c.cen.z);

		}

	}

	public void trim() {
		double mindistonstart = (4 * radius + 4 * distance) * (4 * radius + 4 * distance);
		double mindist;
		double dist;
		for (Cluster cluster : clusters) {
			mindist = mindistonstart;
			for (Cluster clusterfromall : clusters) {
				dist = cluster.getSqrDistanseTo(clusterfromall.cen);
				if (dist == 0) {
					continue;
				} else if (dist < (4 * radius * radius) && dist < mindist) {
					mindist = dist;
				}

			}
			if (Math.sqrt(mindist) / 2 < cluster.r) {
				cluster.r = (Math.sqrt(mindist) / 2) * trimCoef;
			}

		}

	}

	public void setSphare(Cluster cluster) {// розставляю точки для 1 кластера
		for (int i = 0; i < grid.dimX; i++) {
			for (int j = 0; j < grid.dimY; j++) {
				for (int k = 0; k < grid.dimZ; k++) {
					if (cluster.getSqrDistanseTo(new Point(i, j, k)) < (cluster.r * cluster.r)) {
						grid.setPoint(i, j, k);
						// 1
					}
				}
			}
		}

	}

	public void drawClusters() {
		for (Cluster cluster : clusters) {
			setSphare(cluster);
		}

	}

	public ArrayList<Cluster> toSpaceCoord(HexGrid grid, ArrayList<Cluster> clusters) {
		Cluster tempClust;
		Point tempPoint;
		ArrayList<Cluster> spaceClusters = new ArrayList<Cluster>();
		System.out.println(clusters.size());
		for (Cluster clast : clusters) {
			tempPoint = clast.cen;
			tempPoint = grid.toSpaceCoordinates(tempPoint);

			tempClust = new Cluster(tempPoint, radius * 10);

			spaceClusters.add(tempClust);

		}
		System.out.println(spaceClusters.size());
		return spaceClusters;
	}
}
