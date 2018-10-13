package hexagonalGrid;

import java.io.Serializable;

public class Molecula implements Serializable{
	public int x;
	public int y;
	public int z;
	
	public Molecula(int i, int j, int k ) {
		x = i;
		y = j;
		z = k;
	}
	
	public String toString() {
		return x + " " + y + " " + z;
	}
	
}
