/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobcenter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
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
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
    public static String url = "jdbc:mysql://localhost/jobcenter";
    public static String userdb = "vangfc";//Username of database  
    public static String passdb = "password";//Password of database
    Statement st = null;
    ResultSet rs = null;
    public static Connection conn;
    public ScreenPane myScreenPane;

    public RadioButton prodChk, hourChk;

    public ListView adminList, taskList, proList, employeeSelect, employeeSelected,
            vehicleEquipSelect, vehicleEquipSelected, custListing, empAddJobView, vehAddJobView,
            taskTypeList;
    public Pane CreateJobBox, settingsPane, displayJobs, equipVehPane, employeePane, managerPane,
            usersPane, proposalsPane;
    public ToolBar AdminToolBar, FunctionsToolBar, ReportsToolBar, employeeToolbar;
    public TableView usersTable;
    public static ObservableList<String> jStatus = FXCollections.observableArrayList(
            "IN PROGRESS", "COMPLETE", "HOLD-CUSTOMER", "HOLD-WEATHER", "HOLD-OTHER", "PROJECTED", "CANCELLED");

    public static TableView<employee> employeeTable = new TableView<employee>();
    public static TableView<equipment> equipmentTable = new TableView<equipment>();
    public static TableColumn emp_fname, emp_lname, emp_phone, emp_email,
            vehNameIns, typeIns, statusIns;

    public Button chgPasswd, addEmp, addVehBut, deleteVehBut, clearJob,
            saveJob, confirmJob, cancelJob, addCustBut, addVehEqToTreeBut, addEmpToTreeBut,
            displayJobBut, deleteEquipBut, addEquipBut, addTask, deleteEmpBut;

    public TextField jobTitle, jobName, custJobNum, custJobName, startDate, startTime,
            diamStr, feetStr, fNameStrIns, lNameStrIns, phoneStrIns, emailStrIns,
            vehNameNew, typeNew, statusNew, streetAddr, city, state, zip;

    public static String jobTitleStr, jobNameStr, custJobNumStr, custJobNameStr, startDateStr, startTimeStr,
            streetAddrStr, cityStr, stateStr, zipStr, custAdd, phone, fax, pocName, pocPhone, status, custUniqueID,
            billing, cid, jobtypecompiled, empCompiled, equipCompiled, sI, dI, tI, wI;

    public Text setCustPhone, setCustName, setCustCity, setCustState, setCustPOC, setCustCompPhone,
            setCustFax, setCustAddr, setCustZip;

    public ComboBox screenList, taskComboBox, jobStatus;
    ObservableList<String> admin = FXCollections.observableArrayList(
            "Manager status", "People", "Vehicles", "Create/Delete a JobCenter User", "Settings");
    //ObservableList<String> functions = FXCollections.observableArrayList(
    //      "Show job board", "Summary report");
    ObservableList<String> tasks = FXCollections.observableArrayList(
            "Create new job", "Display jobs");
    ObservableList<String> proposals = FXCollections.observableArrayList(
            "New proposal", "View Current Proposals");
    public static List<String> list = new ArrayList<String>();
    public static ObservableList<String> options = FXCollections.observableList(list),
            taskListBox = FXCollections.observableList(list),
            taskTypeListStr = FXCollections.observableList(list);
    public static List<String> empListSel = new ArrayList<String>(),
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

    public TextArea sInstr, tInstr, dInstr, wInstr;

    public int empID;

    TreeItem<String> root559;
    @FXML
    TreeView<String> currentJobsDisplay;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Note** If you want to manipulate objects within the FXML loaded you need to 
        // initialize them here to obtain a pointer in memory for dynamic changes.

        /*  emp_fname.setCellValueFactory(new PropertyValueFactory<employee, String>("firstName"));
         emp_lname.setCellValueFactory(new PropertyValueFactory<employee, String>("lastName"));
         emp_email.setCellValueFactory(new PropertyValueFactory<employee, String>("email"));
         emp_phone.setCellValueFactory(new PropertyValueFactory<employee, String>("phone"));

         employeeTable.setItems(populateDB());
        
         /*  vehNameIns.setCellValueFactory(new PropertyValueFactory<equipment, String>("veh"));
         typeIns.setCellValueFactory(new PropertyValueFactory<equipment, String>("type"));
         statusIns.setCellValueFactory(new PropertyValueFactory<equipment, String>("stat"));

         //display data in table
         equipmentTable.setItems(populateEquip());*/
        prodChk.setSelected(false);
        hourChk.setSelected(false);

        sInstr.setText("");
        dInstr.setText("");
        tInstr.setText("");
        wInstr.setText("");

        diamStr.setText("");
        feetStr.setText("");

        setCustPhone.setText("");
        setCustName.setText("");
        setCustCity.setText("");
        setCustState.setText("");
        setCustPOC.setText("");
        setCustCompPhone.setText("");
        setCustFax.setText("");
        setCustAddr.setText("");
        setCustZip.setText("");
        streetAddr.setText("");
        city.setText("");
        state.setText("");
        zip.setText("");
        status = "";
        
       /* try {
            empID = getEmpId();
        } catch (UnknownHostException ex) {
            Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        //Connect to database
        databaseConnect();
    }

    public int getEmpId() throws UnknownHostException {

        String myIp = getMyIp(),
                qryRun = "select employees_uid from session where ipAddr = " + myIp,
                uidRet="";
        
        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery(qryRun);

            while (rs.next()) {
                uidRet = rs.getString(1); 
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(Integer.parseInt(uidRet)>=0)
            return Integer.parseInt(uidRet);
        else
            return -1;
    }

    private String getMyIp() throws UnknownHostException {
        //unique identifier for different computers
        InetAddress IP = InetAddress.getLocalHost();
        String ipAddr = IP.getHostAddress();
        return ipAddr;
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

    public ObservableList<equipment> populateEquip() {
        ObservableList<equipment> tester2 = FXCollections.observableArrayList();

        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select VehicleName, VehicleType, VehicleStatus from vehicles;");

            while (rs.next()) {
                tester2.add(new equipment(rs.getString(1), rs.getString(2), rs.getString(3)));
                //System.out.println(rs.getString(1));
                //System.out.println(rs.getString(2));
                //System.out.println(rs.getString(3));

            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tester2;
    }

    public ObservableList<employee> populateDB() {
        ObservableList<employee> tester2 = FXCollections.observableArrayList();

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);
            st = conn.createStatement();
            rs = st.executeQuery("select fname, lname, phone, email from employees;");

            while (rs.next()) {
                tester2.add(new employee(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(3)));
                //System.out.println(rs.getString(1));
                //System.out.println(rs.getString(2));
                //System.out.println(rs.getString(3));
                //System.out.println(rs.getString(4));

            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tester2;
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
                            taskListBox = FXCollections.observableList(new ArrayList<String>());

                            //make the connection
                            try {
                                st = conn.createStatement();
                                rs = st.executeQuery("select jobName from jobtype;");
                                while (rs.next()) {
                                    //System.out.println(rs.getString(1));
                                    taskListBox.add(rs.getString(1));
                                }

                            } catch (SQLException ex) {
                                Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            ObservableList<String> emp = FXCollections.observableArrayList(empList);
                            ObservableList<String> vehEquip = FXCollections.observableArrayList(vehList);
                            ObservableList<String> custListingObs = FXCollections.observableArrayList(custList);

                            //set items on the job form
                            employeeSelect.setItems(emp);
                            vehicleEquipSelect.setItems(vehEquip);
                            custListing.setItems(custListingObs);

                            CreateJobBox.setVisible(true);
                            taskComboBox.setItems(taskListBox);
                            CreateJobBox.setVisible(true);
                            jobStatus.setItems(jStatus);
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

    @FXML
    private void deleteEquipAction(ActionEvent event) throws IOException, SQLException {
        String vnamestr = equipmentTable.getSelectionModel().selectedItemProperty().getValue().getVeh();
        String queryDelete = "DELETE FROM vehicles WHERE VehicleName = '" + vnamestr + "'";
        //System.out.println(queryDelete);

        //insert into database
        Statement updateDb = null;

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user.
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryDelete);
            equipmentTable.setItems(populateEquip());

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addEquipAction(ActionEvent event) throws IOException, SQLException {

        String queryRunNow = "insert into vehicles (VehicleName, VehicleType,VehicleStatus) "
                + " values('" + vehNameNew.getText() + "','" + typeNew.getText() + "','"
                + statusNew.getText() + "');";

        //insert into database
        Statement updateDb = null;
        //System.out.println("Add EQUIP: " + queryRunNow);

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryRunNow);
            equipmentTable.setItems(populateEquip());

            //show the complete box dialog
            Label label2;
            label2 = new Label("Equipment Added");
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

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addTaskList(ActionEvent event) {
        String itemChosen = taskComboBox.getValue().toString();
        itemChosen += "," + diamStr.getText() + ",";
        itemChosen += feetStr.getText();

        //System.out.println(itemChosen);
        taskTypeListStr.add(itemChosen);
        taskTypeList.setItems(taskTypeListStr);

    }

    @FXML
    private void addEmpJob(ActionEvent event) throws SQLException {
        String val = employeeSelect.getSelectionModel().selectedItemProperty().getValue().toString();
        //System.out.println(val);

        if (!empListSel.contains(val)) {
            empListSel.add(val);
        }
        empSelect = FXCollections.observableArrayList(empListSel);
        employeeSelected.setItems(empSelect);
    }

    @FXML
    private void deleteEmp(ActionEvent event) throws SQLException {
        String del = employeeSelected.getSelectionModel().selectedItemProperty().getValue().toString();
        //System.out.println("delete: " + del);
        for (int i = 0; i < empListSel.size(); i++) {
            if (empListSel.get(i).toString().equals(del)) {
                empListSel.remove(i);
            }
        }
        empSelect = FXCollections.observableArrayList(empListSel);
        employeeSelected.setItems(empSelect);

    }

    @FXML
    private void addVehEquip(ActionEvent event) throws SQLException {
        String val = vehicleEquipSelect.getSelectionModel().selectedItemProperty().getValue().toString();
        //System.out.println(val);

        if (!vehList.contains(val)) {
            vehList.add(val);
        }
        vehList11 = FXCollections.observableArrayList(vehList);
        vehicleEquipSelected.setItems(vehList11);
    }

    @FXML
    private void deleteVeh(ActionEvent event) throws SQLException {
        String del = vehicleEquipSelected.getSelectionModel().selectedItemProperty().getValue().toString();
        //System.out.println("delete: " + del);
        for (int i = 0; i < vehList.size(); i++) {
            if (vehList.get(i).toString().equals(del)) {
                vehList.remove(i);
            }
        }
        vehList11 = FXCollections.observableArrayList(vehList);
        vehicleEquipSelected.setItems(vehList11);
    }

    @FXML
    private void addCustButAction(ActionEvent event) throws SQLException {
        custAdd = custListing.getSelectionModel().selectedItemProperty().getValue().toString();

        st = conn.createStatement();
        String qry = "select * from customer where CompanyName ='" + custAdd.trim() + "';";
        //System.out.println("qry: " + qry);

        rs = st.executeQuery(qry);
        while (rs.next()) {
            cid = rs.getString(1);
            streetAddrStr = rs.getString(4);
            cityStr = rs.getString(5);
            stateStr = rs.getString(6);
            zipStr = rs.getString(7);
            phone = rs.getString(8);
            fax = rs.getString(9);
            pocName = rs.getString(14);
            pocPhone = rs.getString(15);
            custList.add(rs.getString(1));
        }

        setCustPhone.setText(phone);
        setCustName.setText(custAdd.trim());
        setCustCity.setText(cityStr);
        setCustState.setText(stateStr);
        setCustPOC.setText(pocPhone);
        setCustCompPhone.setText(phone);
        setCustFax.setText(fax);
        setCustAddr.setText(streetAddrStr);
        setCustZip.setText(zipStr);
    }

    @FXML
    private void delTaskList(ActionEvent event) {
        String del = taskTypeList.getSelectionModel().selectedItemProperty().getValue().toString();
        //System.out.println("delete: " + del);
        for (int i = 0; i < taskTypeListStr.size(); i++) {
            if (taskTypeListStr.get(i).toString().equals(del)) {
                taskTypeListStr.remove(i);
            }
        }
        taskTypeList.setItems(taskTypeListStr);
    }

    @FXML
    private void saveJobDb(ActionEvent event) throws SQLException, IOException {

        jobTitleStr = jobTitle.getText();
        jobNameStr = jobName.getText();
        custJobNumStr = custJobNum.getText();
        custJobNameStr = custJobName.getText();
        startDateStr = startDate.getText();
        startTimeStr = startTime.getText();

        jobtypecompiled = "";
        empCompiled = "";
        equipCompiled = "";

        //compile job types 
        for (int i = 0; i < taskTypeListStr.size(); i++) {
            jobtypecompiled += "/" + taskTypeListStr.get(i);
        }

        //compile employees  
        for (int j = 0; j < empListSel.size(); j++) {
            empCompiled += "/" + empListSel.get(j);
        }

        //compile equipment  
        for (int k = 0; k < vehList.size(); k++) {
            equipCompiled += "/" + vehList.get(k);
        }

        //System.out.println("CID: " + cid);
        //System.out.println("Job name: " + jobNameStr);
        //System.out.println("Cust job #: " + custJobNumStr);
        //System.out.println("Cust job name: " + custJobNameStr);
        //System.out.println("start date: " + startDateStr);
        //System.out.println("start time: " + startTimeStr);
        //System.out.println("street: " + streetAddr.getText());
        //System.out.println("city: " + city.getText());
        //System.out.println("state: " + state.getText());
        //System.out.println("zip: " + zip.getText());
        if (prodChk.isSelected()) {
            billing = "Production Payment";
        }
        if (hourChk.isSelected()) {
            billing = "Hourly Payment";
        }

        sI = sInstr.getText();
        dI = dInstr.getText();
        tI = tInstr.getText();
        wI = wInstr.getText();
        status = jobStatus.getSelectionModel().selectedItemProperty().getValue().toString();

        System.out.println("job type");
        for (int i = 0; i < jobTypePicked.size(); i++) {
            System.out.print(jobTypePicked.get(i));
            System.out.print(",");
        }

        System.out.println("equipment");
        for (int i = 0; i < vehList.size(); i++) {
            System.out.print(vehList.get(i));
            System.out.print(",");
        }

        System.out.println("employees");
        for (int i = 0; i < empListSel.size(); i++) {
            System.out.println(empListSel.get(i));
            System.out.print(",");
        }
        System.out.println("CID: " + cid);
        String qry = "INSERT INTO currentjobs (CurJobID, Customer_CID, "
                + "CustJobNum, CustJobName, JobTitle, JobName, JobWorkDate, "
                + "JobStartTime, JobType, JobEmployees, JobEandV, S_Instr, "
                + "D_Instr, T_Instr, W_Instr, billing,status, jobSiteAddr,"
                + "jobCitySite, jobStateLoc, jobZipLoc,employeeID) "
                + "VALUES (NULL, '" + cid + "', '" + custJobNumStr + "', '" + custJobNameStr + "', '" + jobTitleStr
                + "', '" + jobNameStr + "', '" + startDateStr + "', '" + startTimeStr + "', '" + jobtypecompiled
                + "','" + empCompiled + "', '" + equipCompiled + "', '" + sI + "', '" + dI + "'"
                + ", '" + tI + "', '" + wI + "', '" + billing + "','" + status + "','" + streetAddr + "','"
                + city + "','" + state + "','" + zip + "','" + empID + "');";

        System.out.println("qry: " + qry);

        //delete all entries associated with IP before exiting to the login screen
        Statement updateDb = null;
        updateDb = conn.createStatement();

        //set our session id and ip address in order to identify user.
        int executeUpdate = updateDb.executeUpdate(qry);

        if (executeUpdate > 0) {
            System.out.println("Database updated...");
            //show the complete box dialog
            Label label2;
            label2 = new Label("Job Successfully Added.");
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

    }

    @FXML
    private void clearJobEntries(ActionEvent event) throws SQLException {
        vehList.clear();
        vehList11 = FXCollections.observableArrayList(vehList);
        vehicleEquipSelected.setItems(vehList11);

        empListSel.clear();
        empSelect = FXCollections.observableArrayList(empListSel);
        employeeSelected.setItems(empSelect);

        jobTitle.setText("");
        jobName.setText("");
        custJobNum.setText("");
        custJobName.setText("");
        jobName.setText("");
        custJobNum.setText("");

        streetAddr.setText("");
        city.setText("");
        state.setText("");
        zip.setText("");
        startDate.setText("");
        startTime.setText("");

        setCustPhone.setText("");
        setCustName.setText("");
        setCustCity.setText("");
        setCustState.setText("");
        setCustPOC.setText("");
        setCustCompPhone.setText("");
        setCustFax.setText("");
        setCustAddr.setText("");
        setCustZip.setText("");

        feetStr.setText("");
        diamStr.setText("");

        sInstr.setText("");
        dInstr.setText("");
        tInstr.setText("");
        wInstr.setText("");

        taskTypeListStr.clear();
        taskTypeList.setItems(taskTypeListStr);

    }

}
