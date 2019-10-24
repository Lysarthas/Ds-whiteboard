package client.drawclient;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import rmi.share.*;

public class DrawClient extends UnicastRemoteObject implements DrawInterface, Runnable, UncaughtExceptionHandler{
	/**
     *
     */
    private static final long serialVersionUID = -6635749533907857673L;

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
	
	public DrawClient(int port, String ip, String name) throws RemoteException {
        Thread.currentThread().setUncaughtExceptionHandler(this);
		this.IP = ip;
		this.Port = port;
        this.Username = name;
        this.id = new Identity(this.Username);
	}
	
	public Identity user() {
		return id;
	}
	
	public void broadcast(String shape, String timeline, String color, Object o) throws RemoteException {
		
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
				System.out.println("join success");
            } else {
                System.out.println("You cannot join this room maybe caused by duplicate user name. You can try again with different username later");
                System.exit(1);
            }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			this.uncaughtException(Thread.currentThread(), e);
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof RemoteException) {
            System.out.println("failed to connect to server");
        }
        System.exit(1);;
    }
}
