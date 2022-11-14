package it.ecteam.easycharge;

import it.ecteam.easycharge.dao.UserDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        stage.getIcons().add(new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/it/ecteam/easycharge/images/icon.png"))));
        stage.setTitle("EasyCharge");
        stage.setMinWidth(1080);
        stage.setMinHeight(720);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}