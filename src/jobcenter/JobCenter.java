package jobcenter;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author vang
 */
public class JobCenter extends Application {

    public static String loginFXML = "JobCenter.fxml";
    public static String mainFXML = "JobCenterMain.fxml";
    public ListView adminList;
    
     @Override
    public void start(Stage stage) throws Exception {
        int screenOption = 0;
        
        ScreenPane mainContainer = new ScreenPane();
        mainContainer.loadScreen("login", MultipleScreenFramework.loginFXML);
        mainContainer.loadScreen("mainScreen", MultipleScreenFramework.mainFXML);          
        mainContainer.setScreen("mainScreen");        
        
        Scene scene = new Scene(mainContainer); 
        stage.setScene(scene); 
        stage.setResizable(false);

        //set which screen the application will show up on.
        javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getScreens().get(screenOption).getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());  
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