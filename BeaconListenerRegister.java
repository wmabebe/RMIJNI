import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class implements the rmi interface for sending
 * out beacons.
 * @author wmabebe
 *
 */
public class BeaconListenerRegister extends UnicastRemoteObject implements BeaconListener  {
	private Beacon beacon;
	
	public BeaconListenerRegister() throws RemoteException {
		super();
	}
	
	@Override
	public void deposit(Beacon b) throws RemoteException {
		this.beacon = b;
		this.beacon.setReceived();
		try {
			Manager.addBeacon(b);
		} catch (MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}
	}
	
}

