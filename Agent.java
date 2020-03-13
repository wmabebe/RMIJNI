import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

/**
 * This agent class is responsible for sending out
 * beacons to a remote manager class, and performing
 * commands sent out by the manager class. 
 * @author wmabebe
 *
 */
public class Agent {
	
	public static void main(String[] args) throws RemoteException {
		Random rand = new Random();
		int port = 5000 + rand.nextInt(1000);//Random port for each agent
		
		//Register the remote cmdAgent remote object
		String agentId = "CmdAgent";
		Registry registry = LocateRegistry.createRegistry(port);
		registry.rebind(agentId, new CmdAgentRegister());
		
		//Setup beacon properties and fire a beacon sender
		int lifeTime = 10; //Number of beacons sent
		int interval = 1; //Interval between beacon sends in seconds
		int beaconId = rand.nextInt(1000);//Random beacon Id to avoid collisions
		
		int startup = (int) System.currentTimeMillis() / 1000;//Beacon startup time on in seconds (now)
		BeaconSender bs = new BeaconSender(beaconId,startup,interval,lifeTime,agentId,port);
		bs.start();//Fire up the beacon sender thread
		System.out.println("Agent-" + beaconId + "...");
	}
}

/**
 * This class is responsible for repeatedly sending out
 * beacons to the remote Manager class. The beacons are
 * sent via an rmi call. Beacons have a designated lifetime
 * and interval.
 * @author wmabebe
 *
 */
class BeaconSender extends Thread
{
	private int beaconId,startup,lifeTime,interval,port;
	private String cmdAgentID;
	BeaconSender(int id, int startup,int interval,int lifeTime,String agentId,int port){
		this.beaconId = id;
		this.startup = startup;
		this.interval = interval;
		this.lifeTime = lifeTime;
		this.cmdAgentID = agentId;
		this.port = port;
	}
	/**
	 * This method periodically deposits a beacon to the
	 * remote Manager.
	 */
	@Override
	public void run() {
		BeaconListener service;
		try {
			for (int i=0;i<this.lifeTime;i++){
				service = (BeaconListener) Naming.lookup("rmi://localhost:5050/BeaconListener");
				Beacon b = new Beacon(this.beaconId,this.startup,this.interval,this.cmdAgentID,port);
				service.deposit(b); //call the rmi deposit method
				//Sleep for interval before sending next beacon
				//The third beacon is delayed to demonstrate agent
				//death and resurrection
				Thread.sleep(i == 2 ? this.interval * 1000 * 4 : this.interval * 1000);
			}
		} catch (MalformedURLException | RemoteException | NotBoundException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}