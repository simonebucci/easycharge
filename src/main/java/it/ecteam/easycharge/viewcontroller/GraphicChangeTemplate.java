package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.utils.Roles;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public  abstract class GraphicChangeTemplate {
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
                LoginViewController lvc = new LoginViewController();
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                lvc.init();
            }
        });
    }

    public void toRegister(Stage stage){
        this.catcher(new GraphicChangeAction() {
            @Override
            public void act() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("register-view.fxml"));
                RegisterViewController lvc = new RegisterViewController();
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                lvc.init();
            }
        });
    }

    public void toHome(Stage stage){
        this.catcher(new GraphicChangeAction() {
            @Override
            public void act() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
                MainViewController mvc = new MainViewController();
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                mvc.init();
            }
        });
    }

    public void toAuth(Stage stage){
        this.catcher(new GraphicChangeAction() {
            @Override
            public void act() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("auth-view.fxml"));
                RouteViewController rvc = new RouteViewController();
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                rvc.init();
            }
        });
    }

    public abstract void toLoggedHome(Stage stage);
}
