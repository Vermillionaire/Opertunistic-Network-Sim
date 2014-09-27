/*
 * This class establishes the communication protocols for the different
 * states, emulating delays that a real WiFi network might experience
 * when disconnecting, connecting, and sending messages
 */
public class Connections {

	//Input current node, output found edge
	public Edge search(Nodes n) {
		
		System.out.println("Searching from "+n.toString());
		Edge e = n.getEdge();
		while (e != null) {
			System.out.println("\tChecking "+e.n.toString());
			System.out.println("\tRel = "+e.n.getRel());
			System.out.println("\tOpen = "+e.open);
			if (e.open && e.n.getRel()== Relationship.Master) {
				System.out.println("\tFound\n");
				return e;
			}
			System.out.println("\tNot found\n");
			e = e.next;
		}
		
		return null;
	}
	
	//Input current node
	public synchronized void connect(Nodes n, Edge e) throws InterruptedException {
		Nodes master = e.n;
		System.out.println("\nConnecting to "+master.toString()+" from "+n.toString());
		if(master.canConnect()) {
			
			while(master.clock)
				wait();
			
			master.clock = true;
			master.plusConnection();
			master.clock = false;
			notifyAll();
			
			
			e.open = false;
			n.setSlave();
			///Thread.sleep(1000);
		}
	}
	
	public void disconnect(Nodes n){
		
		n.setIso();
		
		Edge e = n.getEdge();
		while (e != null) {
			if (e.open) {
				e.open = false;
				n.minusConnection();
				e.n.setIso();
			}
			
			e = e.next;
		}
	}
	
}
