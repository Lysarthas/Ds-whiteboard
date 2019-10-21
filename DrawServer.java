import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DrawServer extends UnicastRemoteObject implements DrawInterface{
	
	private static final long serialVersionUID = 1L;
	ArrayList<DrawInterface> clients;
	HashMap<DrawInterface, String> clientname;
	
	protected DrawServer() throws RemoteException{
		clients = new ArrayList<>();
		clientname = new HashMap<>();
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		Naming.rebind("RMIServer", new DrawServer());

	}
	
	public void broadcast(DrawInterface di, String shape, String timeline, String color, Object o) throws RemoteException{
		String taskclient = clientname.get(di);
		for(int i=0; i < clients.size(); i++) {
			DrawInterface client = clients.get(i);
			if(clientname.get(client).equals(taskclient)) {
				continue;
			}
			else {
				client.drawtask(shape, timeline, color, o);
			}
		}
	}
	
	public void drawtask(String shape, String timeline, String color, Object o) throws RemoteException{}
	
	public void login(DrawInterface di, String name) {
		this.clients.add(di);
		this.clientname.put(di, name);
	}

}
