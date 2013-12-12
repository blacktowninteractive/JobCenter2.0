/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobcenter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

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
            vehicleEquipSelect, vehicleEquipSelected, custListing, empAddJobView, vehAddJobView;
    public Pane CreateJobBox, settingsPane, displayJobs, equipVehPane, employeePane, managerPane,
            usersPane, proposalsPane;
    public ToolBar AdminToolBar, FunctionsToolBar, ReportsToolBar, employeeToolbar;
    public TableView usersTable;
    public TableView<employee> employeeTable = new TableView<employee>();
    public TableColumn emp_fname, emp_lname, emp_phone, emp_email;
    public Button chgPasswd, addEmp, addVehBut, deleteVehBut, clearJob,
            saveJob, confirmJob, cancelJob, addCustBut, addVehEqToTreeBut, addEmpToTreeBut,
            displayJobBut;
    public ComboBox screenList;
    ObservableList<String> admin = FXCollections.observableArrayList(
            "Manager status", "People", "Vehicles", "Create/Delete a JobCenter User", "Settings");
    //ObservableList<String> functions = FXCollections.observableArrayList(
    //      "Show job board", "Summary report");
    ObservableList<String> tasks = FXCollections.observableArrayList(
            "Create new job", "Display jobs");
    ObservableList<String> proposals = FXCollections.observableArrayList(
            "New proposal", "View Current Proposals");
    List<String> list = new ArrayList<String>();
    ObservableList<String> options = FXCollections.observableList(list);

    List<String> empListSel = new ArrayList<String>(),
            vehList = new ArrayList<String>(),
            custList = new ArrayList<String>(),
            jobTypePicked = new ArrayList<String>(),
            jobList = new ArrayList<String>();

    ObservableList<String> vehList11 = FXCollections.observableArrayList(vehList);
    ObservableList<String> empSelect = FXCollections.observableArrayList(empListSel);
    ObservableList<String> custListingObs = FXCollections.observableArrayList(custList);

    TextField jTitleField = new TextField(),
            jCustField = new TextField(),
            jNameField = new TextField(),
            jStartField = new TextField();

    TreeItem<String> root559;
    @FXML
    TreeView<String> currentJobsDisplay;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Connect to database
        databaseConnect();
    }

    public void refreshList() {
        root559 = new TreeItem<String>("Active Jobs");
        jobList = getJobListTitles();
        root559.setExpanded(true);
        for (int i = 0; i < jobList.size(); i++) {

            //adds each job child node to the treeview
            TreeItem<String> depNode = new TreeItem<String>(jobList.get(i));

            //once job added, grab all cust data needed and add info to that child node
            List<String> jobListInfo = getJobListInfo(jobList.get(i));

            depNode.setExpanded(false);
            for (int j = 0; j < jobListInfo.size(); j++) {
                TreeItem<String> var = new TreeItem<String>(jobListInfo.get(j));
                depNode.getChildren().add(var);
            }

            //add the node and its info into the list
            root559.getChildren().add(depNode);
        }
        currentJobsDisplay.setRoot(root559);

        /*
         currentJobsDisplay.addEventHandler(EventType.ROOT, new EventHandler<Event>() {
         @Override
         public void handle(Event event) {
         //System.out.println("event " + event);
         root559 = new TreeItem<String>("Active Jobs");
         jobList = getJobListTitles();
         root559.setExpanded(true);

         for (int i = 0; i < jobList.size(); i++) {
         TreeItem<String> depNode = new TreeItem<String>(jobList.get(i));

         //grab all cust data needed
         List<String> jobListInfo = getJobListInfo(jobList.get(i));

         depNode.setExpanded(false);
         for (int j = 0; j < jobListInfo.size(); j++) {
         TreeItem<String> var = new TreeItem<String>(jobListInfo.get(j));
         depNode.getChildren().add(var);
         }

         //add the node and its info into the list
         root559.getChildren().add(depNode);
         }
         currentJobsDisplay.setRoot(root559);
         }
         });*/
    }

    //returns the title of all jobs for the root node
    public List<String> getJobListTitles() {
        List<String> jobList = new ArrayList<String>();

        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select JobTitle from currentjobs;");
            while (rs.next()) {
                jobList.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jobList;
    }

    //adds infor to child node of root node in treeview... yeah thats confusing kind of... but think hard its easy!
    public List<String> getJobListInfo(String jobtitle) {
        List<String> jobListInfo = new ArrayList<String>();
        //make the connection
        try {
            st = conn.createStatement();
            String qry = "SELECT (select CompanyName from customer where CID = Customer_CID), status, JobName, JobWorkDate, JobEmployees, JobEandV from currentjobs where JobTitle='" + jobtitle + "'";
            rs = st.executeQuery(qry);

            while (rs.next()) {
                jobListInfo.add(rs.getString(1));
                jobListInfo.add(rs.getString(2));
                jobListInfo.add(rs.getString(3));
                jobListInfo.add(rs.getString(4));

                //employees list, parse it out and add to the list one by one... 
                jobListInfo.add(rs.getString(5));
                jobListInfo.add(rs.getString(6));
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jobListInfo;
    }

    public void databaseConnect() {
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);
            st = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    //returns true or false if an employee exists in a list
    public boolean empExist(String listEmp, String empToAdd) {
        if (listEmp.contains(empToAdd)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setScreenPane(ScreenPane screenPage) {
        //*****************************
        //**************Initialize data
        //*****************************
        final List<String> empList = new ArrayList<String>();
        final List<String> vehList = new ArrayList<String>();
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select fname,lname from employees;");
            while (rs.next()) {
                //System.out.println(rs.getString(1));
                empList.add(rs.getString(1) + " " + rs.getString(2));
            }
            rs = st.executeQuery("select VehicleName from vehicles;");
            while (rs.next()) {
                //System.out.println(rs.getString(1));
                vehList.add(rs.getString(1));
            }
            rs = st.executeQuery("select CompanyName from customer;");
            while (rs.next()) {
                //System.out.println(rs.getString(1));
                custList.add(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

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

                            ObservableList<String> emp2 = FXCollections.observableArrayList(empList);
                            ObservableList<String> vehEquip2 = FXCollections.observableArrayList(vehList);

                            empAddJobView.setItems(emp2);
                            vehAddJobView.setItems(vehEquip2);

                            //adds the treeview here!
                            refreshList();

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

    //*****************************
    //****************FXML Button Actions
    //******************************
    static Stage stageJob;

    @FXML
    private void addVehEqToTree(ActionEvent event) throws IOException, SQLException {
        //get job name selected
        String getJobTitle = currentJobsDisplay.getSelectionModel().selectedItemProperty().getValue().toString();
        getJobTitle = getJobTitle.substring(getJobTitle.indexOf(":") + 1, getJobTitle.indexOf("]"));
        getJobTitle = getJobTitle.trim();

        //get veh/equip name selected
        String vehStrConv = vehAddJobView.getSelectionModel().selectedItemProperty().getValue().toString();

        System.out.println("Root: " + currentJobsDisplay.getSelectionModel().selectedItemProperty().getBean());
        System.out.println("Index: " + currentJobsDisplay.getSelectionModel().getSelectedIndex());
        //System.out.println("RootStr: " + currentJobsDisplay.getChildrenUnmodifiable().get(currentJobsDisplay.getSelectionModel().getSelectedIndex()));
        System.out.println("Adding: " + vehStrConv + " to job: " + getJobTitle);

        //check make sure job exists
        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);
            st = conn.createStatement();
            String qryCurJob = "select CurJobID, JobEandV from currentJobs where JobTitle = '" + getJobTitle + "';";

            rs = st.executeQuery(qryCurJob);
            if (rs.next()) {
                String idMod = rs.getString(1);
                String vehList = rs.getString(2);
                System.out.println("JobID: " + idMod);
                System.out.println("employess: " + vehList);

                if (empExist(vehList, vehStrConv)) {
                    System.out.println("Name Exists!!");
                    //popup explain to user that this selection is invalid
                    //show the complete box dialog
                    Label label2;
                    label2 = new Label("veh/equip already exists.");
                    HBox hb2 = new HBox();
                    Group root = new Group();

                    Button closeWindow = new Button("Close");
                    hb2.getChildren().addAll(label2, closeWindow);
                    hb2.setSpacing(10);
                    hb2.setLayoutX(25);
                    hb2.setLayoutY(48);
                    root.getChildren().add(hb2);

                    final Scene scene2 = new Scene(root);
                    final Stage stage2 = new Stage();

                    stage2.close();
                    stage2.setScene(scene2);
                    stage2.setHeight(150);
                    stage2.setWidth(310);
                    stage2.setResizable(false);
                    stage2.show();

                    closeWindow.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            stage2.close();
                        }
                    });
                } else {
                    System.out.println("Name not found, adding to job!!");
                    vehList += "/" + vehStrConv;

                    String addEmpStrQry = "update currentjobs set "
                            + "JobEandV='" + vehList
                            + "' where CurJobID = " + idMod;

                    Statement updateDb = null;
                    //set our session id and ip address in order to identify user
                    updateDb = conn.createStatement();

                    int executeUpdate = updateDb.executeUpdate(addEmpStrQry);
                    refreshList();
                }
            } else {
                //popup explain to user that this selection is invalid
                //show the complete box dialog
                Label label2;
                label2 = new Label("Invalid Selection");
                HBox hb2 = new HBox();
                Group root = new Group();

                Button closeWindow = new Button("Close");
                hb2.getChildren().addAll(label2, closeWindow);
                hb2.setSpacing(10);
                hb2.setLayoutX(25);
                hb2.setLayoutY(48);
                root.getChildren().add(hb2);

                final Scene scene2 = new Scene(root);
                final Stage stage2 = new Stage();

                stage2.close();
                stage2.setScene(scene2);
                stage2.setHeight(150);
                stage2.setWidth(310);
                stage2.setResizable(false);
                stage2.show();

                closeWindow.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        stage2.close();
                    }
                });

            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //if not exist message to user that what they selected is not a job
        //else add the employee/vehicle in 
        String qryRun = "";
    }

    @FXML
    private void addEmpToTree(ActionEvent event) throws IOException, SQLException {
        //get job name selected
        String getJobTitle = currentJobsDisplay.getSelectionModel().selectedItemProperty().getValue().toString();
        getJobTitle = getJobTitle.substring(getJobTitle.indexOf(":") + 1, getJobTitle.indexOf("]"));
        getJobTitle = getJobTitle.trim();

        //get employee name selected
        String empStrConv = empAddJobView.getSelectionModel().selectedItemProperty().getValue().toString();

        System.out.println("Root: " + currentJobsDisplay.getSelectionModel().selectedItemProperty().getBean());
        System.out.println("Index: " + currentJobsDisplay.getSelectionModel().getSelectedIndex());
        //System.out.println("RootStr: " + currentJobsDisplay.getChildrenUnmodifiable().get(currentJobsDisplay.getSelectionModel().getSelectedIndex()));
        System.out.println("Adding: " + empStrConv + " to job: " + getJobTitle);

        //check make sure job exists
        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);
            st = conn.createStatement();
            String qryCurJob = "select CurJobID, JobEmployees from currentJobs where JobTitle = '" + getJobTitle + "';";

            rs = st.executeQuery(qryCurJob);
            if (rs.next()) {
                String idMod = rs.getString(1);
                String empList = rs.getString(2);
                System.out.println("JobID: " + idMod);
                System.out.println("employess: " + empList);

                if (empExist(empList, empStrConv)) {
                    System.out.println("Name Exists!!");
                    //popup explain to user that this selection is invalid
                    //show the complete box dialog
                    Label label2;
                    label2 = new Label("Employee already exists.");
                    HBox hb2 = new HBox();
                    Group root = new Group();

                    Button closeWindow = new Button("Close");
                    hb2.getChildren().addAll(label2, closeWindow);
                    hb2.setSpacing(10);
                    hb2.setLayoutX(25);
                    hb2.setLayoutY(48);
                    root.getChildren().add(hb2);

                    final Scene scene2 = new Scene(root);
                    final Stage stage2 = new Stage();

                    stage2.close();
                    stage2.setScene(scene2);
                    stage2.setHeight(150);
                    stage2.setWidth(310);
                    stage2.setResizable(false);
                    stage2.show();

                    closeWindow.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            stage2.close();
                        }
                    });
                } else {
                    System.out.println("Name not found, adding to job!!");
                    empList += "/" + empStrConv;

                    String addEmpStrQry = "update currentjobs set "
                            + "JobEmployees='" + empList
                            + "' where CurJobID = " + idMod;

                    Statement updateDb = null;
                    //set our session id and ip address in order to identify user
                    updateDb = conn.createStatement();

                    int executeUpdate = updateDb.executeUpdate(addEmpStrQry);

                    //refresh the treeview after user clicks add employee....
                    refreshList();

                }
            } else {
                //popup explain to user that this selection is invalid
                //show the complete box dialog
                Label label2;
                label2 = new Label("Invalid Selection");
                HBox hb2 = new HBox();
                Group root = new Group();

                Button closeWindow = new Button("Close");
                hb2.getChildren().addAll(label2, closeWindow);
                hb2.setSpacing(10);
                hb2.setLayoutX(25);
                hb2.setLayoutY(48);
                root.getChildren().add(hb2);

                final Scene scene2 = new Scene(root);
                final Stage stage2 = new Stage();

                stage2.close();
                stage2.setScene(scene2);
                stage2.setHeight(150);
                stage2.setWidth(310);
                stage2.setResizable(false);
                stage2.show();

                closeWindow.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        stage2.close();
                    }
                });

            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //if not exist message to user that what they selected is not a job
        //else add the employee/vehicle in 
        String qryRun = "";
    }

    @FXML
    private void displayJobAction(ActionEvent event) throws IOException, SQLException {
        //Parent root = FXMLLoader.load(getClass().getResource("DisplayJobBoard.fxml"));
        conn = DriverManager.getConnection(url, userdb, passdb);
        st = conn.createStatement();
        String qry = "select * from currentjobs where status ='IN PROGRESS';";
        //System.out.println("qry: " + qry);
        String jobTxtStr = "", jobTypeStr = "", jobDateTxtStr = "", jobStatusStr = "", empListStr = "", equipListStr = "";
        int countAmt = 1, area1 = 3, area2 = 4, area3 = 5, area4 = 6;
        Group root = new Group();
        GridPane grid = new GridPane();

        List<String> empListSort = new ArrayList<String>();
        List<String> equipListSort = new ArrayList<String>();

        rs = st.executeQuery(qry);
        while (rs.next()) {
            jobTxtStr = rs.getString(5);
            jobTypeStr = rs.getString(6);
            jobDateTxtStr = rs.getString(7);
            jobStatusStr = rs.getString(17);

            empListStr = rs.getString(10);
            equipListStr = rs.getString(11);

            //System.out.println(jobTxtStr);
            //System.out.println("Count: " + Integer.toString(countAmt));
            ToolBar addme = new ToolBar();
            Button test = new Button("Print");

            addme.getItems().add(test);
            addme.setMinWidth(1255);
            grid.setHgap(10);
            grid.setVgap(3);

            //grid.setBlendMode(BlendMode.DIFFERENCE);
            TextField jobTxt = new TextField(),
                    jobTypeTxt = new TextField(),
                    jobDateTxt = new TextField(),
                    jobStatusBox = new TextField();

            jobTxt.setStyle("-fx-background-color: lightblue;"
                    + "-fx-font-size: 15;");
            jobTxt.setMaxWidth(150);
            jobTxt.setEditable(false);
            jobTxt.setText(jobTxtStr);
            jobTxt.setAlignment(Pos.CENTER);

            jobTypeTxt.setStyle("-fx-background-color: lightblue;"
                    + "-fx-font-size: 15;");
            jobTypeTxt.setMaxWidth(150);
            jobTypeTxt.setEditable(false);
            jobTypeTxt.setText(jobTypeStr);
            jobTypeTxt.setAlignment(Pos.CENTER);

            jobDateTxt.setStyle("-fx-background-color: white;"
                    + "-fx-font-size: 15;");
            jobDateTxt.setMaxWidth(150);
            jobDateTxt.setEditable(false);
            jobDateTxt.setText(jobDateTxtStr);
            jobDateTxt.setAlignment(Pos.CENTER);

            jobStatusBox.setStyle("-fx-background-color: white;"
                    + "-fx-font-size: 15;");
            jobStatusBox.setMaxWidth(150);
            jobStatusBox.setEditable(false);
            jobStatusBox.setText(jobStatusStr);
            jobStatusBox.setAlignment(Pos.CENTER);

            if (countAmt == 10) {
                countAmt = 1;
                area1 += 50;
                area2 += 50;
                area3 += 50;
                area4 += 50;
            }
            if (countAmt == 20) {
                countAmt = 1;
                area1 += 100;
                area2 += 100;
                area3 += 100;
                area4 += 100;
            }
            if (countAmt == 30) {
                countAmt = 1;
                area1 += 30;
                area2 += 30;
                area3 += 30;
                area4 += 30;
            }
            grid.add(jobTxt, countAmt, area1);
            grid.add(jobTypeTxt, countAmt, area2);
            grid.add(jobDateTxt, countAmt, area3);
            grid.add(jobStatusBox, countAmt, area4);

            //System.out.println("Index: " + empListStr.indexOf("/"));
            //System.out.println("at row: " + countAmt);
            String nameOfPerson;
            int counterArea = area4 + 1;

            //displays the employees who are set for the job
            while (true) {

                if (empListStr.indexOf("/") < 0) {
                    break;
                }

                if (empListStr.indexOf("/") >= 0) {
                    //sort out employees for display
                    empListStr = empListStr.substring(0, empListStr.length());

                    //System.out.println("Unprocessed string: " + empListStr);
                    //System.out.println("Before: " + empListStr);
                    //System.out.println(empListStr.indexOf("/"));
                    if (empListStr.indexOf("/") == 0) {
                        empListStr = empListStr.substring(1, empListStr.length());

                        if (empListStr.indexOf("/") > 0) {
                            nameOfPerson = empListStr.substring(0, empListStr.indexOf("/"));
                            empListStr = empListStr.substring(empListStr.indexOf("/"), empListStr.length());
                        } else {
                            nameOfPerson = empListStr.substring(0, empListStr.length());
                        }

                        //empListStr = empListStr.substring(empListStr.indexOf("/") + 1, empListStr.length());
                        //System.out.println("After: " + empListStr);
                        //System.out.println("Adding: " + nameOfPerson);
                        TextField nameTxt = new TextField();
                        nameTxt.setMaxWidth(150);
                        nameTxt.setStyle("-fx-background-color: lightgreen;"
                                + "-fx-font-size: 15;");

                        nameTxt.setMaxHeight(100);
                        nameTxt.setEditable(false);
                        nameTxt.setText(nameOfPerson);
                        nameTxt.setAlignment(Pos.CENTER);

                        grid.add(nameTxt, countAmt, counterArea);
                        //System.out.println("at row: " + countAmt);
                        counterArea++;

                    }
                } else {
                    //System.out.println("Adding2: " + empListStr);
                    nameOfPerson = empListStr;
                    TextField nameTxt = new TextField();
                    nameTxt.setStyle("-fx-background-color: green;"
                            + "-fx-font-size: 15;");
                    nameTxt.setMaxWidth(150);
                    nameTxt.setEditable(false);
                    nameTxt.setText(nameOfPerson);
                    nameTxt.setAlignment(Pos.CENTER);

                    grid.add(nameTxt, countAmt, counterArea);
                    //System.out.println("at row2: " + countAmt);
                    empListStr = "";
                    counterArea++;

                    break;
                }

            }
            String equipNameStr;
            //displays the equipment set for the job
            while (true) {
                if (equipListStr.indexOf("/") >= 0) {
                    //sort out employees for display
                    equipListStr = equipListStr.substring(1, equipListStr.length() - 1);
                    //System.out.println("\n\nUnprocessed string: " + equipListStr);
                    equipNameStr = "";
                    //System.out.println("Before: " + equipListStr);
                    //System.out.println(equipListStr.indexOf("/"));
                    if (equipListStr.indexOf("/") > 0) {
                        equipNameStr = equipListStr.substring(0, equipListStr.indexOf("/"));
                        equipListStr = equipListStr.substring(equipListStr.indexOf("/"), equipListStr.length());
                        //System.out.println("After: " + equipListStr);

                        //System.out.println("Adding: " + equipNameStr);
                        TextField nameTxt = new TextField();

                        nameTxt.setStyle("-fx-background-color: yellow;"
                                + "-fx-font-size: 15;");
                        nameTxt.setMaxWidth(150);
                        nameTxt.setMaxHeight(70);
                        nameTxt.setEditable(false);
                        nameTxt.setText(equipNameStr);
                        nameTxt.setAlignment(Pos.CENTER);

                        grid.add(nameTxt, countAmt, counterArea);
                        //System.out.println("at row: " + countAmt);
                        counterArea++;

                    }
                } else {
                    //System.out.println("Adding2: " + equipListStr);
                    equipNameStr = equipListStr;
                    TextField nameTxt = new TextField();

                    nameTxt.setStyle("-fx-background-color: yellow;"
                            + "-fx-font-size: 15;");
                    nameTxt.setMaxWidth(150);
                    nameTxt.setEditable(false);
                    nameTxt.setText(equipNameStr);
                    nameTxt.setAlignment(Pos.CENTER);

                    grid.add(nameTxt, countAmt, counterArea);
                    //System.out.println("at row2: " + countAmt);
                    empListStr = "";
                    counterArea++;

                    break;
                }

            }

            countAmt++;
        }

        root.getChildren().add(grid);

        Scene scene2 = new Scene(root, Color.BLACK);
        stageJob = new Stage();
        stageJob.setHeight(662);
        stageJob.setWidth(1224);
        stageJob.setResizable(false);
        stageJob.setFullScreen(true);

        stageJob.setScene(scene2);
        stageJob.setResizable(false);
        javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getScreens().get(1).getVisualBounds();
        stageJob.setX(primaryScreenBounds.getMinX());
        stageJob.setY(primaryScreenBounds.getMinY());

        stageJob.show();

    }

    
}
