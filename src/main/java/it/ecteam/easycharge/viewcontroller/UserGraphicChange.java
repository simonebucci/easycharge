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

    @Override
    public void toLoggedHome(Stage stage){
        this.catcher(new GraphicChangeAction() {
            @Override
            public void act() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("logged-view.fxml"));
                MainViewController mvc = new MainViewController();
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                mvc.init();
            }
        });
    }
    public void toUser(Stage stage){
        this.catcher(new GraphicChangeAction() {
            @Override
            public void act() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("settings-view.fxml"));
                SettingsViewController svc = new SettingsViewController();
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                svc.init();
            }
        });
    }

    public void toRoute(Stage stage){
        this.catcher(new GraphicChangeAction() {
            @Override
            public void act() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("route-logged-view.fxml"));
                RouteViewController rvc = new RouteViewController();
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                rvc.init();
            }
        });
    }
}
