import java.util.Random;


/*
 * Class that contains the graph and the main function of the program.
 * It is responsible for generating the nodes randomly and building the graph.
 */
public class Graph {
	
	private int numNodes = 15;		//Number of nodes on the graph
	public Nodes all[];					//Array with all the nodes
	private Thread nodes[];					//Threads for each node
	private int XMax;
	private int YMax;
	
	public Graph(Scale s) {
		
		if (s == Scale.SMALL) {
			numNodes = 15;
			XMax = 10;
			YMax = 10;
		}
		else {
			numNodes = 150;
			XMax = 50;
			YMax = 20;
		}
		
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

		for (int i=0; i<numNodes; i++) {
			//Generates random number
			p[0] = (new Random()).nextInt(XMax+1);
			p[1] = (new Random()).nextInt(YMax+1);
			
			//Checks if there is a node with that point already
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
	
	//Builds all possible edges between nodes.
	//This is determined by their connection radius
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
		
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	//Compares the distance between two nodes to their radius 
	//If they both have a greater radius than the distance,
	//then they are in range and can connect
	private boolean inRange(Nodes here, Nodes there) {
		double d = distance(here.getPos(), there.getPos());
		
		if (here.getPos().rad >= d && there.getPos().rad >=d) {
			return true;
		}
		return false;
	}
	
	
	//Starts all threads
	public void startThreads() {
		for (int i=0; i< numNodes; i++) {
			nodes[i].start();
		}
	}
	
	//Stops all threads
	public void stopThreads() {
		for (int i=0; i< numNodes; i++) {
			all[i].stop = true;;
		}
	}

	
	public static void main(String[] args) {
		
		Scale size = Scale.LARGE;
		Graph test = new Graph(size);
		GUI display = new GUI(test, size);
	
		Thread screen = new Thread(display);
		screen.start();
		screen.yield();
		
		test.startThreads();
		test.all[(new Random()).nextInt(15)].getRunning().setMessageID(22);
		
	}
}
