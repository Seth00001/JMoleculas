package engine_2_0;

public class Cluster {
	public Point cen;
	public double r;

	public Cluster() {

	}

	public Cluster(Point p, double radius ) {
		cen = p;
		r = radius;
	}
	
	public double getSqrDistanseTo(Point p) {

		return (((cen.x - p.x) *(cen.x - p.x) ) 
				+ ((cen.y - p.y) * (cen.y - p.y)) 
				+ ((cen.z - p.z) * (cen.z - p.z)));
	}
}
