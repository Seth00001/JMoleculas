package engine_2_0;

import java.io.Serializable;

public class DPoint implements Serializable {

	public double x, y, z;

	public DPoint() {
		x = 0;
		y = 0;
		z = 0;
	}

	public DPoint(double X, double Y, double Z) {
		x = X;
		y = Y;
		z = Z;
	}

	public double calcModule() {
		return Math.sqrt((x * x) + (y * y) + (z * z));
	}

	public DPoint normalizePoint() {
		double module = calcModule();
		return new DPoint(x / module, y / module, z / module);
	}
	public void  normalize() {
		double module = calcModule();
		x/=module;
		y/=module;
		z/=module;
		
		
		
	}
}
