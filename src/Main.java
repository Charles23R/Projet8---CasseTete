import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
        primaryStage.setHeight(900);
        primaryStage.setWidth(900);
        primaryStage.setMaxWidth(1000);
        primaryStage.setMinWidth(500);


        MenuItem raph = new MenuItem("Raphaël");
        MenuItem marioMenu = new MenuItem("Mario");
        MenuItem max = new MenuItem("Maxime");
        MenuItem legault = new MenuItem("Marc-Antoine");

        Menu menu = new Menu("Images");
        menu.getItems().addAll(raph, marioMenu, max, legault);

        MenuBar menuBar = new MenuBar(menu);

        Image[] tableau1 = {
                new Image("raph/raph0.jpg"),
                new Image("raph/raph1.jpg"),
                new Image("raph/raph2.jpg"),
                new Image("raph/raph3.jpg"),
                new Image("raph/raph4.jpg"),
                new Image("raph/raph5.jpg"),
                new Image("raph/raph6.jpg"),
                new Image("raph/raph7.jpg"),
                new Image("raph/raph8.jpg")};

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

        Image[] tableau3 = {
                new Image("max/max0.jpg"),
                new Image("max/max1.jpg"),
                new Image("max/max2.jpg"),
                new Image("max/max3.jpg"),
                new Image("max/max4.jpg"),
                new Image("max/max5.jpg"),
                new Image("max/max6.jpg"),
                new Image("max/max7.jpg"),
                new Image("max/max8.jpg")};

        Image[] tableau4 = {
                new Image("leg/leg0.jpg"),
                new Image("leg/leg1.jpg"),
                new Image("leg/leg2.jpg"),
                new Image("leg/leg3.jpg"),
                new Image("leg/leg4.jpg"),
                new Image("leg/leg5.jpg"),
                new Image("leg/leg6.jpg"),
                new Image("leg/leg7.jpg"),
                new Image("leg/leg8.jpg")};

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
        vBox.setPadding(new Insets(10, 40, 10, 40));

        shuffle();

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->{
            for (int i=0; i<list.size(); i++){
                list.get(i).fitWidthProperty().bind(primaryStage.widthProperty());
                list.get(i).setFitHeight(list.get(i).getFitWidth()/3.5);
            } };

        primaryStage.widthProperty().addListener(stageSizeListener);
        primaryStage.heightProperty().addListener(stageSizeListener);
        primaryStage.maxHeightProperty().bind(primaryStage.widthProperty().multiply(1500/1920));
        primaryStage.minHeightProperty().bind(primaryStage.widthProperty().multiply(1920/1080));


        //MEnus
        raph.setOnAction(event -> change(tableau1));
        marioMenu.setOnAction(event -> change(tableau2));
        max.setOnAction(event -> change(tableau3));
        legault.setOnAction(event -> change(tableau4));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);
        borderPane.setTop(menuBar);

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
            double rotateTarget = target.getRotate();
            double rotateSource = source.getRotate();
            source.setImage(target.getImage());
            target.setImage(tempo);
            source.setRotate(rotateTarget);
            target.setRotate(rotateSource);

            event.setDropCompleted(true);
        });

        imageView.setOnDragDone(event -> {
            verif();
        });


        imageView.setOnMouseClicked(event1 -> {
            imageView.setRotate(imageView.getRotate()+90);
            verif();
        });

    }

    public void shuffle(){
        for (int i=0; i<imagesTrue.size(); i++)
            images.set(i, imagesTrue.get(i));

        Collections.shuffle(images);

        for (int i=0; i<images.size(); i++) {
            list.get(i).setImage(images.get(i));
            list.get(i).setRotate((int)(Math.random() * 4) * 90);
        }
    }

    public void verif(){
        boolean done=true;

        for (int i=0; i<list.size(); i++){
            System.out.println(list.get(i).getImage().equals(imagesTrue.get(i)));
            if(!list.get(i).getImage().equals(imagesTrue.get(i)) ||
                    list.get(i).getRotate()%360!=0)
                done=false;
        }

        if (done){
            Alert alerte = new Alert(Alert.AlertType.CONFIRMATION);
            alerte.setTitle("Partie terminée");
            alerte.setHeaderText("Vous avez réussi! Félicitations!");
            alerte.setContentText("Voulez-vous rejouer?");

            ButtonType rejouer = new ButtonType("Rejouer", ButtonBar.ButtonData.OK_DONE);
            ButtonType annuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            alerte.getButtonTypes().clear();
            alerte.getButtonTypes().add(rejouer);
            alerte.getButtonTypes().add(annuler);

            ButtonType resultat = alerte.showAndWait().get();
            if(resultat == rejouer)
                shuffle();
        }
    }

    public void change(Image[] tableau){
        imagesTrue.clear();
        images.clear();

        for (int i = 0; i<tableau.length; i++) {
            imagesTrue.add(tableau[i]);
            images.add(tableau[i]);
        }

        shuffle();
    }
}