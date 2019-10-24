package manager.drawserver;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import javax.imageio.ImageIO;

import lombok.Getter;
import lombok.Setter;
import rmi.share.*;

public class DrawServer extends UnicastRemoteObject implements DrawInterface, Runnable {

    private static final long serialVersionUID = 1L;
    ArrayList<DrawInterface> clients;

    @Setter
    @Getter
	Identity id;

    String IP, Port;
    private static DrawServer serv_ins = null;
    Hashtable lpts = new Hashtable();

    DrawPictureFrame pFrame;

    private DrawServer(String Port) throws RemoteException {
        clients = new ArrayList<DrawInterface>();
        this.IP = "localhost";
        this.Port = Port;
    }

    public static DrawServer newserver(String p) throws RemoteException {
        if (serv_ins == null) {
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

    public void broadcast(Identity id, String shape, String timeline, Object color, Object o, String message) throws RemoteException {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).user().equals(id)) {
                continue;
            }
            try {
                clients.get(i).drawtask(id, shape, timeline, color, o, message);
            } catch (RemoteException e) {
                clients.remove(i);
                i--;
            }
        }
    }

    @Override
    public void chattask(Identity editor, String message) throws RemoteException {
        pFrame.displayMessage(editor, message);
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

    public boolean login(DrawInterface client, Identity id) {
        for (int i = 0; i < clients.size(); i++) {
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
        for (int i = 0; i < clients.size(); i++) {
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

    public boolean removeClient(Identity id) throws RemoteException {
        for (int i = 0; i < clients.size(); i++) {
            Identity uid = clients.get(i).user();
            if (uid.equals(id)) {
                clients.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        try {
            DrawServer server = getInstance();
            String url = "rmi://" + this.IP + ":" + this.Port + "/RMIServer";
            LocateRegistry.createRegistry(Integer.parseInt(this.Port));
            Naming.rebind(url, server);
            this.login(this, this.id);

            Locale.setDefault(Locale.ENGLISH);
            pFrame = DrawPictureFrame.drawfram(server, server);
            pFrame.setVisible(true);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public byte[] getCurrentGraph() throws RemoteException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(pFrame.getImage(), "png", baos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

}