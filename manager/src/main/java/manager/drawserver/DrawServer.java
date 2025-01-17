package manager.drawserver;

import java.awt.Point;
import java.io.ByteArrayInputStream;
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
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import lombok.Getter;
import lombok.Setter;
import rmi.share.*;

public class DrawServer extends UnicastRemoteObject implements DrawInterface, Runnable {

    private static final long serialVersionUID = 1L;

    @Getter
    volatile ArrayList<DrawInterface> clients;

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
        byte[] bImage = null;
        if (timeline.equals("upload")) {
            int index = getClientIndexByName(id.getName());
            bImage = clients.get(index).getCurrentGraph();
        }
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).user().equals(id)) {
                continue;
            }
            try {
                if (timeline.equals("upload") && bImage != null) {
                    clients.get(i).drawImage(bImage);
                } else {
                    clients.get(i).drawtask(id, shape, timeline, color, o, message);
                }
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
        if (shape != null && !shape.isEmpty()) {
            this.pFrame.showEditing(id);
        }

        if (shape.isEmpty() || shape == null || shape.equals("chat")) {
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

    public Boolean login(DrawInterface client, Identity id) {
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
        
        int reply = JOptionPane.showConfirmDialog(this.pFrame, id.getName() + " want to join this room");
        if (reply == JOptionPane.YES_OPTION) {
            clients.add(client);
            return true;
        } else {
            return null;
        }
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

    public int getClientIndexByName(String name) throws RemoteException {
        for (int i = 0; i < clients.size(); i++) {
            String uName = clients.get(i).user().getName();
            if (uName.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void run() {
        try {
            DrawServer server = getInstance();
            String url = "rmi://" + this.IP + ":" + this.Port + "/RMIServer";
            LocateRegistry.createRegistry(Integer.parseInt(this.Port));
            Naming.rebind(url, server);
            this.clients.add(this);

            Locale.setDefault(Locale.ENGLISH);
            pFrame = DrawPictureFrame.drawfram(server, server);
            pFrame.setTitle(id.getName());
            pFrame.setVisible(true);

            pFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    closing();
                }
            });
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void closing() {
        for (int i = 1; i < clients.size(); i++) {
            try {
                clients.get(i).notify("The server is closing. You are offline now", true);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
            }
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

    @Override
    public void notify(String message, boolean isClosed) throws RemoteException {
    }

    @Override
    public void drawImage(byte[] byteImage) throws RemoteException {
        // TODO Auto-generated method stub
        BufferedImage bImage2;
        try {
            bImage2 = ImageIO.read(new ByteArrayInputStream(byteImage));
            pFrame.getG().drawImage(bImage2, 0, 0, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        pFrame.getCanvas().repaint();
    }

}