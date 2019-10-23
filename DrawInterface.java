import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DrawInterface extends Remote {
	
	public Identity user() throws RemoteException;
	public void broadcast(String shape, String timeline, String color, Object o) throws RemoteException;
	public void drawtask(String shape, String timeline, String color, Object o) throws RemoteException;
	public boolean login(DrawInterface client, Identity id) throws RemoteException;

}
