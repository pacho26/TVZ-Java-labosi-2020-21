package main.java.hr.java.covidportal.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage mainStage;

    /**
     * Služi za pokretanje početne scene aplikacije
     *
     * @param primaryStage početna scena iz koje krećemo koristiti aplikaciju.
     * @throws Exception baca se u slučaju ako je krivo napisan naziv fxml-datoteke ili ako datoteka ne postoji.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root =
                FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        primaryStage.setTitle("Covid portal");
        primaryStage.setScene(new Scene(root, 600, 400));
        root.getStylesheets().add(getClass().getClassLoader().getResource("css/pocetniEkran.css").toExternalForm());
        Image windowIcon = new Image("/icons/ps.jpg");
        primaryStage.getIcons().add(windowIcon);
        primaryStage.show();
        mainStage = primaryStage;
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}