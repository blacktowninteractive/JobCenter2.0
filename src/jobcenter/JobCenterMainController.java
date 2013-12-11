/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobcenter;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author angelacaicedo
 */
public class JobCenterMainController implements Initializable, ScreenController {

    //database connection info
    private static String url = "jdbc:mysql://localhost/jobcenter";
    private static String userdb = "vangfc";//Username of database  
    private static String passdb = "password";//Password of database
    Statement st = null;
    ResultSet rs = null;
    private static Connection conn;
    private ScreenPane myScreenPane;
    public ListView adminList, taskList, proList, employeeSelect, employeeSelected,
            vehicleEquipSelect, vehicleEquipSelected, custListing;
    public Pane CreateJobBox, settingsPane, displayJobs, equipVehPane, employeePane, managerPane,
            usersPane, proposalsPane;
    public ToolBar AdminToolBar, FunctionsToolBar, ReportsToolBar, employeeToolbar;
    public TableView usersTable;
    public TableView<employee> employeeTable = new TableView<employee>();
    public TableColumn emp_fname, emp_lname, emp_phone, emp_email;
    public Button chgPasswd, addEmp, addVehBut, deleteVehBut, clearJob,
            saveJob, confirmJob, cancelJob, addCustBut;
    public ComboBox screenList;
    ObservableList<String> admin = FXCollections.observableArrayList(
            "Manager status", "People", "Vehicles", "Create/Delete a JobCenter User", "Settings");
    //ObservableList<String> functions = FXCollections.observableArrayList(
    //      "Show job board", "Summary report");
    ObservableList<String> tasks = FXCollections.observableArrayList(
            "Create new job", "Display jobs");
    ObservableList<String> proposals = FXCollections.observableArrayList(
            "New proposal");
    List<String> list = new ArrayList<String>();
    ObservableList<String> options = FXCollections.observableList(list);

    List<String> empListSel = new ArrayList<String>();
    List<String> vehList = new ArrayList<String>();
    List<String> custList = new ArrayList<String>();
    List<String> jobTypePicked = new ArrayList<String>();

    ObservableList<String> vehList11 = FXCollections.observableArrayList(vehList);
    ObservableList<String> empSelect = FXCollections.observableArrayList(empListSel);
    ObservableList<String> custListingObs = FXCollections.observableArrayList(custList);

    TextField jTitleField = new TextField(),
            jCustField = new TextField(),
            jNameField = new TextField(),
            jStartField = new TextField();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Connect to database
        databaseConnect();
    }

    public void databaseConnect() {
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);
            st = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setScreenPane(ScreenPane screenPage) {
        //*****************************
        //****************MENU OPTIONS
        //******************************

        //loads the 'Administration' list in the menu option
        adminList.setItems(admin);
        adminList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov,
                            String old_val, String new_val) {
                        //clear pane first
                        clearPane();

                        if (new_val == "Manager status") {
                            managerPane.setVisible(true);
                        }
                        if (new_val == "People") {
                            employeePane.setVisible(true);
                        }
                        if (new_val == "Vehicles") {
                            equipVehPane.setVisible(true);
                        }
                        if (new_val == "Settings") {
                            settingsPane.setVisible(true);
                        }
                        if (new_val == "Create/Delete a JobCenter User") {
                            usersPane.setVisible(true);
                        }

                    }
                });

        //loads the 'Tasks' list in the menu option
        taskList.setItems(tasks);
        taskList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov,
                            String old_val, String new_val) {
                        clearPane();
                        if (new_val == "Create new job") {
                            CreateJobBox.setVisible(true);
                        }
                        if (new_val == "Display jobs") {
                            displayJobs.setVisible(true);
                        }

                    }
                });

        proList.setItems(proposals);
        proList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov,
                            String old_val, String new_val) {
                        clearPane();
                        if (new_val == "New proposal") {
                            proposalsPane.setVisible(true);        
                        }  
                    }
                });

    }

    public void clearPane() {
        //clears the main task area
        CreateJobBox.setVisible(false);
        displayJobs.setVisible(false);
        equipVehPane.setVisible(false);
        employeePane.setVisible(false);
        managerPane.setVisible(false);
        usersPane.setVisible(false);
        settingsPane.setVisible(false);
        usersPane.setVisible(false);
        proposalsPane.setVisible(false);
    }

}
