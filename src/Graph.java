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
			for (int j=0; j<all.length; j++) {
				if (inRange(all[i],all[j])) 
					all[i].addEdge(all[j]);
				
			}
		}
	}
	
	private int distance(Position a, Position b) {
		double x = Math.abs(a.x - b.x);
		double y = Math.abs(a.y - b.y);
		
		System.out.print("subx =" + x + " sub7 =" + y + " ");
		return (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	private boolean inRange(Nodes here, Nodes there) {
		int d = distance(here.getPos(), there.getPos());
		
		System.out.print("distance = " + d + " rad = " + here.getPos().rad + " [ " + here.toString() + "(" + here.getPos().x +","+here.getPos().y+")  "+ there.toString() +"("+there.getPos().x+","+here.getPos().y+") ]");
		if (here.getPos().rad >= d) {
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
