import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CmdAgent extends Remote{
	public Object execute(String cmdId, Object cmdObj) throws RemoteException;
}
