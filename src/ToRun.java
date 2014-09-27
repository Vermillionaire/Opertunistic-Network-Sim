import java.util.Random;

/*
 * This is the area where behavior of each node in a particular state
 * is controlled. This is where you implement the "routing algorithm" for 
 * the opportunistic network
 */

public class ToRun extends Connections implements Behavior{

	private Nodes n;
	int messageID = 0;
	int pastID = 0;
	long masterLife = 0;
	int forceIdle = 0;
	
	public ToRun(Nodes n) {
		this.n = n;
	}
	
	public void Idle() {
		
		
		if (forceIdle == 0) {
			if (messageID == pastID)
				messageID = 0;
			if (pastID == 0)
				pastID = messageID;
			if (messageID != 0) {
				n.setMaster();
				return;
			}
		}
		else
			forceIdle--;
		
		Edge e = search(n);
		
		if (e != null)
			try {
				connect(n,e);
				forceIdle = 0;
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
	}
	public void Master() {
		long current = System.currentTimeMillis();
		if (masterLife == 0.0)
			masterLife = current;
		
		if ( (current - masterLife) >= 3000) {
			if (n.getNumConnections() == 0) {
				//forceIdle = 10;
				messageID = 0;
				n.setIso();
				return;
			}
			
			Edge e = n.getEdge();
			while (e != null) {
				Nodes other = e.n;
				if (other.getIsConnectedTo() != null && other.getIsConnectedTo().n.equals(n)) {
					System.out.print("Sending message");
					e.n.getRunning().setMessageID(messageID);
				}
				e = e.next;
			}
			
			messageID = 0;
			disconnect(n);
		}
	
	}
	public void Slave() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setMessageID(int i) {
		messageID = i;
		
	}
	
	public int getMessageID() {
		return messageID;
	}
}
