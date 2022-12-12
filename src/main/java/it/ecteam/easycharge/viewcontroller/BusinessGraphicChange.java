package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.utils.Roles;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BusinessGraphicChange extends GraphicChangeTemplate{
    private static BusinessGraphicChange myInstance=null;

    private BusinessGraphicChange() {
        whoAmI = Roles.BUSINESS;
    }

    public static BusinessGraphicChange getInstance(){
        if(myInstance==null) {
            myInstance=new BusinessGraphicChange();
        }
        return myInstance;
    }

    @Override
    public void toLoggedHome(Stage stage) {
        this.catcher(new GraphicChangeAction() {
            @Override
            public void act() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("business-view.fxml"));
                BusinessViewController bvc = new BusinessViewController();
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                bvc.init();
            }
        });
    }

    public void toUser(Stage stage){
        this.catcher(new GraphicChangeAction() {
            @Override
            public void act() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("bsettings-view.fxml"));
                SettingsViewController svc = new SettingsViewController();
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                svc.init();
            }
        });
    }
}
