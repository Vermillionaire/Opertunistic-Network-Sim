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
		
		all = randomNodes(numNodes);
		buildGraph();
	}
	
	public void resetNodes() {
		all = randomNodes(numNodes);
		buildGraph();
	}
	
	private Nodes[] randomNodes(int num) {
		Nodes[] n = new Nodes[num];
		
		for (int i=0; i<num; i++) {
			n[i] = new Nodes((new Random()).nextInt(10), (new Random()).nextInt(10), "Node:"+i);
			System.out.println("x = " + n[i].getPos().x +"  y = "+ n[i].getPos().y);
		}
		
		return n;
	}
	
	private void buildGraph() {
		
		for (int i=0; i<all.length; i++) {
			for (int j=i; j<all.length; j++) {
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
		if (here.getPos().rad >= d || there.getPos().rad >=d) {
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
