/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobcenter;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

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
    public ToolBar editJobToolbar, createJobToolbar;

    public RadioButton prodChk, hourChk;

    public ListView adminList, taskList, proList, employeeSelect, employeeSelected,
            vehicleEquipSelect, vehicleEquipSelected, custListing, empAddJobView, vehAddJobView,
            taskTypeList;
    public Pane CreateJobBox, settingsPane, displayJobs, equipVehPane, employeePane, managerPane,
            usersPane, proposalsPane;
    public ToolBar AdminToolBar, FunctionsToolBar, ReportsToolBar, employeeToolbar;

    public static ObservableList<String> jStatus = FXCollections.observableArrayList(
            "IN PROGRESS", "COMPLETE", "HOLD-CUSTOMER", "HOLD-WEATHER", "HOLD-OTHER", "PROJECTED", "CANCELLED");
    public TableView usersTable;
    public TableView<manager> managerView = new TableView<manager>();
    public TableView<employee> employeeTable = new TableView<employee>();
    public TableView<equipment> equipmentTable = new TableView<equipment>();

    public TableColumn emp_fname, emp_lname, emp_phone, emp_email,
            vehNameIns, typeIns, statusIns, usrFName, usrLName, usrUName, usrPwd,
            manage_lname, manage_fname, manage_phone, manage_office, manage_email;

    public Button chgPasswd, addEmp, addVehBut, deleteVehBut, clearJob,
            saveJob, confirmJob, cancelJob, addCustBut, addVehEqToTreeBut, addEmpToTreeBut,
            displayJobBut, deleteEquipBut, addEquipBut, addTask, deleteEmpBut,
            saveChangesBut, previewJob, printSummaryBut, addNewUsr,
            addEmployeeBut, deleteManagerBut, addManagerBut;

    public TextField jobTitle, jobName, custJobNum, custJobName, startDate, startTime,
            diamStr, feetStr, fNameStrIns, lNameStrIns, phoneStrIns, emailStrIns,
            vehNameNew, typeNew, statusNew, streetAddr, city, state, zip,
            newUsrName,fname_str,lname_str,phone_str,email_str,office_str;
        
    public PasswordField newPwd;

    public static String jobTitleStr, jobNameStr, custJobNumStr, custJobNameStr, startDateStr, startTimeStr,
            streetAddrStr, cityStr, stateStr, zipStr, custAdd, phone, fax, pocName, pocPhone, status, custUniqueID,
            billing, cid, jobtypecompiled, empCompiled, equipCompiled, sI, dI, tI, wI;

    public Text setCustPhone, setCustName, setCustCity, setCustState, setCustPOC, setCustCompPhone,
            setCustFax, setCustAddr, setCustZip;

    public ComboBox screenList, taskComboBox, jobStatus, empListUsr;
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
            taskTypeListStr = FXCollections.observableList(list),
            empNameBox = FXCollections.observableList(list);
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

    public static String empID;

    TreeItem<String> root559;
    @FXML
    TreeView<String> currentJobsDisplay;
    private String custName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Note** If you want to manipulate objects within the FXML loaded you need to 
        // initialize them here to obtain a pointer in memory for dynamic changes.

        emp_fname.setCellValueFactory(new PropertyValueFactory<employee, String>("firstName"));
        emp_lname.setCellValueFactory(new PropertyValueFactory<employee, String>("lastName"));
        emp_email.setCellValueFactory(new PropertyValueFactory<employee, String>("email"));
        emp_phone.setCellValueFactory(new PropertyValueFactory<employee, String>("phone"));

        employeeTable.setItems(populateDB());

        vehNameIns.setCellValueFactory(new PropertyValueFactory<equipment, String>("veh"));
        typeIns.setCellValueFactory(new PropertyValueFactory<equipment, String>("type"));
        statusIns.setCellValueFactory(new PropertyValueFactory<equipment, String>("stat"));

        //display data in table
        equipmentTable.setItems(populateEquip());

        manage_lname.setCellValueFactory(new PropertyValueFactory<manager, String>("lastName"));
        manage_fname.setCellValueFactory(new PropertyValueFactory<manager, String>("firstName"));
        manage_phone.setCellValueFactory(new PropertyValueFactory<manager, String>("phone"));
        manage_office.setCellValueFactory(new PropertyValueFactory<manager, String>("office"));
        manage_email.setCellValueFactory(new PropertyValueFactory<manager, String>("email"));

        managerView.setItems(populateManagers());
        
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

        createJobToolbar.setVisible(false);
        editJobToolbar.setVisible(false);

        //Connect to database
        databaseConnect();
    }

    // a local accessible method
    private void clearJobEntriesNow() {
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
        createJobToolbar.setVisible(false);

        CreateJobBox.setVisible(true);
        editJobToolbar.setVisible(true);
    }

    public String getEmpId() throws UnknownHostException {

        String myIp = getMyIp(),
                qryRun = "select employees_uid from session where ipAddr = '" + myIp + "'",
                uidRet = "";

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
        return uidRet;
    }

    private String getMD5(String val) throws NoSuchAlgorithmException {
        //for secure password
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(val.getBytes());

        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtextpw = bigInt.toString(16);

        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtextpw.length() < 32) {
            hashtextpw = "0" + hashtextpw;
        }
        return hashtextpw;

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

    public ObservableList<manager> populateManagers() {
        ObservableList<manager> tester55 = FXCollections.observableArrayList();

        //make the connection
        try { 
            st = conn.createStatement();
            rs = st.executeQuery("select fName, lName, email, phone, office from manager;");

            while (rs.next()) {
                tester55.add(new manager(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5)));
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));
                System.out.println(rs.getString(5));

            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tester55;
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

    public void displayMsg(String msgTxt) {
        //show the complete box dialog
        Label label2;
        label2 = new Label(msgTxt);
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
                            //make the connection
                            try {
                                st = conn.createStatement();
                                rs = st.executeQuery("select fname, lname, uid from employees;");
                                while (rs.next()) {
                                    //System.out.println(rs.getString(1));
                                    empNameBox.add(rs.getString(2) + ", " + rs.getString(1) + ", " + rs.getString(3));
                                }

                            } catch (SQLException ex) {
                                Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            empListUsr.setItems(empNameBox);
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
                            createJobToolbar.setVisible(true);
                            editJobToolbar.setVisible(false);

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
    private void printSummary(ActionEvent event) throws SQLException {
        System.out.println("tester");
        //Node node = new GridPane();

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
                    + "-fx-font-size: 12;");
            jobTxt.setMaxWidth(100);
            jobTxt.setEditable(false);
            jobTxt.setText(jobTxtStr);
            jobTxt.setAlignment(Pos.CENTER);

            jobTypeTxt.setStyle("-fx-background-color: lightblue;"
                    + "-fx-font-size: 12;");
            jobTypeTxt.setMaxWidth(100);
            jobTypeTxt.setEditable(false);
            jobTypeTxt.setText(jobTypeStr);
            jobTypeTxt.setAlignment(Pos.CENTER);

            jobDateTxt.setStyle("-fx-background-color: white;"
                    + "-fx-font-size: 12;");
            jobDateTxt.setMaxWidth(100);
            jobDateTxt.setEditable(false);
            jobDateTxt.setText(jobDateTxtStr);
            jobDateTxt.setAlignment(Pos.CENTER);

            jobStatusBox.setStyle("-fx-background-color: white;"
                    + "-fx-font-size: 12;");
            jobStatusBox.setMaxWidth(100);
            jobStatusBox.setEditable(false);
            jobStatusBox.setText(jobStatusStr);
            jobStatusBox.setAlignment(Pos.CENTER);

            if (countAmt == 10) {
                countAmt = 1;
                area1 += 30;
                area2 += 30;
                area3 += 30;
                area4 += 30;
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
                        nameTxt.setMaxWidth(100);
                        nameTxt.setStyle("-fx-background-color: lightgreen;"
                                + "-fx-font-size: 12;");

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
                            + "-fx-font-size: 12;");
                    nameTxt.setMaxWidth(100);
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
                                + "-fx-font-size: 12;");
                        nameTxt.setMaxWidth(100);
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
                            + "-fx-font-size: 12;");
                    nameTxt.setMaxWidth(100);
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

        //bounds in root node
        //stageJob.setHeight(662);
        //stageJob.setWidth(1224);
        //print stuffs
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);

        double scaleX = pageLayout.getPrintableWidth() / 1224;

        double scaleY = pageLayout.getPrintableHeight() / 662;

        root.getTransforms().add(new Scale(scaleX, scaleY));

        /*
         PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);

         double scaleX = pageLayout.getPrintableWidth() / root.getBoundsInParent().getWidth();

         double scaleY = pageLayout.getPrintableHeight() / root.getBoundsInParent().getHeight();

         root.getTransforms().add(new Scale(scaleX, scaleY));
         */
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null) {

            boolean success = job.printPage(root);

            if (success) {

                job.endJob();

            }

        }

        /*PrinterJob job = PrinterJob.createPrinterJob();
         if (job != null) {
         boolean success = job.printPage(node);
         if (success) {
         job.endJob();
         }
         }*/
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
    private void previewJobAction(ActionEvent event) throws SQLException {
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
                    + "-fx-font-size: 12;");
            jobTxt.setMaxWidth(100);
            jobTxt.setEditable(false);
            jobTxt.setText(jobTxtStr);
            jobTxt.setAlignment(Pos.CENTER);

            jobTypeTxt.setStyle("-fx-background-color: lightblue;"
                    + "-fx-font-size: 12;");
            jobTypeTxt.setMaxWidth(100);
            jobTypeTxt.setEditable(false);
            jobTypeTxt.setText(jobTypeStr);
            jobTypeTxt.setAlignment(Pos.CENTER);

            jobDateTxt.setStyle("-fx-background-color: white;"
                    + "-fx-font-size: 12;");
            jobDateTxt.setMaxWidth(100);
            jobDateTxt.setEditable(false);
            jobDateTxt.setText(jobDateTxtStr);
            jobDateTxt.setAlignment(Pos.CENTER);

            jobStatusBox.setStyle("-fx-background-color: white;"
                    + "-fx-font-size: 12;");
            jobStatusBox.setMaxWidth(100);
            jobStatusBox.setEditable(false);
            jobStatusBox.setText(jobStatusStr);
            jobStatusBox.setAlignment(Pos.CENTER);

            if (countAmt == 10) {
                countAmt = 1;
                area1 += 30;
                area2 += 30;
                area3 += 30;
                area4 += 30;
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
                        nameTxt.setMaxWidth(100);
                        nameTxt.setStyle("-fx-background-color: lightgreen;"
                                + "-fx-font-size: 12;");

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
                            + "-fx-font-size: 12;");
                    nameTxt.setMaxWidth(100);
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
                                + "-fx-font-size: 12;");
                        nameTxt.setMaxWidth(100);
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
                            + "-fx-font-size: 12;");
                    nameTxt.setMaxWidth(100);
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

        stageJob.setScene(scene2);
        stageJob.setResizable(false);

        // which screen to display the popup on ....
        javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getScreens().get(0).getVisualBounds();
        stageJob.setX(primaryScreenBounds.getMinX());
        stageJob.setY(primaryScreenBounds.getMinY());

        stageJob.show();
    }

    @FXML
    private void displayJobAction(ActionEvent event) throws IOException, SQLException {
        //Parent root = FXMLLoader.load(getClass().getResource("DisplayJobBoard.fxml")); 
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

        final Rectangle rectBasicTimeline = new Rectangle(100, 50, 100, 50);
        rectBasicTimeline.setFill(Color.RED);

        final Text txtTimeline = new Text("ICY CONDITIONS -- CAREFUL OUT THERE!");
        txtTimeline.setFill(Color.RED);
        txtTimeline.setStyle("-fx-font-size: 50;" + "-fx-background-color: yellow;");

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        final KeyValue kv = new KeyValue(rectBasicTimeline.xProperty(), 2000);
        final KeyFrame kf = new KeyFrame(Duration.millis(4500), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        root.getChildren().addAll(rectBasicTimeline, txtTimeline);
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

        try {
            empID = getEmpId();
        } catch (UnknownHostException ex) {
            Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                + ", '" + tI + "', '" + wI + "', '" + billing + "','" + status + "','" + streetAddr.getText() + "','"
                + city.getText() + "','" + state.getText() + "','" + zip.getText() + "'," + empID + ");";

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
    private void editJobAction(ActionEvent event) throws IOException, SQLException {
        String getJobTitle = currentJobsDisplay.getSelectionModel().selectedItemProperty().getValue().toString();
        getJobTitle = getJobTitle.substring(getJobTitle.indexOf(":") + 1, getJobTitle.indexOf("]"));
        getJobTitle = getJobTitle.trim();

        //clear the entries on job form first
        clearJobEntriesNow();

        conn = DriverManager.getConnection(url, userdb, passdb);
        st = conn.createStatement();

        String listOfTasks;
        String empLister, vehLister;

        rs = st.executeQuery("select * from currentjobs where JobTitle = '" + getJobTitle + "';");
        while (rs.next()) {
            jobTitle.setText(rs.getString(5));
            cid = rs.getString(2);
            jobName.setText(rs.getString(6));
            custJobNum.setText(rs.getString(3));
            custJobName.setText(rs.getString(4));
            startDate.setText(rs.getString(7));
            startTime.setText(rs.getString(8));
            status = rs.getString(17);
            listOfTasks = rs.getString(9);

            custUniqueID = rs.getString(1);

            sI = rs.getString(12);
            dI = rs.getString(13);
            tI = rs.getString(14);
            wI = rs.getString(15);

            if (rs.getString(15) == "Production Payment") {
                prodChk.setSelected(true);
            } else {
                hourChk.setSelected(true);
            }

            streetAddr.setText(rs.getString(18));
            city.setText(rs.getString(19));
            state.setText(rs.getString(20));
            zip.setText(rs.getString(21));

            sInstr.setText(rs.getString(12));
            dInstr.setText(rs.getString(13));
            tInstr.setText(rs.getString(14));
            wInstr.setText(rs.getString(15));

            empLister = rs.getString(10);
            vehLister = rs.getString(11);

            taskListBox = FXCollections.observableList(new ArrayList<String>());

            List<String> getJobTypes = new ArrayList<String>();
            //make the connection
            try {
                conn = DriverManager.getConnection(url, userdb, passdb);
                st = conn.createStatement();
                rs = st.executeQuery("select jobName from jobType;");
                while (rs.next()) {
                    //System.out.println(rs.getString(1));
                    getJobTypes.add(rs.getString(1));

                }
                rs = st.executeQuery("select jobName from jobtype;");
                while (rs.next()) {
                    //System.out.println(rs.getString(1));
                    taskListBox.add(rs.getString(1));

                }
            } catch (SQLException ex) {
                Logger.getLogger(JobCenterController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            String tmpStr = "", holder = "", tmpStr2 = "", tmpStr3;

            tmpStr = listOfTasks.substring(1, listOfTasks.length());
            tmpStr2 = empLister.substring(1, empLister.length());
            tmpStr3 = vehLister.substring(1, vehLister.length());

            //3 while statements
            //1. add equipment list
            while (true) {
                if (tmpStr3.indexOf("/") < 0) {
                    vehList.add(tmpStr3.substring(0, tmpStr3.length()));
                    break;
                }
                if (tmpStr3.indexOf("/") == 0) {
                    tmpStr3 = tmpStr3.substring(1, tmpStr3.length());
                    if (tmpStr3.indexOf("/") < 0) {
                        vehList.add(tmpStr3.substring(0, tmpStr3.length()));
                        break;
                    } else {
                        vehList.add(tmpStr3.substring(0, tmpStr3.indexOf("/")));
                        tmpStr3 = tmpStr3.substring(tmpStr3.indexOf("/"), tmpStr3.length());
                    }

                } else {

                    holder = tmpStr3;
                    tmpStr3 = tmpStr3.substring(0, tmpStr3.indexOf("/"));
                    vehList.add(tmpStr3);

                    tmpStr3 = holder;

                    tmpStr3 = tmpStr3.substring(tmpStr3.indexOf("/"), tmpStr3.length());
                }
            }
            vehList11 = FXCollections.observableArrayList(vehList);
            vehicleEquipSelected.setItems(vehList11);

            //2. add employee list
            while (true) {
                if (tmpStr2.indexOf("/") < 0) {
                    empListSel.add(tmpStr2.substring(0, tmpStr2.length()));
                    break;
                }
                if (tmpStr2.indexOf("/") == 0) {
                    tmpStr2 = tmpStr2.substring(1, tmpStr2.length());
                    if (tmpStr2.indexOf("/") < 0) {
                        empListSel.add(tmpStr2.substring(0, tmpStr2.length()));
                        break;
                    } else {
                        empListSel.add(tmpStr2.substring(0, tmpStr2.indexOf("/")));
                        tmpStr2 = tmpStr2.substring(tmpStr2.indexOf("/"), tmpStr2.length());
                    }

                } else {

                    holder = tmpStr2;
                    tmpStr2 = tmpStr2.substring(0, tmpStr2.indexOf("/"));
                    empListSel.add(tmpStr2);

                    tmpStr2 = holder;

                    tmpStr2 = tmpStr2.substring(tmpStr2.indexOf("/"), tmpStr2.length());
                }
            }
            empSelect = FXCollections.observableArrayList(empListSel);
            employeeSelected.setItems(empSelect);

            //3. add task type list
            while (true) {

                if (tmpStr.indexOf("/") < 0) {
                    taskTypeListStr.add(tmpStr.substring(0, tmpStr.length()));
                    break;
                }

                if (tmpStr.indexOf("/") == 0) {
                    taskTypeListStr.add(tmpStr.substring(1, tmpStr.length()));
                    break;
                }

                holder = tmpStr;
                tmpStr = tmpStr.substring(0, tmpStr.indexOf("/"));
                taskTypeListStr.add(tmpStr);

                tmpStr = holder;

                tmpStr = tmpStr.substring(tmpStr.indexOf("/"), tmpStr.length());

            }

            taskTypeList.setItems(taskTypeListStr);

            taskComboBox.setItems(taskListBox);
            taskComboBox.setValue(st);
            jobStatus.setItems(jStatus);
            jobStatus.setValue(status);

            /*
             System.out.println(rs.getString(1));
             System.out.println(rs.getString(2));
             System.out.println(rs.getString(3));
             System.out.println(rs.getString(4));
             System.out.println(rs.getString(5));
             System.out.println(rs.getString(6));
             System.out.println(rs.getString(7));
             System.out.println(rs.getString(8));
             System.out.println(rs.getString(9));
             System.out.println(rs.getString(10));
             System.out.println(rs.getString(11));
             System.out.println(rs.getString(12));
             System.out.println(rs.getString(13));
             System.out.println(rs.getString(14));
             System.out.println(rs.getString(15));
             System.out.println(rs.getString(16));
             System.out.println(rs.getString(17));*/
        }
        String qry = "select * from customer where CID ='" + cid + "';";
        //System.out.println("qry: " + qry);

        rs = st.executeQuery(qry);
        while (rs.next()) {
            cid = rs.getString(1);
            custName = rs.getString(3);
            streetAddrStr = rs.getString(4);
            cityStr = rs.getString(5);
            stateStr = rs.getString(6);
            zipStr = rs.getString(7);
            phone = rs.getString(8);
            fax = rs.getString(9);
            pocName = rs.getString(14);
            pocPhone = rs.getString(15);

            //custList.add(rs.getString(1));
        }
        setCustPhone.setText(phone);
        setCustName.setText(custName);
        setCustCity.setText(cityStr);
        setCustState.setText(stateStr);
        setCustPOC.setText(pocPhone);
        setCustCompPhone.setText(phone);
        setCustFax.setText(fax);
        setCustAddr.setText(streetAddrStr);
        setCustZip.setText(zipStr);

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

        ObservableList<String> emp = FXCollections.observableArrayList(empList);
        ObservableList<String> vehEquip = FXCollections.observableArrayList(vehList);
        ObservableList<String> custListingObs = FXCollections.observableArrayList(custList);

        //set items on the job form
        employeeSelect.setItems(emp);
        vehicleEquipSelect.setItems(vehEquip);
        custListing.setItems(custListingObs);

        taskComboBox.setItems(taskListBox);
        CreateJobBox.setVisible(true);
        jobStatus.setItems(jStatus);

        displayJobs.setVisible(false);
        CreateJobBox.setVisible(true);
        taskList.getSelectionModel().select(0);
        createJobToolbar.setVisible(false);
        editJobToolbar.setVisible(true);

    }

    @FXML
    private void saveChangesAction(ActionEvent event) throws IOException, SQLException {

        try {
            empID = getEmpId();
        } catch (UnknownHostException ex) {
            Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
        }

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

        streetAddrStr = streetAddr.getText();
        cityStr = city.getText();
        stateStr = state.getText();
        zipStr = zip.getText();

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

        String qry = "update currentjobs set "
                + "CustJobNum='" + custJobNumStr
                + "', CustJobName='" + custJobNameStr
                + "', JobTitle='" + jobTitleStr
                + "', JobName='" + jobNameStr
                + "', JobWorkDate='" + startDateStr
                + "', JobStartTime='" + startTimeStr
                + "', JobType='" + jobtypecompiled
                + "', JobEmployees='" + empCompiled
                + "', JobEandV='" + equipCompiled
                + "', S_Instr='" + sI
                + "', D_Instr='" + dI
                + "', T_Instr='" + tI
                + "', W_Instr='" + wI
                + "', billing='" + billing
                + "', status='" + status
                + "', jobSiteAddr='" + streetAddr.getText()
                + "', jobCitySite='" + city.getText()
                + "', jobStateLoc='" + state.getText()
                + "', jobZipLoc='" + zip.getText()
                + "', employeeID =" + empID
                + " where CurJobID = " + custUniqueID;

        //delete all entries associated with IP before exiting to the login screen
        Statement updateDb = null;
        updateDb = conn.createStatement();

        //set our session id and ip address in order to identify user.
        int executeUpdate = updateDb.executeUpdate(qry);

        //show popup that changes are made
        Label label2;
        label2 = new Label("Job Updated/Saved to Database.");
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

    @FXML
    private void addNewUsrAction(ActionEvent event) throws NoSuchAlgorithmException {

        try {
            String usrNameSelected = empListUsr.getSelectionModel().getSelectedItem().toString(),
                    uidSelected = usrNameSelected.substring(usrNameSelected.lastIndexOf(",") + 2, usrNameSelected.length()),
                    newPwdStr = newPwd.getText(),
                    newUsrStr = newUsrName.getText();

            if (newPwdStr.length() <= 0) {
                System.out.print("err pwd");
            } else if (newUsrStr.length() <= 0) {
                System.out.print("err usr");
            } else {
                System.out.println(uidSelected + "\n" + newPwdStr + "\n" + newUsrStr);
                newPwdStr = getMD5(newPwdStr);

                String queryRunNow = "insert into users (userName, password,employees_uid) "
                        + " values('" + newUsrStr + "','" + newPwdStr + "'," + uidSelected + ");";
                String qry = "select userName from users;";

                //insert into database
                Statement updateDb = null;

                try {
                    updateDb = conn.createStatement();
                    List<String> tmpList = new ArrayList<String>();
                    boolean nameExists = false;

                    rs = st.executeQuery(qry);
                    while (rs.next()) {
                        //System.out.println(rs.getString(1));
                        tmpList.add(rs.getString(1));
                    }

                    for (int i = 0; i < tmpList.size(); i++) {
                        //System.out.println(tmpList.get(i));
                        if (tmpList.get(i).equals(newUsrStr.trim())) {
                            nameExists = true;
                        }
                    }

                    int executeUpdate;

                    if (!nameExists) {
                        executeUpdate = updateDb.executeUpdate(queryRunNow);

                        if (executeUpdate > 0) {
                            displayMsg("User successfully added to database.");
                        }
                    } else {
                        displayMsg("Please check username & try again.");
                    }

                    //equipmentTable.setItems(populateEquip());
                } catch (SQLException ex) {
                    Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (NullPointerException e) {
            System.err.println(e);
        }

    }

    @FXML
    private void addEmployeeAction(ActionEvent event)
            throws IOException, SQLException {
        String queryRun = "insert into employees (fname, lname,address,phone,email) "
                + "values('" + fNameStrIns.getText() + "','" + lNameStrIns.getText() + "','"
                + "null','" + phoneStrIns.getText() + "','" + emailStrIns.getText() + "')";

        //insert into database
        Statement updateDb = null;
        //System.out.println(queryRun);

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryRun);
            employeeTable.setItems(populateDB());

            //show the complete box dialog
            Label label2;
            label2 = new Label("Employee Added");
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

        fNameStrIns.setText("");
        lNameStrIns.setText("");
        phoneStrIns.setText("");
        emailStrIns.setText("");

    }

    @FXML
    private void deleteEmpAction(ActionEvent event) throws IOException, SQLException {
        String emailToDelete = employeeTable.getSelectionModel().selectedItemProperty().getValue().getEmail();
        String queryDelete = "DELETE FROM employees WHERE email = '" + emailToDelete + "'";
        //System.out.println(queryDelete);

        //insert into database
        Statement updateDb = null;

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user.
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryDelete);
            employeeTable.setItems(populateDB());

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addManagerAction(ActionEvent event)
            throws IOException, SQLException {
        String queryRun = "insert into manager (fName, lName,email,phone,office) "
                + "values('" + fname_str.getText() + "','" + lname_str.getText() + "','"
                + phone_str.getText() + "','" + email_str.getText()+ "','" + office_str.getText()
                + "')";

        //insert into database
        Statement updateDb = null;
        //System.out.println(queryRun);

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryRun);
            employeeTable.setItems(populateDB());

            //show the complete box dialog
            Label label2;
            label2 = new Label("Employee Added");
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

        fname_str.setText("");
        lname_str.setText("");
        phone_str.setText("");
        email_str.setText("");
        office_str.setText("");         

    }

    @FXML
    private void deleteManagerAction(ActionEvent event) throws IOException, SQLException {
        String emailToDelete = managerView.getSelectionModel().selectedItemProperty().getValue().getEmail();
        String queryDelete = "DELETE FROM manager WHERE email = '" + emailToDelete + "'";
        //System.out.println(queryDelete);

        //insert into database
        Statement updateDb = null;

        //make the connection
        try { 
            //set our session id and ip address in order to identify user.
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryDelete);
            managerView.setItems(populateManagers());

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
