import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;


public class Main extends Application {
    public static void main(String[] args) {launch(args);}

    public static ArrayList<ImageView> list = new ArrayList<ImageView>();
    public static ArrayList<Image> imagesTrue = new ArrayList<Image>();
    public static ArrayList<Image> images = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Casse-Tête");
        primaryStage.setResizable(true);
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setMaxWidth(1000);


        Image[] tableau2 = {
                new Image("images/mario0.jpg"),
                new Image("images/mario1.jpg"),
                new Image("images/mario2.jpg"),
                new Image("images/mario3.jpg"),
                new Image("images/mario4.jpg"),
                new Image("images/mario5.jpg"),
                new Image("images/mario6.jpg"),
                new Image("images/mario7.jpg"),
                new Image("images/mario8.jpg")};

        ImageView[] tableau = {
                new ImageView(tableau2[0]),
                new ImageView(tableau2[1]),
                new ImageView(tableau2[2]),
                new ImageView(tableau2[3]),
                new ImageView(tableau2[4]),
                new ImageView(tableau2[5]),
                new ImageView(tableau2[6]),
                new ImageView(tableau2[7]),
                new ImageView(tableau2[8])};


        for (int i = 0; i<tableau.length; i++){
            imagesTrue.add(tableau2[i]);
            images.add(tableau2[i]);
            list.add(tableau[i]);

            list.get(i).setPreserveRatio(true);
            list.get(i).fitWidthProperty().bind(primaryStage.widthProperty());
            list.get(i).setFitHeight(tableau[i].getFitWidth()/3);

            dragAndDrop(list.get(i));
        }

        //Disposition
        HBox hBox1 = new HBox(list.get(0), list.get(1), list.get(2));
        HBox hBox2 = new HBox(list.get(3), list.get(4), list.get(5));
        HBox hBox3 = new HBox(list.get(6), list.get(7), list.get(8));

        VBox vBox = new VBox(hBox1, hBox2, hBox3);

        shuffle();

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->{
            for (int i=0; i<list.size(); i++){
                list.get(i).fitWidthProperty().bind(primaryStage.widthProperty());
                list.get(i).setFitHeight(list.get(i).getFitWidth()/3);
            } };

        primaryStage.widthProperty().addListener(stageSizeListener);
        primaryStage.heightProperty().addListener(stageSizeListener);
        primaryStage.minHeightProperty().bind(primaryStage.widthProperty().multiply(1));
        primaryStage.maxHeightProperty().bind(primaryStage.widthProperty().multiply(1));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.M && event.isControlDown())
                shuffle();
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public void dragAndDrop(ImageView imageView){

        imageView.setOnDragDetected((event) -> {
            Dragboard dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent contenu = new ClipboardContent();
            contenu.putImage(imageView.getImage());
            dragboard.setContent(contenu);
        });

        imageView.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
        });

        imageView.setOnDragDropped(event -> {

            ImageView target = (ImageView)event.getGestureTarget();
            ImageView source = (ImageView)event.getGestureSource();
            Image tempo = source.getImage();
            source.setImage(target.getImage());
            target.setImage(tempo);


            event.setDropCompleted(true);
        });

        imageView.setOnDragDone(event -> {
            boolean done=true;

            for (int i=0; i<list.size(); i++){
                System.out.println(list.get(i).getImage().equals(imagesTrue.get(i)));
                if(!list.get(i).getImage().equals(imagesTrue.get(i)))
                    done=false;
            }

            if (done){
                Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
                alert.setTitle("Partie terminée");
                alert.setHeaderText("Félicitations! Vous avez réussi!");
                alert.setContentText("Que voulez-vous faire?");
                alert.showAndWait();
            }
        });


        imageView.setOnMouseClicked(event1 -> {
            imageView.setRotate(imageView.getRotate()+90);
        });

    }

    public void shuffle(){
        for (int i=0; i<imagesTrue.size(); i++)
            images.set(i, imagesTrue.get(i));

        Collections.shuffle(images);

        for (int i=0; i<images.size(); i++)
            list.get(i).setImage(images.get(i));
    }
}