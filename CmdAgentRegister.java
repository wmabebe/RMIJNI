import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class implements the CmdAgent rmi interface.
 * It receives remote commands, executes them and
 * returns the result.
 * @author wmabebe
 *
 */

public class CmdAgentRegister extends UnicastRemoteObject implements CmdAgent {

	protected CmdAgentRegister() throws RemoteException {
		super();
	}

	@Override
	public Object execute(String cmdId, Object cmdObj) throws RemoteException {
		if (cmdObj.getClass().getName().equals("GetLocalTime")) {
			GetLocalTime glt = (GetLocalTime) cmdObj;
			return new JNIClass().C_GetLocalTime(glt); //Do native call
		}
		return null;
	}

}
