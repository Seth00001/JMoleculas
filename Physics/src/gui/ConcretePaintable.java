package gui;

import java.awt.Graphics2D;

public class ConcretePaintable implements Paintable{
	public int x, y, r;
	
	
	public ConcretePaintable() {
		x = 0;
		y = 0;
		r = 1;
	}
	
	public void paint(Graphics2D g) {
		g.fillOval(x, y, r, r);
	}
	
	
	
}
