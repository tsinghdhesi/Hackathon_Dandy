import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class MazeGame extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("MazeGame");
        primaryStage.setMaximized(true);
        VBox layout = new VBox();
        ImageView[][] iv = new ImageView[9][18];//initialising grid of images
        ImageView img; //dummy variable to hold individual image
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream("Hackathon_demo\\dirt.jpg");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 
        Image drt = new Image(inputstream); 
        for(int i =0; i < 9; i++){
            for(int j = 0; j < 18;j++){
                img = iv[i][j];
                img.setImage(drt);
                img.setLayoutX(i);
                layout.getChildren().add(img);
            }
        }
        // Create a label
        Label label = new Label("Hello, JavaFX!");

        // Create a button
        Button button = new Button("Click Me");
        button.setOnAction(e -> label.setText("Button Clicked!"));

        // Create a layout and add the label and button to it
         // 10 is the spacing between elements
        layout.getChildren().addAll(label, button);

        // Create a scene and add the layout to it
        Scene scene = new Scene(layout, 300, 200); // Width and height of the scene

        // Set the scene for the primary stage
        primaryStage.setScene(scene);

        
        // Show the stage
        primaryStage.show();
        
    }
    
    public void Kruskals(ImageView iv, ArrayList<ArrayList<HashSet<ImageView>>> pathTiles, LinkedList<ImageView> edgeTiles){
        // 1. grid of images

        // 2. set of sets for path tile (2d arr?)

        //3. list of edges 

        //
        //ImageView tmp = edgeTiles.pop();

        //

        

        
    }
        
    
}
