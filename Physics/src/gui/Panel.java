package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Panel extends JPanel{
	
	ArrayList<Paintable> painters;

	public Panel() {
		setBackground(Color.WHITE);
		painters = new ArrayList<Paintable>();
	}
	
	public void addPainter(Paintable p) {
		painters.add(p);
	}
	
	public void paint(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 3000, 3000);
		
		g.setColor(Color.BLUE);

		for(int i = 0; i < painters.size(); i++) {
			painters.get(i).paint(g);
		}
	}
	
}
