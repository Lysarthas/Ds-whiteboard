import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DrawInterface extends Remote {
	
	void broadcast(DrawInterface di, String shape, String timeline, String color, Object o) throws RemoteException;
	void drawtask(String shape, String timeline, String color, Object o) throws RemoteException;
	void login(DrawInterface di, String myname) throws RemoteException;

}
