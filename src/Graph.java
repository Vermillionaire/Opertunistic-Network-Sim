import java.util.Random;



public class Graph {
	
	private int numNodes = 15;
	Nodes all[];
	Thread nodes[];
	
	public Graph() {
		randomNodes();
		buildGraph();
		makeThreads();
		
	}
	
	//Assigns the nodes to a thread so they can run independently
	private void makeThreads() {
		nodes = new Thread[numNodes];
		
		for (int i=0; i<numNodes; i++) {
			nodes[i] = new Thread(all[i]);
		}
	}
	
	
	//Randomly generates nodes on the graph
	private void randomNodes() {
		all = new Nodes[numNodes];
		int p[] = new int[2];
		boolean same = false;
		for (int i=0; i<numNodes; i++) {
			p[0] = (new Random()).nextInt(10);
			p[1] = (new Random()).nextInt(10);
			
			if (samePoint(p[0],p[1])) {
				i--;
				continue;
			}
			all[i] = new Nodes(p[0],p[1],"N:"+i);
		}
	
	}
	
	
	//Checks if another node already exists on that point
	private boolean samePoint(int x, int y) {
		for (int i=0; i<numNodes; i++) {
			if (all[i] == null)
				return false;
			if (all[i].getPos().x == x && all[i].getPos().y == y)
				return true;
		}
		
		return false;
	}
	
	//Builds edges between all nodes that can connect
	private void buildGraph() {
		
		for (int i=0; i<all.length; i++) {
			for (int j=0; j<all.length; j++) {
				if (inRange(all[i],all[j])) 
					all[i].addEdge(all[j]);
				
			}
		}
	}
	
	
	//Calculates the distance between two points
	private double distance(Position a, Position b) {
		double x = Math.abs(a.x - b.x);
		double y = Math.abs(a.y - b.y);
		
		System.out.print("subx =" + x + " sub7 =" + y + " ");
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	//Compares the distance between two nodes to their radius 
	//If they both have a greater radius than the distance,
	//then they are in range and can connect
	private boolean inRange(Nodes here, Nodes there) {
		double d = distance(here.getPos(), there.getPos());
		
		System.out.print("distance = " + d + " rad = " + here.getPos().rad + " [ " + here.toString() + "(" + here.getPos().x +","+here.getPos().y+")  "+ there.toString() +"("+there.getPos().x+","+here.getPos().y+") ]");
		if (here.getPos().rad >= d && there.getPos().rad >=d) {
			System.out.println(" -> true");
			return true;
		}
			
		System.out.println(" -> false");
		return false;
	}
	
	public void startThreads() {
		for (int i=0; i< numNodes; i++) {
			nodes[i].start();
		}
	}
	
	public void stopThreads() {
		for (int i=0; i< numNodes; i++) {
			all[i].stop = true;;
		}
	}

	
	public static void main(String[] args) {
		Graph test = new Graph();
		GUI display = new GUI(test);
	
		Thread screen = new Thread(display);
		screen.start();
		screen.yield();
		
		test.startThreads();
		test.all[10].getRunning().setMessageID(22);
		
	}
}
