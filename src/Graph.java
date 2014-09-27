import java.util.Random;

/*
 * Builds the graph of nodes based on their distances to each other
 * 
 * To-Do: creates edges for graphs and builds lines on gui of edges
 */
public class Graph {
	public static boolean start = false;
	public static boolean pause = false;
	
	private int numNodes = 15;
	Nodes all[];
	
	
	public Graph() {
		randomNodes();
		buildGraph();
	}
	
	
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
		
		
		
		/////////////////Testing Code//////////////////////////////
		Connections c = new Connections();
		
		all[(new Random()).nextInt(numNodes)].setMaster();
		all[(new Random()).nextInt(numNodes)].setMaster();
		all[(new Random()).nextInt(numNodes)].setMaster();
		
		for (int i=0; i<all.length; i++) {
			if (all[i].getRel() == Relationship.Master)
				continue;
			
			Edge e = c.search(all[i]);
			
			if (e != null)
				try {
					c.connect(all[i], e);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
		/////////////////Delete Later///////////////////////////////////
	}
	
	private double distance(Position a, Position b) {
		double x = Math.abs(a.x - b.x);
		double y = Math.abs(a.y - b.y);
		
		System.out.print("subx =" + x + " sub7 =" + y + " ");
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
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
	
	public static void main(String[] args) {
		Graph test = new Graph();
		GUI display = new GUI(test);
	
		
		
	}
}
