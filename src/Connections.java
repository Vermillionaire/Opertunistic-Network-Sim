/*
 * This class establishes the communication protocols for the different
 * states, emulating delays that a real WiFi network might experience
 * when disconnecting, connecting, and sending messages
 */
public class Connections {

	//Input current node, output found edge
	public Edge search(Nodes n) {
		Edge e = n.getEdge();
		while (e != null) {
			if (e.open && e.n.getRel()== Relationship.Master) {
				return e;
			}
			e = e.next;
		}
		
		return null;
	}
	
	//Input current node
	public synchronized void connect(Nodes n, Edge e) throws InterruptedException {
		Nodes master = e.n;
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
