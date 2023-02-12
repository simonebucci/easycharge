package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.utils.Roles;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GraphicChangeTemplate {
    protected final Logger logger = Logger.getLogger("GraphicChange");

    protected Roles whoAmI;

    public void catcher(GraphicChangeAction gca) {
        try {
            gca.act();
        } catch (IOException ie) {
            logger.log(Level.WARNING, ie.toString());
        }
    }


    public void toLogin(Stage stage){
        this.catcher(new GraphicChangeAction() {
            @Override
            public void act() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
            }
        });
    }

    public void toRegister(Stage stage){
        this.catcher(new GraphicChangeAction() {
            @Override
            public void act() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("register-view.fxml"));
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
            }
        });
    }

    public void toHome(Stage stage){
        this.catcher(new GraphicChangeAction() {
            @Override
            public void act() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
            }
        });
    }

    public void toAuth(Stage stage){
        this.catcher(new GraphicChangeAction() {
            @Override
            public void act() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("auth-view.fxml"));
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
            }
        });
    }

    public abstract void toLoggedHome(Stage stage);
}
