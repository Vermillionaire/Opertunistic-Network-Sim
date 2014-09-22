import java.util.Random;

/*
 * Builds the graph of nodes based on their distances to each other
 */
public class Graph {
	public static boolean start = false;
	public static boolean pause = false;
	
	private int numNodes = 15;
	Nodes all[];
	public Graph() {
		all = new Nodes[numNodes];
		
		for (int i=0; i<numNodes; i++) {
			all[i] = new Nodes((new Random()).nextInt(10), (new Random()).nextInt(10));
			System.out.println("x = " + all[i].getPos().x +"  y = "+ all[i].getPos().y);
		}
	}
	
	public static void main(String[] args) {
		Graph test = new Graph();
		GUI display = new GUI(test);
	}
}
