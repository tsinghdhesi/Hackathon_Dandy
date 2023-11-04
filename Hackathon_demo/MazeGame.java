import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MazeGame extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Example");

        // Create a label
        Label label = new Label("Hello, JavaFX!");

        // Create a button
        Button button = new Button("Click Me");
        button.setOnAction(e -> label.setText("Button Clicked!"));

        // Create a layout and add the label and button to it
        VBox layout = new VBox(10); // 10 is the spacing between elements
        layout.getChildren().addAll(label, button);

        // Create a scene and add the layout to it
        Scene scene = new Scene(layout, 300, 200); // Width and height of the scene

        // Set the scene for the primary stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }
}
