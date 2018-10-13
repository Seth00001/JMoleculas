package hexagonalGrid;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import gui.Paintable;
import physicsCore.ThreadPool;

public class HexGrid implements Serializable{
	public ThreadPool pool;

	public Molecula[][][] volume;
	public ArrayList<Molecula> moleculas;
	public int dimX, dimY, dimZ;
	private Random r;
	
	public double p0 = 5; //vacancy weight modifier
    public double p1 = 0.1; //place lack of energy modifier

	public HexGrid(int DimX, int DimY, int DimZ) {
		dimX = DimX;
		dimY = DimY;
		dimZ = DimZ;

		volume = new Molecula[dimX][dimY][dimZ];
		moleculas = new ArrayList<Molecula>(0);
		r = new Random();
	}

	public HexGrid(int dim) {
		this(dim, dim, dim);
	}


	
	public int getDimX() {
		return dimX;
	}

	public int getDimY() {
		return dimY;
	}

	public int getDimZ() {
		return dimZ;
	}

	public void placeOne(int i, int j, int k)
    {
        Molecula mol = new Molecula(i, j, k);
        moleculas.add(mol);
        volume[i][j][k] = mol;
    }
	
	public void move(Molecula m1, int x, int y, int z) {
		volume[m1.x][m1.y][m1.z] = null;
		volume[x][y][z] = m1;
		m1.x = x;
		m1.y = y;
		m1.z = z;
	}

	public int getRandomized(double[] d) {
		int i = 0;
		double sum = 0;
		
		for(Double doub : d) {
			sum += doub;
		}
		
		double rand = sum * r.nextDouble();
		sum = 0;
		for (i = 0; i < d.length; i++) {
			sum += d[i];
			if (sum > rand) {
				break;
			}
		}
		return i;
	}

	public boolean isCross(int x, int y, int z) {
		if ((x + y + z) % 2 == 0) {
			return true;
		}
		return false;
	}

	public boolean Validate(int i, int j, int k) {
		if (i >= 0 && i < dimX && j >= 0 && j < dimY && k >= 0 && k < dimZ) {
			return true;
		} else {
			return (false);
		}
	}

	public int GetNeirbourgsCount(int i, int j, int k) {
		byte count = 0;

		if (!isCross(i, j, k)) {
			if (Validate(i + 1, j, k) && volume[i + 1][j][k] != null) {
				count++;
			}

			if (Validate(i, j + 1, k) && volume[i][j + 1][k] != null) {
				count++;
			}
			if (Validate(i, j - 1, k) && volume[i][j - 1][k] != null) {
				count++;
			}

			if (Validate(i, j, k + 1) && volume[i][j][k + 1] != null) {
				count++;
			}
		} else {
			if (Validate(i - 1, j, k) && volume[i - 1][j][k] != null) {
				count++;
			}

			if (Validate(i, j + 1, k) && volume[i][j + 1][k] != null) {
				count++;
			}
			if (Validate(i, j - 1, k) && volume[i][j - 1][k] != null) {
				count++;
			}

			if (Validate(i, j, k - 1) && volume[i][j][k - 1] != null) {
				count++;
			}
		}
		return (count);
	}

	public void Shuffle() {
		Molecula temp;
		int coord1, coord2;
		for (int i = 0; i < moleculas.size(); i++) {
			coord1 = r.nextInt(moleculas.size());
			coord2 = r.nextInt(moleculas.size());

			temp = moleculas.get(coord1);
			moleculas.set(coord1, moleculas.get(coord2));
			moleculas.set(coord1, temp);
		}
	}
	
	public LinkedList<Molecula> Shuffle (LinkedList<Molecula> list){	
		
		ListIterator<Molecula> iter = (ListIterator) list.iterator();
		
		Molecula temp = null;
		
		for(int i = 0; i < list.size(); i++) {
			
			if(r.nextDouble() > 0.9) {
				if(temp != null) {
					iter.add(temp );
				}
				temp = iter.next();
				iter.remove();
			}
			else {
				iter.next();
			}
		}
		if(temp != null) {
			iter.add(temp );
		}
		
		return(list);
	}
		
		
		
	public ArrayList<Molecula> Shuffle(ArrayList<Molecula> mol) {
		Molecula temp;
		int coord1, coord2;
		for (int i = 0; i < mol.size(); i++) {
			coord1 = r.nextInt(mol.size());
			coord2 = r.nextInt(mol.size());

			temp = mol.get(coord1);
			mol.set(coord1, mol.get(coord2));
			mol.set(coord1, temp);
		}
		return(mol);
	}
	
