package manager.controller;

import java.lang.Thread.UncaughtExceptionHandler;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import manager.drawserver.DrawServer;

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
    private ObservableList<String> observableList = FXCollections.observableArrayList();

    private int port;

    private DrawServer drawServer;
    private Thread drawServerThread;

    @FXML
    public void initialize() throws RemoteException {
        Thread.setDefaultUncaughtExceptionHandler(this);
        serverOutput.appendText("Welcome!!\n");
        userListView.setEditable(false);
    }

    public void setPort(int port) {
        this.port = port;
    }

    @FXML 
    protected void startServer(ActionEvent event) throws RemoteException {
        this.logging("Starting the server");
        this.drawServer = DrawServer.newserver(Integer.toString(this.port));
        this.drawServerThread = new Thread(this.drawServer);
        this.drawServerThread.start();
        this.refreshUserList();
        this.logging("Server started");
    }

    @FXML
    protected void stopServer(ActionEvent event) {
        this.logging("Stopping the server");
    }

    private void refreshUserList() throws RemoteException {
        this.userList = this.drawServer.getClientlist();
        observableList.setAll(this.userList);
        userListView.setItems(observableList);
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