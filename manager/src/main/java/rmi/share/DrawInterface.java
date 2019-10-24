package rmi.share;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DrawInterface extends Remote {
	
	public Identity user() throws RemoteException;
	public void broadcast(Identity editor, String shape, String timeline, Object color, Object o) throws RemoteException;
	public void drawtask(Identity editor, String shape, String timeline, Object color, Object o) throws RemoteException;
	public boolean login(DrawInterface client, Identity id) throws RemoteException;

}