/*
 * To-Do:
 * Message Queue
 * Connection protocols
 * Graph Builder
 * Gui
 * Make ToRun more abstract
 */

//Position sets the physical location of the node relative to other nodes.
//The position is only important to the graph when it is being built because the 
//edges represent physically possible connections
class Position {
	int x;
	int y;
}


//The edge weight represents the current status of the connection 
class Edge {
	boolean open;
	Nodes n;
	Edge next;
}

enum Relationship {
	Slave,
	Master,
	Isolated;
}


public class Nodes implements Runnable{
	
	final int maxConnections = 8;
	private int numConnections = 0;
	public boolean clock = false;	//Locks node while establishing connection
	private Edge edges;			//All edges of a node
	private Position pos;		//Real world position
	private String name;
	private ToRun code;			//Roughting algorithm to run
	private Relationship rel;	//Current status of the 
	private int radius;			//Radius is the distance from a node that it can connect to
	public Message buffer[] = new Message[10];
	
	@Override
	public void run() {
		
		do {
			try {	
				Thread.sleep(100); 
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while(!Graph.start || Graph.pause);
		
		
		if (code == null) {
			System.out.println("Error: Node " + name + " has no code to run");
			return;
		}
		
		
		switch(rel) {
			case Slave: 
				code.Slave(this);
				break;
			case Master:
				code.Master(this);
				break;
			case Isolated:
				Thread.yield();
				code.Idle(this);
				break;
		}

		
	}
	
	public Relationship getRel() {
		return rel;
	}
	
	public Edge getEdge() {
		return edges;
	}
	
	
	public boolean canConnect() {
		if (numConnections >= maxConnections)
			return false;
		if (rel != Relationship.Master)
			return false;
		
		return true;
	}
	public void plusConnection() {
		numConnections++;
	}
	
	public void minusConnection() {
		numConnections--;
	}
	
	public void setSlave() {
		rel = Relationship.Slave;
	}
	
	public void setMaster() {
		rel = Relationship.Master;
	}
	
	public void setIso() {
		rel = Relationship.Isolated;
	}
	
}
