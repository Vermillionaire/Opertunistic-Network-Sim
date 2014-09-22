import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class GUI implements ActionListener{
	private JFrame window;
;
	private int dim = 500;
	private int padding = dim / 10;
	private Draw d;
	
	public GUI(Graph g) {
	
		
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		d = new Draw(g);
		
		Container c = window.getContentPane();
		c.setLayout(new BoxLayout(c,BoxLayout.PAGE_AXIS));
		c.add(d);
		
		Container buttons = new Container();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		
		JButton radius = new JButton("Radius");
		radius.addActionListener(this);
		JButton lines = new JButton("Lines");
		lines.addActionListener(this);
		
		buttons.add(radius);
		buttons.add(lines);
		
		c.add(buttons);
		
		//window.setUndecorated(true);
		window.setLocation(100,100);
		
		window.pack();
		window.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand() == "Lines")
			d.drawLines = !d.drawLines;
		else if (arg0.getActionCommand() == "Radius")
			d.drawRadius = !d.drawRadius;
		
	
		window.repaint();
		
	}


}

class Draw extends JPanel {
	private int w = 550;
	private int h = 550;
	private int m = 25;
	private Graph grr;
	public boolean drawRadius = true;
	public boolean drawLines = true;
	
	public Draw(Graph g){
		grr = g;
		this.setPreferredSize(new Dimension(w,w));
	}
	
	public void paintComponent(Graphics g) {
		
		
		if (drawLines) 
			for (int i=0; i<10; i++) {
				g.drawLine(i*50 + m,0+m,i*50 + m,h-m);
				g.drawLine(0+m,i*50+m,w-m,i*50+m);
			}
		
		
		g.drawRect(m, m, w-m*2, h-m*2);
		
		
		
		int radius = 16;
		for (int i=0; i<grr.all.length; i++) {
			g.setColor(Color.red);
			g.fillOval( (grr.all[i].getPos().x*50 + m) - radius/2, (grr.all[i].getPos().y*50 + m) - radius/2, radius, radius);
			
			if (drawRadius) {
				g.setColor(Color.green);
			
				int tr = grr.all[i].getPos().rad*50;
				g.drawOval((grr.all[i].getPos().x*50 + m) - tr/2, (grr.all[i].getPos().y*50 + m) - tr/2, tr, tr);
		
			}
		}
	}
}
