package client.drawclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import client.share.*;

public class DrawClient implements DrawInterface,Runnable{
	private DrawInterface server;
	String IP, Username;
	int Port;
	Identity id;
	boolean connection = false;
	
	public static DrawClient clt_ins = null;
	
	public static DrawClient newclient(int p, String i, String n) throws RemoteException {
		if(clt_ins == null) {
			clt_ins = new DrawClient(p, i, n);
		}
		return clt_ins;
	}
	
	public static DrawClient getclient() {
		return clt_ins;
	}
	
	public DrawClient(int port, String ip, String name) {
		this.IP = ip;
		this.Port = port;
		this.Username = name;
	}
	
	public Identity user() {
		
		return id;
	}
	
	public void broadcast(String status, Object o) throws RemoteException {
		
	}
	
	public void drawtask(String shape, String status, String color, Object o) throws RemoteException {
		
	}
	
	public boolean login(DrawInterface client, Identity id) throws RemoteException {
		return true;
	}
	
	public void connect(DrawInterface serInf, Identity id) throws RemoteException {
		this.server = serInf;
		connection = server.login(this, id);
	}

	@Override
	public void run() {
		
		try {
			Integer p = this.Port; 
			String url = "rmi://"+this.IP+":"+p.toString()+"/RMIServer";
			DrawInterface serverinterface = (DrawInterface)Naming.lookup(url);
			connect(serverinterface, this.id);
			if(connection) {
				
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
