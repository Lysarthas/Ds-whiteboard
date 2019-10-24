package rmi.share;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DrawInterface extends Remote {
	
	public Identity user() throws RemoteException;
	public void broadcast(Identity editor, String shape, String timeline, Object color, Object o, String message) throws RemoteException;
    public void drawtask(Identity editor, String shape, String timeline, Object color, Object o, String message) throws RemoteException;
    public void chattask(Identity editor, String message) throws RemoteException;
	public Boolean login(DrawInterface client, Identity id) throws RemoteException;
    public byte[] getCurrentGraph() throws RemoteException;
    public void notify(String message, boolean isClosed) throws RemoteException;
}