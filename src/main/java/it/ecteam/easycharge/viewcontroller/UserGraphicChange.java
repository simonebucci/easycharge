package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.utils.Roles;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserGraphicChange extends GraphicChangeTemplate {
    private static UserGraphicChange myInstance=null;
    protected static final Logger logger = Logger.getLogger("gc");

    private UserGraphicChange() {
        whoAmI = Roles.USER;
    }

    public static UserGraphicChange getInstance(){
        if(myInstance==null) {
            myInstance=new UserGraphicChange();
        }
        return myInstance;
    }

    private void changeScene(String fxmlFile, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
            Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
            stage.setScene(scene);
        } catch (IOException e) {
            logger.log(Level.WARNING, (Supplier<String>) e);
        }
    }

    @Override
    public void toLoggedHome(Stage stage){
        changeScene("logged-view.fxml", stage);
    }

    public void toUser(Stage stage){
        changeScene("settings-view.fxml", stage);
    }

    public void toRoute(Stage stage){
        changeScene("route-logged-view.fxml", stage);
    }

}
