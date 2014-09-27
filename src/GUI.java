import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class GUI implements ActionListener, Runnable{
	private JFrame window;
	private Draw d;
	private long time = 0;
	
	public GUI(Graph g) {
	
		
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		Container c = window.getContentPane();
		c.setLayout(new BoxLayout(c,BoxLayout.PAGE_AXIS));
		
		//Creates new object to draw on
		d = new Draw(g);
		c.add(d);
		
		//Container for buttons
		Container buttons = new Container();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		
		//Buttons with action listeners
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
		JButton stop = new JButton("Stop");
		stop.addActionListener(this);		
		
		buttons.add(radius);
		buttons.add(lines);
		buttons.add(edges);
		buttons.add(newn);
		buttons.add(connect);
		buttons.add(names);
		buttons.add(stop);
		
		c.add(buttons);
		
		
		//Puts window on screen
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
		else if (arg0.getActionCommand() == "Stop")
			d.stop();
		
		d.stateChange = !d.stateChange;
	}

	
	
	//Thread that handles the screen refresh rate
	@Override
	public void run() {
		
		time = System.currentTimeMillis();
		
		while (true) {
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//if (d.stateChange) {
				//System.out.println("Painting Window");
				window.repaint();
			//}
			
			long currentTime = System.currentTimeMillis();
			d.fps = (int) (1000/(currentTime - time));
			time = currentTime;
			
			
		}
		
		
	}


}


//New Class for drawing objects
class Draw extends JPanel {
	
	//dimensions of the window
	private int w = 550;
	private int h = 550;
	
	//?
	private int m = 25;
	private Graph graph;
	
	//Values to control what is being displayed
	public boolean drawRadius = false;
	public boolean drawLines = true;
	public boolean drawEdges = false;
	public boolean drawConnections = true;
	public boolean drawNames = true;
	
	//Saves system resources by letting the thread refreshing the 
	//screen know that the display hasn't changed.
	public boolean stateChange = false;
	
	//Current Frames Per Second of the display
	public int fps = 0;
	
	//Draw constructor
	public Draw(Graph g){
		graph = g;
		this.setPreferredSize(new Dimension(w,w));
	}
	
	public void newGraph() {
		graph.stopThreads();
		graph = new Graph();
		graph.startThreads();
		graph.all[10].getRunning().setMessageID(22);
	}
	
	public void stop() {
		graph.stopThreads();
	}
	
	
	//Function for instruction on how to paint the screen
	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D)g;
	    
	    g2.drawString("FPS: "+fps, 1, 10);
	    stateChange = !stateChange;
		
		g2.drawRect(m, m, w-m*2, h-m*2);
		if (drawLines) 
			for (int i=0; i<10; i++) {
				g2.drawLine(i*50 + m,0+m,i*50 + m,h-m);
				g2.drawLine(0+m,i*50+m,w-m,i*50+m);
			}
		
		
		if (drawEdges) {
			g2.setColor(Color.blue);
			for (int i=0; i<graph.all.length; i++) {
				Edge e = graph.all[i].getEdge();
				
				if (e == null)
					break;
				
				do {
					//System.out.println("Edge");
					g2.drawLine((graph.all[i].getPos().x*50 + m), (graph.all[i].getPos().y*50 + m), (e.n.getPos().x*50 + m), (e.n.getPos().y*50 + m));
					e = e.next;
				}while (e != null);
				//System.out.println("NextNode");
			}
			g2.setColor(Color.black);
		}
		
		if (drawConnections) {
			g2.setColor(Color.green);
			for (int i=0; i<graph.all.length; i++) {
				Edge e = graph.all[i].getEdge();
				
				if (e == null)
					break;
				
				do {
					if (!e.open) {
						int x1 = (graph.all[i].getPos().x*50 + m);
						int x2 = (e.n.getPos().x*50 + m);
						int y1 = (graph.all[i].getPos().y*50 + m);
						int y2 = (e.n.getPos().y*50 + m);
						int size = 3;
						
						if (Math.abs(y1-y2) >= Math.abs(x1-x2)) {
							int px[] = {x2,x1-size,x1+size};
							int py[] = {y2,y1,y1};
							g2.fillPolygon(px,py, 3);
							
						}
						else {
							int px[] = {x2,x1,x1};
							int py[] = {y2,y1-size,y1+size};
							g2.fillPolygon(px,py, 3);
						}
						
					
					}
						
					e = e.next;
				}while (e != null);

			}
			g.setColor(Color.black);
		}
		
		
		
		
		
		//////////////////////////////Draws Nodes on the graph///////////////////////////////////
		int radius = 16;
		for (int i=0; i<graph.all.length; i++) {
			
			
			if (graph.all[i].getRel() == Relationship.Isolated)
				g2.setColor(Color.red);
			else if (graph.all[i].getRel() == Relationship.Master )
					g2.setColor(Color.blue);
			else 
				g2.setColor(Color.cyan);
			
			g2.fillOval( (graph.all[i].getPos().x*50 + m) - radius/2, (graph.all[i].getPos().y*50 + m) - radius/2, radius, radius);
			

			if (drawNames) {
				g2.setColor(Color.GRAY);
				g2.drawString(graph.all[i].toString(), graph.all[i].getPos().x*50 + m - 10, graph.all[i].getPos().y*50 + m - 10);
			}
		}
		///////////////////////////////////////////////////////////////////////////////////////////
		
		
		if (drawRadius) {
			for (int i=0; i<graph.all.length; i++) {
				g2.setColor(Color.green);
		
				int tr = graph.all[i].getPos().rad*50*2;
				g2.drawOval((graph.all[i].getPos().x*50 + m) - tr/2, (graph.all[i].getPos().y*50 + m) - tr/2, tr, tr);
			}
		}
	}
}
