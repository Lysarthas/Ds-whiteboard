package client.drawclient;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Locale;

import javax.imageio.ImageIO;

import rmi.share.*;

public class DrawClient extends UnicastRemoteObject implements DrawInterface, Runnable, UncaughtExceptionHandler {
    /**
     *
     */
    private static final long serialVersionUID = -6635749533907857673L;

    private DrawInterface server;
    private DrawPictureFrame pFrame;

    String IP, Username;
    int Port;
    Identity id;
    boolean connection = false;
    Hashtable lpts = new Hashtable();

    public static DrawClient clt_ins = null;

    public static DrawClient newclient(int p, String i, String n) throws RemoteException {
        if (clt_ins == null) {
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

    public void broadcast(Identity id, String shape, String timeline, Object color, Object o, String message) throws RemoteException {
    }

    public void drawtask(Identity id, String shape, String status, Object color, Object o, String message) throws RemoteException {
        if (shape != null) {
            this.pFrame.showEditing(id);
        }

        if (shape == null) {
            this.chattask(id, message);
            return;
        }

        if (status.equals("start")) {
            lpts.put(id.getName(), o);
        } else if (status.equals("drag") && DrawPictureFrame.isDrag(shape)) {
            Point last = (Point) lpts.get(id.getName());
            Point current = (Point) o;
            DrawPictureFrame.getFrame().drawpic(color, last, current, shape, message);
            lpts.put(id.getName(), o);
        } else if (status.equals("end")) {
            Point last = (Point) lpts.get(id.getName());
            Point current = (Point) o;
            DrawPictureFrame.getFrame().drawpic(color, last, current, shape, message);
            lpts.remove(id.getName());
        }
    }

    @Override
    public void chattask(Identity editor, String message) throws RemoteException {
        pFrame.displayMessage(editor, message);
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
            String url = "rmi://" + this.IP + ":" + p.toString() + "/RMIServer";
            DrawInterface serverinterface = (DrawInterface) Naming.lookup(url);
            connect(serverinterface, this.id);
            if (connection) {
                System.out.println("join success");
                Locale.setDefault(Locale.ENGLISH);
                pFrame = DrawPictureFrame.drawfram(serverinterface, this);
                syncCurrentImage(serverinterface);
                pFrame.setVisible(true);
            } else {
                System.out.println(
                        "You cannot join this room maybe caused by duplicate user name. You can try again with different username later");
                System.exit(1);
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.uncaughtException(Thread.currentThread(), e);
        } catch (NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
    }
    
    private void syncCurrentImage(DrawInterface serverinterface) throws IOException {
        byte[] imageBytesArray = serverinterface.getCurrentGraph();
        BufferedImage bImage2 = ImageIO.read(new ByteArrayInputStream(imageBytesArray));
        pFrame.getG().drawImage(bImage2, 0, 0, null);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof RemoteException) {
            System.out.println("failed to connect to server");
        }
        System.exit(1);
    }

    @Override
    public byte[] getCurrentGraph() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
}
