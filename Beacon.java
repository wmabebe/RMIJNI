import java.io.Serializable;

/**
 * This class represents a beacon that agents
 * send to the remote manager.
 * @author wmabebe
 *
 */
public class Beacon implements Serializable{
	private int id, startup, interval;
	private int[] ip;
	private int received;
	private String cmdAgentID;
	private int port;
	public Beacon(int id,int startup, int interval,String agentId,int port) {
		this.id= id;
		this.startup = startup;
		this.interval = interval;
		this.cmdAgentID = agentId;
		this.port = port;
	}
	public int getId() {
		return this.id;
	}
	
	public int getStartup() {
		return this.startup;
	}
	
	public int getInterval() {
		return this.interval;
	}
	
	public String getCmdAgentID() {
		return this.cmdAgentID;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public void setIP(int[] ip) {
		for (int i=0;i<ip.length;i++) {
			this.ip[i] = ip[i];
		}
	}
	
	public int[] getIp() {
		return this.ip;
	}
	
	public long getReceived() {
		return received;
	}
	
	public void setReceived() {
		this.received = (int) (System.currentTimeMillis() / 1000);
	}
	
	public boolean isStale() {
		return (int)(System.currentTimeMillis() / 1000) - received > (2 * this.interval);
	}
	
	@Override
	public String toString() {
		return this.id + "";
	}
}