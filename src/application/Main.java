package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        TaskManager taskManager = new TaskManager();
        TaskUI taskUI = new TaskUI(taskManager);

        HBox mainLayout = taskUI.getMainLayout();
        
        Scene scene = new Scene(mainLayout, 800, 500);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        
        primaryStage.setTitle("TODO List Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
