/*
 * This class establishes the communication protocols for the different
 * states, emulating delays that a real WiFi network might experience
 * when disconnecting, connecting, and sending messages
 */
public class Connections {

	//Input current node, output found edge
	public Edge search(Nodes n) {
		
		//System.out.println("Searching from "+n.toString());
		Edge e = n.getEdge();
		while (e != null) {
			//System.out.println("\tChecking "+e.n.toString());
			//System.out.println("\tRel = "+e.n.getRel());
			//System.out.println("\tOpen = "+e.open);
			if (e.open && e.n.getRel()== Relationship.Master) {
				//System.out.println("\tFound\n");
				return e;
			}
			//System.out.println("\tNot found\n");
			e = e.next;
		}
		
		return null;
	}
	
	//Input current node and the edge you are connecting through
	//Changes state of current node if successful
	public synchronized void connect(Nodes n, Edge e) throws InterruptedException {
		Nodes master = e.n;
		//System.out.println("\nConnecting to "+master.toString()+" from "+n.toString());
		
		//If the master allows, connect
		if(master.canConnect()) {
			
			//c-lock not clock
			//Checks if connecting to the master is locked
			while(master.clock)
				wait();
			
			//Locks master while connecting
			master.clock = true;
			master.plusConnection();
			master.clock = false;
			notifyAll();
			
			
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
