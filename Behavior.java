/*
 * Interface for the only possible behaviors for a node
 */
public interface Behavior {
	void Idle(Nodes n);
	void Master(Nodes n);
	void Slave(Nodes n);
}