	public void JumpFree(int i, int j, int k)
    {
        double[] probabs = new double[5];
        probabs[0] = 1;
        if (!isCross(i, j, k))
        {
            if (Validate(i + 1, j, k))
            {
            	if(volume[i + 1][j][k] == null) {
                    probabs[1] = 1;	
            	}
            }
            else {
            	probabs[1] = 0;
            }
            
            
            if (Validate(i, j + 1, k))
            {
            	if(volume[i][j + 1][k] == null) {
                    probabs[2] = 1;	
            	}
            }
            else {
            	probabs[2] = 0;
            }
            if (Validate(i, j - 1, k))
            {
            	if(volume[i][j - 1][k] == null) {
                    probabs[3] = 1;	
            	}
            }
            else {
            	probabs[3] = 0;
            }
            
            
            if (Validate(i, j, k + 1))
            {
            	if(volume[i][j][k + 1] == null) {
                    probabs[4] = 1;	
            	}
            }
            else {
            	probabs[4] = 0;
            }
            
            switch (getRandomized(probabs))
            {
                case 0:
                    {
                        break;
                    }

                case 1:
                    {
                        move(volume[i][j][k], i + 1, j, k);
                        break;
                    }

                case 2:
                    {
                        move(volume[i][j][k], i, j + 1, k);
                        break;
                    }

                case 3:
                    {
                        move(volume[i][j][k], i, j - 1, k);
                        break;
                    }

                case 4:
                    {
                        move(volume[i][j][k], i, j, k + 1);
                        break;
                    }
            }




        }
        else
        {
        	if (Validate(i - 1, j, k))
            {
            	if(volume[i - 1][j][k] == null) {
                    probabs[1] = 1;	
            	}
            }
            else {
            	probabs[1] = 0;
            }
            
            
            if (Validate(i, j + 1, k))
            {
            	if(volume[i][j + 1][k] == null) {
                    probabs[2] = 1;	
            	}
            }
            else {
            	probabs[2] = 0;
            }
            if (Validate(i, j - 1, k))
            {
            	if(volume[i][j - 1][k] == null) {
                    probabs[3] = 1;	
            	}
            }
            else {
            	probabs[3] = 0;
            }
            
            
            if (Validate(i, j, k - 1))
            {
            	if(volume[i][j][k - 1] == null) {
                    probabs[4] = 1;	
            	}
            }
            else {
            	probabs[4] = 0;
            }

            switch (getRandomized(probabs))
            {
                case 0:
                    {
                        break;
                    }

                case 1:
                    {
                        move(volume[i][j][k], i - 1, j, k);
                        break;
                    }

                case 2:
                    {
                        move(volume[i][j][k], i, j + 1, k);
                        break;
                    }

                case 3:
                    {
                        move(volume[i][j][k], i, j - 1, k);
                        break;
                    }

                case 4:
                    {
                        move(volume[i][j][k], i, j, k - 1);
                        break;
                    }
            }
        
        }
    }

