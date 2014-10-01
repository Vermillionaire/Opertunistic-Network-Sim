/*
 * Interface to describe the possible behaviors of a node
 */
public interface Behavior {
	void Idle();		//Node is idle
	void Master();		//Node is receiving connections
	void Slave();		//Node is connecting
}
