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
		JButton edges = new JButton("Edges");
		edges.addActionListener(this);
		JButton newn = new JButton("New");
		newn.addActionListener(this);
		JButton connect = new JButton("Connections");
		connect.addActionListener(this);
		JButton names = new JButton("Names");
		names.addActionListener(this);
		
		buttons.add(radius);
		buttons.add(lines);
		buttons.add(edges);
		buttons.add(newn);
		buttons.add(connect);
		buttons.add(names);
		
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
		else if (arg0.getActionCommand() == "Edges")
			d.drawEdges = !d.drawEdges;
		else if (arg0.getActionCommand() == "New")
			d.newGraph();
		else if (arg0.getActionCommand() == "Connections")
			d.drawConnections = !d.drawConnections;
		else if (arg0.getActionCommand() == "Names")
			d.drawNames = !d.drawNames;
	
		window.repaint();
		
	}


}

class Draw extends JPanel {
	private int w = 550;
	private int h = 550;
	private int m = 25;
	private Graph grr;
	public boolean drawRadius = false;
	public boolean drawLines = true;
	public boolean drawEdges = false;
	public boolean drawConnections = true;
	public boolean drawNames = false;
	
	public Draw(Graph g){
		grr = g;
		this.setPreferredSize(new Dimension(w,w));
	}
	
	public void newGraph() {
		grr = new Graph();
	}
	
	public void paintComponent(Graphics g) {
		
		g.drawRect(m, m, w-m*2, h-m*2);
		if (drawLines) 
			for (int i=0; i<10; i++) {
				g.drawLine(i*50 + m,0+m,i*50 + m,h-m);
				g.drawLine(0+m,i*50+m,w-m,i*50+m);
			}
		
		
		if (drawEdges) {
			g.setColor(Color.blue);
			for (int i=0; i<grr.all.length; i++) {
				Edge e = grr.all[i].getEdge();
				
				if (e == null)
					break;
				
				do {
					//System.out.println("Edge");
					g.drawLine((grr.all[i].getPos().x*50 + m), (grr.all[i].getPos().y*50 + m), (e.n.getPos().x*50 + m), (e.n.getPos().y*50 + m));
					e = e.next;
				}while (e != null);
				//System.out.println("NextNode");
			}
			g.setColor(Color.black);
		}
		
		if (drawConnections) {
			g.setColor(Color.GREEN);
			for (int i=0; i<grr.all.length; i++) {
				Edge e = grr.all[i].getEdge();
				
				if (e == null)
					break;
				
				do {
					if (!e.open)
						g.drawLine((grr.all[i].getPos().x*50 + m), (grr.all[i].getPos().y*50 + m), (e.n.getPos().x*50 + m), (e.n.getPos().y*50 + m));
					e = e.next;
				}while (e != null);

			}
			g.setColor(Color.black);
		}
		
		
		
		
		
		//////////////////////////////Draws Nodes on the graph///////////////////////////////////
		int radius = 16;
		for (int i=0; i<grr.all.length; i++) {
			
			
			if (grr.all[i].getRel() == Relationship.Isolated)
				g.setColor(Color.red);
			else if (grr.all[i].getRel() == Relationship.Master )
					g.setColor(Color.blue);
			else 
				g.setColor(Color.cyan);
			
			g.fillOval( (grr.all[i].getPos().x*50 + m) - radius/2, (grr.all[i].getPos().y*50 + m) - radius/2, radius, radius);
			

			if (drawNames) {
				g.setColor(Color.GRAY);
				g.drawString(grr.all[i].toString(), grr.all[i].getPos().x*50 + m - 10, grr.all[i].getPos().y*50 + m - 10);
			}
		}
		///////////////////////////////////////////////////////////////////////////////////////////
		
		
		if (drawRadius) {
			for (int i=0; i<grr.all.length; i++) {
				g.setColor(Color.green);
		
				int tr = grr.all[i].getPos().rad*50*2;
				g.drawOval((grr.all[i].getPos().x*50 + m) - tr/2, (grr.all[i].getPos().y*50 + m) - tr/2, tr, tr);
			}
		}
	}
}
