import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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



        pair player = new pair(numrows - 1, 0);
        pair centre = new pair(numrows - 5, 0);
        
        ImageView[][] iv = new ImageView[numrows][numcols];//initialising grid of images
        ImageView[][] zoom = new ImageView[5][5];
        ImageView img; //dummy variable to hold individual image
        Image fl = getimg("floor.png");
        for(int i = 0; i < numrows; i++){
            for(int j = 0; j < numcols; j++){
                img = new ImageView();
                img.setImage(fl);
                img.setFitHeight(900/5);
                img.setFitWidth(900/5);    
                img.setId("p");
                iv[i][j] = img;
                
            }
        }
        updateZoom(centre, zoom, iv, layout);
        Image chr = getimg("front.png");
        iv[numrows-1][0].setImage(chr); 
        Image door = getimg("chest.png");
        iv[0][numcols - 1].setImage(door); 
        iv[0][numcols - 1].setId("GOAL");
        Image wall = getimg("brick.png");
        for(int i = 0; i < (numrows/2); i++){
            for(int j = 0; j < numcols;j++){
                img = iv[2*i+1][j];
                img.setImage(wall);
                img.setId("w");
            }
        }
        for(int i = 0; i < numrows; i++){
            for(int j = 0; j < (numcols/2);j++){
                img = iv[i][2*j+1];
                img.setImage(wall);
                img.setId("w");
            }
        }
        Image tch = getimg("torch.png");
        for(int i = 0; i < numrows; i++){
            for(int j = 0; j < numcols;j++){
                if(i % 2 == 1 && j % 2 == 1){
                    img = iv[i][j];
                    img.setImage(tch);                
                    img.setId("w");
                }
            }
        }
        Kruskals(iv);
        // Create a layout and add the label and button to it
         // 10 is the spacing between elements
        //layout.getChildren().addAll(label, button);
        
        // Create a scene and add the layout to it
        Scene scene = new Scene(layout, 1920, 1080); // Width and height of the scene
        // Set the background color of the Scene
        scene.setFill(Color.DARKSLATEGRAY); // Replace with your desired color

        scene.setOnKeyPressed(event -> {
            String code = event.getCode().toString();
            makeMove(code, player, centre,iv, primaryStage, zoom, layout);
        });
        //Set the scene for the primary stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
        
    }
    public void makeMove(String code, pair player, pair centre, ImageView[][] iv, Stage primaryStage, ImageView[][] zoom, GridPane layout) {
        code = code.toLowerCase();
        pair tmp = null;
        Image path = getimg("floor.png");
        Image pl = null;
    
        if (code.equals("w")) {
            pl = getimg("back.png");
            tmp = new pair(player.getRow() - 1, player.getColumn());
        } else if (code.equals("a")) {
            pl = getimg("left.png");
            tmp = new pair(player.getRow(), player.getColumn() - 1);
        } else if (code.equals("s")) {
            pl = getimg("front.png");
            tmp = new pair(player.getRow() + 1, player.getColumn());
        } else if (code.equals("d")) {
            pl = getimg("right.png");
            tmp = new pair(player.getRow(), player.getColumn() + 1);
        }
    
        boolean isWall = iv[tmp.getRow()][tmp.getColumn()].getId().equals("w");
    
        if (!isWall && checkBounds(tmp)) {
            if (iv[tmp.getRow()][tmp.getColumn()].getId().equals("GOAL")) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText("You completed the maze!");
                alert.setTitle("Well done!");
                alert.setContentText("You found the Treasure!");
                alert.showAndWait();
                primaryStage.close();
            }
            iv[tmp.getRow()][tmp.getColumn()].setImage(pl);
            iv[player.getRow()][player.getColumn()].setImage(path);
            player.setRow(tmp.getRow());
            player.setColumn(tmp.getColumn());
    
            // Check if the player's movement goes beyond the bounds of the 5x5 zoomed screen
            if (!checkBoundsZoom(centre, tmp)) {
                centre = calculateNewCentre(tmp);
                updateZoom(centre, zoom, iv, layout);
            }
        }
    }
    
    public boolean checkAlternateBoundsZoom(pair tmp, pair centre){
        if(tmp.getColumn() >= 2 && tmp.getColumn() <= numcols - 3 && tmp.getRow() >= numrows - 2){
            centre.setColumn(tmp.getColumn());
        } else if(tmp.getColumn() >= numcols - 2 && tmp.getRow() <= numrows - 3 && tmp.getRow() >= 2){
            centre.setRow(tmp.getRow());
        } else if(tmp.getColumn() <= numcols - 2 && tmp.getColumn() >= 2 && tmp.getRow() <= 1){
            centre.setColumn(tmp.getColumn());
        } else if(tmp.getColumn() <= 1 && tmp.getRow() >= 2 && tmp.getRow() <= numrows - 3){
            centre.setRow(tmp.getRow());
        } else if(tmp.getRow() >= 2 && tmp.getRow() <= numrows - 3 && tmp.getColumn() >= 2 && tmp.getColumn() <= numcols - 3){
            centre.setRow(tmp.getRow());
            centre.setColumn(tmp.getColumn());
        }
        return false;
    }

    public boolean checkBoundsZoom(pair centre, pair tmp) {
        int minRow = Math.max(0, centre.getRow() - 2);
        int maxRow = Math.min(numrows - 1, centre.getRow() + 2);
        int minCol = Math.max(0, centre.getColumn() - 2);
        int maxCol = Math.min(numcols - 1, centre.getColumn() + 2);
    
        return (tmp.getRow() >= minRow && tmp.getRow() <= maxRow && tmp.getColumn() >= minCol && tmp.getColumn() <= maxCol);
    }
    
    
    
    
    public pair calculateNewCentre(pair tmp) {
        // Calculate and return the new center position for the 5x5 zoomed screen based on tmp
        int newCentreRow = tmp.getRow() - 2; // Adjust as needed
        int newCentreCol = tmp.getColumn() - 2; // Adjust as needed
        return new pair(newCentreRow, newCentreCol);
    }
    
    public void updateZoom(pair centre, ImageView[][] zoom, ImageView[][] iv, GridPane layout) {
        ImageView img;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                zoom[i][j] = iv[centre.getRow() + i][centre.getColumn() + j];
            }
        }
        layout.getChildren().clear(); // Clear the old zoomed screen
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                img = zoom[i][j];
                layout.add(img, j, i); // Add the updated zoomed screen to the layout
            }
        }
    }
    

    public boolean checkBounds(pair tmp){
        if(tmp.getRow() < numrows && tmp.getRow() >= 0 && tmp.getColumn() < numcols && tmp.getColumn() >= 0){
            return true;
        }
        return false;
    }
    public Image getimg(String img){
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream("Hackathon_demo\\" + img);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 
        Image image = new Image(inputstream);
        return image;
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

        Image fl = getimg("floor.png");
        
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
                path2 = new pair(row, col - 1);
            }

            if(!(checkContains(pathtiles[path1.getRow()][path1.getColumn()], path2))){//checks if path2 is a member of path1's set (are they connected)
                //connect the paths
                pathtiles[path1.getRow()][path1.getColumn()].addAll(pathtiles[path2.getRow()][path2.getColumn()]);
                for(pair p: pathtiles[path1.getRow()][path1.getColumn()]){
                        pathtiles[p.getRow()][p.getColumn()] = (pathtiles[path1.getRow()][path1.getColumn()]);
                }

                pathtiles[path2.getRow()][path2.getColumn()] = pathtiles[path1.getRow()][path1.getColumn()];
                iv[randEdge.getRow()][randEdge.getColumn()].setImage(fl);
                iv[randEdge.getRow()][randEdge.getColumn()].setId("p");
            }
            head++;
        }
    }

    
}