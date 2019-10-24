package manager.controller;

import java.lang.Thread.UncaughtExceptionHandler;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;
import manager.drawserver.DrawServer;
import rmi.share.DrawInterface;
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

    @Getter
    private ArrayList<String> userList = new ArrayList<String>();

    @Getter
    @Setter
    private int port;

    @Getter
    @Setter
    private String userName;

    private DrawServer drawServer;
    private Thread drawServerThread;

    private String userToBeDeleted = "";

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
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            while (true) {
                try {
                    if (userToBeDeleted != null && !userToBeDeleted.isEmpty()) {
                        if (drawServer != null && !userToBeDeleted.equals(drawServer.getId().getName())) {
                            int index = drawServer.getClientIndexByName(userToBeDeleted);
                            if (index > -1) {
                                drawServer.getClients().get(index).notify("You have been kicked out from this channel", false);
                                drawServer.getClients().remove(index);
                                userToBeDeleted = "";
                            }
                        }
                    }

                    this.userList = drawServer.getClientlist();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            List<String> viewList = new ArrayList<String>(userListView.getItems());
                            for (String name: userList) {
                                if (!viewList.contains(name)) {
                                    userListView.getItems().add(name);
                                }
                            }
                            for (String name: viewList) {
                                if (!userList.contains(name)) {
                                    userListView.getItems().remove(name);
                                }
                            }
                        }
                    });
                    try {
                        TimeUnit.SECONDS.sleep(1);
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
        e.printStackTrace();
    }

    @FXML
    protected void deleteUser(MouseEvent arg0) {
        String selected = userListView.getSelectionModel().getSelectedItem();
        if (selected != null && !selected.isEmpty() && !selected.equals(drawServer.getId().getName())) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Click to kick this user " + selected);
            alert.setContentText("Are you ok with this?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                userToBeDeleted = selected;
                userListView.getItems().remove(selected);
            }
        }
    }
}