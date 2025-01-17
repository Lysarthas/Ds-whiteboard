/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package manager;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import manager.controller.UserManagementController;
import manager.drawserver.DrawServer;

public class App extends Application {
    private static int DEFAULT_PORT = 8888;

    private Stage primaryStage;

    private static final int MIN_HEIGHT = 400;
    private static final int MIN_WIDTH = 500;

    private static int port = DEFAULT_PORT;
    private static String username;

    public static void main(String[] args) {
        final Options options = getOptions();
        CommandLine cmdLine;
        try {
            cmdLine = new DefaultParser().parse(options, args);
            port = getIntValue(cmdLine, 'p', DEFAULT_PORT);
            username = getValue(cmdLine, 'n', null);
        } catch (ParseException e) {
            handleParseException(e, options);
            return;
        }

        if (cmdLine.hasOption('h')) {
            displayUsage(options);
            System.exit(0);
            return;
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("White board server management");
		initRootLayout();
    }

    private void initRootLayout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Mangement.fxml"));
        GridPane root = fxmlLoader.load();
        UserManagementController umc = fxmlLoader.<UserManagementController>getController();
        umc.setPort(port);
        umc.setUserName(username);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setOnCloseRequest(e->{
            try {
                DrawServer.newserver("").closing();
            } catch (RemoteException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }

    @SuppressWarnings("static-access")
    private static Options getOptions() {
        final Options options = new Options();
        options.addOption(Option.builder("p")
                .longOpt("port")
                .desc(String.format("port to listen (default: %d)", DEFAULT_PORT))
                .hasArg()
                .argName("PORT")
                .type(Number.class)
                .build());

        options.addOption(Option.builder("n")
                .longOpt("name")
                .desc("username")
                .required(true)
                .hasArg()
                .argName("USERNAME")
                .build());
        
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("display help")
                .build());
        return options;
    }

    private static void handleParseException(ParseException e, Options options) {
        if (e instanceof MissingOptionException) {
            StringBuilder sb = new StringBuilder();
            Iterator<String> missingOptions = ((MissingOptionException) e).getMissingOptions().iterator();
            while (missingOptions.hasNext()) {
                sb.append(missingOptions.next());
                if (missingOptions.hasNext()) {
                    sb.append(", ");
                }
            }
            System.out.println(String.format("Missing required option(s) %s.", sb.toString()));
        } else if (e instanceof MissingArgumentException) {
            System.out.println(String.format("%s is missing a required argument.",
            ((MissingArgumentException) e).getOption()));
        } else if (e instanceof UnrecognizedOptionException) {
            System.out.println(String.format("%s is not a valid option.",
            ((UnrecognizedOptionException) e).getOption()));
        } else {
            //pass
        }
        displayUsage(options);
        System.exit(-1);
    }

    private static int getIntValue(CommandLine cmdLine, char option, int defaultValue) throws ParseException {
        if (cmdLine.hasOption(option)) {
            return ((Number) cmdLine.getParsedOptionValue(String.valueOf(option))).intValue();
        } else {
            return defaultValue;
        }
    }

    private static String getValue(CommandLine cmdLine, char option, String defaultValue) throws ParseException {
        if (cmdLine.hasOption(option)) {
            return cmdLine.getOptionValue(String.valueOf(option));
        } else {
            return defaultValue;
        }
    }

    private static void displayUsage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar jhttp*.jar", "Starts a white board server", options, null, true);
    }
}
