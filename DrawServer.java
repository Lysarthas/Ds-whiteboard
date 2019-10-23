import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Locale;


public class DrawServer extends UnicastRemoteObject implements DrawInterface, Runnable{
	
	private static final long serialVersionUID = 1L;
	ArrayList<DrawInterface> clients;
	Identity id;
	String IP, Port;
	private static DrawServer serv_ins = null;
	
	public DrawServer(String Port) throws RemoteException{
		clients = new ArrayList<>();
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
			clients.get(i).drawtask(shape, timeline, color, o);
		}
	}
	
	public void drawtask(String shape, String timeline, String color, Object o) throws RemoteException{}
	
	public boolean login(DrawInterface client, Identity id) throws RemoteException {
		for(int i=0; i < clients.size(); i++) {
			if(clients.get(i).user().equals(client.user())) {
				return false;
			}
		}
		
		clients.add(client);
		return true;
	}
	
	public ArrayList<String> getClientlist() throws RemoteException {
		ArrayList<String> clientname = new ArrayList<>();
		for(int i=0; i< clients.size(); i++) {
			clientname.add(clients.get(i).user().getName());
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
			DrawPictureFrame frame = new DrawPictureFrame();
			frame.setVisible(true);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}

}
