package engine_2_0;

import java.io.Serializable;

public class Point implements Serializable {
	public int x, y, z;

	public Point() {
		x = 0;
		y = 0;
		z = 0;
	}

	public Point(int X, int Y, int Z) {
		x = X;
		y = Y;
		z = Z;
	}

	public double getSqrDistanseTo(Point p) {

		return (((x - p.x) ^ 2) + ((y - p.y) ^ 2) + ((z - p.z) ^ 2));
	}

}
