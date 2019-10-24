package manager.drawserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Locale;

import rmi.share.DrawInterface;
import rmi.share.Identity;

public class DrawServer extends UnicastRemoteObject implements DrawInterface, Runnable{
	
	private static final long serialVersionUID = 1L;
	ArrayList<DrawInterface> clients;
	Identity id;
	String IP, Port;
	private static DrawServer serv_ins = null;
	
	private DrawServer(String Port) throws RemoteException{
		clients = new ArrayList<DrawInterface>();
		this.IP = "localhost";
		this.Port = Port;
	}
	
	public static DrawServer newserver(String p) throws RemoteException {
		if(serv_ins == null) {
			serv_ins = new DrawServer(p);
		}
		return serv_ins;
	}
	
	public static DrawServer getInstance() {
		
		return serv_ins;
	}
	
	public Identity user() {
		return id;
	}
	
	public void broadcast(String shape, String timeline, String color, Object o) throws RemoteException{
		for(int i=0; i < clients.size(); i++) {
            try {
                clients.get(i).drawtask(shape, timeline, color, o);
            } catch (RemoteException e) {
                clients.remove(i);
                i--;
            }
		}
	}
	
	public void drawtask(String shape, String timeline, String color, Object o) throws RemoteException{

    }
	
	public boolean login(DrawInterface client, Identity id) {
		for(int i=0; i < clients.size(); i++) {
            try {
                Identity tempUser = clients.get(i).user();
                if (tempUser.equals(client.user())) {
                    return false;
                }
            } catch (RemoteException e) {
                clients.remove(i);
                i--;
            }
		}
		
		clients.add(client);
		return true;
	}
	
	public ArrayList<String> getClientlist() throws RemoteException {
		ArrayList<String> clientname = new ArrayList<String>();
		for(int i=0; i< clients.size(); i++) {
            try {
                String clientName = clients.get(i).user().getName();
                clientname.add(clientName);
            } catch (RemoteException e) {
                clients.remove(i);
                i--;
            }	
		}
		return clientname;
	}
	
	public boolean removeClient(Identity id) {
		return true;
	}

	@Override
	public void run() {
		try {
			DrawServer server = getInstance();
			String url = "rmi://"+this.IP+":"+this.Port+"/RMIServer";
			LocateRegistry.createRegistry(Integer.parseInt(this.Port));
			Naming.rebind(url, server);
			Locale.setDefault(Locale.ENGLISH);
			// DrawPictureFrame frame = new DrawPictureFrame();
			// frame.setVisible(true);
		} catch (RemoteException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}