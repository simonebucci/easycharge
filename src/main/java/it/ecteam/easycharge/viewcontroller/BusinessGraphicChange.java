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

public class BusinessGraphicChange extends GraphicChangeTemplate{
    private static BusinessGraphicChange myInstance=null;
    protected static final Logger logger = Logger.getLogger("gc");

    private BusinessGraphicChange() {
        whoAmI = Roles.BUSINESS;
    }

    public static BusinessGraphicChange getInstance(){
        if(myInstance==null) {
            myInstance=new BusinessGraphicChange();
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
    public void toLoggedHome(Stage stage) {
        changeScene("business-view.fxml", stage);
    }

    public void toUser(Stage stage){
        changeScene("business-settings-view.fxml", stage);
    }

    public void toMyBusiness(Stage stage) {
        changeScene("mybusiness-view.fxml", stage);
    }

}
