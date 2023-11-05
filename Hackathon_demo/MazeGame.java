import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
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
        pair centre = new pair(numrows - 3, 2);
        
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
        iv[0][numcols-1].setId("GOAL");
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
    
        if (checkBounds(tmp)) {
            boolean isWall = iv[tmp.getRow()][tmp.getColumn()].getId().equals("w");
            if(!isWall){
                if (iv[tmp.getRow()][tmp.getColumn()].getId().equals("GOAL")) {
                    showEndgameScreen(primaryStage);
                    return; // Exit the method early since the game is over
                }
                iv[tmp.getRow()][tmp.getColumn()].setImage(pl);
                iv[player.getRow()][player.getColumn()].setImage(path);
                player.setRow(tmp.getRow());
                player.setColumn(tmp.getColumn());
                checkBoundsZoom(centre, tmp, zoom, iv, layout);
            }
        }
    }

    public void checkBoundsZoom(pair centre, pair tmp, ImageView[][] zoom, ImageView[][] iv, GridPane layout) {
        if(tmp.getColumn() >= 2 && tmp.getColumn() <= numcols - 3 && tmp.getRow() >= numrows - 2){
            centre.setColumn(tmp.getColumn());
            updateZoom(centre, zoom, iv, layout);
        } else if(tmp.getColumn() >= numcols - 2 && tmp.getRow() <= numrows - 3 && tmp.getRow() >= 2){
            centre.setRow(tmp.getRow());
            updateZoom(centre, zoom, iv, layout);
        } else if(tmp.getColumn() <= numcols - 2 && tmp.getColumn() >= 2 && tmp.getRow() <= 1){
            centre.setColumn(tmp.getColumn());
            updateZoom(centre, zoom, iv, layout);
        } else if(tmp.getColumn() <= 1 && tmp.getRow() >= 2 && tmp.getRow() <= numrows - 3){
            centre.setRow(tmp.getRow());
            updateZoom(centre, zoom, iv, layout);
        } else if(tmp.getRow() >= 2 && tmp.getRow() <= numrows - 3 && tmp.getColumn() >= 2 && tmp.getColumn() <= numcols - 3){
            centre.setRow(tmp.getRow());
            centre.setColumn(tmp.getColumn());
            updateZoom(centre, zoom, iv, layout);
        }
    }
    
    public void updateZoom(pair centre, ImageView[][] zoom, ImageView[][] iv, GridPane layout) {
        ImageView img;
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                zoom[i + 2][j + 2] = iv[centre.getRow() + i][centre.getColumn() + j];
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

    public void showEndgameScreen(Stage primaryStage) {
        // Create a new Stage for the endgame screen
        Stage endgameStage = new Stage();

        // Make it full screen
        endgameStage.initStyle(StageStyle.UNDECORATED); // Remove window decorations
        endgameStage.setFullScreen(true);
        endgameStage.setFullScreenExitHint(""); // Hide the exit full screen message

        // Create a Pane with a custom background image
        Pane endgamePane = new Pane();
        
        // Set a background image
        try {
        // Set a background image
        Image backgroundImage = new Image("Hackathon_demo\\maze_bg.jpg"); // true parameter for background loading
        BackgroundImage backgroundImg = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
        endgamePane.setBackground(new Background(backgroundImg));
    } catch (IllegalArgumentException e) {
        System.out.println("Error loading background image: " + e.getMessage());
        endgamePane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    }

        // You can add additional UI elements here, such as a message
        Label endgameLabel = new Label("Congratulations! You have completed the maze!");
        endgameLabel.setTextFill(Color.WHITE); // Set text color
        endgameLabel.setStyle("-fx-font-size: 50px; -fx-alignment: center;"); // Set font size and center alignment
        endgameLabel.setMaxWidth(Double.MAX_VALUE); // Set label width to the maximum
        endgameLabel.setAlignment(Pos.CENTER); // Center the label text
        
        // Add the label to the pane and center it within the pane
        endgamePane.getChildren().add(endgameLabel);
        endgameLabel.setLayoutX(0);
        endgameLabel.setLayoutY(primaryStage.getHeight() / 4); // Position label at 1/4th the height of the screen for aesthetic vertical centering

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 30px;"); // Make button text bigger
        exitButton.setPrefHeight(60); // Make button bigger
        exitButton.setPrefWidth(200);
        exitButton.setOnAction(e -> {
            // What you want to do on button click; for example:
            Platform.exit();
        });
        // Center the button horizontally and place it below the label
        exitButton.setLayoutX(primaryStage.getWidth() / 2 - exitButton.getPrefWidth() / 2);
        exitButton.setLayoutY(primaryStage.getHeight() / 2); // Position it at half the height of the screen

        // Add everything to the pane
        endgamePane.getChildren().add(exitButton);

        // Create a scene with the endgame pane
        Scene endgameScene = new Scene(endgamePane, primaryStage.getWidth(), primaryStage.getHeight());

        // Set the scene and show the stage
        endgameStage.setScene(endgameScene);
        endgameStage.show();

        // Hide the main game window
        primaryStage.hide();
    }
}