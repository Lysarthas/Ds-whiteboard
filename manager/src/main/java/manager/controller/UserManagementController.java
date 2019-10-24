package manager.controller;

import java.lang.Thread.UncaughtExceptionHandler;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;
import manager.drawserver.DrawServer;
import rmi.share.Identity;

public class UserManagementController implements UncaughtExceptionHandler {
    @FXML
    private Button startServerButton;

    @FXML
    private Button stopServerButton;

    @FXML
    private TextArea serverOutput;

    @FXML
    private ListView<String> userListView;
    private ArrayList<String> userList = new ArrayList<String>();

    @Getter
    @Setter
    private int port;

    @Getter
    @Setter
    private String userName;

    private DrawServer drawServer;
    private Thread drawServerThread;

    @FXML
    public void initialize() throws RemoteException {
        Thread.setDefaultUncaughtExceptionHandler(this);
        serverOutput.appendText("Welcome!!\n");
        userListView.setEditable(false);
        this.stopServerButton.setDisable(true);
        this.startServerButton.setDisable(false);
    }

    @FXML
    protected void startServer(ActionEvent event) throws RemoteException, InterruptedException {
        this.logging("Starting the server");
        this.startServerButton.setDisable(true);
        this.stopServerButton.setDisable(false);
        this.drawServer = DrawServer.newserver(Integer.toString(this.port));
        this.drawServer.setId(new Identity(this.userName));
        this.drawServerThread = new Thread(this.drawServer);
        this.drawServerThread.start();
        this.refreshUserList();
        this.logging("Server started");
    }

    @FXML
    protected void stopServer(ActionEvent event) {
        this.stopServerButton.setDisable(true);
        this.startServerButton.setDisable(false);
        this.logging("Stopping the server");
    }

    private void refreshUserList() throws RemoteException {
        new Thread(() -> {
            while (true) {
                try {
                    this.userList = drawServer.getClientlist();
                    userListView.getItems().setAll(userList);
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void logging(String log) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(Instant.now().toString()).append("] ").append(log).append("\n");
        serverOutput.appendText(sb.toString());
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        this.logging(e.getMessage());
    }
}