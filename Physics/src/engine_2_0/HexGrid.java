package engine_2_0;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class HexGrid implements Serializable{ //2.0
	public boolean volume[][][];  //the volume itself
	public ArrayList<Point> coordinates;  //where molecules are sitting
	public int dimX, dimY, dimZ; //easy accessible dimensions
	public Random r;
	
	public Point center;
	public int radiusA, radiusB;
	
	public double p0, p1, ep0;
	public int ax = 10;  //відстань між атомами
	public int ay = 6;   //відступи
	public int az = 10;
	public int a2 = 3;
	public 	double d; //2500000;
	public int x0, xA, y0, yA, z0, zA, calculateBoundary, higherBoundary = 1300, lowerBoundary = 1000,  radius = 1000000;
	public double coefA = 1, coefB = 0, coefC = 0, offsetA = 0, offsetB = 1100, offsetC = 1100;
	
	
	
	
	public HexGrid(int dx, int dy, int dz) {
		center = new Point(4000, 2300, 4000);
		radiusA = 5;
		radiusB = 4000;
		
		r = new Random();
		
		dimX = dx;
		dimY = dy;
		dimZ = dz;
		volume = new boolean[dx][dy][dz];
		coordinates = new ArrayList<>();
	}
	
	public HexGrid(int dim) {
		this(dim, dim, dim);
	}

	public void setP0(double value) {
		p0 = value;
		ep0 = Math.pow(Math.E, p0);
		p1 = Math.pow(0.685, p0);
	}

	public static double Pow(double x, int y) {
		double res = 1;
		
		if(y < 0) return 0;
		
		for(int i = 0; i < y; i++) {
			res = res * x;
		}
		
		return res;
	}
	
	public boolean validate(int x, int y, int z) {
		int i, j, k;


		Point p = toSpaceCoordinates(x, y, z);
	//	d = (coefA*p.x + coefB*p.y + coefC*p.z - coefA*offsetA - coefB*offsetB - coefC*offsetC) 
				/// (coefA * coefA + coefB * coefB + coefC * coefC) ;
		
		return( ( !( x < 0 ||  y < 0 || z < 0 || x >= dimX || y >= dimY || z >= dimZ) )	
				&& ( x + y + z) % 2 == 0
				
				);
	}
	
	public boolean isCross(int i, int j, int k) {
		return ((i + j + k) %2 == 0);		
	}
	
	public boolean isCross(Point p) {
		return(isCross(p.x, p.y, p.z));
	}
	
	public byte getNeirbourgsCount(int x, int y, int z) {
		byte count = 0;
		
			if(validate(x + 1, y + 1, z) && volume[x + 1][y + 1][z]) count++;
			if(validate(x + 1, y - 1, z) && volume[x + 1][y - 1][z]) count++;
			if(validate(x - 1, y + 1, z) && volume[x - 1][y + 1][z]) count++;
			if(validate(x - 1, y - 1, z) && volume[x - 1][y - 1][z]) count++;
			
			if(validate(x + 1, y, z + 1) && volume[x + 1][y][z + 1]) count++;
			if(validate(x - 1, y, z + 1) && volume[x - 1][y][z + 1]) count++;
			if(validate(x, y + 1, z + 1) && volume[x][y + 1][z + 1]) count++;
			if(validate(x, y - 1, z + 1) && volume[x][y - 1][z + 1]) count++;
			
			if(validate(x + 1, y, z - 1) && volume[x + 1][y][z - 1]) count++;
			if(validate(x - 1, y, z - 1) && volume[x - 1][y][z - 1]) count++;
			if(validate(x, y + 1, z - 1) && volume[x][y + 1][z - 1]) count++;
			if(validate(x, y - 1, z - 1) && volume[x][y - 1][z - 1]) count++;
						
		return count;
	}
	
	/*public byte getNeirbourgsCount2(int x, int y, int z) {
		byte count = 0;
		
		if(isCross(x, y, z)) {
			if(validate(x + 1, y, z) && volume[x + 1][y][z]) count++;
			if(validate(x, y + 1, z) && volume[x][y + 1][z]) count++;
			if(validate(x, y - 1, z) && volume[x][y - 1][z]) count++;
			if(validate(x, y, z + 1) && volume[x][y][z + 1]) count++;
		}
		else {
			if(validate(x - 1, y, z) && volume[x - 1][y][z]) count++;
			if(validate(x, y + 1, z) && volume[x][y + 1][z]) count++;
			if(validate(x, y - 1, z) && volume[x][y - 1][z]) count++;
			if(validate(x, y, z - 1) && volume[x][y][z - 1]) count++;			
		}
		return count;
	}
	*/
	public byte getNeirbourgsCount(Point p) {
		return(getNeirbourgsCount(p.x, p.y, p.z));
	}
	
	public byte getPositionWeight(int x, int y, int z){
		if(volume[x][y][z]) {return -1;}
		else {return(getNeirbourgsCount(x, y, z));}
	}
	
	public byte getPositionWeight(Point p){
		return(getNeirbourgsCount(p.x, p.y, p.z));
	}
	
	/*public float[] getFreeProbabs2(int x, int y, int z) {
		float[] probabs = new float[5];

		for(int i = 0; i < probabs.length; i++) {
			probabs[i] = 0;
		}
		
		probabs[0] = (float) Pow(ep0, getNeirbourgsCount(x, y, z));
		
		if(isCross(x, y, z)) {
			if(validate(x + 1, y, z) ) probabs[1] = (float) 1;
			if(validate(x, y + 1, z) ) probabs[2] = (float) 1;
			if(validate(x, y - 1, z) ) probabs[3] = (float) 1;
			if(validate(x, y, z + 1) ) probabs[4] = (float) 1;
		}
		else {
			if(validate(x - 1, y, z) ) probabs[1] = (float) 1;
			if(validate(x, y + 1, z) ) probabs[2] = (float) 1;
			if(validate(x, y - 1, z) ) probabs[3] = (float) 1;
			if(validate(x, y, z - 1) ) probabs[4] = (float) 1;			
		}
		
		return probabs;
	}
	*/
	public float[] getFreeProbabs(int x, int y, int z) {
		float[] probabs = new float[13];

		for(int i = 0; i < probabs.length; i++) {
			probabs[i] = 0;
		}
		
		probabs[0] = (float) Pow(ep0, getNeirbourgsCount(x, y, z));
		
		
		if(validate(x + 1, y + 1, z)) probabs[1] = (float) 1;
		if(validate(x + 1, y - 1, z)) probabs[2] = (float) 1;
		if(validate(x - 1, y + 1, z)) probabs[3] = (float) 1;
		if(validate(x - 1, y - 1, z)) probabs[4] = (float) 1;
		 
		if(validate(x + 1, y, z + 1)) probabs[5] = (float) 1;
		if(validate(x - 1, y, z + 1)) probabs[6] = (float) 1;
		if(validate(x, y + 1, z + 1)) probabs[7] = (float) 1;
		if(validate(x, y - 1, z + 1)) probabs[8] = (float) 1;
		
		if(validate(x + 1, y, z - 1)) probabs[9] = (float) 1;
		if(validate(x - 1, y, z - 1)) probabs[10] = (float) 1;
		if(validate(x, y + 1, z - 1)) probabs[11] = (float) 1;
		if(validate(x, y - 1, z - 1)) probabs[12] = (float) 1;
		
		return probabs;
	}
	
	public float[] getFreeProbabs(Point p) {
		return getFreeProbabs(p.x, p.y, p.z);
	}
	
	/*public float[] getBoundedProbabs2(int x, int y, int z) {
		float[] probabs = new float[5];
		
		for(float f : probabs) f = 0;
		
		probabs[0] = (float) Pow(ep0, getNeirbourgsCount(x, y, z));
		
		if(isCross(x, y, z)) {
			if(validate(x + 1, y, z) && !volume[x + 1][y][z]) probabs[1] = (float) Pow(ep0, getNeirbourgsCount(x + 1, y, z));
			if(validate(x, y + 1, z) && !volume[x][y + 1][z]) probabs[2] = (float) Pow(ep0, getNeirbourgsCount(x, y + 1, z));
			if(validate(x, y - 1, z) && !volume[x][y - 1][z]) probabs[3] = (float) Pow(ep0, getNeirbourgsCount(x, y - 1, z));
			if(validate(x, y, z + 1) && !volume[x][y][z + 1]) probabs[4] = (float) Pow(ep0, getNeirbourgsCount(x, y, z + 1));
		}
		else {
			if(validate(x - 1, y, z) && !volume[x - 1][y][z]) probabs[1] = (float) Pow(ep0, getNeirbourgsCount(x - 1, y, z));
			if(validate(x, y + 1, z) && !volume[x][y + 1][z]) probabs[2] = (float) Pow(ep0, getNeirbourgsCount(x, y + 1, z));
			if(validate(x, y - 1, z) && !volume[x][y - 1][z]) probabs[3] = (float) Pow(ep0, getNeirbourgsCount(x, y - 1, z));
			if(validate(x, y, z - 1) && !volume[x][y][z - 1]) probabs[4] = (float) Pow(ep0, getNeirbourgsCount(x, y, z - 1));			
		}

		return probabs;
	}
	*/
	public float[] getBoundedProbabs(int x, int y, int z) {
		float[] probabs = new float[13];
		
		for(float f : probabs) f = 0;
		
		probabs[0] = (float) Pow(ep0, getNeirbourgsCount(x, y, z));
		
		if(validate(x + 1, y + 1, z) && !volume[x + 1][y + 1][z]) probabs[1] = (float) Pow(ep0, getNeirbourgsCount(x + 1, y + 1, z));
		if(validate(x + 1, y - 1, z) && !volume[x + 1][y - 1][z]) probabs[2] = (float) Pow(ep0, getNeirbourgsCount(x + 1, y - 1, z));
		if(validate(x - 1, y + 1, z) && !volume[x - 1][y + 1][z]) probabs[3] = (float) Pow(ep0, getNeirbourgsCount(x - 1, y + 1, z));
		if(validate(x - 1, y - 1, z) && !volume[x - 1][y - 1][z]) probabs[4] = (float) Pow(ep0, getNeirbourgsCount(x - 1, y - 1, z));
		
		if(validate(x + 1, y, z + 1) && !volume[x + 1][y][z + 1]) probabs[5] = (float) Pow(ep0, getNeirbourgsCount(x + 1, y, z + 1));
		if(validate(x - 1, y, z + 1) && !volume[x - 1][y][z + 1]) probabs[6] = (float) Pow(ep0, getNeirbourgsCount(x - 1, y, z + 1));
		if(validate(x, y + 1, z + 1) && !volume[x][y + 1][z + 1]) probabs[7] = (float) Pow(ep0, getNeirbourgsCount(x, y + 1, z + 1));
		if(validate(x, y - 1, z + 1) && !volume[x][y - 1][z + 1]) probabs[8] = (float) Pow(ep0, getNeirbourgsCount(x, y - 1, z + 1));
		
		if(validate(x + 1, y, z - 1) && !volume[x + 1][y][z - 1]) probabs[9] = (float) Pow(ep0, getNeirbourgsCount(x + 1, y, z - 1));
		if(validate(x - 1, y, z - 1) && !volume[x - 1][y][z - 1]) probabs[10] = (float) Pow(ep0, getNeirbourgsCount(x - 1, y, z - 1));
		if(validate(x, y + 1, z - 1) && !volume[x][y + 1][z - 1]) probabs[11] = (float) Pow(ep0, getNeirbourgsCount(x, y + 1, z - 1));
		if(validate(x, y - 1, z - 1) && !volume[x][y - 1][z - 1]) probabs[12] = (float) Pow(ep0, getNeirbourgsCount(x, y - 1, z - 1));

		return probabs;
	}

	public float[] getBoundedProbabs(Point p) {
		return getBoundedProbabs(p.x, p.y, p.z);
	}
	
	public int getRandomized(float[] d) {
		int i = 0;
		double sum = 0;
		
		for(float doub : d) {
			sum += doub;
		}
		
		float rand = (float) (sum * r.nextDouble() );
		sum = 0;
		for (i = 0; i < d.length; i++) {
			sum += d[i];
			if (sum > rand) {
				return i;
			}
		}
		return 0;
	}
	
	public void move(int i, int j, int k, int x, int y, int z, int n) {
		volume[i][j][k] = false;
		volume[x][y][z] = true;	
		
		Point p = coordinates.get(n);
		
		int n2 = r.nextInt(coordinates.size());
		
		Point p2 = coordinates.get(n2);
		
		p.x = p2.x;
		p.y = p2.y;
		p.z = p2.z;
		
		p2.x = x;
		p2.y = y;
		p2.z = z;
		
	}
	
	///x, y, z - coordinates
	///n - point position in the list
	/*public void jump2(int x, int y, int z, int n) {
		
		if(!validate(x, y, z)) {
			return;
		}
		
		if(getNeirbourgsCount(x, y, z) == 0) {
			switch(getRandomized(getFreeProbabs(x, y, z))) {
				case 0:{
					break;
				}
				
				case 1:{
					if(isCross(x, y, z)) { move(x, y, z, x + 1, y, z, n);} else { move(x, y, z, x - 1, y, z, n);}
					break;
				}
				
				case 2:{
					move(x, y, z, x, y + 1, z, n);			
					break;
							}
				case 3:{
					move(x, y, z, x, y - 1, z, n);
					break;
				}
				case 4:{
					if(isCross(x, y, z)) { move(x, y, z, x, y, z + 1, n);} else { move(x, y, z, x, y, z - 1, n);}
					break;
				}
			}
		}
		else {
			if(r.nextDouble() > Pow(p1, getNeirbourgsCount(x, y, z))) {
				return;
			}
			switch(getRandomized(getBoundedProbabs(x, y, z))) {
				case 0:{
					break;
				}
				
				case 1:{
					if(isCross(x, y, z)) { move(x, y, z, x + 1, y, z, n);} else { move(x, y, z, x - 1, y, z, n);}
					break;
				}
				
				case 2:{
					move(x, y, z, x, y + 1, z, n);			
					break;
							}
				case 3:{
					move(x, y, z, x, y - 1, z, n);
					break;
				}
				case 4:{
					if(isCross(x, y, z)) { move(x, y, z, x, y, z + 1, n);} else { move(x, y, z, x, y, z - 1, n);}
					break;
				}
			}
		}
		
		
	}
	*/
	public void jump(int x, int y, int z, int n) {
		
		if(!validate(x, y, z)) {
			return;
		}
		
		byte neirbourgsCount = getNeirbourgsCount(x, y, z);

		if(neirbourgsCount == 0) {
			switch(getRandomized(getFreeProbabs(x, y, z))) {
				case 0:{
					break;
				}
				
				case 1:{
					move(x, y, z, x + 1, y + 1, z , n);
					break;
				}
				
				case 2:{
					move(x, y, z, x + 1, y - 1, z , n);			
					break;
				}
				case 3:{
					move(x, y, z, x - 1, y + 1, z , n);
					break;
				}
				case 4:{
					move(x, y, z, x - 1, y - 1, z , n);
					break;
				}
				case 5:{
					move(x, y, z, x + 1, y, z + 1 , n);
					break;
				}
				
				case 6:{
					move(x, y, z, x - 1, y, z + 1 , n);			
					break;
				}
				case 7:{
					move(x, y, z, x, y + 1, z + 1 , n);
					break;
				}
				case 8:{
					move(x, y, z, x, y - 1, z + 1 , n);
					break;
				}
				case 9:{
					move(x, y, z, x + 1, y, z - 1 , n);
					break;
				}
				
				case 10:{
					move(x, y, z, x - 1, y, z - 1 , n);			
					break;
				}
				case 11:{
					move(x, y, z, x, y + 1, z - 1 , n);
					break;
				}
				case 12:{
					move(x, y, z, x, y - 1, z - 1 , n);
					break;
				}
			}
		}
		else {
			if(r.nextDouble() > Pow(p1, neirbourgsCount)) {
				return;
			}
			switch(getRandomized(getBoundedProbabs(x, y, z))) {
				case 0:{
					break;
				}
				
				case 1:{
					move(x, y, z, x + 1, y + 1, z , n);
					break;
				}
				
				case 2:{
					move(x, y, z, x + 1, y - 1, z , n);			
					break;
				}
				case 3:{
					move(x, y, z, x - 1, y + 1, z , n);
					break;
				}
				case 4:{
					move(x, y, z, x - 1, y - 1, z , n);
					break;
				}
				case 5:{
					move(x, y, z, x + 1, y, z + 1 , n);
					break;
				}
				
				case 6:{
					move(x, y, z, x - 1, y, z + 1 , n);			
					break;
				}
				case 7:{
					move(x, y, z, x, y + 1, z + 1 , n);
					break;
				}
				case 8:{
					move(x, y, z, x, y - 1, z + 1 , n);
					break;
				}
				case 9:{
					move(x, y, z, x + 1, y, z - 1 , n);
					break;
				}
				
				case 10:{
					move(x, y, z, x - 1, y, z - 1 , n);			
					break;
				}
				case 11:{
					move(x, y, z, x, y + 1, z - 1 , n);
					break;
				}
				case 12:{
					move(x, y, z, x, y - 1, z - 1 , n);
					break;
				}
			}
		}
		
		
	}
	
	
	public void jump(Point p, int n) {
		jump(p.x, p.y, p.z, n);
	}
	
	public void jump(int n) {
		jump(coordinates.get(n), n);
	}
	
	public void setPoint(int x, int y, int z) {
		if(validate(x, y, z) && !volume[x][y][z]) {
			volume[x][y][z] = true;
			coordinates.add(new Point(x, y, z));
		}
	}
	
	public void setPointUnchecked(int x, int y, int z) {
		if(!volume[x][y][z]) {
			volume[x][y][z] = true;
			//coordinates.add(new Point(x, y, z));
		}
	}
	
	public void setPoint(Point p) {
		setPoint(p.x, p.y, p.z);
	}

	public Point toSpaceCoordinates(int x, int y, int z) {
		int i, j, k;
		
/*		i =  ax * x;
		j =  ay * y;
		k =  az * z;*/
		
		i =  x;
		j =  y;
		k =  z;
		
		/*if(z % 2 == 0) {
			
			if(!isCross(x, y, z)) {
				i = i - a2;
			}
		}
		else {
			if(isCross(x, y, z)) {
				i = i - a2;
			}
		}
		
		if(!isCross(x, y, z)) {
			k = k - a2; 
		}*/
		
		return(new Point (i, j, k));
	}
	
	public Point toSpaceCoordinates(Point p) {
		return(this.toSpaceCoordinates(p.x, p.y, p.z));
	}
	
	public synchronized int calculateAtoms() {
		int count = 0;
		
		
		
		for(int k = 0; k < dimZ; k++) {
			for(int i = dimX/2; i < dimX ; i++) {
				for(int j = 0; j < dimY; j++) {
					if( true 
//							&& i + j + k > calculateBoundary
//							&& i + j + k < higherBoundary
							&& validate(i, j, k) 
							&& volume[i][j][k] 
							
						){
						count++;
					}
				}
			}
		}		
		return(count);
	}
	
	public int getVacansys() {
		int count = 0;
		
		
		
		for(int k = 0; k < dimZ; k++) {
			for(int i = dimX/2; i < dimX ; i++) {
				for(int j = 0; j < dimY; j++) {
					if( true 
//							&& i + j + k > calculateBoundary
//							&& i + j + k < higherBoundary
							&& validate(i, j, k) 							
						){
						count++;
					}
				}
			}
		}		
		return count;
	}
	
	public synchronized void populateTopLevels(int count) {
		int x, y, z;
		
		while(count > 0) {
			x = xA/2 + r.nextInt(xA/2);
			y = y0 + r.nextInt(yA);
			z = z0 + r.nextInt(zA);
			//z = (int) (z + zA/2);
			
			if( true
					&& validate(x, y, z) 
					&& !volume[x][y][z] 
					//&& 6 * p.x + 10 * p.y + 6 * p.z >= higherBoundary/50 * 45
					//&& x + y + z >= calculateBoundary
					
				){
				setPoint(x, y, z);
				//System.out.println( count );
			}
			else {
				//System.out.println(x + "  " + y + "  " + z);
				continue;
			}
			count--;
		}
	}
	
	public synchronized void populateAllLevels(int count) {
		int x, y, z;
		
		while(count > 0) {
			x = x0 + r.nextInt(xA);
			y = y0 + r.nextInt(yA);
			z = z0 + r.nextInt(zA);
			
			if(true
				&& validate(x, y, z) 
				&& !volume[x][y][z] 
			) {
				setPoint(x, y, z);
				//System.out.println( count );
			}
			else {
				//System.out.println(z0 + "  " + zA+ "  " + z);
				continue;
			}
			count--;
		}
	}
	
}
