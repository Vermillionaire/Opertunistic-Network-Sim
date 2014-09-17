/*
 * This class establishes the communication protocols for the different
 * states, emulating delays that a real WiFi network might experience
 * when disconnecting, connecting, and sending messages
 */
public class Connections {

	//Input current node, output found node
	private Edge search(Nodes n) {
		Edge e = n.getEdge();
		while (e != null) {
			if (e.open && e.n.getRel()== Relationship.Master) {
				return e;
			}
			e = e.next;
		}
		
		return null;
	}
	
	//Input node to connect to
	private synchronized void connect(Nodes n, Edge e) throws InterruptedException {
		Nodes c = e.n;
		if(c.canConnect()) {
			
			while(c.clock)
				wait();
			
			c.clock = true;
			c.plusConnection();
			c.clock = false;
			notifyAll();
			
			
			e.open = false;
			n.setSlave();
			Thread.sleep(1000);
		}
	}
	
	private void disconnect(Nodes n){
		
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
