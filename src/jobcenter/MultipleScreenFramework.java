/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobcenter;

import java.awt.geom.Rectangle2D;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author angelacaicedo
 */
public class MultipleScreenFramework extends Application {

    public static String loginFXML = "JobCenter.fxml";
    public static String mainFXML = "JobCenterMain.fxml";
    
    @Override
    public void start(Stage stage) throws Exception {
        ScreenPane mainContainer = new ScreenPane();
        mainContainer.loadScreen("login", MultipleScreenFramework.loginFXML);
        mainContainer.loadScreen("mainScreen", MultipleScreenFramework.mainFXML);
        mainContainer.setScreen("login");

        Scene scene = new Scene(mainContainer);


        //xx.getChildren().remove(test);       
        
      


        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
