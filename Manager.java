import java.util.HashMap;
import java.util.Map.Entry;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
/**
 * This class is both an RMI server and client. It registers
 * rmi services, and fires up threads that call remote services.
 * The Manager is a class that monitors remote Agents.
 * @author wmabebe
 *
 */

public class Manager {
	/**
	 * This variable maps beaconIDs with their respective beacons.
	 */
	public static HashMap<Integer,Beacon> map = new HashMap<Integer,Beacon>();
	/**
	 * This is an adjustable sleep interval used by the monitor thread.
	 * The value is updated whenever a smaller beacon interval is detected.
	  * Default = 2 seconds.
	 */
	public static int MIN_INTERVAL = 2;
	
	/**
	 * This method receives a beacon and updates the map.
	 * There are three cases that are handled. The first is
	 * when a new beacon/agent is detected. The second is
	 * when when an existing beacon/agent is detected.
	 * The third when an agent that was previously thought
	 * dead is detected. The first and third case, the remote
	 * agent is commanded to send its time. In the second case
	 * nothing different happens. In all cases, the map variable
	 * is updated to reflect current situation.
	 * @param b  Newly detected beacon
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public static void addBeacon(Beacon b) throws MalformedURLException, RemoteException, NotBoundException {
		if (!map.containsKey(b.getId())) {
			MIN_INTERVAL = b.getInterval() < MIN_INTERVAL ? b.getInterval() : MIN_INTERVAL;
			System.out.println("Agent-" + b.getId() + " detected!");
			new Command(b).start();//Fire up thread that commands the new remote agent
		}
		else if (map.get(b.getId()) == null) {
			System.out.println("Agent-" + b.getId() + "  resurrected!");
			new Command(b).start();//Fire up thread that commands the resurrected remote agent
		}
		map.put(b.getId(), b);
		System.out.println("Agent-" + b.getId() + "   ->   Arrived-" +  b.getReceived());
	}
	
	/**
	 * Main method fires up an agent monitor thread and binds a 
	 * BeaconListener Object to the rmi registry.
	 * @param args
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws RemoteException {
		new Monitor().start();
		Registry registry = LocateRegistry.createRegistry(5050);
		registry.rebind("BeaconListener", new BeaconListenerRegister());
		System.out.println("Manager...");
	}
}

/**
 * This class monitors the liveliness of remote agents.
 * If a stale agent is detected, its last beacon is removed
 * from the Managers map.
 * @author wmabebe
 *
 */
class Monitor extends Thread{
	
	/**
	 * This method indefinitely runs checking whether or not
	 * the remote agents have died. If a stale agent is
	 * discovered, its map value will be set to null.
	 */
	@Override
	public void run() {
		while(true) {
			for(Entry<Integer, Beacon> entry: Manager.map.entrySet()) {
				if (entry.getValue() != null && entry.getValue().isStale()) {
					System.out.println("Agent-" + entry.getValue() + "  died!");
					Manager.map.put(entry.getKey(),null);
				}
			}
			try {
				//Sleep for 2x the minimum beacon interval detected before inspecting
				//the agents again.
				Thread.sleep(Manager.MIN_INTERVAL * 1000 * 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}

/**
 * This class acts as a rmi client calling methods
 * on remote agents. The method called is a command that
 * asks for local time on the remote agent. 
 * @author wmabebe
 *
 */
class Command extends Thread{
	private Beacon beacon;
	public Command(Beacon b) {
		this.beacon = b;
	}
	@Override
	public void run() {
		CmdAgent service;
		try {
			//The default (hard coded) agent ip = localhost
			//Obviously this can be parameterized by including ip value inside beacon.
			//We assume the agent only has 1 command available.
			//We can, however, have multiple commandIDs associated with one agent (String[]).
			//In which case we can iterate over the commandIDs and execute.
			service = (CmdAgent) Naming.lookup("rmi://localhost:"+ beacon.getPort() +"/" + beacon.getCmdAgentID());
			Object obj = service.execute(beacon.getCmdAgentID(), new GetLocalTime());
			Integer result = (obj != null && ((GetLocalTime) obj).getValid() == 1)  ? ((GetLocalTime) obj).getTime() : null;
			System.out.println("Agent-"+ beacon.getId() +"   ->   TIME: " + result);
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}