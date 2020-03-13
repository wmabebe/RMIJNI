import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BeaconListener  extends Remote{
	public void deposit(Beacon b) throws RemoteException;
}
