package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.utils.Roles;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserGraphicChange extends GraphicChangeTemplate {
    private static UserGraphicChange myInstance=null;

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
            e.printStackTrace();
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
