/*
 * This class provides functions for each node that 
 * are critical for their operation.
 * This includes:
 * -A search function that searches all edges for a node to connect to
 * -A function that establishes a connection between nodes (the connection between nodes is represented 
 *  by a boolean value on the edge)
 * -A function to disconnect two nodes
 */
public class Connections {

	//Input current node, output found edge
	public Edge search(Nodes n) {
		
		Edge e = n.getEdge();
		
		//Iterate through linked list of nodes
		while (e != null) {
			
			//If the connection is open and it is a master, return edge
			if (e.open && e.n.getRel()== Relationship.Master) {
				return e;
			}
			e = e.next;
		}
		
		//Returns null if search is unsucessfull
		return null;
	}
	
	//Input current node and the edge you are connecting through
	//Changes state of current node if successful
	public synchronized void connect(Nodes n, Edge e) throws InterruptedException {
		Nodes master = e.n;
		
		if(master.canConnect()) {
			
			/*
			 * c-lock not clock
			 * c-lock is used as a semaphore.
			 * It probably isn't necessary, but I added it just 
			 * in case there was a problem with multiple nodes
			 * trying to connect at once.
			*/
			
			//Checks if connecting to the master is locked
			while(master.clock)
				wait();
			
			//Locks master while connecting, notify everyone when done
			master.clock = true;
			master.plusConnection();
			master.clock = false;
			notifyAll();
			
			
			//Sets connection as connected or closed
			e.open = false;
			n.setSlave();
			n.setIsConnectedTo(e);
			Thread.sleep(500);
		}
	}
	
	public void disconnect(Nodes n){
		
		//Sets current node to Isolated
		n.setIso();
		
		//Gets the list of edges for current node
		Edge e = n.getEdge();
		
		//Goes through list of edges
		while (e != null) {
			
			//Gets the currently connected edge of the other node
			Edge other = e.n.getIsConnectedTo();
			
			//If the node on that edge is the same as this edge
			//disconnect
			if (other != null && other.n.equals(n)) {
				other.open = true;
				n.minusConnection();
				e.n.setIso();
				e.n.setIsConnectedTo(null);
			}
			
			e = e.next;
		}
	}
	
}
