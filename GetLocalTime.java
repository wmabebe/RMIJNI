import java.io.Serializable;

/**
 * This class represents the command sent and
 * received by the manager class. The manager
 * will send an instance of this object when
 * it requests the local time of the agent.
 * The agent updates the object and returns it
 * to the manager.
 * @author wmabebe
 *
 */
public class GetLocalTime implements Serializable{
	private int time;
	private char valid;
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public void setValid(char valid) {
		this.valid = valid;
	}
	
	public char getValid() {
		return this.valid;
	}
}
