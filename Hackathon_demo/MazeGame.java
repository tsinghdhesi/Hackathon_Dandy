import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.util.*;
public class MazeGame extends Application {
    public static final int numrows = 17;
    public static final int numcols = 37;
    public static final int numedges = (numrows/2 + numrows/2 + 1) *(numcols/2) + numrows/2;
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("MazeGame");
        primaryStage.setMaximized(true);
        GridPane layout = new GridPane();
        ImageView[][] iv = new ImageView[numrows][numcols];//initialising grid of images
        // System.out.println("Primary Screen Width: " + screenWidth + " pixels");
        // System.out.println("Primary Screen Height: " + screenHeight + " pixels");
        ImageView img; //dummy variable to hold individual image
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream("Hackathon_demo\\floor.jpg");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 
        Image fl = new Image(inputstream); 
        for(int i = 0; i < numrows; i++){
            for(int j = 0; j < numcols; j++){
                img = new ImageView();
                img.setImage(fl);
                img.setFitHeight(1900/numcols);
                img.setFitWidth(900/numrows);                
                iv[i][j] = img;
                layout.add(img, j, i);
            }
        }
        try {
            inputstream = new FileInputStream("Hackathon_demo\\front-overlay.jpg");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 
        Image chr = new Image(inputstream);
        iv[numrows-1][0].setImage(chr);
        try {
            inputstream = new FileInputStream("Hackathon_demo\\wall.png");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 
        Image wall = new Image(inputstream);
        for(int i = 0; i < (numrows/2); i++){
            for(int j = 0; j < numcols;j++){
                img = iv[2*i+1][j];
                img.setImage(wall);
            }
        }
        for(int i = 0; i < numrows; i++){
            for(int j = 0; j < (numcols/2);j++){
                img = iv[i][2*j+1];
                img.setImage(wall);
            }
        }
        try {
            inputstream = new FileInputStream("Hackathon_demo\\rock.jpg");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 
        Image rck = new Image(inputstream);
        for(int i = 0; i < numrows; i++){
            for(int j = 0; j < numcols;j++){
                if(i % 2 == 1 && j % 2 == 1){
                    img = iv[i][j];
                    img.setImage(rck);
                }
            }
        }
        Kruskals(iv);
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
    public boolean checkContains(HashSet<pair> hs, pair p){
        for (pair p2: hs){
            if(p2.equals(p)){
                return true;
            }
        }
        return false;
    }

    public void Kruskals(ImageView[][] iv){
        HashSet<pair>[][] pathtiles = new HashSet[numrows][numcols];
        
        for(int i = 0; i < pathtiles.length; i++) {
            for(int j = 0; j < pathtiles[i].length; j++) {
                pathtiles[i][j] = new HashSet<pair>();
                pathtiles[i][j].add(new pair(i, j)); //add each ImageView to its own HashSet
            }
        }
        
        pair[] edgeTile = new pair[numedges];
        int k = 0;
        for (int row = 0; row < numrows; row++) {  // 9 row
            for (int col = 0; col <= (numcols-1); col++) {  // 18 columns
                if (row % 2 == 0 && col != 0 && col %2 != 0){
                    edgeTile[k++] = new pair(row, col);
                }
                if (row % 2 != 0 && col % 2 == 0) {
                    edgeTile[k++] = new pair(row, col);
                }
            }
        }

        List<pair> edgeTileList = Arrays.asList(edgeTile);// Convert to an list to shuffle
        Collections.shuffle(edgeTileList);
        int head = 0;
        
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream("Hackathon_demo\\floor.jpg");
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 
        Image fl = new Image(inputstream);
        
        while (head < numedges){
            pair randEdge = edgeTileList.get(head);// Keep track of the first element
            int row = randEdge.getRow();
            int col = randEdge.getColumn();
            pair path1;
            pair path2;

            if(randEdge.getColumn() % 2 == 0){
                path1 = new pair(row + 1, col);
                path2 = new pair(row - 1, col);
            } else {
                path1 = new pair(row, col + 1);
                path2 = new pair(row, col        - 1);
            }

            if(!(checkContains(pathtiles[path1.getRow()][path1.getColumn()], path2))){//checks if path2 is a member of path1's set (are they connected)
                //connect the paths
                pathtiles[path1.getRow()][path1.getColumn()].addAll(pathtiles[path2.getRow()][path2.getColumn()]);
                for(pair p: pathtiles[path1.getRow()][path1.getColumn()]){
                        pathtiles[p.getRow()][p.getColumn()] = (pathtiles[path1.getRow()][path1.getColumn()]);
                }

                pathtiles[path2.getRow()][path2.getColumn()] = pathtiles[path1.getRow()][path1.getColumn()];
                iv[randEdge.getRow()][randEdge.getColumn()].setImage(fl);
            }
            head++;
        }
    }
}