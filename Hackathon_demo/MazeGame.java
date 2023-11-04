import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.util.*;

public class MazeGame extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("MazeGame");
        primaryStage.setMaximized(true);
        GridPane layout = new GridPane();
        ImageView[][] iv = new ImageView[9][19];//initialising grid of images
        // System.out.println("Primary Screen Width: " + screenWidth + " pixels");
        // System.out.println("Primary Screen Height: " + screenHeight + " pixels");
        ImageView img; //dummy variable to hold individual image
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream("Hackathon_demo\\dirt.jpg");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 
        Image drt = new Image(inputstream); 
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 19;j++){
                img = new ImageView();
                img.setImage(drt);
                img.setFitHeight(100);
                img.setFitWidth(100);                
                iv[i][j] = img;
                layout.add(img, j, i);
            }
        }try {
            inputstream = new FileInputStream("Hackathon_demo\\rock.jpg");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 
        Image rck = new Image(inputstream);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 19;j++){
                img = iv[2*i+1][j];
                img.setImage(rck);
            }
        }
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9;j++){
                img = iv[i][2*j+1];
                img.setImage(rck);
            }
        }
        // Create a label
        Label label = new Label("Hello, JavaFX!");

        // Create a button
        Button button = new Button("Click Me");
        button.setOnAction(e -> label.setText("Button Clicked!"));

        // Create a layout and add the label and button to it
         // 10 is the spacing between elements
        //layout.getChildren().addAll(label, button);

        // Create a scene and add the layout to it
        Scene scene = new Scene(layout, 1920, 1080); // Width and height of the scene

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