	public void jump(int i, int j, int k)
    {
        if (r.nextDouble() >= Math.pow(p1, GetNeirbourgsCount(i, j, k)))
        {
            return;
        }

        double[] probabs = new double[5];
        probabs[0] = 1;
        if (!isCross(i, j, k))
        {
            if (Validate(i + 1, j, k) && volume[i + 1][j][k] == null)
            {
                probabs[1] = Math.exp(p0 * GetNeirbourgsCount(i + 1, j, k));
            }

            if (Validate(i, j + 1, k) && volume[i][j + 1][k] == null)
            {
                probabs[2] = Math.exp(p0 * GetNeirbourgsCount(i, j + 1, k));
            }
            if (Validate(i, j - 1, k) && volume[i][j - 1][k] == null)
            {
                probabs[3] = Math.exp(p0 * GetNeirbourgsCount(i, j - 1, k));
            }

            if (Validate(i, j, k + 1) && volume[i][j][k + 1] == null)
            {
                probabs[4] = Math.exp(p0 * GetNeirbourgsCount(i, j, k + 1));
            }

            switch (getRandomized(probabs))
            {
                case 0:
                    {
                        break;
                    }

                case 1:
                    {
                        move(volume[i][j][k], i + 1, j, k);
                        break;
                    }

                case 2:
                    {
                        move(volume[i][j][k], i, j + 1, k);
                        break;
                    }

                case 3:
                    {
                        move(volume[i][j][k], i, j - 1, k);
                        break;
                    }

                case 4:
                    {
                        move(volume[i][j][k], i, j, k + 1);
                        break;
                    }
            }
        }
        else
        {
            if (Validate(i - 1, j, k) && volume[i - 1][j][k] == null)
            {
                probabs[1] = Math.exp(p0 * GetNeirbourgsCount(i - 1, j, k));
            }

            if (Validate(i, j + 1, k) && volume[i][j + 1][k] == null)
            {
                probabs[2] = Math.exp(p0 * GetNeirbourgsCount(i, j + 1, k));
            }
            if (Validate(i, j - 1, k) && volume[i][j - 1][k] == null)
            {
                probabs[3] = Math.exp(p0 * GetNeirbourgsCount(i, j - 1, k));
            }

            if (Validate(i, j, k - 1) && volume[i][j][k - 1] == null)
            {
                probabs[4] = Math.exp(p0 * GetNeirbourgsCount(i, j, k - 1));
            }

            switch (getRandomized(probabs))
            {
                case 0:
                    {
                        break;
                    }

                case 1:
                    {
                        move(volume[i][j][k], i - 1, j, k);
                        break;
                    }

                case 2:
                    {
                        move(volume[i][j][k], i, j + 1, k);
                        break;
                    }

                case 3:
                    {
                        move(volume[i][j][k], i, j - 1, k);
                        break;
                    }

                case 4:
                    {
                        move(volume[i][j][k], i, j, k - 1);
                        break;
                    }
            }

        }
    
    
    }

	public void cycledMethod()
    {
		Shuffle();
        for (Molecula a : moleculas)
        {
            if (volume[a.x][a.y][a.z] != null)
            {
                if (GetNeirbourgsCount(a.x, a.y, a.z) == 0)
                {
                    JumpFree(a.x, a.y, a.z);
                }
                else
                {
                    jump(a.x, a.y, a.z);
                }
            }   
        }
    }
	
	public void engageOne(Molecula a)
    {
        if (volume[a.x][a.y][a.z] != null)
        {
            if (GetNeirbourgsCount(a.x, a.y, a.z) == 0)
            {
                JumpFree(a.x, a.y, a.z);
            }
            else
            {
                jump(a.x, a.y, a.z);
            }
        }   
    
    }

	public void PrintOne(int i, int j, int k)
    {
        if (volume[i][j][k] != null)
        {
            if (isCross(i, j, k))
            {
                System.out.print("X  ");

            }
            else
            {
            	System.out.print("  O");
            }
        }
        else
        {
        	System.out.print("   ");
        }
    }

	public void HorsPlaneXY(int n) // n is the number of XY plane to be printed
    {
        for (int j = dimY - 1; j >= 0 ; j--)
        {
            for (int i = 0; i < dimX; i++)
            {
                PrintOne(i, j, n);
            }
            System.out.println();
        }
    }

    public void VertPlaneYZ(int n) // n is the number of YX plane to be printed
    {
        for (int k = dimZ - 1; k >= 0; k--)
        {
            for (int j = 0; j < dimY; j++)
            {
                PrintOne(n, j, k); 
            }
            System.out.println();
        }
    }
	
    public synchronized HexGrid clone() {
    	HexGrid h = new HexGrid(dimX, dimY, dimZ);
    	h.volume = volume.clone();
    	h.moleculas = (ArrayList<Molecula>)moleculas.clone();
    	return h;
    }
    
    //external methods
    public ArrayList<Molecula> moleculasInRange(Molecula m, int rX, int rY, int rZ){
    	ArrayList<Molecula> mol = new ArrayList<>();
    	
    	for(int i = m.x; i <= m.x + rX; i++) {
    		for(int j = m.y; j <= m.y + rY; j++) {
    			for(int k = m.z; k <= m.z + rZ; k++) {
    	    		if(volume[i][j][k] != null) {
    	    			mol.add(volume[i][j][k]);
    	    		}
    	    	}
        	}
    	}
    	
    	return(mol);
    }
    
	public String getOne(int i, int j, int k)
    {
        if (volume[i][j][k] != null)
        {
            if (isCross(i, j, k))
            {
                return("X  ");//"X  ");

            }
            else
            {
            	return("  O");//"  O");
            }
        }
        else
        {
        	return("   ");//"   ");
        } 
    }


    
    
    
    
}
