/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobcenter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.ComboBoxTreeCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;

/**
 * FXML Controller class
 *
 * @author angelacaicedo
 */
public class JobCenterMainController implements Initializable, ScreenController {

    //database connection info -- 192.168.1.112 customer ip
    //my ip 192.168.1.108
    //jdbc:mysql://hostname:port/databasename
    private static String url = "jdbc:mysql://localhost/jobcenter";
    //public static String url = "jdbc:mysql://192.168.1.104/jobcenter";
    public static String userdb = "vangfc";//Username of database  
    public static String passdb = "password";//Password of database

    //public static String url = "jdbc:mysql://192.168.1.112/jobcenter"; 
    //public static String userdb = "videoPipe";//Username of database  
    //public static String passdb = "Vps1566!!";//Password of database
    public static String scrollingTxt = "";
    String emailList;
    String[] emailTo;

    Statement st = null;
    ResultSet rs = null;

    public static Connection conn;
    public ScreenPane myScreenPane;
    public ToolBar editJobToolbar, createJobToolbar;
    boolean calSet;

    public RadioButton prodChk, hourChk;

    public ListView adminList, taskList, proList, employeeSelect, employeeSelected,
            vehicleEquipSelect, vehicleEquipSelected, custListing, empAddJobView, vehAddJobView,
            taskTypeList, empEmailListView, empAdminListView, counterList, dateList;
    public Pane CreateJobBox, settingsPane, displayJobs, equipVehPane, employeePane, managerPane,
            usersPane, proposalsPane, userHist;
    public ToolBar AdminToolBar, FunctionsToolBar, ReportsToolBar, employeeToolbar;

    public static ObservableList<String> jStatus = FXCollections.observableArrayList(
            "IN PROGRESS", "COMPLETE", "HOLD-CUSTOMER", "HOLD-WEATHER", "HOLD-OTHER", "PROJECTED", "CANCELLED");

    public TableView<manager> managerView = new TableView<manager>();
    public TableView<employee> employeeTable = new TableView<employee>();
    public TableView<equipment> equipmentTable = new TableView<equipment>();
    public TableView<users> usersTable = new TableView<users>();
    public TableView<editHistClass> usrHistTable = new TableView<editHistClass>();

    public TableColumn emp_fname, emp_lname, emp_phone, emp_email, emp_shop,
            vehNameIns, typeIns, statusIns, usrFName, usrLName, usrUName, usrPwd,
            manage_lname, manage_fname, manage_phone, manage_office, manage_email,
            jname, dte, fnme, lnme, descrpt;

    public Button chgPasswd, addEmp, addVehBut, deleteVehBut, clearJob,
            saveJob, confirmJob, cancelJob, addCustBut, addVehEqToTreeBut, addEmpToTreeBut,
            displayJobBut, deleteEquipBut, addEquipBut, addTask, deleteEmpBut,
            saveChangesBut, previewJob, addNewUsr, addEmployeeBut, deleteManagerBut, addManagerBut,
            usrDeleteBut, handlePasswdBut, saveScrollingBut, displayCalendar,
            emailJobBoard, deleteJob, editCustBut, addEmpEmailerBut,
            deleteEmpEmailBut, delAdminBut, insertReportBut, showEmpBut, viewChosenTrackerBut,
            deleteTrackerBut, createCustBut, chkEmp, archiveBut, printJobInfoBut;

    public TextField jobTitle, jobName, custJobNum, custJobName, startDate, startTime,
            diamStr, feetStr, fNameStrIns, lNameStrIns, phoneStrIns, emailStrIns,
            vehNameNew, typeNew, statusNew, streetAddr, city, state, zip,
            newUsrName, fname_str, lname_str, phone_str, email_str, office_str,
            scrollingTxtSet, dateTracking, notesTracking;

    public PasswordField newPwd;

    public static String jobTitleStr = "", jobNameStr = "", custJobNumStr = "", custJobNameStr = "", startDateStr = "", startTimeStr = "",
            streetAddrStr = "", cityStr = "", stateStr = "", zipStr = "", custAdd = "", phone = "", fax = "", pocName = "", pocPhone = "", status = "", custUniqueID = "",
            billing = "", cid = "", jobtypecompiled = "", empCompiled = "", equipCompiled = "", sI = "", dI = "", tI = "", wI = "";

    public Text setCustPhone, setCustName, setCustCity, setCustState, setCustPOC, setCustCompPhone,
            setCustFax, setCustAddr, setCustZip, useridInfor, nameInfor, emailInfor, phoneInfor, usernameInfor,
            currentScrolling, nameTracker;

    public ComboBox screenList, taskComboBox, jobStatus, empListUsr, selEmpEmail, selEmpAdmin, trackingBox;
    ObservableList<String> admin = FXCollections.observableArrayList(
            "People", "Vehicles", "Create/Delete a JobCenter User", "Settings");
    //ObservableList<String> functions = FXCollections.observableArrayList(
    //      "Show job board", "Summary report");
    ObservableList<String> tasks = FXCollections.observableArrayList(
            "Create new job", "Display jobs", "Changelog");
    ObservableList<String> proposals = FXCollections.observableArrayList(
            "New proposal", "View Current Proposals");

    public static List<String> list = new ArrayList<String>();
    public static ObservableList<String> options = FXCollections.observableList(list),
            taskListBox = FXCollections.observableList(list),
            taskTypeListStr = FXCollections.observableList(list),
            empNameBox = FXCollections.observableList(list),
            tracking = FXCollections.observableList(list),
            theReport = FXCollections.observableList(list),
            theDates = FXCollections.observableList(list);
    public static List<String> empListSel = new ArrayList<String>(),
            empListSelected = new ArrayList<String>(),
            vehList = new ArrayList<String>(),
            custList = new ArrayList<String>(),
            jobTypePicked = new ArrayList<String>(),
            jobList = new ArrayList<String>(),
            editH = new ArrayList<String>(),
            empEmailList = new ArrayList<String>(),
            adminNameBox = new ArrayList<String>(),
            listAdmin = new ArrayList<String>(),
            theReportList = new ArrayList<String>(),
            vehListSelected = new ArrayList<String>(),
            vehListAll = new ArrayList<String>();

    ObservableList<String> vehList11 = FXCollections.observableArrayList(vehList);
    ObservableList<String> empSelect = FXCollections.observableArrayList(empListSel);
    ObservableList<String> custListingObs = FXCollections.observableArrayList(custList);
    ObservableList<String> editHistory = FXCollections.observableArrayList(editH);
    ObservableList<String> empEmailObs = FXCollections.observableArrayList(empEmailList);
    ObservableList<String> adminObs = FXCollections.observableArrayList(adminNameBox);
    ObservableList<String> empSelected = FXCollections.observableArrayList(empListSelected);

    TextField jTitleField = new TextField(),
            jCustField = new TextField(),
            jNameField = new TextField(),
            jStartField = new TextField();

    public TextArea sInstr, tInstr, dInstr, wInstr;

    public static String empID;

    int start;

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
        //emp_shop.setCellValueFactory(new PropertyValueFactory<employee, String>("shpEmp55"));

        employeeTable.setItems(populateDB());
        employeeTable.setEditable(true);

        emp_fname.setCellFactory(TextFieldTableCell.forTableColumn());
        emp_lname.setCellFactory(TextFieldTableCell.forTableColumn());
        emp_email.setCellFactory(TextFieldTableCell.forTableColumn());
        emp_phone.setCellFactory(TextFieldTableCell.forTableColumn());
        // emp_shop.setCellFactory(TextFieldTableCell.forTableColumn());

        emp_fname.setOnEditStart(new EventHandler<TableColumn.CellEditEvent<employee, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<employee, String> t) {
                ((employee) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFirstName(t.getNewValue());
                //system.out.println("onStart");
                //system.out.println(t.getTableView().getItems().get(t.getTablePosition().getRow()).getFirstName());
            }
        });

        emp_fname.setOnEditCancel(new EventHandler<TableColumn.CellEditEvent<employee, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<employee, String> t) {
                ((employee) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFirstName(t.getNewValue());
                //system.out.println("onCancel");
                //system.out.println(t.getTableView().getItems().get(t.getTablePosition().getRow()).getFirstName());
            }
        });
        emp_fname.setOnEditCommit(new EventHandler() {

            @Override
            public void handle(Event t) {
                //system.out.println("Commit");
            }
        });

        /*emp_fname.setOnEditCommit(
         new EventHandler<TableColumn.CellEditEvent<employee,String>> (){
         @Override
         public void handle(CellEditEvent<employee, String> t) {
         //system.out.println("Changed!");
         }
         }
        
        
         new EventHandler<CellEditEvent<employee, String>>() {
         @Override
         public void handle(CellEditEvent<employee, String> t) {
         //system.out.println("Changed!");
         }
         }
         );*/
        jname.setCellValueFactory(new PropertyValueFactory<editHistClass, String>("jobName"));
        dte.setCellValueFactory(new PropertyValueFactory<editHistClass, String>("date"));
        fnme.setCellValueFactory(new PropertyValueFactory<editHistClass, String>("firstName"));
        lnme.setCellValueFactory(new PropertyValueFactory<editHistClass, String>("lastName"));
        descrpt.setCellValueFactory(new PropertyValueFactory<editHistClass, String>("descrNow"));
        usrHistTable.setItems(populateHist());

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

        usrFName.setCellValueFactory(new PropertyValueFactory<users, String>("firstName"));
        usrLName.setCellValueFactory(new PropertyValueFactory<users, String>("lastName"));
        usrUName.setCellValueFactory(new PropertyValueFactory<users, String>("username"));
        usrPwd.setCellValueFactory(new PropertyValueFactory<users, String>("pwd"));

        usersTable.setItems(populateUsers());

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

        //need to set scrolling text from database...
        scrollingTxt = getScrollingTxt();

        //Connect to database
        databaseConnect();

        //  initialize user settings text
        initializeText();
    }

    public int howManyRowsPrinter() {
        String qryRun55 = "select count(*) from currentjobs where status = 'IN PROGRESS'";
        int number = 0;
        //make the connection
        try {
            st = conn.createStatement();
            ResultSet rs2 = st.executeQuery(qryRun55);

            rs2.next();
            number = rs2.getInt(1);
            rs2.close();

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return number;
    }

    public int howManyVehEq(String theTitle) {
        int counter55 = 0;
        String theItems = "";

        try {
            st = conn.createStatement();

            String qryCurJob = "select CurJobID, JobEandV from currentJobs where JobTitle = '" + theTitle + "';";

            rs = st.executeQuery(qryCurJob);
            while (rs.next()) {
                theItems = rs.getString(2);
            }

        } catch (Exception e) {
            System.err.println(e);
        }

        //take away that first '/'
        if (!theItems.equals("")) {
            theItems = theItems.substring(theItems.indexOf("/") + 1, theItems.length());
        }

        while (theItems.lastIndexOf("/") >= 0) {
            theItems = theItems.substring(theItems.indexOf("/") + 1, theItems.length());
            //system.out.println(theItems);
            counter55++;
        }
        //add that last item
        counter55++;

        return counter55;
    }

    public int howManyPeople(String theTitle) {
        int counter55 = 0;
        String theItems = "";

        try {
            st = conn.createStatement();

            String qryCurJob = "select CurJobID, JobEmployees from currentJobs where JobTitle = '" + theTitle + "';";

            rs = st.executeQuery(qryCurJob);
            while (rs.next()) {
                theItems = rs.getString(2);
            }

        } catch (Exception e) {
            System.err.println(e);
        }

        //take away that first '/'
        if (!theItems.equals("")) {
            theItems = theItems.substring(theItems.indexOf("/") + 1, theItems.length());
        }

        while (theItems.lastIndexOf("/") >= 0) {
            theItems = theItems.substring(theItems.indexOf("/") + 1, theItems.length());
            //system.out.println(theItems);
            counter55++;
        }
        //add that last item
        counter55++;

        return counter55;
    }

    //this function checks to see what is really assigned in the 'currentjobs' table 
    private void checkAssignedEquip() {
        String qrySelect = "select JobEandV from currentjobs",
                tmpStr5 = "";
        ArrayList jobsNequip_raw = new ArrayList<String>(),
                jobsNequip_parsed = new ArrayList<String>();

        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery(qrySelect);

            while (rs.next()) {
                jobsNequip_raw.add(rs.getString(1));
            }

            for (int i = 0; i < jobsNequip_raw.size(); i++) {
                tmpStr5 = jobsNequip_raw.get(i).toString();

                while (true) {
                    if (tmpStr5 != "") {
                        if (tmpStr5.indexOf("/") < 0) {
                            jobsNequip_parsed.add(tmpStr5.substring(0, tmpStr5.length()));
                            break;
                        }
                    }

                    if (tmpStr5.indexOf("/") == 0) {
                        tmpStr5 = tmpStr5.substring(1, tmpStr5.length());
                        if (tmpStr5.indexOf("/") < 0) {
                            jobsNequip_parsed.add(tmpStr5.substring(0, tmpStr5.length()));
                            break;
                        } else {
                            jobsNequip_parsed.add(tmpStr5.substring(0, tmpStr5.indexOf("/")));
                            tmpStr5 = tmpStr5.substring(tmpStr5.indexOf("/"), tmpStr5.length());
                        }

                    } else {

                        if (tmpStr5 == "") {
                            break;
                        }

                        String holder = tmpStr5;
                        tmpStr5 = tmpStr5.substring(0, tmpStr5.indexOf("/"));
                        jobsNequip_parsed.add(tmpStr5);

                        tmpStr5 = holder;

                        tmpStr5 = tmpStr5.substring(tmpStr5.indexOf("/"), tmpStr5.length());

                    }
                }
            }

            for (int i = 0; i < jobsNequip_parsed.size(); i++) {
                //system.out.println(jobsNequip_parsed.get(i));
            }

            //system.out.println("DONE");

            String theItem = "";
            //RESET THE ENTIRE LIST
            String querySetVehRESET = "update vehicles set VehicleStatus = 'AVAILABLE';";
            //insert into database
            Statement resetdb = null;
            //make the connection
            try {
                conn = DriverManager.getConnection(url, userdb, passdb);
                //set our session id and ip address in order to identify user.
                resetdb = conn.createStatement();

                int executeUpdate = resetdb.executeUpdate(querySetVehRESET);

            } catch (SQLException ex) {
                Logger.getLogger(JobCenterController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            //set the assigned values in the pre-recorded list to assigned.
            for (int i = 0; i < jobsNequip_parsed.size(); i++) {
                theItem = jobsNequip_parsed.get(i).toString();
                String querySetVeh = "update vehicles set VehicleStatus = 'ASSIGNED' where VehicleName = '" + theItem + "'";
                ////system.out.println(queryDelete);

                //insert into database
                Statement updateDb = null;

                //make the connection
                try {
                    conn = DriverManager.getConnection(url, userdb, passdb);

                    //set our session id and ip address in order to identify user.
                    updateDb = conn.createStatement();

                    int executeUpdate = updateDb.executeUpdate(querySetVeh);

                } catch (SQLException ex) {
                    Logger.getLogger(JobCenterController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void refreshEmp() {
        try {
            empListSel = new ArrayList<String>();

            st = conn.createStatement();
            rs = st.executeQuery("select fname,lname from employees order by fname;");
            while (rs.next()) {
                ////system.out.println(rs.getString(1));
                empListSel.add(rs.getString(1) + " " + rs.getString(2));
            }

            ObservableList<String> empLister11232 = FXCollections.observableArrayList(empListSel);
            employeeSelect.setItems(empLister11232);
            //update items for employees
            // employeeSelect.setItems(empLister);
            empAddJobView.setItems(empLister11232);

        } catch (Exception e) {
            System.err.println(e);
        }

    }

    private void refreshVeh() {
        try {
            vehListSelected = new ArrayList<String>();

            st = conn.createStatement();
            rs = st.executeQuery("select VehicleName from vehicles where VehicleStatus = 'AVAILABLE' order by VehicleName;");
            while (rs.next()) {
                ////system.out.println(rs.getString(1));
                vehListSelected.add(rs.getString(1));
            }

            ObservableList<String> vehiLister22 = FXCollections.observableArrayList(vehListSelected);

            //update items for vehicle/equipment
            vehicleEquipSelect.setItems(vehiLister22);
            vehAddJobView.setItems(vehiLister22);

        } catch (Exception e) {
            System.err.println(e);
        }

    }

    private final class TextFieldTreeCellImpl extends TreeCell<String> {

        private TextField textField;
        private ContextMenu addMenu = new ContextMenu();
        Menu subMenuStatus = new Menu("Toggle");

        public TextFieldTreeCellImpl() {
            MenuItem addMenuItem = new MenuItem("Delete"),
                    addMenuItem3 = new MenuItem("IN PROGRESS"),
                    addMenuItem4 = new MenuItem("COMPLETE"),
                    addMenuItem5 = new MenuItem("HOLD-CUSTOMER"),
                    addMenuItem6 = new MenuItem("HOLD-WEATHER"),
                    addMenuItem7 = new MenuItem("HOLD-OTHER"),
                    addMenuItem8 = new MenuItem("PROJECTED"),
                    addMenuItem9 = new MenuItem("CANCELLED");

            subMenuStatus.getItems().add(addMenuItem3);
            subMenuStatus.getItems().add(addMenuItem4);
            subMenuStatus.getItems().add(addMenuItem5);
            subMenuStatus.getItems().add(addMenuItem6);
            subMenuStatus.getItems().add(addMenuItem7);
            subMenuStatus.getItems().add(addMenuItem8);
            subMenuStatus.getItems().add(addMenuItem9);

            //addMenu.getItems().add(addMenuItem);
            addMenu.getItems().add(subMenuStatus);
            
            
            
            addMenuItem3.setOnAction(new EventHandler() {
                 public void handle(Event t) {
                    try {                                            
                        
                        //system.out.println(getTreeItem().getValue().toString());
                        //system.out.println(getTreeItem().getParent().getValue().toString());
                        
                        //should be three fields: employee/equipment/status
                        updateToJob("IN PROGRESS", "status", getTreeItem().getParent().getValue().toString());
                            
                        refreshList();       
                        
                        //need to expand the updated list
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            });
            
            addMenuItem4.setOnAction(new EventHandler() {
                 public void handle(Event t) {
                    try {                                            
                        
                        //system.out.println(getTreeItem().getValue().toString());
                        //system.out.println(getTreeItem().getParent().getValue().toString());
                        
                        //should be three fields: employee/equipment/status
                        updateToJob("COMPLETE", "status", getTreeItem().getParent().getValue().toString());
                            
                        refreshList();       
                        
                        //need to expand the updated list
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            });
            
            addMenuItem5.setOnAction(new EventHandler() {
                 public void handle(Event t) {
                    try {                                            
                        
                        //system.out.println(getTreeItem().getValue().toString());
                        //system.out.println(getTreeItem().getParent().getValue().toString());
                        
                        //should be three fields: employee/equipment/status
                        updateToJob("HOLD-CUSTOMER", "status", getTreeItem().getParent().getValue().toString());
                            
                        refreshList();       
                        
                        //need to expand the updated list
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            });
            
            addMenuItem6.setOnAction(new EventHandler() {
                 public void handle(Event t) {
                    try {                                            
                        
                        //system.out.println(getTreeItem().getValue().toString());
                        //system.out.println(getTreeItem().getParent().getValue().toString());
                        
                        //should be three fields: employee/equipment/status
                        updateToJob("HOLD-WEATHER", "status", getTreeItem().getParent().getValue().toString());
                            
                        refreshList();       
                        
                        //need to expand the updated list
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            });
            
            addMenuItem7.setOnAction(new EventHandler() {
                 public void handle(Event t) {
                    try {                                            
                        
                        //system.out.println(getTreeItem().getValue().toString());
                        //system.out.println(getTreeItem().getParent().getValue().toString());
                        
                        //should be three fields: employee/equipment/status
                        updateToJob("HOLD-OTHER", "status", getTreeItem().getParent().getValue().toString());
                            
                        refreshList();       
                        
                        //need to expand the updated list
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            });
            
            addMenuItem8.setOnAction(new EventHandler() {
                 public void handle(Event t) {
                    try {                                            
                        
                        //system.out.println(getTreeItem().getValue().toString());
                        //system.out.println(getTreeItem().getParent().getValue().toString());
                        
                        //should be three fields: employee/equipment/status
                        updateToJob("PROJECTED", "status", getTreeItem().getParent().getValue().toString());
                            
                        refreshList();       
                        
                        //need to expand the updated list
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            });
            
            addMenuItem9.setOnAction(new EventHandler() {
                 public void handle(Event t) {
                    try {                                            
                        
                        //system.out.println(getTreeItem().getValue().toString());
                        //system.out.println(getTreeItem().getParent().getValue().toString());
                        
                        //should be three fields: employee/equipment/status
                        updateToJob("CANCELLED", "status", getTreeItem().getParent().getValue().toString());
                            
                        refreshList();       
                        
                        //need to expand the updated list
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            });
            
            /*addMenuItem.setOnAction(new EventHandler() {

                public void handle(Event t) {
                    try {
                        //system.out.println(getTreeItem().getValue().toString());
                        //system.out.println(getTreeItem().getParent().getValue().toString());
                        
                        int indexNode = 0;
                        indexNode = currentJobsDisplay.getSelectionModel().getSelectedIndex();
                        
                        //should be three fields: employee/equipment/status
                        updateToJob(getTreeItem().getValue().toString(), "employee", getTreeItem().getParent().getValue().toString());
                                                
                        currentJobsDisplay.getTreeItem(indexNode).setExpanded(true);
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });*/
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText((String) getItem());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                    if (!getTreeItem().isLeaf() && getTreeItem().getParent() != null) {
                        setContextMenu(addMenu);
                    }
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(textField.getText());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });

        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

    private int empParser(String listofEmp) {
        int count = 1;
        String strManip = listofEmp;

        listofEmp.lastIndexOf("/");
        //system.out.println(listofEmp);

        while (strManip.lastIndexOf("/") > 0) {
            strManip = strManip.substring(0, strManip.lastIndexOf("/"));
            count++;
            //system.out.println(strManip);
        }
        //system.out.println("Employee count: " + count);
        return count;
    }

    private int vehParser(String listofVeh) {
        int count = 1;
        String strManip = listofVeh;

        strManip.lastIndexOf("/");
        //system.out.println(strManip);

        while (strManip.lastIndexOf("/") > 0) {
            strManip = strManip.substring(0, strManip.lastIndexOf("/"));
            count++;
            //system.out.println(strManip);
        }
        //system.out.println("Veh count: " + count);
        return count;
    }

    private String getEmpID() {

        String lastNameEmp = employeeTable.getSelectionModel().selectedItemProperty().getValue().getLastName(),
                firstNameEmp = employeeTable.getSelectionModel().selectedItemProperty().getValue().getFirstName(),
                emailEmp = employeeTable.getSelectionModel().selectedItemProperty().getValue().getEmail();

        String qryRun = "select uid from employees where fname = '" + firstNameEmp + "' and "
                + "lname = '" + lastNameEmp + "' and email = '" + emailEmp + "'",
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

    //returns a list of all employees that are not assigned to a job
    public List<String> listEmpAssigned() {
        List<String> listAssigned = new ArrayList<String>();
        String queryEmployeesAssigned = "select JobEmployees from currentJobs where status = 'IN PROGRESS';";

        //system.out.println(queryEmployeesAssigned);

        //insert into database
        Statement updateDb = null;

        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery(queryEmployeesAssigned);

            while (rs.next()) {

                ////system.out.println(rs.getString(1));
                listAssigned.add(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return listAssigned;
    }

    private void setScrollingTxt(String textSet) throws SQLException {
        delScrollTxt();

        String qry3 = "insert into scrolltext (text) values ('" + textSet + "');";
        Statement updateDb = null;
        updateDb = conn.createStatement();
        int executeUpdate = updateDb.executeUpdate(qry3);
    }

    private String getScrollingTxt() {
        String qryRun = "select text from scrolltext;",
                txtReturn = "";

        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery(qryRun);

            while (rs.next()) {
                txtReturn = rs.getString(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return txtReturn;
    }

    void updateToJob(String itemStr, String whichField, String jobNameWorkIt) throws SQLException {
        String qry3 = "";
        if (whichField.equals("employee")) {
            qry3 = "";
        }
        if (whichField.equals("status")) {
            qry3 = "update currentjobs set status = '"+itemStr+"' where JobTitle = '"+jobNameWorkIt+"'";
            
            Statement updateDb = null;
            updateDb = conn.createStatement();
            int executeUpdate = updateDb.executeUpdate(qry3);            
        }
        if (whichField.equals("equipment")) {
            qry3 = "";
        }

        
    }

    void delScrollTxt() throws SQLException {
        String qry3 = "delete from scrolltext;";
        Statement updateDb = null;
        updateDb = conn.createStatement();
        int executeUpdate = updateDb.executeUpdate(qry3);
    }

    private void clearJobList() {
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

    private void initializeText() {
        String idEmpStr = null;

        try {
            idEmpStr = getEmpId();
        } catch (UnknownHostException ex) {
            Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select (select fname from employees where uid = employees_uid), "
                    + "(select lname from employees where uid = employees_uid), "
                    + "(select email from employees where uid = employees_uid),"
                    + "(select phone from employees where uid = employees_uid),"
                    + "userName from users where employees_uid = " + idEmpStr + ";");

            while (rs.next()) {

                //system.out.println(rs.getString(1));
                //system.out.println(rs.getString(2));
                //system.out.println(rs.getString(3));
                //system.out.println(rs.getString(4));
                //system.out.println(rs.getString(5));

                useridInfor.setText(idEmpStr);
                nameInfor.setText(rs.getString(1) + " " + rs.getString(2));
                emailInfor.setText(rs.getString(3));
                phoneInfor.setText(rs.getString(4));
                usernameInfor.setText(rs.getString(5));

            }
        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    private boolean checkIfJobExists(String theJobTitle) {
        String chkIfJobQry = "select * from currentjobs where JobTitle = '" + theJobTitle + "'";
        boolean jobexist = false;

        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery(chkIfJobQry);

            while (rs.next()) {
                jobexist = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jobexist;
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
        root559.setExpanded(false);

        for (int i = 0; i < jobList.size(); i++) {

            //adds each job child node to the treeview
            TreeItem<String> depNode = new TreeItem<String>(jobList.get(i));

            //once job added, grab all cust data needed and add info to that child node
            List<String> jobListInfo = getJobListInfo(jobList.get(i));

            depNode.setExpanded(false);
            for (int j = 0; j < jobListInfo.size(); j++) {
                String varStr = jobListInfo.get(j);
                if (!varStr.equals("")) {
                    if (varStr.subSequence(0, 1).equals("/")) {
                        while (varStr.indexOf("/") >= 0) {
                            String empNameStr = "";
                            varStr = varStr.substring(varStr.indexOf("/") + 1, varStr.length());

                            if (varStr.indexOf("/") >= 0) {
                                empNameStr = varStr.substring(0, varStr.indexOf("/"));
                            } else {
                                empNameStr = varStr.substring(0, varStr.length());
                            }

                            TreeItem<String> var = new TreeItem<String>(empNameStr);
                            depNode.getChildren().add(var);

                        }

                    } else {
                        TreeItem<String> var = new TreeItem<String>(jobListInfo.get(j));
                        depNode.getChildren().add(var);
                    }
                }
            }

            //add the node and its info into the list
            root559.getChildren().add(depNode);

        }

        currentJobsDisplay.setRoot(root559);
        root559.setExpanded(true);
        currentJobsDisplay.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> p) {
                return new TextFieldTreeCellImpl();
            }
        });
    }

    public ObservableList<users> populateUsers() {
        ObservableList<users> tester55 = FXCollections.observableArrayList();

        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select (select fname from employees where uid = employees_uid), (select lname from employees where uid = employees_uid), userName, password from users;");

            while (rs.next()) {
                tester55.add(new users(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                //system.out.println(rs.getString(1));
                //system.out.println(rs.getString(2));
                //system.out.println(rs.getString(3));
                //system.out.println(rs.getString(4));

            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //system.out.println(tester55.get(0).getPwd());
        //system.out.println(tester55.get(0).getUsername());

        return tester55;
    }

    //popluate the shop employees
    public ObservableList<manager> populateManagers() {
        ObservableList<manager> tester55 = FXCollections.observableArrayList();

        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select fName, lName, email, phone, office from manager;");

            while (rs.next()) {
                tester55.add(new manager(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                //system.out.println(rs.getString(1));
                //system.out.println(rs.getString(2));
                //system.out.println(rs.getString(3));
                //system.out.println(rs.getString(4));
                //system.out.println(rs.getString(5));

            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tester55;
    }

    public ObservableList<editHistClass> populateHist() {
        ObservableList<editHistClass> tester66 = FXCollections.observableArrayList();

        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select usrFname,usrLname,description,JobName,DateEdited from edithistory;");

            while (rs.next()) {
                tester66.add(new editHistClass(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5)));
                //system.out.println(rs.getString(1));
                //system.out.println(rs.getString(2));
                //system.out.println(rs.getString(3));
                //system.out.println(rs.getString(4));
                //system.out.println(rs.getString(5));

            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tester66;

    }

    //returns t/f if a current user is an administrator.
    public boolean isAdmin() throws UnknownHostException {

        String ans = "0";
        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select adminPriv from administrators where employees_uid = " + getEmpId() + ";");

            while (rs.next()) {
                //system.out.println(rs.getString(1));
                ans = rs.getString(1);
                //system.out.println(ans);
                ////system.out.println(rs.getString(2));
                ////system.out.println(rs.getString(3));

            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (ans.equals("0")) {
            return false;
        } else if (ans.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    public ObservableList<equipment> populateEquip() {
        ObservableList<equipment> tester2 = FXCollections.observableArrayList();

        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select VehicleName, VehicleType, VehicleStatus from vehicles ORDER BY VehicleName ASC;");

            while (rs.next()) {
                tester2.add(new equipment(rs.getString(1), rs.getString(2), rs.getString(3)));
                ////system.out.println(rs.getString(1));
                ////system.out.println(rs.getString(2));
                ////system.out.println(rs.getString(3));

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
            rs = st.executeQuery("select fname, lname, phone, email, shopEmp from employees ORDER BY fname ASC;");

            while (rs.next()) {
                tester2.add(new employee(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(3), rs.getString(5)));
                //system.out.println(rs.getString(1));
                //system.out.println(rs.getString(2));
                //system.out.println(rs.getString(3));
                //system.out.println(rs.getString(4));
                //system.out.println(rs.getString(5));
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

    public void updateEmailList() {
        empEmailObs.clear();
        empEmailList.clear();

        try {
            st = conn.createStatement();
            rs = st.executeQuery("select emailSend from emaillist;");
            while (rs.next()) {
                ////system.out.println(rs.getString(1));
                empEmailList.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.err.print(e);
        }

        empEmailObs = FXCollections.observableArrayList(empEmailList);
        empEmailListView.setItems(empEmailObs);

    }

    public void updateAdminList() {
        adminObs.clear();
        adminNameBox.clear();
        listAdmin = new ArrayList<String>();

        try {
            st = conn.createStatement();
            rs = st.executeQuery("select employees_uid from administrators;");
            while (rs.next()) {
                //system.out.println(rs.getString(1));
                listAdmin.add(rs.getString(1));
            }

            for (int k = 0; k < listAdmin.size(); k++) {
                rs = st.executeQuery("select fname, lname from employees where uid = " + listAdmin.get(k) + ";");
                while (rs.next()) {
                    //system.out.println(rs.getString(1));
                    //system.out.println(rs.getString(2));
                    adminNameBox.add(rs.getString(2) + ", " + rs.getString(1) + ":" + listAdmin.get(k));
                }
            }

        } catch (Exception e) {
            System.err.print(e);
        }

        adminObs = FXCollections.observableArrayList(adminNameBox);
        empAdminListView.setItems(adminObs);

    }

    public void clearPane() {
        //clears the main task area
        userHist.setVisible(false);
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

    public boolean emailExist(String emailToAdd, String listEmpEmail) {
        if (listEmpEmail.contains(emailToAdd)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean adminExist(String theUserID) {
        String test = "";
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select aid from administrators where employees_uid = " + theUserID + ";");
            while (rs.next()) {
                //system.out.println(rs.getString(1));
                test = rs.getString(1);
            }

        } catch (Exception e) {
            System.err.print(e);
        }
        if (test.equals("")) {
            return false;
        } else {
            return true;
        }
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
        final List<String> empList = new ArrayList<String>();
        final List<String> vehList = new ArrayList<String>();
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select fname,lname from employees order by fname;");
            while (rs.next()) {
                ////system.out.println(rs.getString(1));
                empList.add(rs.getString(1) + " " + rs.getString(2));
            }
            rs = st.executeQuery("select VehicleName from vehicles where VehicleStatus = 'AVAILABLE' order by VehicleName;");
            while (rs.next()) {
                ////system.out.println(rs.getString(1));
                vehList.add(rs.getString(1));
            }
            rs = st.executeQuery("select CompanyName from customer order by CompanyName;");
            while (rs.next()) {
                ////system.out.println(rs.getString(1));
                custList.add(rs.getString(1));
            }
            // rs = st.executeQuery("select usrLname,usrFname,JobName,DateEdited,description from edithistory;");
            rs = st.executeQuery("select description from edithistory;");
            while (rs.next()) {
                ////system.out.println(rs.getString(1));
                editH.add(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        adminList.setItems(admin);
        adminList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov,
                            String old_val, String new_val) {
                        try {
                            //clear pane first
                            //get user, if not in the allowed userlist disable the management bar
                            if (isAdmin()) {
                                adminList.disableProperty().set(false);
                                clearPane();

                                // if (new_val == "Shop employees/managers") {
                                //      managerPane.setVisible(true);
                                //  }
                                if (new_val == "People") {
                                    employeePane.setVisible(true);
                                    tracking = FXCollections.observableList(new ArrayList<String>());

                                    tracking.add("Suspended");
                                    tracking.add("Verbal Warning");
                                    tracking.add("Unsafe Driving");
                                    tracking.add("Equip Damage");
                                    tracking.add("Call Out");
                                    tracking.add("Vacation");
                                    tracking.add("Personal Day");

                                    trackingBox.setItems(tracking);

                                }
                                if (new_val == "Vehicles") {
                                    equipVehPane.setVisible(true);
                                }
                                if (new_val == "Settings") {
                                    settingsPane.setVisible(true);
                                    empNameBox.clear();

                                    updateEmailList();

                                    //set the user feedback text to whats in the database
                                    currentScrolling.setText(scrollingTxt);

                                    //make the connection
                                    try {
                                        String uidToDel = "";

                                        st = conn.createStatement();
                                        rs = st.executeQuery("select fname, lname, email from employees order by fname;");
                                        while (rs.next()) {
                                            ////system.out.println(rs.getString(1));
                                            empNameBox.add(rs.getString(1) + " " + rs.getString(2) + ": " + rs.getString(3));
                                        }

                                        selEmpEmail.setItems(empNameBox);
                                        selEmpAdmin.setItems(empNameBox);

                                        adminNameBox.clear();
                                        st = conn.createStatement();
                                        rs = st.executeQuery("select employees_uid from administrators;");
                                        while (rs.next()) {
                                            //system.out.println(rs.getString(1));
                                            uidToDel = rs.getString(1);
                                            rs = st.executeQuery("select fname, lname from employees where uid = " + rs.getString(1) + ";");
                                            while (rs.next()) {
                                                //system.out.println(rs.getString(1));
                                                //system.out.println(rs.getString(2));
                                                adminNameBox.add(rs.getString(2) + ", " + rs.getString(1) + ":" + uidToDel);
                                            }
                                        }
                                        adminObs = FXCollections.observableArrayList(adminNameBox);
                                        empAdminListView.setItems(adminObs);
                                        updateAdminList();

                                    } catch (SQLException ex) {
                                        Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                }
                                if (new_val == "Create/Delete a JobCenter User") {
                                    empNameBox.clear();

                                    //make the connection
                                    try {
                                        st = conn.createStatement();
                                        rs = st.executeQuery("select fname, lname, uid from employees;");
                                        while (rs.next()) {
                                            ////system.out.println(rs.getString(1));
                                            empNameBox.add(rs.getString(2) + ", " + rs.getString(1) + ", " + rs.getString(3));
                                        }

                                    } catch (SQLException ex) {
                                        Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    updateEmailList();
                                    empListUsr.setItems(empNameBox);

                                    usersPane.setVisible(true);
                                }

                            } else {
                                adminList.disableProperty().set(true);
                            }

                        } catch (UnknownHostException ex) {
                            Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });
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
                                    ////system.out.println(rs.getString(1));
                                    taskListBox.add(rs.getString(1));
                                }

                            } catch (SQLException ex) {
                                Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            ObservableList<String> emp = FXCollections.observableArrayList(empList);
                            ObservableList<String> vehEquip = FXCollections.observableArrayList(vehList);
                            ObservableList<String> custListingObs = FXCollections.observableArrayList(custList);

                            //set items on the job form
                            //employeeSelect.setItems(emp);
                            vehicleEquipSelect.setItems(vehEquip);
                            custListing.setItems(custListingObs);
                            custListing.getSelectionModel().selectNext();
                            employeeSelect.getSelectionModel().selectNext();
                            vehicleEquipSelect.getSelectionModel().selectNext();

                            taskComboBox.setItems(taskListBox);
                            CreateJobBox.setVisible(true);
                            jobStatus.setItems(jStatus);

                            //refresh equip/veh
                            refreshVeh();

                            refreshEmp();
                        }
                        if (new_val == "Display jobs") {

                            displayJobs.setVisible(true);

                            ObservableList<String> emp2 = FXCollections.observableArrayList(empList);
                            ObservableList<String> vehEquip2 = FXCollections.observableArrayList(vehList);

                            empAddJobView.setItems(emp2);
                            vehAddJobView.setItems(vehEquip2);

                            //refresh the available/assigned things 
                            checkAssignedEquip();

                            //adds the treeview here!
                            refreshList();

                            //refresh the veh/equip view
                            refreshVeh();

                            refreshEmp();

                        }
                        if (new_val == "Changelog") {
                            usrHistTable.setItems(populateHist());
                            userHist.setVisible(true);
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
        //system.out.println("tester");
        //Node node = new GridPane();

        st = conn.createStatement();
        String qry = "select * from currentjobs where status ='IN PROGRESS';";
        ////system.out.println("qry: " + qry);
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

            ////system.out.println(jobTxtStr);
            ////system.out.println("Count: " + Integer.toString(countAmt));
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

            ////system.out.println("Index: " + empListStr.indexOf("/"));
            ////system.out.println("at row: " + countAmt);
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

                    ////system.out.println("Unprocessed string: " + empListStr);
                    ////system.out.println("Before: " + empListStr);
                    ////system.out.println(empListStr.indexOf("/"));
                    if (empListStr.indexOf("/") == 0) {
                        empListStr = empListStr.substring(1, empListStr.length());

                        if (empListStr.indexOf("/") > 0) {
                            nameOfPerson = empListStr.substring(0, empListStr.indexOf("/"));
                            empListStr = empListStr.substring(empListStr.indexOf("/"), empListStr.length());
                        } else {
                            nameOfPerson = empListStr.substring(0, empListStr.length());
                        }

                        //empListStr = empListStr.substring(empListStr.indexOf("/") + 1, empListStr.length());
                        ////system.out.println("After: " + empListStr);
                        ////system.out.println("Adding: " + nameOfPerson);
                        TextField nameTxt = new TextField();
                        nameTxt.setMaxWidth(100);
                        nameTxt.setStyle("-fx-background-color: lightgreen;"
                                + "-fx-font-size: 12;");

                        nameTxt.setMaxHeight(100);
                        nameTxt.setEditable(false);
                        nameTxt.setText(nameOfPerson);
                        nameTxt.setAlignment(Pos.CENTER);

                        grid.add(nameTxt, countAmt, counterArea);
                        ////system.out.println("at row: " + countAmt);
                        counterArea++;

                    }
                } else {
                    ////system.out.println("Adding2: " + empListStr);
                    nameOfPerson = empListStr;
                    TextField nameTxt = new TextField();
                    nameTxt.setStyle("-fx-background-color: green;"
                            + "-fx-font-size: 12;");
                    nameTxt.setMaxWidth(100);
                    nameTxt.setEditable(false);
                    nameTxt.setText(nameOfPerson);
                    nameTxt.setAlignment(Pos.CENTER);

                    grid.add(nameTxt, countAmt, counterArea);
                    ////system.out.println("at row2: " + countAmt);
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
                    ////system.out.println("\n\nUnprocessed string: " + equipListStr);
                    equipNameStr = "";
                    ////system.out.println("Before: " + equipListStr);
                    ////system.out.println(equipListStr.indexOf("/"));
                    if (equipListStr.indexOf("/") > 0) {
                        equipNameStr = equipListStr.substring(0, equipListStr.indexOf("/"));
                        equipListStr = equipListStr.substring(equipListStr.indexOf("/"), equipListStr.length());
                        ////system.out.println("After: " + equipListStr);

                        ////system.out.println("Adding: " + equipNameStr);
                        TextField nameTxt = new TextField();

                        nameTxt.setStyle("-fx-background-color: yellow;"
                                + "-fx-font-size: 12;");
                        nameTxt.setMaxWidth(100);
                        nameTxt.setMaxHeight(70);
                        nameTxt.setEditable(false);
                        nameTxt.setText(equipNameStr);
                        nameTxt.setAlignment(Pos.CENTER);

                        grid.add(nameTxt, countAmt, counterArea);
                        ////system.out.println("at row: " + countAmt);
                        counterArea++;

                    }
                } else {
                    ////system.out.println("Adding2: " + equipListStr);
                    equipNameStr = equipListStr;
                    TextField nameTxt = new TextField();

                    nameTxt.setStyle("-fx-background-color: yellow;"
                            + "-fx-font-size: 12;");
                    nameTxt.setMaxWidth(100);
                    nameTxt.setEditable(false);
                    nameTxt.setText(equipNameStr);
                    nameTxt.setAlignment(Pos.CENTER);

                    grid.add(nameTxt, countAmt, counterArea);
                    ////system.out.println("at row2: " + countAmt);
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
        //PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
        PageLayout pageLayout = printer.createPageLayout(Paper.A0, PageOrientation.REVERSE_LANDSCAPE, Printer.MarginType.EQUAL);
        /*  double scaleX = pageLayout.getPrintableWidth() / 1224;
         double scaleY = pageLayout.getPrintableHeight() / 662;
         */
        // root.getTransforms().add(new Scale(scaleX, scaleY));

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
        int indexNode = 1;

        //get veh/equip name selected
        String vehStrConv = vehAddJobView.getSelectionModel().selectedItemProperty().getValue().toString();

        //system.out.println("Root: " + currentJobsDisplay.getSelectionModel().selectedItemProperty().getBean());
        //system.out.println("Index: " + currentJobsDisplay.getSelectionModel().getSelectedIndex());
        ////system.out.println("RootStr: " + currentJobsDisplay.getChildrenUnmodifiable().get(currentJobsDisplay.getSelectionModel().getSelectedIndex()));
        //system.out.println("Adding: " + vehStrConv + " to job: " + getJobTitle);

        indexNode = currentJobsDisplay.getSelectionModel().getSelectedIndex();

        //check make sure job exists
        //make the connection
        try {
            st = conn.createStatement();

            String qryCurJob = "select CurJobID, JobEandV from currentJobs where JobTitle = '" + getJobTitle + "';";

            rs = st.executeQuery(qryCurJob);
            if (rs.next()) {
                String idMod = rs.getString(1);
                String vehList = rs.getString(2);
                //system.out.println("JobID: " + idMod);
                //system.out.println("employess: " + vehList);

                if (empExist(vehList, vehStrConv)) {
                    //system.out.println("Name Exists!!");
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
                    if (howManyVehEq(getJobTitle) >= 5) {
                        displayMsg("Cannot add more than 5 items.");
                        return;
                    }

                    ////system.out.println("Name not found, adding to job!!");
                    vehList += "/" + vehStrConv;

                    String addEmpStrQry = "update currentjobs set "
                            + "JobEandV='" + vehList
                            + "' where CurJobID = " + idMod;

                    Statement updateDb = null;

                    //set our session id and ip address in order to identify user
                    updateDb = conn.createStatement();

                    //mark the vehicle/equip as unavailable
                    updateVehStatus("ASSIGNED", vehStrConv);

                    //refresh the veh/equip views
                    refreshVeh();

                    //execute the query
                    int executeUpdate = updateDb.executeUpdate(addEmpStrQry);

                    //refresh the tree nodes and expand changed item
                    refreshList();
                    currentJobsDisplay.getTreeItem(indexNode).setExpanded(true);

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

        vehAddJobView.getSelectionModel().selectNext();
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

       // //system.out.println("Root: " + currentJobsDisplay.getSelectionModel().selectedItemProperty().getBean());
        ////system.out.println("Index: " + currentJobsDisplay.getSelectionModel().getSelectedIndex());
        ////system.out.println("RootStr: " + currentJobsDisplay.getChildrenUnmodifiable().get(currentJobsDisplay.getSelectionModel().getSelectedIndex()));
       // //system.out.println("Adding: " + empStrConv + " to job: " + getJobTitle);

        //check make sure job exists
        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);
            st = conn.createStatement();
            String qryCurJob = "select CurJobID, JobEmployees from currentJobs where JobTitle = '" + getJobTitle + "';";
            int indexNode = 1;

            rs = st.executeQuery(qryCurJob);
            indexNode = currentJobsDisplay.getSelectionModel().getSelectedIndex();
            if (rs.next()) {
                String idMod = rs.getString(1);
                String empList = rs.getString(2);
               // //system.out.println("JobID: " + idMod);
               // //system.out.println("employess: " + empList);

                if (empExist(empList, empStrConv)) {
                  //  //system.out.println("Name Exists!!");
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
                    if (howManyPeople(getJobTitle) >= 5) {
                        displayMsg("Cannot add more than 5 items.");
                        return;
                    }

                    // //system.out.println("Name not found, adding to job!!");
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
                    currentJobsDisplay.getTreeItem(indexNode).setExpanded(true);

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

        empAddJobView.getSelectionModel().selectNext();

        //if not exist message to user that what they selected is not a job
        //else add the employee/vehicle in 
        String qryRun = "";
    }

    @FXML
    private void previewJobAction(ActionEvent event) throws SQLException {

        if (howManyRowsPrinter() <= 0) {
            displayMsg("Nothing to display.");
            return;

        }

        final Group root = new Group();
        Button printScreen = new Button("Print");

        Rectangle r = new Rectangle(0, 0, 1120, 40);
        r.setFill(Color.LIGHTGREY);
        r.strokeProperty().set(Color.GRAY);

        Rectangle r2 = new Rectangle(0, 42, 1120, 18);
        r2.setFill(Color.YELLOW);
        r2.strokeProperty().set(Color.YELLOW);

        Rectangle r3 = new Rectangle(0, 342, 1120, 18);
        r3.setFill(Color.YELLOW);
        r3.strokeProperty().set(Color.YELLOW);

        GridPane gridpane = new GridPane();

        String qry = "select * from currentjobs where status ='IN PROGRESS';";

        List<String> empListSort = new ArrayList<String>();
        List<String> equipListSort = new ArrayList<String>();
        String jobTxtStr = "", jobTypeStr = "", jobDateTxtStr = "", jobStatusStr = "",
                empListStr = "", equipListStr = "", jobNameStr = "";

        //set location and build of grid pane...
        gridpane.setLayoutY(40);
        int startAnew = 9, empNumber = 0, vehNumber = 0, numLocation = 0, columnLoc = 0,
                columnLoc2 = 1;

        //set location and build of grid pane...
        gridpane.setLayoutY(40);
        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints(20);
            row.setValignment(VPos.CENTER);

            gridpane.getRowConstraints().add(row);

        }
        for (int i = 0; i < 10; i++) {
            ColumnConstraints column = new ColumnConstraints(112);
            column.setHalignment(HPos.CENTER);
            gridpane.getColumnConstraints().add(column);
        }

        // or convenience methods set more than one constraint at once...
        Text label = new Text("Job Number"),
                label1 = new Text("Status"),
                label2 = new Text("Job Name"),
                label3 = new Text("Work Type"),
                label4 = new Text("Work Date"),
                labelTitle = new Text("Job Summary Report: "),
                labelComp = new Text("Video Pipe Service");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();

        String dateToday = (dateFormat.format(cal.getTime()));
        Text labelDate = labelDate = new Text(dateToday);

        label.setStyle("-fx-font-size: 10;");
        label1.setStyle("-fx-font-size: 10;");
        label2.setStyle("-fx-font-size: 10;");
        label3.setStyle("-fx-font-size: 10;");
        label4.setStyle("-fx-font-size: 10;");

        labelTitle.setStyle("-fx-font-size: 16;");
        labelTitle.setY(25);
        labelTitle.setX(10);
        labelDate.setStyle("-fx-font-size: 15;");
        labelDate.setY(25);
        labelDate.setX(175);
        labelComp.setStyle("-fx-font-size: 16;");
        labelComp.setY(25);
        labelComp.setX(965);
        printScreen.setLayoutX(265);
        printScreen.setLayoutY(7);

        gridpane.add(label, 0, 0); // column=2 row=1        
        gridpane.add(label1, 0, 1);  // column=3 row=1
        gridpane.add(label2, 0, 2); // column=1 row=1
        gridpane.add(label3, 0, 3); // column=2 row=1
        gridpane.add(label4, 0, 4); // column=2 row=1
        numLocation = 5;
        columnLoc = 1;
        //gridlines for preview job board
        gridpane.setGridLinesVisible(true);

        for (int i = 1; i <= 5; i++) {
            RowConstraints row = new RowConstraints(20);
            row.setValignment(VPos.CENTER);
            String nameLabel = "Person " + i;
            Text tmpTxt = new Text(nameLabel);
            tmpTxt.setStyle("-fx-font-size: 10;");

            gridpane.getRowConstraints().add(row);
            gridpane.add(tmpTxt, 0, numLocation);

            //increment to track which column we are at.
            numLocation++;

        }
        for (int i = 1; i <= 5; i++) {
            RowConstraints row = new RowConstraints(20);
            row.setValignment(VPos.CENTER);
            String nameLabel = "Veh/Equip " + i;
            Text tmpTxt = new Text(nameLabel);
            tmpTxt.setStyle("-fx-font-size: 10;");

            gridpane.getRowConstraints().add(row);
            gridpane.add(tmpTxt, 0, numLocation);

            //increment to track which column we are at.
            numLocation++;

        }

        rs = st.executeQuery(qry);

        while (rs.next()) {

            int counter = 0;

            jobTxtStr = rs.getString(5);
            jobTypeStr = rs.getString(9);
            if (jobTypeStr.length() > 8) {
                jobTypeStr = jobTypeStr.substring(1, 8);
            } else {
                jobTypeStr = jobTypeStr.substring(1, jobTypeStr.length());
            }

            jobDateTxtStr = rs.getString(7);
            jobStatusStr = rs.getString(17);
            jobNameStr = rs.getString(6);
            empListStr = rs.getString(10);
            equipListStr = rs.getString(11);

            Text add;

            if (columnLoc <= startAnew) {
                add = new Text(jobTxtStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;
                add = new Text(jobStatusStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;
                add = new Text(jobNameStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;
                add = new Text(jobTypeStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;
                add = new Text(jobDateTxtStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;

                String strManip = empListStr;

                while (strManip.lastIndexOf("/") >= 0) {

                    add = new Text(strManip.substring(strManip.lastIndexOf("/") + 1, strManip.length()));
                    add.setStyle("-fx-font-size: 10;");
                    gridpane.add(add, columnLoc, counter);
                    counter++;

                    strManip = strManip.substring(0, strManip.lastIndexOf("/"));

                }

                counter = 10;
                strManip = equipListStr;
                while (strManip.lastIndexOf("/") >= 0) {
                    //system.out.println(strManip);
                    add = new Text(strManip.substring(strManip.lastIndexOf("/") + 1, strManip.length()));
                    add.setStyle("-fx-font-size: 10;");
                    gridpane.add(add, columnLoc, counter);
                    counter++;

                    strManip = strManip.substring(0, strManip.lastIndexOf("/"));
                    //system.out.println(strManip);

                }

            } else {

                counter = 15;
                //system.out.println("TIME TO PRINT ANOTHER TABLE");

                for (int i = 0; i < 14; i++) {
                    RowConstraints row = new RowConstraints(20);
                    row.setValignment(VPos.CENTER);

                    gridpane.getRowConstraints().add(row);

                }

                // or convenience methods set more than one constraint at once...
                Text label00 = new Text("Job Number"),
                        label11 = new Text("Status"),
                        label22 = new Text("Job Name"),
                        label33 = new Text("Work Type"),
                        label44 = new Text("Work Date");

                label00.setStyle("-fx-font-size: 10;");
                label11.setStyle("-fx-font-size: 10;");
                label22.setStyle("-fx-font-size: 10;");
                label33.setStyle("-fx-font-size: 10;");
                label44.setStyle("-fx-font-size: 10;");

                gridpane.add(label00, 0, 15); // column=2 row=1        
                gridpane.add(label11, 0, 16);  // column=3 row=1
                gridpane.add(label22, 0, 17); // column=1 row=1
                gridpane.add(label33, 0, 18); // column=2 row=1
                gridpane.add(label44, 0, 19); // column=2 row=1
                numLocation = 20;

                for (int i = 1; i <= 5; i++) {
                    RowConstraints row = new RowConstraints(20);
                    row.setValignment(VPos.CENTER);
                    String nameLabel = "Person " + i;
                    Text tmpTxt = new Text(nameLabel);
                    tmpTxt.setStyle("-fx-font-size: 10;");

                    gridpane.getRowConstraints().add(row);
                    gridpane.add(tmpTxt, 0, numLocation);

                    //increment to track which column we are at.
                    numLocation++;

                }

                for (int i = 1; i <= 5; i++) {
                    RowConstraints row = new RowConstraints(20);
                    row.setValignment(VPos.CENTER);
                    String nameLabel = "Veh/Equip " + i;
                    Text tmpTxt = new Text(nameLabel);
                    tmpTxt.setStyle("-fx-font-size: 10;");

                    gridpane.getRowConstraints().add(row);
                    gridpane.add(tmpTxt, 0, numLocation);

                    //increment to track which column we are at.
                    numLocation++;

                }

                jobTxtStr = rs.getString(5);
                jobTypeStr = rs.getString(9);
                if (jobTypeStr.length() > 8) {
                    jobTypeStr = jobTypeStr.substring(1, 8);
                } else {
                    jobTypeStr = jobTypeStr.substring(1, jobTypeStr.length());
                }

                jobDateTxtStr = rs.getString(7);
                jobStatusStr = rs.getString(17);
                jobNameStr = rs.getString(6);
                empListStr = rs.getString(10);
                equipListStr = rs.getString(11);

                Text add2;

                add2 = new Text(jobTxtStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;
                add2 = new Text(jobStatusStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;
                add2 = new Text(jobNameStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;
                add2 = new Text(jobTypeStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;
                add2 = new Text(jobDateTxtStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;

                String strManip = empListStr;

                while (strManip.lastIndexOf("/") >= 0) {

                    add2 = new Text(strManip.substring(strManip.lastIndexOf("/") + 1, strManip.length()));
                    add2.setStyle("-fx-font-size: 10;");
                    gridpane.add(add2, columnLoc2, counter);
                    counter++;

                    strManip = strManip.substring(0, strManip.lastIndexOf("/"));

                }
                /*
                 counter = 25;
                 strManip = equipListStr;
                 while (strManip.lastIndexOf("/") >= 0) {
                 //system.out.println(strManip);
                 add2 = new Text(strManip.substring(strManip.lastIndexOf("/") + 1, strManip.length()));
                 add2.setStyle("-fx-font-size: 10;");
                 gridpane.add(add2, columnLoc2, counter);
                 counter++;

                 strManip = strManip.substring(0, strManip.lastIndexOf("/"));
                 //system.out.println(strManip);

                 }*/
                columnLoc2++;
            }
            columnLoc++;
        }

        root.getChildren().add(r);
        root.getChildren().add(r2);
        root.getChildren().add(r3);
        root.getChildren().add(printScreen);
        root.getChildren().add(labelTitle);
        root.getChildren().add(labelDate);
        root.getChildren().add(labelComp);

        root.getChildren().add(gridpane);

        Scene scene2 = new Scene(root);
        stageJob = new Stage();
        stageJob.setHeight(700);
        stageJob.setWidth(1224);
        stageJob.setResizable(false);

        stageJob.setScene(scene2);
        stageJob.setResizable(false);

        // which screen to display the popup on ....
        javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getScreens().get(0).getVisualBounds();
        stageJob.setX(primaryScreenBounds.getMinX());
        stageJob.setY(primaryScreenBounds.getMinY());

        printScreen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Printer printer = Printer.getDefaultPrinter();
                PageLayout pageLayout = printer.createPageLayout(Paper.LEGAL, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);

                //double scaleX = pageLayout.getPrintableWidth() / 1524;
                //double scaleY = pageLayout.getPrintableHeight() / 682;
                //root.getTransforms().add(new Scale(scaleX, scaleY));
                //this is the landscape printing that needs to be fixed..... printer function adjustment
                //figure out how many rows 1 or 2
                // if more than 9 then 2 rows
                // if 9 or less then just 1 row
                if (howManyRowsPrinter() == 1) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(385);
                    root.setLayoutX(-228);
                }
                if (howManyRowsPrinter() == 2) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(385);
                    root.setLayoutX(-228);
                }
                if (howManyRowsPrinter() == 3) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(385);
                    root.setLayoutX(-228);
                }

                if (howManyRowsPrinter() == 4) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(385);
                    root.setLayoutX(-228);
                }

                if (howManyRowsPrinter() == 5) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(385);
                    root.setLayoutX(-228);
                }

                if (howManyRowsPrinter() == 6) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(385);
                    root.setLayoutX(-252);
                }

                if (howManyRowsPrinter() == 7) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(385);
                    root.setLayoutX(-252);
                }

                if (howManyRowsPrinter() == 8) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(385);
                    root.setLayoutX(-252);
                }

                if (howManyRowsPrinter() == 9) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(385);
                    root.setLayoutX(-252);
                }
                if (howManyRowsPrinter() == 10) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(145);
                    //root.setLayoutX(-924);
                    root.setLayoutX(-483);
                }
                if (howManyRowsPrinter() == 11) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(-96);
                    //root.setLayoutX(-924);
                    root.setLayoutX(-724);
                }
                if (howManyRowsPrinter() == 12) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);

                    root.setLayoutY(-335);
                    //root.setLayoutX(-924);
                    root.setLayoutX(-963);

                    //root.setLayoutY(200);
                    //root.setLayoutX(-124);
                    //root.setLayoutY(-100);
                    //root.setLayoutX(-724);
                }
                if (howManyRowsPrinter() == 13) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(-577);
                    //root.setLayoutX(-924);
                    root.setLayoutX(-1207);
                }
                if (howManyRowsPrinter() == 14) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(-815);
                    //root.setLayoutX(-924);
                    root.setLayoutX(-1443);
                }
                if (howManyRowsPrinter() == 15) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(-1058);
                    //root.setLayoutX(-924);
                    root.setLayoutX(-1682);
                }
                if (howManyRowsPrinter() == 16) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(-1297);
                    //root.setLayoutX(-924);
                    root.setLayoutX(-1923);
                }
                if (howManyRowsPrinter() == 17) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(-1538);
                    //root.setLayoutX(-924);
                    root.setLayoutX(-2164);
                }
                if (howManyRowsPrinter() == 18) {
                    double scaleX = pageLayout.getPrintableWidth() / 1524;
                    double scaleY = pageLayout.getPrintableHeight() / 682;

                    root.getTransforms().add(new Scale(scaleX, scaleY));
                    root.setRotate(90);
                    root.setLayoutY(-1776);
                    //root.setLayoutX(-924);
                    root.setLayoutX(-2403);
                }

                PrinterJob job = PrinterJob.createPrinterJob();

                if (job != null) {

                    boolean success = job.printPage(root);

                    if (success) {

                        job.endJob();
                        stageJob.close();
                    }
                }

            }

        });

        stageJob.show();

    }

    @FXML
    private void displayJobAction(ActionEvent event) throws IOException, SQLException {
        //Parent root = FXMLLoader.load(getClass().getResource("DisplayJobBoard.fxml")); 
        st = conn.createStatement();
        String qry = "select * from currentjobs where status ='IN PROGRESS';";
        ////system.out.println("qry: " + qry);
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

            ////system.out.println(jobTxtStr);
            ////system.out.println("Count: " + Integer.toString(countAmt));
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

            ////system.out.println("Index: " + empListStr.indexOf("/"));
            ////system.out.println("at row: " + countAmt);
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

                    ////system.out.println("Unprocessed string: " + empListStr);
                    ////system.out.println("Before: " + empListStr);
                    ////system.out.println(empListStr.indexOf("/"));
                    if (empListStr.indexOf("/") == 0) {
                        empListStr = empListStr.substring(1, empListStr.length());

                        if (empListStr.indexOf("/") > 0) {
                            nameOfPerson = empListStr.substring(0, empListStr.indexOf("/"));
                            empListStr = empListStr.substring(empListStr.indexOf("/"), empListStr.length());
                        } else {
                            nameOfPerson = empListStr.substring(0, empListStr.length());
                        }

                        //empListStr = empListStr.substring(empListStr.indexOf("/") + 1, empListStr.length());
                        ////system.out.println("After: " + empListStr);
                        ////system.out.println("Adding: " + nameOfPerson);
                        TextField nameTxt = new TextField();
                        nameTxt.setMaxWidth(150);
                        nameTxt.setStyle("-fx-background-color: lightgreen;"
                                + "-fx-font-size: 15;");

                        nameTxt.setMaxHeight(100);
                        nameTxt.setEditable(false);
                        nameTxt.setText(nameOfPerson);
                        nameTxt.setAlignment(Pos.CENTER);

                        grid.add(nameTxt, countAmt, counterArea);
                        ////system.out.println("at row: " + countAmt);
                        counterArea++;

                    }
                } else {
                    ////system.out.println("Adding2: " + empListStr);
                    nameOfPerson = empListStr;
                    TextField nameTxt = new TextField();
                    nameTxt.setStyle("-fx-background-color: green;"
                            + "-fx-font-size: 15;");
                    nameTxt.setMaxWidth(150);
                    nameTxt.setEditable(false);
                    nameTxt.setText(nameOfPerson);
                    nameTxt.setAlignment(Pos.CENTER);

                    grid.add(nameTxt, countAmt, counterArea);
                    ////system.out.println("at row2: " + countAmt);
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
                    int strLenTester = equipListStr.length();

                    equipListStr = equipListStr.substring(1, strLenTester);
                    //system.out.println("\n\nUnprocessed string: " + equipListStr);
                    equipNameStr = "";
                    ////system.out.println("Before: " + equipListStr);
                    ////system.out.println(equipListStr.indexOf("/"));
                    if (equipListStr.indexOf("/") > 0) {
                        equipNameStr = equipListStr.substring(0, equipListStr.indexOf("/"));
                        equipListStr = equipListStr.substring(equipListStr.indexOf("/"), equipListStr.length());
                        ////system.out.println("After: " + equipListStr);

                        ////system.out.println("Adding: " + equipNameStr);
                        TextField nameTxt = new TextField();

                        nameTxt.setStyle("-fx-background-color: yellow;"
                                + "-fx-font-size: 15;");
                        nameTxt.setMaxWidth(150);
                        nameTxt.setMaxHeight(70);
                        nameTxt.setEditable(false);
                        nameTxt.setText(equipNameStr);
                        nameTxt.setAlignment(Pos.CENTER);

                        grid.add(nameTxt, countAmt, counterArea);
                        ////system.out.println("at row: " + countAmt);
                        counterArea++;

                    }
                } else {
                    ////system.out.println("Adding2: " + equipListStr);
                    equipNameStr = equipListStr;
                    TextField nameTxt = new TextField();

                    nameTxt.setStyle("-fx-background-color: yellow;"
                            + "-fx-font-size: 15;");
                    nameTxt.setMaxWidth(150);
                    nameTxt.setEditable(false);
                    nameTxt.setText(equipNameStr);
                    nameTxt.setAlignment(Pos.CENTER);

                    grid.add(nameTxt, countAmt, counterArea);
                    ////system.out.println("at row2: " + countAmt);
                    empListStr = "";
                    counterArea++;

                    break;
                }

            }

            countAmt++;
        }

        //starting point -- width -- height
        //rectangle((x,y),width,height)
        final Rectangle rectBasicTimeline = new Rectangle(0, 500, 400, 60);
        rectBasicTimeline.setFill(Color.RED);

        final Text txtTimeline = new Text(-100, 500, scrollingTxt);

        // txtTimeline.setStyle("-fx-font-size: 50;" + "-fx-background-color: yellow;");
        // txtTimeline.setTextAlignment(TextAlignment.CENTER);
        txtTimeline.setFill(Color.WHITE);
        //txtTimeline.setTextOrigin(VPos.CENTER);
        txtTimeline.setFont(Font.font("courier", 45));
        txtTimeline.setFontSmoothingType(FontSmoothingType.LCD);

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        final KeyValue kv = new KeyValue(txtTimeline.xProperty(), 2000);
        final KeyFrame kf = new KeyFrame(Duration.millis(9999), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        root.getChildren().addAll(txtTimeline);
        root.getChildren().add(grid);

        Scene scene2 = new Scene(root, Color.BLACK);
        stageJob = new Stage();
       // stageJob.setX(Screen.getScreens().get(2).getVisualBounds().getMinX());
        // stageJob.setY(Screen.getScreens().get(2).getVisualBounds().getMinY());
        //stageJob.setFullScreen(true); 
        //stageJob.setResizable(false);

        //set the dimesions to the screen size:
        //stageJob.setWidth(Screen.getScreens().get(2).getVisualBounds().getWidth());
        //stageJob.setHeight(Screen.getScreens().get(2).getVisualBounds().getHeight());
        //change screens here  --- change to 2 for Video Pipe Services production
        javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getScreens().get(2).getVisualBounds();
        stageJob.setX(primaryScreenBounds.getMinX());
        stageJob.setY(primaryScreenBounds.getMinY());

        stageJob.setFullScreen(true);

        stageJob.setX(Screen.getScreens().get(2).getVisualBounds().getMinX());
        stageJob.setY(Screen.getScreens().get(2).getVisualBounds().getMinY());
        stageJob.setWidth(Screen.getScreens().get(2).getVisualBounds().getWidth());
        stageJob.setHeight(Screen.getScreens().get(2).getVisualBounds().getHeight());

        //stageJob.initStyle(StageStyle.UNDECORATED);
        stageJob.setScene(scene2);
        stageJob.show();
    }

    @FXML
    private void deleteEquipAction(ActionEvent event) throws IOException, SQLException {
        String vnamestr = equipmentTable.getSelectionModel().selectedItemProperty().getValue().getVeh();
        String queryDelete = "DELETE FROM vehicles WHERE VehicleName = '" + vnamestr + "'";
        ////system.out.println(queryDelete);

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
        ////system.out.println("Add EQUIP: " + queryRunNow);

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
    private void addTaskList(ActionEvent event
    ) {
        String itemChosen = taskComboBox.getValue().toString();
        itemChosen += "," + diamStr.getText() + ",";
        itemChosen += feetStr.getText();

        ////system.out.println(itemChosen);
        taskTypeListStr.add(itemChosen);
        taskTypeList.setItems(taskTypeListStr);
        jobName.setText(taskComboBox.getValue().toString());

    }

    @FXML
    private void addEmpJob(ActionEvent event) throws SQLException {
        String val = employeeSelect.getSelectionModel().selectedItemProperty().getValue().toString();
        ////system.out.println(val);        

        if (empListSelected.size() < 5) {
            if (!empListSelected.contains(val)) {
                empListSelected.add(val);
            }
        } else {
            displayMsg("Cannot add more than 5 items per job.");
            return;
        }

        empSelect = FXCollections.observableArrayList(empListSelected);
        employeeSelected.setItems(empSelect);
    }

    @FXML
    private void deleteEmp(ActionEvent event) throws SQLException {
        try {
            String del = employeeSelected.getSelectionModel().selectedItemProperty().getValue().toString();
            ////system.out.println("delete: " + del);
            for (int i = 0; i < empListSelected.size(); i++) {
                if (empListSelected.get(i).toString().equals(del)) {
                    empListSelected.remove(i);
                }
            }
            empSelected = FXCollections.observableArrayList(empListSelected);
            employeeSelected.setItems(empSelected);
            employeeSelected.getSelectionModel().selectNext();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void updateVehStatus(String changeStatus, String VehName) {
        String queryDelete = "update vehicles set VehicleStatus = '" + changeStatus + "' where VehicleName = '" + VehName + "'";
        ////system.out.println(queryDelete);

        //insert into database
        Statement updateDb = null;

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user.
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryDelete);

            //may need to refresh the 2 vehicle tables
        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addVehEquip(ActionEvent event) throws SQLException {
        String val = vehicleEquipSelect.getSelectionModel().selectedItemProperty().getValue().toString();
        ////system.out.println(val);

        if (vehList.size() < 5) {
            if (!vehList.contains(val)) {
                vehList.add(val);
            }
        } else {
            displayMsg("Cannot add more than 5 items per job.");
            return;
        }
        vehList11 = FXCollections.observableArrayList(vehList);
        vehicleEquipSelected.setItems(vehList11);

        //mark the vehicle/equip as available
        updateVehStatus("ASSIGNED", val);

        refreshVeh();

        vehicleEquipSelect.getSelectionModel().selectNext();

    }

    @FXML
    private void deleteVeh(ActionEvent event) throws SQLException {
        try {
            String del = vehicleEquipSelected.getSelectionModel().selectedItemProperty().getValue().toString();
            ////system.out.println("delete: " + del);
            for (int i = 0; i < vehList.size(); i++) {
                if (vehList.get(i).toString().equals(del)) {
                    vehList.remove(i);
                }
            }
            vehList11 = FXCollections.observableArrayList(vehList);
            vehicleEquipSelected.setItems(vehList11);

            //mark the vehicle/equip as available
            updateVehStatus("AVAILABLE", del);

            //refresh the lists of vehicle/equip
            refreshVeh();

            vehicleEquipSelected.getSelectionModel().selectNext();
            vehicleEquipSelect.getSelectionModel().selectNext();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @FXML
    private void addCustButAction(ActionEvent event) throws SQLException {
        custAdd = custListing.getSelectionModel().selectedItemProperty().getValue().toString();

        st = conn.createStatement();
        String qry = "select * from customer where CompanyName ='" + custAdd.trim() + "';";
        ////system.out.println("qry: " + qry);

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
        setCustPOC.setText(pocName);
        setCustCompPhone.setText(phone);
        setCustFax.setText(fax);
        setCustAddr.setText(streetAddrStr);
        setCustZip.setText(zipStr);
    }

    @FXML
    private void delTaskList(ActionEvent event
    ) {
        String del = taskTypeList.getSelectionModel().selectedItemProperty().getValue().toString();
        ////system.out.println("delete: " + del);
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

        //check if jobtitle doesn't already exist in db
        //the same jobtitle will result in duplicate entries
        if (checkIfJobExists(jobTitleStr)) {
            displayMsg("Job Number already exists!");
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal123 = Calendar.getInstance();
        String dateTimeStamp = dateFormat.format(cal123.getTime());

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

        if (jobTitleStr.equals("")) {
            displayMsg("Job Title Information Missing.");
        } else if (jobStatus.getSelectionModel().isEmpty()) {
            displayMsg("Job Status Missing.");
        } else if (jobNameStr.equals("")) {
            displayMsg("Job Name Information Missing.");
        } else if (custJobNumStr.equals("")) {
            displayMsg("Cust Job No. Information Missing.");
        } else if (startDateStr.equals("")) {
            displayMsg("Start Date Information Missing.");
        } /*else if (custJobNameStr.equals("")) {
         displayMsg("Cust Job Name Information Missing.");
         } else if (startDateStr.equals("")) {
         displayMsg("Start Date Information Missing.");
         } else if (startTimeStr.equals("")) {
         displayMsg("Start Time Information Missing.");
         } else if (sI.equals("")) {
         displayMsg("Special Instructions Missing.");
         } else if (dI.equals("")) {
         displayMsg("Disposal Missing.");
         } else if (tI.equals("")) {
         displayMsg("Traffic Control Missing.");
         } else if (wI.equals("")) {
         displayMsg("Water Source Missing.");
         } 
        
         */ else {
            status = jobStatus.getSelectionModel().selectedItemProperty().getValue().toString();

            //UPDATE THE LOGGING DATABASE TABLE
            String queryRunNow = "insert into edithistory (DateEdited, JobName,usrFname, usrLname, EmpEditorID,  description) "
                    + " values('" + dateTimeStamp + "','" + jobTitleStr + "',"
                    + "(select fname from employees where uid = " + empID + "),"
                    + "(select lname from employees where uid = " + empID + "),"
                    + empID + ",'Job Creation'"
                    + statusNew.getText() + ");";

            //insert into database
            Statement updateLog = null;

            //make the connection
            try {
                conn = DriverManager.getConnection(url, userdb, passdb);

                //set our session id and ip address in order to identify user
                updateLog = conn.createStatement();

                usrHistTable.setItems(populateHist());

                int executeUpdate = updateLog.executeUpdate(queryRunNow);
            } catch (SQLException e) {
                System.err.println(e);
            }

            //compile job types 
            for (int i = 0; i < taskTypeListStr.size(); i++) {
                jobtypecompiled += "/" + taskTypeListStr.get(i);
            }

            //compile employees  
            for (int j = 0; j < empListSelected.size(); j++) {
                empCompiled += "/" + empListSelected.get(j);
            }

            //compile equipment  
            for (int k = 0; k < vehList.size(); k++) {
                equipCompiled += "/" + vehList.get(k);
            }

            if (equipCompiled.equals("")) {
                displayMsg("Equipment Missing.");

            } else if (jobtypecompiled.equals("")) {
                displayMsg("Job Type Missing.");

            } else if (empCompiled.equals("")) {
                displayMsg("Employees Missing.");
            } else if (cid.equals("")) {
                displayMsg("Customer not selected.");
            } else {
                ////system.out.println("CID: " + cid);
                ////system.out.println("Job name: " + jobNameStr);
                ////system.out.println("Cust job #: " + custJobNumStr);
                ////system.out.println("Cust job name: " + custJobNameStr);
                ////system.out.println("start date: " + startDateStr);
                ////system.out.println("start time: " + startTimeStr);
                ////system.out.println("street: " + streetAddr.getText());
                ////system.out.println("city: " + city.getText());
                ////system.out.println("state: " + state.getText());
                ////system.out.println("zip: " + zip.getText());
            /*  //system.out.println("job type");
                 for (int i = 0; i < jobTypePicked.size(); i++) {
                 System.out.print(jobTypePicked.get(i));
                 System.out.print(",");
                 }

                 //system.out.println("equipment");
                 for (int i = 0; i < vehList.size(); i++) {
                 System.out.print(vehList.get(i));
                 System.out.print(",");
                 }

                 //system.out.println("employees");
                 for (int i = 0; i < empListSel.size(); i++) {
                 //system.out.println(empListSel.get(i));
                 System.out.print(",");
                 }
                 //system.out.println("CID: " + cid);
                 */

                if (status.equals("IN PROGRESS") && empCompiled.equals("")) {
                    displayMsg("You must have employees assigned\nto an 'IN PROGRESS' job.");
                    return;
                }

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

                //system.out.println("qry: " + qry);

                //delete all entries associated with IP before exiting to the login screen
                Statement updateDb = null;
                updateDb = conn.createStatement();

                //set our session id and ip address in order to identify user.
                int executeUpdate = updateDb.executeUpdate(qry);

                if (executeUpdate > 0) {
                    //system.out.println("Database updated...");
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
                            taskList.getSelectionModel().select(1);
                            clearJobList();
                            CreateJobBox.setVisible(false);
                            displayJobs.setVisible(true);

                            stage2.close();
                        }
                    });

                }
            }
        }

        refreshVeh();

    }

    @FXML
    private void editJobAction(ActionEvent event) throws IOException, SQLException {
        String getJobTitle = currentJobsDisplay.getSelectionModel().selectedItemProperty().getValue().toString();
        getJobTitle = getJobTitle.substring(getJobTitle.indexOf(":") + 1, getJobTitle.indexOf("]"));
        getJobTitle = getJobTitle.trim();

        empListSelected = new ArrayList<String>();

        //refresh veh/equip
        refreshVeh();

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
                    ////system.out.println(rs.getString(1));
                    getJobTypes.add(rs.getString(1));

                }
                rs = st.executeQuery("select jobName from jobtype;");
                while (rs.next()) {
                    ////system.out.println(rs.getString(1));
                    taskListBox.add(rs.getString(1));

                }
            } catch (SQLException ex) {
                Logger.getLogger(JobCenterController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            String tmpStr = "", holder = "", tmpStr2 = "", tmpStr3;
            tmpStr = listOfTasks.substring(1, listOfTasks.length());

            //system.out.println("EMP SIZE: " + empLister.length());
            //system.out.println("VEH SIZE: " + vehLister.length());

            //take out that initial '/'
            if (empLister.length() > 0) {
                tmpStr2 = empLister.substring(1, empLister.length());
            } else {
                tmpStr2 = "";
            }
            if (vehLister.length() > 0) {
                tmpStr3 = vehLister.substring(1, vehLister.length());
            } else {
                tmpStr3 = "";
            }

            //3 while statements
            //1. add equipment list
            while (true) {
                //this means there is only 1 item in the list, unless its empty then it will also hit, we need to 
                //wrap it with another if
                if (tmpStr3 != "") {
                    if (tmpStr3.indexOf("/") < 0) {
                        vehList.add(tmpStr3.substring(0, tmpStr3.length()));
                        break;
                    }
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

                    if (tmpStr3 == "") {
                        break;
                    }

                    holder = tmpStr3;
                    tmpStr3 = tmpStr3.substring(0, tmpStr3.indexOf("/"));
                    vehList.add(tmpStr3);

                    tmpStr3 = holder;

                    tmpStr3 = tmpStr3.substring(tmpStr3.indexOf("/"), tmpStr3.length());
                }
            }
            vehList11 = FXCollections.observableArrayList(vehList);

            if (tmpStr3 != "") {
                vehicleEquipSelected.setItems(vehList11);
                vehicleEquipSelected.getSelectionModel().selectNext();
            }

            //2. add employee list
            while (true) {
                if (tmpStr2 != "") {
                    if (tmpStr2.indexOf("/") < 0) {
                        empListSelected.add(tmpStr2.substring(0, tmpStr2.length()));
                        break;
                    }
                }

                if (tmpStr2.indexOf("/") == 0) {
                    tmpStr2 = tmpStr2.substring(1, tmpStr2.length());
                    if (tmpStr2.indexOf("/") < 0) {
                        empListSelected.add(tmpStr2.substring(0, tmpStr2.length()));
                        break;
                    } else {
                        empListSelected.add(tmpStr2.substring(0, tmpStr2.indexOf("/")));
                        tmpStr2 = tmpStr2.substring(tmpStr2.indexOf("/"), tmpStr2.length());
                    }

                } else {

                    if (tmpStr2 == "") {
                        break;
                    }

                    holder = tmpStr2;
                    tmpStr2 = tmpStr2.substring(0, tmpStr2.indexOf("/"));
                    empListSelected.add(tmpStr2);

                    tmpStr2 = holder;

                    tmpStr2 = tmpStr2.substring(tmpStr2.indexOf("/"), tmpStr2.length());
                }
            }
            empSelected = FXCollections.observableArrayList(empListSelected);

            if (tmpStr2 != "") {
                employeeSelected.setItems(empSelected);
                employeeSelected.getSelectionModel().selectNext();
            }
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
             //system.out.println(rs.getString(1));
             //system.out.println(rs.getString(2));
             //system.out.println(rs.getString(3));
             //system.out.println(rs.getString(4));
             //system.out.println(rs.getString(5));
             //system.out.println(rs.getString(6));
             //system.out.println(rs.getString(7));
             //system.out.println(rs.getString(8));
             //system.out.println(rs.getString(9));
             //system.out.println(rs.getString(10));
             //system.out.println(rs.getString(11));
             //system.out.println(rs.getString(12));
             //system.out.println(rs.getString(13));
             //system.out.println(rs.getString(14));
             //system.out.println(rs.getString(15));
             //system.out.println(rs.getString(16));
             //system.out.println(rs.getString(17));*/
        }
        String qry = "select * from customer where CID ='" + cid + "';";
        ////system.out.println("qry: " + qry);

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
            rs = st.executeQuery("select fname,lname from employees order by fname;");
            while (rs.next()) {
                ////system.out.println(rs.getString(1));
                empList.add(rs.getString(1) + " " + rs.getString(2));
            }
            rs = st.executeQuery("select VehicleName from vehicles where VehicleStatus = 'AVAILABLE' order by VehicleName;");
            while (rs.next()) {
                ////system.out.println(rs.getString(1));
                vehList.add(rs.getString(1));
            }
            rs = st.executeQuery("select CompanyName from customer;");
            while (rs.next()) {
                ////system.out.println(rs.getString(1));
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

        vehicleEquipSelect.getSelectionModel().selectNext();
        employeeSelect.getSelectionModel().selectNext();

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

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal123 = Calendar.getInstance();
        String dateTimeStamp = dateFormat.format(cal123.getTime());

        //UPDATE THE LOGGING DATABASE TABLE
        String queryRunNow = "insert into edithistory (DateEdited, JobName,usrFname, usrLname, EmpEditorID,  description) "
                + " values('" + dateTimeStamp + "','" + jobTitleStr + "',"
                + "(select fname from employees where uid = " + empID + "),"
                + "(select lname from employees where uid = " + empID + "),"
                + empID + ",'Job Edit'"
                + statusNew.getText() + ");";

        //insert into database
        Statement updateLog = null;

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user
            updateLog = conn.createStatement();

            usrHistTable.setItems(populateHist());

            int executeUpdate = updateLog.executeUpdate(queryRunNow);
        } catch (SQLException e) {
            System.err.println(e);
        }

        //compile job types 
        for (int i = 0; i < taskTypeListStr.size(); i++) {
            jobtypecompiled += "/" + taskTypeListStr.get(i);
        }

        //compile employees  
        for (int j = 0; j < employeeSelected.getItems().size(); j++) {
            empCompiled += "/" + employeeSelected.getItems().get(j);
        }

        //compile equipment  
        for (int k = 0; k < vehList.size(); k++) {
            equipCompiled += "/" + vehList.get(k);
        }

        ////system.out.println("CID: " + cid);
        ////system.out.println("Job name: " + jobNameStr);
        ////system.out.println("Cust job #: " + custJobNumStr);
        ////system.out.println("Cust job name: " + custJobNameStr);
        ////system.out.println("start date: " + startDateStr);
        ////system.out.println("start time: " + startTimeStr);
        ////system.out.println("street: " + streetAddr.getText());
        ////system.out.println("city: " + city.getText());
        ////system.out.println("state: " + state.getText());
        ////system.out.println("zip: " + zip.getText());
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

        //system.out.println("job type");
        for (int i = 0; i < jobTypePicked.size(); i++) {
            System.out.print(jobTypePicked.get(i));
            System.out.print(",");
        }

        //system.out.println("equipment");
        for (int i = 0; i < vehList.size(); i++) {
            System.out.print(vehList.get(i));
            System.out.print(",");
        }

        /*//system.out.println("employees");
         for (int i = 0; i < empSelected.size(); i++) {
         //system.out.println(empSelected.get(i));
         System.out.print(",");
         }*/
        ////system.out.println("CID: " + cid);
        if (status.equals("IN PROGRESS") && empCompiled.equals("")) {
            displayMsg("You must have employees assigned\nto an 'IN PROGRESS' job.");
            return;
        }

        String qry = "update currentjobs set "
                + "Customer_CID='" + cid
                + "', CustJobNum='" + custJobNumStr
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

        //refresh the veh/equip view
        refreshVeh();

        refreshEmp();

        closeWindow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                taskList.getSelectionModel().select(1);
                clearJobList();
                CreateJobBox.setVisible(false);
                displayJobs.setVisible(true);

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
                //system.out.println(uidSelected + "\n" + newPwdStr + "\n" + newUsrStr);
                newPwdStr = getMD5(newPwdStr);

                String queryRunNow = "insert into users (userName, password,employees_uid) "
                        + " values('" + newUsrStr + "','" + newPwdStr + "'," + uidSelected + ");";
                String qry = "select userName from users;";

                //insert into database
                Statement updateDb = null;

                try {
                    updateDb = conn.createStatement();
                    List<String> tmpList = new ArrayList<String>();
                    boolean nameExists = false, uidExists = false;

                    rs = st.executeQuery(qry);
                    while (rs.next()) {
                        ////system.out.println(rs.getString(1));
                        tmpList.add(rs.getString(1));
                    }

                    qry = "select userName from users where employees_uid = " + uidSelected + ";";
                    rs = st.executeQuery(qry);
                    while (rs.next()) {
                        if (rs.getString(1) != null) {
                            uidExists = true;
                        }

                    }

                    for (int i = 0; i < tmpList.size(); i++) {
                        ////system.out.println(tmpList.get(i));
                        if (tmpList.get(i).equals(newUsrStr.trim())) {
                            nameExists = true;
                        }
                    }

                    int executeUpdate;

                    if (!nameExists && uidExists == false) {
                        executeUpdate = updateDb.executeUpdate(queryRunNow);

                        if (executeUpdate > 0) {
                            displayMsg("User successfully added to database.");
                            usersTable.setItems(populateUsers());
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
        String queryRun = "insert into employees (fname, lname,address,phone,email,shopEmp, empAssigned) "
                + "values('" + fNameStrIns.getText() + "','" + lNameStrIns.getText() + "','"
                + "null','" + phoneStrIns.getText() + "','" + emailStrIns.getText() + "','"
                + "no','no')";

        if (fNameStrIns.getText().toString().equals("") || lNameStrIns.getText().toString().equals("")) {
            displayMsg("You cannot have a blank first/last name.");
            return;
        }

        //insert into database
        Statement updateDb = null;
        ////system.out.println(queryRun);

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

        //refrresh the employee list 
        refreshEmp();
    }

    int executeUpdateThis;
    Statement updateDbThis;

    @FXML
    private void deleteEmpAction(ActionEvent event) throws IOException, SQLException {
        String emailToDelete = employeeTable.getSelectionModel().selectedItemProperty().getValue().getEmail(),
                fnameToDel = employeeTable.getSelectionModel().selectedItemProperty().getValue().getFirstName(),
                lnametoDel = employeeTable.getSelectionModel().selectedItemProperty().getValue().getLastName();

        final String queryDelete = "DELETE FROM employees WHERE email = '" + emailToDelete + "'"
                + " and fname = '" + fnameToDel + "'"
                + " and lname = '" + lnametoDel + "'";

        ////system.out.println(queryDelete);
        //insert into database
        updateDbThis = null;

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user.
            updateDbThis = conn.createStatement();
//show the complete box dialog
            Label label2;
            label2 = new Label("Confirm Employee Deletion?");
            HBox hb2 = new HBox();
            Group root = new Group();

            Button closeWindow = new Button("Confirm");
            hb2.getChildren().addAll(label2, closeWindow);
            hb2.setSpacing(10);
            hb2.setLayoutX(25);
            hb2.setLayoutY(48);
            root.getChildren().add(hb2);

            final Scene scene2 = new Scene(root);
            final Stage stage2 = new Stage();
            int executeUpdate;

            stage2.close();
            stage2.setScene(scene2);
            stage2.setHeight(150);
            stage2.setWidth(310);
            stage2.setResizable(false);
            stage2.show();

            closeWindow.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    try {
                        executeUpdateThis = updateDbThis.executeUpdate(queryDelete);
                    } catch (SQLException ex) {
                        Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    employeeTable.setItems(populateDB());
                    stage2.close();
                }
            });

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
                + phone_str.getText() + "','" + email_str.getText() + "','" + office_str.getText()
                + "')";

        //insert into database
        Statement updateDb = null;
        ////system.out.println(queryRun);

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryRun);
            managerView.setItems(populateManagers());

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
        ////system.out.println(queryDelete);

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

        managerView.getSelectionModel().selectNext();
    }

    @FXML
    private void usrDeleteAction(ActionEvent event) {
        String usrToDelete = usersTable.getSelectionModel().selectedItemProperty().getValue().getUsername();
        String queryDelete = "DELETE FROM users WHERE userName = '" + usrToDelete + "'";
        ////system.out.println(queryDelete);

        //insert into database
        Statement updateDb = null;

        //make the connection
        try {
            //set our session id and ip address in order to identify user.
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryDelete);
            usersTable.setItems(populateUsers());

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handlePasswdAction(ActionEvent event) {
        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //title of pane
        Label title = new Label("Reset Password");
        title.getText();
        GridPane.setConstraints(title, 0, 0);
        grid.getChildren().add(title);

        //Defining the Name text field
        final PasswordField oldPass = new PasswordField();
        oldPass.setPromptText("Enter old password");
        oldPass.setPrefColumnCount(10);
        oldPass.getText();
        GridPane.setConstraints(oldPass, 0, 1);
        grid.getChildren().add(oldPass);

        //Defining the Name text field
        final PasswordField newPw = new PasswordField();
        newPw.setPromptText("Enter new password");
        newPw.setPrefColumnCount(10);
        newPw.getText();
        GridPane.setConstraints(newPw, 0, 2);
        grid.getChildren().add(newPw);
        //Defining the Last Name text field
        final PasswordField verHash = new PasswordField();
        verHash.setPromptText("Re-enter password");
        GridPane.setConstraints(verHash, 0, 3);
        grid.getChildren().add(verHash);
        //Defining the Submit button
        Button submit = new Button("Submit");

        GridPane.setConstraints(submit, 1, 3);
        grid.getChildren().add(submit);

        Group root = new Group(), root2 = new Group(), root3 = new Group();
        final Stage stage2 = new Stage();

        Scene scene = new Scene(root);
        root.getChildren().addAll(grid);

        stage2.setScene(scene);
        stage2.setHeight(150);
        stage2.setWidth(250);
        stage2.setResizable(false);
        stage2.show();

        Label label1 = new Label("Password Changed!");
        Button closeWindow = new Button("Close");
        HBox hb = new HBox();

        hb.getChildren().addAll(label1, closeWindow);
        hb.setSpacing(10);
        hb.setLayoutX(48);
        hb.setLayoutY(48);

        final Scene scene2 = new Scene(root2);
        root2.getChildren().addAll(hb, closeWindow);

        Label label2 = new Label("Password not changed!");
        HBox hb2 = new HBox();

        hb2.getChildren().addAll(label2, closeWindow);
        hb2.setSpacing(10);
        hb2.setLayoutX(48);
        hb2.setLayoutY(48);

        final Scene scene3 = new Scene(root3);
        root3.getChildren().addAll(hb2);

        //***************************************************************
        //here we define what all our buttons do within this transaction :)
        //***************************************************************
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                String idEmpStr = null;
                String user = "";

                try {
                    idEmpStr = getEmpId();
                } catch (UnknownHostException ex) {
                    Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                }

                //make the connection
                try {
                    st = conn.createStatement();
                    rs = st.executeQuery("select userName from users where employees_uid = " + idEmpStr + ";");

                    while (rs.next()) {
                        user = rs.getString(1);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    String pass = oldPass.getText();
                    boolean authorized = false;

                    //get md5 of old password (for verification); then get md5 of new password to store
                    String oldPwHash = getMD5(oldPass.getText()),
                            newPwHash = getMD5(newPw.getText()),
                            verifyHash = getMD5(verHash.getText());

                    if (verifyHash.equals(newPwHash)) {
                        st = conn.createStatement();
                        rs = st.executeQuery("select userName from users;");

                        while (rs.next()) {
                            //system.out.println(rs.getString(1));
                            //check to see if username matches
                            if (rs.getString(1).equals(user)) {
                                String qry = "select password from users where userName = '" + user + "';";
                                ////system.out.println(qry);
                                rs = st.executeQuery(qry);

                                while (rs.next()) {

                                    //check to see if password matches
                                    if (rs.getString(1).equals(oldPwHash)) {
                                        ////system.out.println("matched!" + rs.getString(1));
                                        authorized = true;
                                    }
                                }
                            }
                        }

                        if (authorized == true) {
                            String qry3 = "update users set password = '" + newPwHash
                                    + "' where userName = '" + user + "';";
                            ////system.out.println(qry3);
                            Statement updateDb = null;
                            updateDb = conn.createStatement();
                            int executeUpdate = updateDb.executeUpdate(qry3);

                            stage2.close();
                            stage2.setScene(scene2);
                            stage2.setHeight(150);
                            stage2.setWidth(250);
                            stage2.setResizable(false);
                            stage2.show();

                        } else {
                            stage2.close();
                            stage2.setScene(scene3);
                            stage2.setHeight(150);
                            stage2.setWidth(250);
                            stage2.setResizable(false);
                            stage2.show();
                        }
                    } //if the hashes for new password do not match then exit immediately.....
                    else {
                        stage2.close();
                        stage2.setScene(scene3);
                        stage2.setHeight(150);
                        stage2.setWidth(250);
                        stage2.setResizable(false);
                        stage2.show();

                    }

                } catch (SQLException ex) {
                    Logger.getLogger(JobCenterMainController.class
                            .getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(JobCenterMainController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        closeWindow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stage2.close();
            }
        });

    }

    @FXML
    private void saveScrollingText(ActionEvent event) throws SQLException {
        setScrollingTxt(scrollingTxtSet.getText().toString());
        //need to set scrolling text from database...
        scrollingTxt = getScrollingTxt();
        displayMsg("Message Updated.");
    }

    //displays the calendar on to monitor
    @FXML
    private void displayCalendarAction(ActionEvent event) throws SQLException {
        final Group root = new Group();
        String curDateStr = "";

        Rectangle r = new Rectangle(0, 0, 1120, 40);
        r.setFill(Color.LIGHTGREY);
        r.strokeProperty().set(Color.GRAY);

        Rectangle r2 = new Rectangle(0, 42, 1120, 18);
        r2.setFill(Color.YELLOW);
        r2.strokeProperty().set(Color.YELLOW);

        Rectangle r3 = new Rectangle(0, 342, 1120, 18);
        r3.setFill(Color.YELLOW);
        r3.strokeProperty().set(Color.YELLOW);

        GridPane gridpane = new GridPane(),
                gridCal = new GridPane(),
                gridJobs = new GridPane();

        String qry = "select * from currentjobs where status ='IN PROGRESS';";

        List<String> empListSort = new ArrayList<String>();
        List<String> equipListSort = new ArrayList<String>();
        String jobTxtStr = "", jobTypeStr = "", jobDateTxtStr = "", jobStatusStr = "",
                empListStr = "", equipListStr = "", jobNameStr = "";

        //set location and build of grid pane...
        gridpane.setLayoutY(40);
        int startAnew = 9, empNumber = 0, vehNumber = 0, numLocation = 0, columnLoc = 0,
                columnLoc2 = 1;

        //add's the square box drawings
        for (int i = 0; i < 1; i++) {
            RowConstraints row = new RowConstraints(30);
            // row.setValignment(VPos.CENTER);

            gridpane.getRowConstraints().add(row);

        }
        for (int i = 0; i < 7; i++) {
            ColumnConstraints column = new ColumnConstraints(190);
            column.setHalignment(HPos.CENTER);
            gridpane.getColumnConstraints().add(column);
        }

        // or convenience methods set more than one constraint at once...
        Text label = new Text("Sunday"),
                label1 = new Text("Monday"),
                label2 = new Text("Tuesday"),
                label3 = new Text("Wednesday"),
                label4 = new Text("Thursday"),
                label5 = new Text("Friday"),
                label6 = new Text("Saturday"),
                labelTitle = new Text(""),
                labelComp = new Text("Video Pipe Service");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        Calendar cal = Calendar.getInstance();
        int year = cal.getWeekYear();

        //should auto adjust for each month, only way to check is to wait till february and see if march comes up....
        //jan=0 feb=1 march=2 etc...
        int month = cal.get(Calendar.MONTH);
        //month=2;
        int date = 1;

        String monthStr = "";

        if (month == 0) {
            monthStr = "January";
        } else if (month == 1) {
            monthStr = "February";
        } else if (month == 2) {
            monthStr = "March";
        } else if (month == 3) {
            monthStr = "April";
        } else if (month == 4) {
            monthStr = "May";
        } else if (month == 5) {
            monthStr = "June";
        } else if (month == 6) {
            monthStr = "July";
        } else if (month == 7) {
            monthStr = "August";
        } else if (month == 8) {
            monthStr = "September";
        } else if (month == 9) {
            monthStr = "October";
        } else if (month == 10) {
            monthStr = "November";
        } else if (month == 11) {
            monthStr = "December";
        }

        String yrStr = Integer.toString(year);
        String dateToday = (dateFormat.format(cal.getTime()));

        String monYrStr = monthStr + " " + yrStr;
        String labelDateStr = dateToday.substring(0, dateToday.indexOf("/"));

        labelTitle = new Text(monYrStr);

        Text labelDate = labelDate = new Text(dateToday);

        label.setStyle("-fx-font-size: 14;");
        label1.setStyle("-fx-font-size: 14;");
        label2.setStyle("-fx-font-size: 14;");
        label3.setStyle("-fx-font-size: 14;");
        label4.setStyle("-fx-font-size: 14;");
        label5.setStyle("-fx-font-size: 14;");
        label6.setStyle("-fx-font-size: 14;");

        labelTitle.setStyle("-fx-font-size: 14;");
        labelTitle.setY(25);
        labelTitle.setX(10);
        // labelDate.setStyle("-fx-font-size: 20;");
        // labelDate.setY(25);
        // labelDate.setX(175);
        labelComp.setStyle("-fx-font-size: 14;");
        labelComp.setY(25);
        labelComp.setX(1165);

        //add the label texts
        gridpane.add(label, 0, 0); // column=2 row=1        
        gridpane.add(label1, 1, 0);  // column=3 row=1
        gridpane.add(label2, 2, 0); // column=1 row=1
        gridpane.add(label3, 3, 0); // column=2 row=1
        gridpane.add(label4, 4, 0); // column=2 row=1
        gridpane.add(label5, 5, 0); // column=2 row=1
        gridpane.add(label6, 6, 0); // column=2 row=1
        numLocation = 5;
        columnLoc = 1;

        //calendar gridlines
        gridpane.setGridLinesVisible(true);

        calSet = false;

        cal.set(year, month, date);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //system.out.println("Number of Days: " + days);
        start = 1;

        //days of week 1-7 starting with monday-sunday
        int startingDay = cal.get(Calendar.DAY_OF_WEEK);

        int monthCorrection = month + 1;
        curDateStr = monthCorrection + "/" + start + "/" + year;
        //system.out.println(startingDay);

        //set location and build of grid pane...
        gridCal.setLayoutY(70);
        gridJobs.setLayoutY(70);

        //set gridlines on/off for the jobs listings -- main calendar gridlines should be turned off for beauty
        gridJobs.setGridLinesVisible(false);

        //build the job board pane... each calendar should have about 8 or so spots ***START
        //how many rows = 112/7 = 16 <------
        //there are 6 rows in the calendar so that makes 42 total job rows
        for (int i = 0; i < 42; i++) {
            //set to 160 when deploying
            RowConstraints row = new RowConstraints(16);
            row.setValignment(VPos.TOP);
            gridJobs.getRowConstraints().add(row);
        }
        //7 columns...
        for (int i = 1; i <= 7; i++) {
            ColumnConstraints column = new ColumnConstraints(190);
            column.setHalignment(HPos.CENTER);
            gridJobs.getColumnConstraints().add(column);
        }
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        //build the job board pane... each calendar should have about 5 or so spots ***FINISH

        //build the calendar days
        for (int i = 0; i < 6; i++) {
            //set to 130 when deploying -- fits to screen ... i think
            RowConstraints row = new RowConstraints(112);
            row.setValignment(VPos.TOP);
            gridCal.getRowConstraints().add(row);
        }

        for (int i = 1; i <= 7; i++) {
            ColumnConstraints column = new ColumnConstraints(190);
            //column.setHalignment(HPos.LEFT);

            if (startingDay == i) {
                Text tmpTxt = new Text(new Integer(start).toString());
                tmpTxt.setStyle("-fx-font-size: 14;");
                // tmpTxt.setStyle("-fx-font-size:10;");
                tmpTxt.setX(77);
                tmpTxt.setY(-77);
                gridCal.add(tmpTxt, i - 1, 0);
                //if the job is in the month/day we are looking for then we add it to the column                       

                int countIn = i,
                        countCal = startingDay;

                while (countCal <= 6) {
                    start++;
                    Text tmpTxt2 = new Text(new Integer(start).toString());
                    tmpTxt2.setStyle("-fx-font-size: 14;");
                    gridCal.add(tmpTxt2, countCal, 0);

                    //if the job is in the month/day we are looking for then we add it to the column                       
                    Text tmpTxt7 = new Text(curDateStr);

                    gridJobs.add(tmpTxt7, countCal, 0);
                    countCal++;

                }
                calSet = true;
            }

            gridCal.getColumnConstraints().add(column);
        }

        //once done increment one before starting new day adding
        start++;

        //add's the rest of the calendar days
        if (calSet) {

            Text tmpTxt2 = new Text(new Integer(start).toString());
            int columns = 1, colToAdd = 1;
            boolean setToZero = true, setToZero1 = true, setToZero2 = true, setToZero3 = true;

            //start = 1 days == the days of the current month
            while (start <= days) {
                String jobDateStr = "",
                        qryRun = "";

                //this represents the 7 rows in each calendar day
                for (int j = 0; j < 7; j++) {
                    qryRun = "select JobWorkDate from currentjobs where JobWorkDate = '" + curDateStr + "';";

                    //this adds the day text on to the upper left corner of the calendar grid block
                    if (start <= days) {
                        tmpTxt2.setStyle("-fx-font-size: 14;");
                        tmpTxt2.setX(0);
                        tmpTxt2.setLayoutY(66);
                        gridCal.add(tmpTxt2, j, columns);

                        //make the connection
                        try {
                            st = conn.createStatement();
                            rs = st.executeQuery(qryRun);
                            while (rs.next()) {
                                jobDateStr = rs.getString(1);

                                if (jobDateStr.equals(curDateStr)) {
                                    ////system.out.println(start);
                                    qryRun = "select JobTitle from currentjobs where JobWorkDate = '" + curDateStr + "';";

                                    rs = st.executeQuery(qryRun);
                                    String JobTitleStr = "";
                                    while (rs.next()) {
                                        JobTitleStr = rs.getString(1);

                                        Text tmpTxt7 = new Text(JobTitleStr);

                                        if (columns == 1) {
                                            gridJobs.add(tmpTxt7, j, (7 + colToAdd));
                                            //for every column added we add one to the row so that the next start - coltoadd == number of columns to skip
                                            colToAdd++;
                                        }
                                        if (columns == 2) {
                                            if (setToZero == true) {
                                                colToAdd = 1;
                                                setToZero = false;
                                            }
                                            gridJobs.add(tmpTxt7, j, (14 + colToAdd));
                                            colToAdd++;
                                        }

                                        if (columns == 3) {
                                            if (setToZero1 == true) {
                                                colToAdd = 1;
                                                setToZero1 = false;
                                            }
                                            gridJobs.add(tmpTxt7, j, (21 + colToAdd));
                                            colToAdd++;
                                        }
                                        if (columns == 4) {
                                            if (setToZero2 == true) {
                                                colToAdd = 1;
                                                setToZero2 = false;
                                            }
                                            gridJobs.add(tmpTxt7, j, (28 + colToAdd));
                                            colToAdd++;
                                        }

                                        if (columns == 5) {
                                            if (setToZero3 == true) {
                                                colToAdd = 1;
                                                setToZero3 = false;
                                            }
                                            gridJobs.add(tmpTxt7, j, (35 + colToAdd));
                                            colToAdd++;
                                        }
                                        //still one more bug, jobs in the same work week also needs to be reset...

                                    }

                                }

                            }
                        } catch (Exception e) {
                            System.err.println(e);
                        }


                        /*
                         Rectangle tr = new Rectangle(0, 0, 45,20);
                         tr.setFill(Color.LIGHTGREY);
                         tr.strokeProperty().set(Color.GRAY);
                        

                         gridCal.add(tr, j, columns);
                         */
                    } else {
                        break;
                    }

                    // gridCal.add(tmpTxt2, 1,1);
                    //gridCal.add(tmpTxt3, 1,1);
                    start++;
                    colToAdd = 1;

                    curDateStr = monthCorrection + "/" + start + "/" + year;
                    tmpTxt2 = new Text(new Integer(start).toString());

                }
                columns++;
            }
        }

        //add's the data into the rows
        gridCal.setGridLinesVisible(true);

        //these are the label title's the month and the year etc...
        root.getChildren().add(labelTitle);

        // root.getChildren().add(labelDate);        
        //video pipe services label
        root.getChildren().add(labelComp);

        //the calendar weekdays
        root.getChildren().add(gridpane);

        root.getChildren().add(gridCal);
        root.getChildren().add(gridJobs);

        Scene scene2 = new Scene(root);

        stageJob = new Stage();
      //  stageJob.setX(Screen.getScreens().get(1).getVisualBounds().getMinX());
        // stageJob.setY(Screen.getScreens().get(1).getVisualBounds().getMinY());
        //stageJob.setResizable(false);    
        //stageJob.setFullScreen(true);
        //set the dimesions to the screen size:
        //stageJob.setWidth(Screen.getScreens().get(1).getVisualBounds().getWidth());
        // stageJob.setHeight(Screen.getScreens().get(1).getVisualBounds().getHeight());

        //stageJob.initStyle(StageStyle.UNDECORATED);
        stageJob.setScene(scene2);

        // which screen to display the popup on .... CALENDAR set to 1 for production
        javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getScreens().get(1).getVisualBounds();
        stageJob.setX(primaryScreenBounds.getMinX());
        stageJob.setY(primaryScreenBounds.getMinY());
        stageJob.setFullScreen(true);

        stageJob.setX(Screen.getScreens().get(1).getVisualBounds().getMinX());
        stageJob.setY(Screen.getScreens().get(1).getVisualBounds().getMinY());
        stageJob.setWidth(Screen.getScreens().get(1).getVisualBounds().getWidth());
        stageJob.setHeight(Screen.getScreens().get(1).getVisualBounds().getHeight());

        stageJob.show();
    }

    private void saveImage(WritableImage snapshot) {
        BufferedImage bufferedImage = new BufferedImage(550, 400, BufferedImage.TYPE_INT_ARGB);
        BufferedImage image;
        //for Production -- administrator
        //File file = new File("C:/Users/vangfc/Documents/job_board.jpg");
        File file = new File("C:/Users/administrator/Documents/job_board.jpg");
        image = javafx.embed.swing.SwingFXUtils.fromFXImage(snapshot, bufferedImage);
        try {
            Graphics2D gd = (Graphics2D) image.getGraphics();
            // gd.translate(root.getWidth(), vbox.getHeight());
            ImageIO.write(image, "png", file);

        } catch (IOException ex) {
            System.err.print(ex);
        };

    }

    @FXML
    private void emailJobBoardAction(ActionEvent event) throws MessagingException, SQLException {
        // here we make image from vbox and add it to scene, can be repeated :) 

        final Group root = new Group();
        Button printScreen = new Button("Print");

        Rectangle r = new Rectangle(0, 0, 1120, 40);
        r.setFill(Color.LIGHTGREY);
        r.strokeProperty().set(Color.GRAY);

        Rectangle r2 = new Rectangle(0, 42, 1120, 18);
        r2.setFill(Color.YELLOW);
        r2.strokeProperty().set(Color.YELLOW);

        Rectangle r3 = new Rectangle(0, 342, 1120, 18);
        r3.setFill(Color.YELLOW);
        r3.strokeProperty().set(Color.YELLOW);

        GridPane gridpane = new GridPane();

        String qry = "select * from currentjobs where status ='IN PROGRESS';";

        List<String> empListSort = new ArrayList<String>();
        List<String> equipListSort = new ArrayList<String>();
        String jobTxtStr = "", jobTypeStr = "", jobDateTxtStr = "", jobStatusStr = "",
                empListStr = "", equipListStr = "", jobNameStr = "";

        //set location and build of grid pane...
        gridpane.setLayoutY(40);
        int startAnew = 9, empNumber = 0, vehNumber = 0, numLocation = 0, columnLoc = 0,
                columnLoc2 = 1;

        //set location and build of grid pane...
        gridpane.setLayoutY(40);
        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints(20);
            row.setValignment(VPos.CENTER);

            gridpane.getRowConstraints().add(row);

        }
        for (int i = 0; i < 10; i++) {
            ColumnConstraints column = new ColumnConstraints(112);
            column.setHalignment(HPos.CENTER);
            gridpane.getColumnConstraints().add(column);
        }

        // or convenience methods set more than one constraint at once...
        Text label = new Text("Job Number"),
                label1 = new Text("Status"),
                label2 = new Text("Job Name"),
                label3 = new Text("Work Type"),
                label4 = new Text("Work Date"),
                labelTitle = new Text("Job Summary Report: "),
                labelComp = new Text("Video Pipe Service");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();

        String dateToday = (dateFormat.format(cal.getTime()));
        Text labelDate = labelDate = new Text(dateToday);

        label.setStyle("-fx-font-size: 10;");
        label1.setStyle("-fx-font-size: 10;");
        label2.setStyle("-fx-font-size: 10;");
        label3.setStyle("-fx-font-size: 10;");
        label4.setStyle("-fx-font-size: 10;");

        labelTitle.setStyle("-fx-font-size: 16;");
        labelTitle.setY(25);
        labelTitle.setX(10);
        labelDate.setStyle("-fx-font-size: 15;");
        labelDate.setY(25);
        labelDate.setX(175);
        labelComp.setStyle("-fx-font-size: 16;");
        labelComp.setY(25);
        labelComp.setX(965);
        printScreen.setLayoutX(265);
        printScreen.setLayoutY(7);

        gridpane.add(label, 0, 0); // column=2 row=1        
        gridpane.add(label1, 0, 1);  // column=3 row=1
        gridpane.add(label2, 0, 2); // column=1 row=1
        gridpane.add(label3, 0, 3); // column=2 row=1
        gridpane.add(label4, 0, 4); // column=2 row=1
        numLocation = 5;
        columnLoc = 1;
        //gridlines for preview job board
        gridpane.setGridLinesVisible(true);

        for (int i = 1; i <= 5; i++) {
            RowConstraints row = new RowConstraints(20);
            row.setValignment(VPos.CENTER);
            String nameLabel = "Person " + i;
            Text tmpTxt = new Text(nameLabel);
            tmpTxt.setStyle("-fx-font-size: 10;");

            gridpane.getRowConstraints().add(row);
            gridpane.add(tmpTxt, 0, numLocation);

            //increment to track which column we are at.
            numLocation++;

        }
        for (int i = 1; i <= 5; i++) {
            RowConstraints row = new RowConstraints(20);
            row.setValignment(VPos.CENTER);
            String nameLabel = "Veh/Equip " + i;
            Text tmpTxt = new Text(nameLabel);
            tmpTxt.setStyle("-fx-font-size: 10;");

            gridpane.getRowConstraints().add(row);
            gridpane.add(tmpTxt, 0, numLocation);

            //increment to track which column we are at.
            numLocation++;

        }

        rs = st.executeQuery(qry);

        while (rs.next()) {

            int counter = 0;

            jobTxtStr = rs.getString(5);
            jobTypeStr = rs.getString(9);
            if (jobTypeStr.length() > 8) {
                jobTypeStr = jobTypeStr.substring(1, 8);
            } else {
                jobTypeStr = jobTypeStr.substring(1, jobTypeStr.length());
            }

            jobDateTxtStr = rs.getString(7);
            jobStatusStr = rs.getString(17);
            jobNameStr = rs.getString(6);
            empListStr = rs.getString(10);
            equipListStr = rs.getString(11);

            Text add;

            if (columnLoc <= startAnew) {
                add = new Text(jobTxtStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;
                add = new Text(jobStatusStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;
                add = new Text(jobNameStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;
                add = new Text(jobTypeStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;
                add = new Text(jobDateTxtStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;

                String strManip = empListStr;

                while (strManip.lastIndexOf("/") >= 0) {

                    add = new Text(strManip.substring(strManip.lastIndexOf("/") + 1, strManip.length()));
                    add.setStyle("-fx-font-size: 10;");
                    gridpane.add(add, columnLoc, counter);
                    counter++;

                    strManip = strManip.substring(0, strManip.lastIndexOf("/"));

                }

                counter = 10;
                strManip = equipListStr;
                while (strManip.lastIndexOf("/") >= 0) {
                    //system.out.println(strManip);
                    add = new Text(strManip.substring(strManip.lastIndexOf("/") + 1, strManip.length()));
                    add.setStyle("-fx-font-size: 10;");
                    gridpane.add(add, columnLoc, counter);
                    counter++;

                    strManip = strManip.substring(0, strManip.lastIndexOf("/"));
                    //system.out.println(strManip);

                }

            } else {

                counter = 15;
                //system.out.println("TIME TO PRINT ANOTHER TABLE");

                for (int i = 0; i < 14; i++) {
                    RowConstraints row = new RowConstraints(20);
                    row.setValignment(VPos.CENTER);

                    gridpane.getRowConstraints().add(row);

                }

                // or convenience methods set more than one constraint at once...
                Text label00 = new Text("Job Number"),
                        label11 = new Text("Status"),
                        label22 = new Text("Job Name"),
                        label33 = new Text("Work Type"),
                        label44 = new Text("Work Date");

                label00.setStyle("-fx-font-size: 10;");
                label11.setStyle("-fx-font-size: 10;");
                label22.setStyle("-fx-font-size: 10;");
                label33.setStyle("-fx-font-size: 10;");
                label44.setStyle("-fx-font-size: 10;");

                gridpane.add(label00, 0, 15); // column=2 row=1        
                gridpane.add(label11, 0, 16);  // column=3 row=1
                gridpane.add(label22, 0, 17); // column=1 row=1
                gridpane.add(label33, 0, 18); // column=2 row=1
                gridpane.add(label44, 0, 19); // column=2 row=1
                numLocation = 20;

                for (int i = 1; i <= 5; i++) {
                    RowConstraints row = new RowConstraints(20);
                    row.setValignment(VPos.CENTER);
                    String nameLabel = "Person " + i;
                    Text tmpTxt = new Text(nameLabel);
                    tmpTxt.setStyle("-fx-font-size: 10;");

                    gridpane.getRowConstraints().add(row);
                    gridpane.add(tmpTxt, 0, numLocation);

                    //increment to track which column we are at.
                    numLocation++;

                }

                for (int i = 1; i <= 5; i++) {
                    RowConstraints row = new RowConstraints(20);
                    row.setValignment(VPos.CENTER);
                    String nameLabel = "Veh/Equip " + i;
                    Text tmpTxt = new Text(nameLabel);
                    tmpTxt.setStyle("-fx-font-size: 10;");

                    gridpane.getRowConstraints().add(row);
                    gridpane.add(tmpTxt, 0, numLocation);

                    //increment to track which column we are at.
                    numLocation++;

                }

                jobTxtStr = rs.getString(5);
                jobTypeStr = rs.getString(9);
                if (jobTypeStr.length() > 8) {
                    jobTypeStr = jobTypeStr.substring(1, 8);
                } else {
                    jobTypeStr = jobTypeStr.substring(1, jobTypeStr.length());
                }

                jobDateTxtStr = rs.getString(7);
                jobStatusStr = rs.getString(17);
                jobNameStr = rs.getString(6);
                empListStr = rs.getString(10);
                equipListStr = rs.getString(11);

                Text add2;

                add2 = new Text(jobTxtStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;
                add2 = new Text(jobStatusStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;
                add2 = new Text(jobNameStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;
                add2 = new Text(jobTypeStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;
                add2 = new Text(jobDateTxtStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;

                String strManip = empListStr;

                while (strManip.lastIndexOf("/") >= 0) {

                    add2 = new Text(strManip.substring(strManip.lastIndexOf("/") + 1, strManip.length()));
                    add2.setStyle("-fx-font-size: 10;");
                    gridpane.add(add2, columnLoc2, counter);
                    counter++;

                    strManip = strManip.substring(0, strManip.lastIndexOf("/"));

                }
                /*
                 counter = 25;
                 strManip = equipListStr;
                 while (strManip.lastIndexOf("/") >= 0) {
                 //system.out.println(strManip);
                 add2 = new Text(strManip.substring(strManip.lastIndexOf("/") + 1, strManip.length()));
                 add2.setStyle("-fx-font-size: 10;");
                 gridpane.add(add2, columnLoc2, counter);
                 counter++;

                 strManip = strManip.substring(0, strManip.lastIndexOf("/"));
                 //system.out.println(strManip);

                 }
                 */
                columnLoc2++;
            }
            columnLoc++;
        }

        root.getChildren().add(r);
        root.getChildren().add(r2);
        root.getChildren().add(r3);
        root.getChildren().add(printScreen);
        root.getChildren().add(labelTitle);
        root.getChildren().add(labelDate);
        root.getChildren().add(labelComp);

        root.getChildren().add(gridpane);

        WritableImage snapshot = root.snapshot(new SnapshotParameters(), null);
        root.getChildren().add(new ImageView(snapshot));
        saveImage(snapshot);

        final Label label122, emailDisp, usrNme, pwdEnt;

        st = conn.createStatement();
        String qry555 = "select emailSend from emaillist;";

        rs = st.executeQuery(qry555);
        emailList = "";
        emailTo = new String[1000];

        int l = 0;
        while (rs.next()) {
            if (emailList == "") {
                emailList = rs.getString(1);
            } else {
                emailList += "," + rs.getString(1);
            }
            emailTo[l] = rs.getString(1);
            l++;
        }

        label122 = new Label("Email Job Board To Users:");
        emailDisp = new Label(emailList);

        HBox hb2 = new HBox();
        Group root123 = new Group();

        final TextArea text = new TextArea(emailList);
        final TextField usrNameField = new TextField();
        final PasswordField pazz = new PasswordField();
        usrNameField.setPromptText("Gmail Username");
        pazz.setPromptText("Password");
        text.setEditable(false);
        text.setWrapText(true);
        Button closeWindow = new Button("Send");

        usrNme = new Label("Gmail Username: ");
        pwdEnt = new Label("Password: ");

        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("Email Job Board To Users:"), 0, 0);
        grid.add(pazz, 0, 3);
        grid.add(usrNameField, 0, 2);

        grid.add(text, 0, 5, 4, 1);
        grid.add(closeWindow, 0, 7);

       // Group root = (Group)scene.getRoot();
        //root123.getChildren().add(grid);
        /*
         hb2.getChildren().addAll(label122,emailDisp, closeWindow);
         hb2.setSpacing(10);
         hb2.setLayoutX(10);
         hb2.setLayoutY(10);*/
        // emailList = text.getText();
        root123.getChildren().add(grid);

        final Scene scene2 = new Scene(root123);
        final Stage stage2 = new Stage();

        stage2.close();
        stage2.setScene(scene2);
        stage2.setHeight(350);
        stage2.setWidth(500);
        stage2.setResizable(false);
        stage2.show();

        closeWindow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                GoogleMail sendMail = new GoogleMail();

                try {
                    for (int k = 0; k < emailTo.length; k++) {
                        if (emailTo[k] == null || emailTo[k] == "") {
                            break;
                        }

                        //system.out.println("sending: " + emailTo[k]);
                        //system.out.println(usrNameField.getText().toString());
                        //system.out.println(pazz.getText().toString());

                        //sendMail.Send("fucheevang", "tanehtmf10", emailTo[k], "", "Job Board Daily Mail", "Job board email.");
                        sendMail.Send(usrNameField.getText().toString(), pazz.getText().toString(), emailTo[k], "", "Job Board Daily Mail", "Job board email.");
                    }

                    //sendMail.Send(usrNameField.getText().toString(), pazz.getText().toString(), "bellis@videopipeservices.com", "", "Job Board Daily Mail", "Job board email.");
                    //File file = new File("C:/Users/vangfc/Documents/job_board.jpg");
                    File file = new File("C:/Users/administrator/Documents/job_board.jpg");
                    file.delete();

                } catch (MessagingException ex) {
                    Logger.getLogger(JobCenterMainController.class.getName()).log(Level.SEVERE, null, ex);
                }

                stage2.close();
            }
        });

    }

    @FXML
    private void deleteJobAction(ActionEvent event) throws SQLException {
        String jobToDelete = (currentJobsDisplay.getTreeItem(currentJobsDisplay.getSelectionModel().getSelectedIndex()).getValue());
        final String queryDelete12345 = "DELETE FROM currentJobs WHERE JobTitle = '" + jobToDelete + "'";
        ////system.out.println(queryDelete);

        //show the complete box dialog
        Label label2;
        label2 = new Label("Confirm deletion?");
        HBox hb2 = new HBox();
        Group root = new Group();

        Button closeWindow = new Button("Delete");
        hb2.getChildren().addAll(label2, closeWindow);
        hb2.setSpacing(10);
        hb2.setLayoutX(25);
        hb2.setLayoutY(48);
        root.getChildren().add(hb2);

        final Scene scene2 = new Scene(root);
        final Stage stage3 = new Stage();

        stage3.close();
        stage3.setScene(scene2);
        stage3.setHeight(150);
        stage3.setWidth(310);
        stage3.setResizable(false);
        stage3.show();

        closeWindow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //delete from database
                Statement updateDb = null;

                //make the connection
                try {
                    //set our session id and ip address in order to identify user.
                    updateDb = conn.createStatement();

                    int executeUpdate = updateDb.executeUpdate(queryDelete12345);
                    refreshList();

                } catch (SQLException ex) {
                    Logger.getLogger(JobCenterController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                stage3.close();

            }
        });
    }

    @FXML
    private void createCustAction(ActionEvent event) throws SQLException {
        //delete cust button
        Button delCust = new Button("Delete this customer");

        //Creating a GridPane container
        GridPane grid = new GridPane();
        Label custLab = new Label("Company Name: ");
        Label custCity = new Label("City: ");
        Label custStr = new Label("Street: ");
        Label custZip = new Label("Zip: ");
        Label custContact = new Label("Point of contact: ");
        Label custPhone = new Label("Phone: ");
        Label custState = new Label("State: ");

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //gridlines for edit customer info
        grid.setGridLinesVisible(false);

        //title of pane
        Label title = new Label("Customer Information");
        title.getText();
        GridPane.setConstraints(title, 0, 0);
        grid.getChildren().add(title);

        //Defining the Name text field
        final TextField custNameBlock = new TextField();
        final TextField custAddrBlock = new TextField();
        final TextField custPhoneBlock = new TextField();
        final TextField custStBlock = new TextField();
        final TextField custStateBlock = new TextField();
        final TextField custCityBlock = new TextField();
        final TextField custZipBlock = new TextField();
        final TextField custFaxBlock = new TextField();
        final TextField custEmailBlock = new TextField();
        final TextField custContactBlock = new TextField();
        final TextField custContactPhoneBlock = new TextField();

        custNameBlock.setPrefColumnCount(20);
        GridPane.setConstraints(custLab, 0, 1);
        GridPane.setConstraints(custStr, 0, 2);
        GridPane.setConstraints(custCity, 0, 3);
        GridPane.setConstraints(custState, 0, 4);
        GridPane.setConstraints(custZip, 0, 5);
        GridPane.setConstraints(custContact, 0, 6);
        GridPane.setConstraints(custPhone, 0, 7);

        GridPane.setConstraints(custNameBlock, 1, 1);
        GridPane.setConstraints(custCityBlock, 1, 2);
        GridPane.setConstraints(custStBlock, 1, 3);
        GridPane.setConstraints(custZipBlock, 1, 5);
        GridPane.setConstraints(custStateBlock, 1, 4);
        GridPane.setConstraints(custContactBlock, 1, 6);
        GridPane.setConstraints(custContactPhoneBlock, 1, 7);

        //the labels
        grid.getChildren().add(custLab);
        grid.getChildren().add(custCity);
        grid.getChildren().add(custStr);
        grid.getChildren().add(custZip);
        grid.getChildren().add(custState);
        grid.getChildren().add(custContact);
        grid.getChildren().add(custPhone);

        //the text fields
        grid.getChildren().add(custNameBlock);
        grid.getChildren().add(custCityBlock);
        grid.getChildren().add(custStBlock);
        grid.getChildren().add(custStateBlock);
        grid.getChildren().add(custZipBlock);
        grid.getChildren().add(custContactBlock);
        grid.getChildren().add(custContactPhoneBlock);

        //Defining the Submit button
        Button submit = new Button("Submit");

        GridPane.setConstraints(submit, 0, 9);
        grid.getChildren().add(submit);

        GridPane.setConstraints(delCust, 1, 9);
        grid.getChildren().add(delCust);

        Group root = new Group(), root2 = new Group(), root3 = new Group();
        final Stage stage2 = new Stage();

        Scene scene = new Scene(root);
        root.getChildren().addAll(grid);

        stage2.setScene(scene);
        stage2.setHeight(330);
        stage2.setWidth(400);
        stage2.setResizable(false);
        stage2.show();

        Label label1 = new Label("Password Changed!");
        Button closeWindow = new Button("Close");
        HBox hb = new HBox();

        hb.getChildren().addAll(label1, closeWindow);
        hb.setSpacing(10);
        hb.setLayoutX(48);
        hb.setLayoutY(48);

        final Scene scene2 = new Scene(root2);
        root2.getChildren().addAll(hb, closeWindow);

        Label label2 = new Label("Password not changed!");
        HBox hb2 = new HBox();

        hb2.getChildren().addAll(label2, closeWindow);
        hb2.setSpacing(10);
        hb2.setLayoutX(48);
        hb2.setLayoutY(48);

        final Scene scene3 = new Scene(root3);
        root3.getChildren().addAll(hb2);

        //***************************************************************
        //here we define what all our buttons do within this transaction :)
        //***************************************************************
        delCust.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                //show the complete box dialog
                Label label2;
                label2 = new Label("Confirm deletion?");
                HBox hb2 = new HBox();
                Group root = new Group();

                Button closeWindow = new Button("Delete");
                hb2.getChildren().addAll(label2, closeWindow);
                hb2.setSpacing(10);
                hb2.setLayoutX(25);
                hb2.setLayoutY(48);
                root.getChildren().add(hb2);

                final Scene scene2 = new Scene(root);
                final Stage stage3 = new Stage();

                stage3.close();
                stage3.setScene(scene2);
                stage3.setHeight(150);
                stage3.setWidth(310);
                stage3.setResizable(false);
                stage3.show();

                closeWindow.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {

                        String custToDeleteNow = custNameBlock.getText();
                        String queryDelete = "DELETE FROM customer WHERE CompanyName = '" + custToDeleteNow + "'";

                        //delete from database
                        Statement updateDb = null;

                        //make the connection
                        try {
                            //set our session id and ip address in order to identify user.
                            updateDb = conn.createStatement();

                            int executeUpdate = updateDb.executeUpdate(queryDelete);

                            custList = new ArrayList<String>();

                            rs = st.executeQuery("select CompanyName from customer order by CompanyName;");
                            while (rs.next()) {
                                ////system.out.println(rs.getString(1));
                                custList.add(rs.getString(1));
                            }
                            ObservableList<String> custListingObs66 = FXCollections.observableArrayList(custList);

                            custListing.setItems(custListingObs66);

                        } catch (SQLException ex) {
                            Logger.getLogger(JobCenterController.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }

                        stage3.close();
                        stage2.close();

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

                        custListing.getSelectionModel().selectNext();
                    }
                });

            }

        });
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                //make the connection
                try {
                    String qry3 = "insert into customer (CompanyName, CompanyCity, CompanyAddress,"
                            + "CompanyZipCode,CompanyState,Contact1Name,CompanyOfficePhone) "
                            + "values ('" + custNameBlock.getText() + "','"
                            + custCityBlock.getText() + "','"
                            + custStBlock.getText() + "','"
                            + custZipBlock.getText() + "','"
                            + custStateBlock.getText() + "','"
                            + custContactBlock.getText() + "','"
                            + custContactPhoneBlock.getText() + "');";

                    Statement updateDb = null;
                    updateDb = conn.createStatement();
                    int executeUpdate = updateDb.executeUpdate(qry3);

                    stage2.close();

                    //delete from database
                    Statement updateDb2 = null;

                    //make the connection
                    try {
                        //set our session id and ip address in order to identify user.
                        custList = new ArrayList<String>();

                        rs = st.executeQuery("select CompanyName from customer order by CompanyName;");
                        while (rs.next()) {
                            ////system.out.println(rs.getString(1));
                            custList.add(rs.getString(1));
                        }
                        ObservableList<String> custListingObs66 = FXCollections.observableArrayList(custList);

                        custListing.setItems(custListingObs66);

                    } catch (SQLException ex) {
                        Logger.getLogger(JobCenterController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                    custListing.getSelectionModel().selectNext();

                    displayMsg("Customer added.");

                } catch (SQLException ex) {
                    Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });
    }

    @FXML
    private void editCustAction(ActionEvent event) throws SQLException {
        //delete cust button
        Button delCust = new Button("Delete this customer");

        //Creating a GridPane container
        GridPane grid = new GridPane();
        Label custLab = new Label("Company Name: ");
        Label custCity = new Label("City: ");
        Label custStr = new Label("Street: ");
        Label custZip = new Label("Zip: ");
        Label custContact = new Label("Point of contact: ");
        Label custPhone = new Label("Phone: ");
        Label custState = new Label("State: ");

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //gridlines for edit customer info
        grid.setGridLinesVisible(false);

        //title of pane
        Label title = new Label("Customer Information");
        title.getText();
        GridPane.setConstraints(title, 0, 0);
        grid.getChildren().add(title);

        //Defining the Name text field
        final TextField custNameBlock = new TextField();
        final TextField custAddrBlock = new TextField();
        final TextField custPhoneBlock = new TextField();
        final TextField custStBlock = new TextField();
        final TextField custStateBlock = new TextField();
        final TextField custCityBlock = new TextField();
        final TextField custZipBlock = new TextField();
        final TextField custFaxBlock = new TextField();
        final TextField custEmailBlock = new TextField();
        final TextField custContactBlock = new TextField();
        final TextField custContactPhoneBlock = new TextField();

        custNameBlock.setPrefColumnCount(20);
        GridPane.setConstraints(custLab, 0, 1);
        GridPane.setConstraints(custStr, 0, 2);
        GridPane.setConstraints(custCity, 0, 3);
        GridPane.setConstraints(custState, 0, 4);
        GridPane.setConstraints(custZip, 0, 5);
        GridPane.setConstraints(custContact, 0, 6);
        GridPane.setConstraints(custPhone, 0, 7);

        GridPane.setConstraints(custNameBlock, 1, 1);
        GridPane.setConstraints(custCityBlock, 1, 2);
        GridPane.setConstraints(custStBlock, 1, 3);
        GridPane.setConstraints(custZipBlock, 1, 5);
        GridPane.setConstraints(custStateBlock, 1, 4);
        GridPane.setConstraints(custContactBlock, 1, 6);
        GridPane.setConstraints(custContactPhoneBlock, 1, 7);

        //the labels
        grid.getChildren().add(custLab);
        grid.getChildren().add(custCity);
        grid.getChildren().add(custStr);
        grid.getChildren().add(custZip);
        grid.getChildren().add(custState);
        grid.getChildren().add(custContact);
        grid.getChildren().add(custPhone);

        //the text fields
        grid.getChildren().add(custNameBlock);
        grid.getChildren().add(custCityBlock);
        grid.getChildren().add(custStBlock);
        grid.getChildren().add(custStateBlock);
        grid.getChildren().add(custZipBlock);
        grid.getChildren().add(custContactBlock);
        grid.getChildren().add(custContactPhoneBlock);

        //the data getter
        String companyNameSelected = custListing.getSelectionModel().selectedItemProperty().getValue().toString();
        try {
            st = conn.createStatement();
            String qry1 = "select CompanyName, CompanyCity,CompanyAddress,"
                    + " CompanyZipCode, CompanyState,Contact1Name,CompanyOfficePhone "
                    + " from customer where CompanyName = '" + companyNameSelected + "';";
            rs = st.executeQuery(qry1);

            while (rs.next()) {

                //system.out.println(rs.getString(1));
                //system.out.println(rs.getString(2));
                //system.out.println(rs.getString(3));
                //system.out.println(rs.getString(4));
                //system.out.println(rs.getString(5));
                //system.out.println(rs.getString(6));
                //system.out.println(rs.getString(7));

                custNameBlock.setText(rs.getString(1));
                custCityBlock.setText(rs.getString(2));
                custStBlock.setText(rs.getString(3));
                custZipBlock.setText(rs.getString(4));
                custStateBlock.setText(rs.getString(5));
                custContactBlock.setText(rs.getString(6));
                custContactPhoneBlock.setText(rs.getString(7));
            }
        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Defining the Submit button
        Button submit = new Button("Submit");

        GridPane.setConstraints(submit, 0, 9);
        grid.getChildren().add(submit);

        GridPane.setConstraints(delCust, 1, 9);
        grid.getChildren().add(delCust);

        Group root = new Group(), root2 = new Group(), root3 = new Group();
        final Stage stage2 = new Stage();

        Scene scene = new Scene(root);
        root.getChildren().addAll(grid);

        stage2.setScene(scene);
        stage2.setHeight(330);
        stage2.setWidth(400);
        stage2.setResizable(false);
        stage2.show();

        Label label1 = new Label("Password Changed!");
        Button closeWindow = new Button("Close");
        HBox hb = new HBox();

        hb.getChildren().addAll(label1, closeWindow);
        hb.setSpacing(10);
        hb.setLayoutX(48);
        hb.setLayoutY(48);

        final Scene scene2 = new Scene(root2);
        root2.getChildren().addAll(hb, closeWindow);

        Label label2 = new Label("Password not changed!");
        HBox hb2 = new HBox();

        hb2.getChildren().addAll(label2, closeWindow);
        hb2.setSpacing(10);
        hb2.setLayoutX(48);
        hb2.setLayoutY(48);

        final Scene scene3 = new Scene(root3);
        root3.getChildren().addAll(hb2);

        //***************************************************************
        //here we define what all our buttons do within this transaction :)
        //***************************************************************
        delCust.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                //show the complete box dialog
                Label label2;
                label2 = new Label("Confirm deletion?");
                HBox hb2 = new HBox();
                Group root = new Group();

                Button closeWindow = new Button("Delete");
                hb2.getChildren().addAll(label2, closeWindow);
                hb2.setSpacing(10);
                hb2.setLayoutX(25);
                hb2.setLayoutY(48);
                root.getChildren().add(hb2);

                final Scene scene2 = new Scene(root);
                final Stage stage3 = new Stage();

                stage3.close();
                stage3.setScene(scene2);
                stage3.setHeight(150);
                stage3.setWidth(310);
                stage3.setResizable(false);
                stage3.show();

                closeWindow.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {

                        String custToDeleteNow = custNameBlock.getText();
                        String queryDelete = "DELETE FROM customer WHERE CompanyName = '" + custToDeleteNow + "'";

                        //delete from database
                        Statement updateDb = null;

                        //make the connection
                        try {
                            //set our session id and ip address in order to identify user.
                            updateDb = conn.createStatement();

                            int executeUpdate = updateDb.executeUpdate(queryDelete);

                            custList = new ArrayList<String>();

                            rs = st.executeQuery("select CompanyName from customer order by CompanyName;");
                            while (rs.next()) {
                                ////system.out.println(rs.getString(1));
                                custList.add(rs.getString(1));
                            }
                            ObservableList<String> custListingObs66 = FXCollections.observableArrayList(custList);

                            custListing.setItems(custListingObs66);

                        } catch (SQLException ex) {
                            Logger.getLogger(JobCenterController.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }

                        stage3.close();
                        stage2.close();

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

                        custListing.getSelectionModel().selectNext();
                    }
                });

            }

        });
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                //make the connection
                try {
                    String qry3 = "update customer set CompanyName='" + custNameBlock.getText() + "', "
                            + "CompanyCity='" + custCityBlock.getText() + "',"
                            + "CompanyAddress='" + custStBlock.getText() + "',"
                            + "CompanyZipCode='" + custZipBlock.getText() + "',"
                            + "CompanyState='" + custStateBlock.getText() + "',"
                            + "Contact1Name='" + custContactBlock.getText() + "',"
                            + "CompanyOfficePhone='" + custContactPhoneBlock.getText()
                            + "' where CompanyName = '" + custNameBlock.getText() + "'";

                    Statement updateDb = null;
                    updateDb = conn.createStatement();
                    int executeUpdate = updateDb.executeUpdate(qry3);

                    stage2.close();

                } catch (SQLException ex) {
                    Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });

    }

    @FXML
    private void addEmpEmailerAction(ActionEvent event) throws SQLException {
        String empToAdd = selEmpEmail.getSelectionModel().getSelectedItem().toString();
        empToAdd = empToAdd.substring(empToAdd.indexOf(":") + 2, empToAdd.length());

        String queryDelete = "insert into emaillist (emailSend) values('" + empToAdd + "');";
        ////system.out.println(queryDelete);

        //insert into database
        Statement updateDb = null;

        //make the connection
        try {
            //set our session id and ip address in order to identify user.
            updateDb = conn.createStatement();

            String emailChkr = "";
            for (int l = 0; l < empEmailList.size(); l++) {
                emailChkr += empEmailList.get(l) + ",";
            }
            if (!emailExist(empToAdd, emailChkr)) {
                int executeUpdate = updateDb.executeUpdate(queryDelete);
                refreshList();
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        updateEmailList();

    }

    @FXML
    private void deleteEmpEmailAction(ActionEvent event) throws SQLException {
        String EmailStr = empEmailListView.getSelectionModel().selectedItemProperty().getValue().toString();
        String queryDelete = "DELETE FROM emaillist WHERE emailSend = '" + EmailStr + "'";
        ////system.out.println(queryDelete);

        //insert into database
        Statement updateDb = null;

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user.
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryDelete);
            updateEmailList();

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void addAdminAction(ActionEvent event) throws SQLException {

        String getEmail = selEmpAdmin.getSelectionModel().getSelectedItem().toString();
        getEmail = getEmail.substring(getEmail.indexOf(":") + 2, getEmail.length());

        String queryAdmin = "", empidtoadd = "";
        //system.out.println(queryAdmin);

        //make the connection
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select uid from employees where email = '" + getEmail + "';");
            while (rs.next()) {
                empidtoadd = rs.getString(1);
                queryAdmin = "insert into administrators (adminPriv, employees_uid) values(1," + empidtoadd + ");";
            }
        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //insert into database
        Statement updateDb = null;

        //make the connection
        try {
            //set our session id and ip address in order to identify user.
            updateDb = conn.createStatement();

            if (adminExist(empidtoadd) == false) {
                int executeUpdate = updateDb.executeUpdate(queryAdmin);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        updateAdminList();

    }

    @FXML
    private void delAdminAction(ActionEvent event) throws SQLException {
        String uidToDelNow = empAdminListView.getSelectionModel().selectedItemProperty().getValue().toString();
        uidToDelNow = uidToDelNow.substring(uidToDelNow.indexOf(":") + 1, uidToDelNow.length());

        String queryDelete = "DELETE FROM administrators WHERE employees_uid = '" + uidToDelNow + "'";
        //system.out.println(queryDelete);

        //insert into database
        Statement updateDb = null;

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user.
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryDelete);
            updateAdminList();

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void insertReportAction(ActionEvent event) throws SQLException {
        String lastNameEmp = employeeTable.getSelectionModel().selectedItemProperty().getValue().getLastName(),
                firstNameEmp = employeeTable.getSelectionModel().selectedItemProperty().getValue().getFirstName(),
                emailEmp = employeeTable.getSelectionModel().selectedItemProperty().getValue().getEmail(),
                theOption = trackingBox.getValue().toString(),
                theDate = dateTracking.getText(),
                theNotes = notesTracking.getText(),
                empIDget;

        trackingBox.setValue("");
        dateTracking.setText("");
        notesTracking.setText("");

        String qryRun = "select uid from employees where fname = '" + firstNameEmp + "' and "
                + "lname = '" + lastNameEmp + "' and email = '" + emailEmp + "'",
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

        String queryDelete = "insert into employeetracking (action, date, description,employees_uid) values('" + theOption + "','"
                + theDate + "','" + theNotes + "'," + uidRet + ")";
        //system.out.println(queryDelete);

        //insert into database
        Statement updateDb = null;

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user.
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryDelete);
            updateAdminList();

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        displayMsg("Employee record noted.");
    }

    @FXML
    private void showEmpReport(ActionEvent event) throws SQLException {
        String qryRun = "";
        int countAction = 0;
        String lastNameEmp = employeeTable.getSelectionModel().selectedItemProperty().getValue().getLastName(),
                firstNameEmp = employeeTable.getSelectionModel().selectedItemProperty().getValue().getFirstName(),
                emailEmp = employeeTable.getSelectionModel().selectedItemProperty().getValue().getEmail();

        nameTracker.setText(firstNameEmp + " " + lastNameEmp);

        theReport = FXCollections.observableList(new ArrayList<String>());

        String qryRun2 = "select uid from employees where fname = '" + firstNameEmp + "' and "
                + "lname = '" + lastNameEmp + "' and email = '" + emailEmp + "'",
                uidRet = "";

        //make the connection and get the currently selected user id
        try {
            st = conn.createStatement();
            rs = st.executeQuery(qryRun2);

            while (rs.next()) {
                uidRet = rs.getString(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //make the connection
        try {

            qryRun = "select * from employeetracking where action = 'Suspended' and employees_uid = " + uidRet;
            st = conn.createStatement();
            rs = st.executeQuery(qryRun);
            while (rs.next()) {
                countAction++;
            }
            theReport.add("Suspended: " + new Integer(countAction));
            countAction = 0;

            qryRun = "select * from employeetracking where action = 'Verbal Warning' and employees_uid = " + uidRet;
            st = conn.createStatement();
            rs = st.executeQuery(qryRun);
            while (rs.next()) {
                countAction++;
            }
            theReport.add("Verbal Warning: " + new Integer(countAction));
            countAction = 0;

            qryRun = "select * from employeetracking where action = 'Unsafe Driving' and employees_uid = " + uidRet;
            st = conn.createStatement();
            rs = st.executeQuery(qryRun);
            while (rs.next()) {
                countAction++;
            }
            theReport.add("Unsafe Driving: " + new Integer(countAction));
            countAction = 0;

            qryRun = "select * from employeetracking where action = 'Equip Damage' and employees_uid = " + uidRet;
            st = conn.createStatement();
            rs = st.executeQuery(qryRun);
            while (rs.next()) {
                countAction++;
            }
            theReport.add("Equip Damage: " + new Integer(countAction));
            countAction = 0;

            qryRun = "select * from employeetracking where action = 'Call Out' and employees_uid = " + uidRet;
            st = conn.createStatement();
            rs = st.executeQuery(qryRun);
            while (rs.next()) {
                countAction++;
            }
            theReport.add("Call Out: " + new Integer(countAction));
            countAction = 0;

            qryRun = "select * from employeetracking where action = 'Vacation' and employees_uid = " + uidRet;
            st = conn.createStatement();
            rs = st.executeQuery(qryRun);
            while (rs.next()) {
                countAction++;
            }
            theReport.add("Vacation: " + new Integer(countAction));
            countAction = 0;

            qryRun = "select * from employeetracking where action = 'Personal Day' and employees_uid = " + uidRet;
            st = conn.createStatement();
            rs = st.executeQuery(qryRun);
            while (rs.next()) {
                countAction++;
            }
            theReport.add("Personal Day: " + new Integer(countAction));
            countAction = 0;

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        counterList.setItems(theReport);
        counterList.getSelectionModel().selectNext();
    }

    @FXML
    private void viewChosenTracker(ActionEvent event) throws SQLException {
        String qryRun = "";
        int countAction = 0;
        String theSelection = counterList.getSelectionModel().selectedItemProperty().getValue().toString(),
                uidRet = "",
                theItem = theSelection.substring(0, theSelection.indexOf(":"));

        theReport = FXCollections.observableList(new ArrayList<String>());

        String qryRun2 = "select * from employeetracking where action = '" + theItem + "' and employees_uid = " + getEmpID();

        //make the connection and get the currently selected user id
        try {
            st = conn.createStatement();
            rs = st.executeQuery(qryRun2);

            while (rs.next()) {
                theReport.add(rs.getString(5) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4));
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        dateList.setItems(theReport);
        dateList.getSelectionModel().selectNext();
    }

    @FXML
    private void deleteTrackerAction(ActionEvent event) throws SQLException {
        String uidToDel = dateList.getSelectionModel().selectedItemProperty().getValue().toString(),
                uidToDelNow = uidToDel.substring(0, uidToDel.indexOf("\t")),
                actionToDel = "",
                dateToDel = "",
                descToDel = "";

        uidToDel = uidToDel.substring(uidToDel.indexOf("\t"), uidToDel.length());
        uidToDel = uidToDel.substring(uidToDel.indexOf("\t") + 1, uidToDel.length());
        actionToDel = uidToDel.substring(0, uidToDel.indexOf("\t"));

        uidToDel = uidToDel.substring(uidToDel.indexOf("\t") + 1, uidToDel.length());
        dateToDel = uidToDel.substring(0, uidToDel.indexOf("\t"));

        uidToDel = uidToDel.substring(uidToDel.indexOf("\t") + 1, uidToDel.length());
        descToDel = uidToDel;

        String queryDelete = "DELETE FROM employeetracking WHERE employees_uid = '" + uidToDelNow
                + "' and action = '" + actionToDel
                + "' and date = '" + dateToDel
                + "' and description = '" + descToDel + "'";

        //system.out.println(queryDelete);

        //insert into database
        Statement updateDb = null;

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            //set our session id and ip address in order to identify user.
            updateDb = conn.createStatement();

            int executeUpdate = updateDb.executeUpdate(queryDelete);

            String qryRun = "";
            int countAction = 0;

            String theSelection = counterList.getSelectionModel().selectedItemProperty().getValue().toString(),
                    uidRet = "",
                    theItem = theSelection.substring(0, theSelection.indexOf(":"));

            theReport = FXCollections.observableList(new ArrayList<String>());

            String qryRun2 = "select * from employeetracking where action = '" + theItem + "' and employees_uid = " + getEmpID();

            //make the connection and get the currently selected user id
            try {
                st = conn.createStatement();
                rs = st.executeQuery(qryRun2);

                while (rs.next()) {
                    theReport.add(rs.getString(5) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4));
                }

            } catch (SQLException ex) {
                Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
            }

            dateList.setItems(theReport);
            dateList.getSelectionModel().selectNext();

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void chkEmpAction(ActionEvent event) throws SQLException {

        String queryDelete = "select uid from employees where not empAssigned = 'no' and shopEmp = 'no'";
        List<String> employeesAssn = new ArrayList<String>(),
                employeesParsed = new ArrayList<String>();
        String theStr = "", tmpStr = "";

        //system.out.println(queryDelete);

        //insert into database
        Statement updateDb = null;

        //returns a list of all employees that are not assigned to a job
        employeesAssn = listEmpAssigned();

        for (int i = 0; i < employeesAssn.size(); i++) {
            //system.out.println(employeesAssn.get(i));
            theStr = employeesAssn.get(i);
            theStr = theStr.substring(1, theStr.length());

            while (true) {
                if (theStr.contains("/")) {
                    employeesParsed.add(theStr.substring(0, theStr.indexOf("/")));
                    theStr = theStr.substring(theStr.indexOf("/") + 1, theStr.length());
                } else {
                    employeesParsed.add(theStr.substring(0, theStr.length()));
                    break;
                }
            }

        }

        //update the employee database to tell it who is assigned and who isnt.
        String addEmpStatus = "";

        //insert into database
        Statement updateDb5 = null;

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);

            updateDb5 = conn.createStatement();

            //everyone gets reset to no
            addEmpStatus = "update employees "
                    + "set empAssigned = 'no'";
            int executeUpdate = updateDb5.executeUpdate(addEmpStatus);

            for (int i = 0; i < employeesParsed.size(); i++) {
                addEmpStatus = "update employees "
                        + "set empAssigned = 'yes'"
                        + " where fname = '" + employeesParsed.get(i).substring(0, employeesParsed.get(i).indexOf(" "))
                        + "' and lname = '" + employeesParsed.get(i).substring(employeesParsed.get(i).indexOf(" ") + 1, employeesParsed.get(i).length()) + "';";

                //system.out.println(addEmpStatus);
                executeUpdate = updateDb5.executeUpdate(addEmpStatus);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        //now we need to scan through the employees table to see if anyone was left out?
        String findEmpLeft = "select * from employees where not empAssigned = 'yes' and shopEmp = 'no';";
        List<String> addToEmp = new ArrayList<String>();

        try {
            st = conn.createStatement();
            rs = st.executeQuery(findEmpLeft);
            while (rs.next()) {
                addToEmp.add(rs.getString(1) + " " + rs.getString(2));
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        String e15 = "";

        for (int i = 0; i < addToEmp.size(); i++) {
            e15 += addToEmp.get(i) + "\n";
        }

        final TextArea text = new TextArea(e15);
        text.setWrapText(true);
        Label label125 = new Label("Employees not yet assigned");

        HBox hb2 = new HBox();
        Group root123 = new Group();

        Button closeWindow = new Button("Send");

        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("Employees not yet assigned"), 0, 0);

        grid.add(text, 0, 5, 2, 1);
        root123.getChildren().add(grid);

        final Scene scene2 = new Scene(root123);
        final Stage stage2 = new Stage();

        stage2.close();
        stage2.setScene(scene2);
        stage2.setHeight(255);
        stage2.setWidth(480);
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
    private void archiveAction(ActionEvent event) throws SQLException {
        // here we make image from vbox and add it to scene, can be repeated :) 
        final Group root = new Group();
        Button printScreen = new Button("Print");

        Rectangle r = new Rectangle(0, 0, 1120, 40);
        r.setFill(Color.LIGHTGREY);
        r.strokeProperty().set(Color.GRAY);

        Rectangle r2 = new Rectangle(0, 42, 1120, 18);
        r2.setFill(Color.YELLOW);
        r2.strokeProperty().set(Color.YELLOW);

        Rectangle r3 = new Rectangle(0, 342, 1120, 18);
        r3.setFill(Color.YELLOW);
        r3.strokeProperty().set(Color.YELLOW);

        GridPane gridpane = new GridPane();

        String qry = "select * from currentjobs where status ='IN PROGRESS';";

        List<String> empListSort = new ArrayList<String>();
        List<String> equipListSort = new ArrayList<String>();
        String jobTxtStr = "", jobTypeStr = "", jobDateTxtStr = "", jobStatusStr = "",
                empListStr = "", equipListStr = "", jobNameStr = "";

        //set location and build of grid pane...
        gridpane.setLayoutY(40);
        int startAnew = 9, empNumber = 0, vehNumber = 0, numLocation = 0, columnLoc = 0,
                columnLoc2 = 1;

        //set location and build of grid pane...
        gridpane.setLayoutY(40);
        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints(20);
            row.setValignment(VPos.CENTER);

            gridpane.getRowConstraints().add(row);

        }
        for (int i = 0; i < 10; i++) {
            ColumnConstraints column = new ColumnConstraints(112);
            column.setHalignment(HPos.CENTER);
            gridpane.getColumnConstraints().add(column);
        }

        // or convenience methods set more than one constraint at once...
        Text label = new Text("Job Number"),
                label1 = new Text("Status"),
                label2 = new Text("Job Name"),
                label3 = new Text("Work Type"),
                label4 = new Text("Work Date"),
                labelTitle = new Text("Job Summary Report: "),
                labelComp = new Text("Video Pipe Service");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();

        String dateToday = (dateFormat.format(cal.getTime()));
        Text labelDate = labelDate = new Text(dateToday);

        label.setStyle("-fx-font-size: 10;");
        label1.setStyle("-fx-font-size: 10;");
        label2.setStyle("-fx-font-size: 10;");
        label3.setStyle("-fx-font-size: 10;");
        label4.setStyle("-fx-font-size: 10;");

        labelTitle.setStyle("-fx-font-size: 16;");
        labelTitle.setY(25);
        labelTitle.setX(10);
        labelDate.setStyle("-fx-font-size: 15;");
        labelDate.setY(25);
        labelDate.setX(175);
        labelComp.setStyle("-fx-font-size: 16;");
        labelComp.setY(25);
        labelComp.setX(965);
        printScreen.setLayoutX(265);
        printScreen.setLayoutY(7);

        gridpane.add(label, 0, 0); // column=2 row=1        
        gridpane.add(label1, 0, 1);  // column=3 row=1
        gridpane.add(label2, 0, 2); // column=1 row=1
        gridpane.add(label3, 0, 3); // column=2 row=1
        gridpane.add(label4, 0, 4); // column=2 row=1
        numLocation = 5;
        columnLoc = 1;

        gridpane.setGridLinesVisible(true);

        for (int i = 1; i <= 5; i++) {
            RowConstraints row = new RowConstraints(20);
            row.setValignment(VPos.CENTER);
            String nameLabel = "Person " + i;
            Text tmpTxt = new Text(nameLabel);
            tmpTxt.setStyle("-fx-font-size: 10;");

            gridpane.getRowConstraints().add(row);
            gridpane.add(tmpTxt, 0, numLocation);

            //increment to track which column we are at.
            numLocation++;

        }
        for (int i = 1; i <= 5; i++) {
            RowConstraints row = new RowConstraints(20);
            row.setValignment(VPos.CENTER);
            String nameLabel = "Veh/Equip " + i;
            Text tmpTxt = new Text(nameLabel);
            tmpTxt.setStyle("-fx-font-size: 10;");

            gridpane.getRowConstraints().add(row);
            gridpane.add(tmpTxt, 0, numLocation);

            //increment to track which column we are at.
            numLocation++;

        }

        rs = st.executeQuery(qry);

        while (rs.next()) {

            int counter = 0;

            jobTxtStr = rs.getString(5);
            jobTypeStr = rs.getString(9);
            if (jobTypeStr.length() > 8) {
                jobTypeStr = jobTypeStr.substring(1, 8);
            } else {
                jobTypeStr = jobTypeStr.substring(1, jobTypeStr.length());
            }

            jobDateTxtStr = rs.getString(7);
            jobStatusStr = rs.getString(17);
            jobNameStr = rs.getString(6);
            empListStr = rs.getString(10);
            equipListStr = rs.getString(11);

            Text add;

            if (columnLoc <= startAnew) {
                add = new Text(jobTxtStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;
                add = new Text(jobStatusStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;
                add = new Text(jobNameStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;
                add = new Text(jobTypeStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;
                add = new Text(jobDateTxtStr);
                add.setStyle("-fx-font-size: 10;");
                gridpane.add(add, columnLoc, counter);
                counter++;

                String strManip = empListStr;

                while (strManip.lastIndexOf("/") >= 0) {

                    add = new Text(strManip.substring(strManip.lastIndexOf("/") + 1, strManip.length()));
                    add.setStyle("-fx-font-size: 10;");
                    gridpane.add(add, columnLoc, counter);
                    counter++;

                    strManip = strManip.substring(0, strManip.lastIndexOf("/"));

                }

                counter = 10;
                strManip = equipListStr;
                while (strManip.lastIndexOf("/") >= 0) {
                    //system.out.println(strManip);
                    add = new Text(strManip.substring(strManip.lastIndexOf("/") + 1, strManip.length()));
                    add.setStyle("-fx-font-size: 10;");
                    gridpane.add(add, columnLoc, counter);
                    counter++;

                    strManip = strManip.substring(0, strManip.lastIndexOf("/"));
                    //system.out.println(strManip);

                }

            } else {

                counter = 15;
                //system.out.println("TIME TO PRINT ANOTHER TABLE");

                for (int i = 0; i < 14; i++) {
                    RowConstraints row = new RowConstraints(20);
                    row.setValignment(VPos.CENTER);

                    gridpane.getRowConstraints().add(row);

                }

                // or convenience methods set more than one constraint at once...
                Text label00 = new Text("Job Number"),
                        label11 = new Text("Status"),
                        label22 = new Text("Job Name"),
                        label33 = new Text("Work Type"),
                        label44 = new Text("Work Date");

                label00.setStyle("-fx-font-size: 10;");
                label11.setStyle("-fx-font-size: 10;");
                label22.setStyle("-fx-font-size: 10;");
                label33.setStyle("-fx-font-size: 10;");
                label44.setStyle("-fx-font-size: 10;");

                gridpane.add(label00, 0, 15); // column=2 row=1        
                gridpane.add(label11, 0, 16);  // column=3 row=1
                gridpane.add(label22, 0, 17); // column=1 row=1
                gridpane.add(label33, 0, 18); // column=2 row=1
                gridpane.add(label44, 0, 19); // column=2 row=1
                numLocation = 20;

                for (int i = 1; i <= 5; i++) {
                    RowConstraints row = new RowConstraints(20);
                    row.setValignment(VPos.CENTER);
                    String nameLabel = "Person " + i;
                    Text tmpTxt = new Text(nameLabel);
                    tmpTxt.setStyle("-fx-font-size: 10;");

                    gridpane.getRowConstraints().add(row);
                    gridpane.add(tmpTxt, 0, numLocation);

                    //increment to track which column we are at.
                    numLocation++;

                }

                for (int i = 1; i <= 5; i++) {
                    RowConstraints row = new RowConstraints(20);
                    row.setValignment(VPos.CENTER);
                    String nameLabel = "Veh/Equip " + i;
                    Text tmpTxt = new Text(nameLabel);
                    tmpTxt.setStyle("-fx-font-size: 10;");

                    gridpane.getRowConstraints().add(row);
                    gridpane.add(tmpTxt, 0, numLocation);

                    //increment to track which column we are at.
                    numLocation++;

                }

                jobTxtStr = rs.getString(5);
                jobTypeStr = rs.getString(9);
                if (jobTypeStr.length() > 8) {
                    jobTypeStr = jobTypeStr.substring(1, 8);
                } else {
                    jobTypeStr = jobTypeStr.substring(1, jobTypeStr.length());
                }

                jobDateTxtStr = rs.getString(7);
                jobStatusStr = rs.getString(17);
                jobNameStr = rs.getString(6);
                empListStr = rs.getString(10);
                equipListStr = rs.getString(11);

                Text add2;

                add2 = new Text(jobTxtStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;
                add2 = new Text(jobStatusStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;
                add2 = new Text(jobNameStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;
                add2 = new Text(jobTypeStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;
                add2 = new Text(jobDateTxtStr);
                add2.setStyle("-fx-font-size: 10;");
                gridpane.add(add2, columnLoc2, counter);
                counter++;

                String strManip = empListStr;

                while (strManip.lastIndexOf("/") >= 0) {

                    add2 = new Text(strManip.substring(strManip.lastIndexOf("/") + 1, strManip.length()));
                    add2.setStyle("-fx-font-size: 10;");
                    gridpane.add(add2, columnLoc2, counter);
                    counter++;

                    strManip = strManip.substring(0, strManip.lastIndexOf("/"));

                }

                counter = 25;
                strManip = equipListStr;
                while (strManip.lastIndexOf("/") >= 0) {
                    //system.out.println(strManip);
                    add2 = new Text(strManip.substring(strManip.lastIndexOf("/") + 1, strManip.length()));
                    add2.setStyle("-fx-font-size: 10;");
                    gridpane.add(add2, columnLoc2, counter);
                    counter++;

                    strManip = strManip.substring(0, strManip.lastIndexOf("/"));
                    //system.out.println(strManip);

                }
                columnLoc2++;
            }
            columnLoc++;
        }

        root.getChildren().add(r);
        root.getChildren().add(r2);
        root.getChildren().add(r3);
        root.getChildren().add(printScreen);
        root.getChildren().add(labelTitle);
        root.getChildren().add(labelDate);
        root.getChildren().add(labelComp);

        root.getChildren().add(gridpane);

        WritableImage snapshot = root.snapshot(new SnapshotParameters(), null);
        root.getChildren().add(new ImageView(snapshot));

        BufferedImage bufferedImage = new BufferedImage(550, 400, BufferedImage.TYPE_INT_ARGB);
        BufferedImage image;
        //for Production -- administrator
        //File file = new File("C:/Users/vangfc/Desktop/job_board.jpg");
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal2 = Calendar.getInstance();

        String dateToday2 = (dateFormat.format(cal.getTime()));
        dateToday2 = dateToday2.replaceAll("/", "-");
        String fileNameSaver = "C:/Users/administrator/Desktop/jobCenter/archive/job_board_" + dateToday2 + ".jpg";

        File file = new File(fileNameSaver);

        image = javafx.embed.swing.SwingFXUtils.fromFXImage(snapshot, bufferedImage);
        try {
            Graphics2D gd = (Graphics2D) image.getGraphics();
            // gd.translate(root.getWidth(), vbox.getHeight());
            ImageIO.write(image, "png", file);
            displayMsg("Job board archived.");

        } catch (IOException ex) {
            displayMsg("Job board archive error.");
            System.err.print(ex);
        };

    }

    @FXML
    private void printJobInfo(ActionEvent event) {
        final Group root = new Group();
        String curDateStr = "";

        Rectangle r = new Rectangle(0, 0, 1120, 40);
        r.setFill(Color.LIGHTGREY);
        r.strokeProperty().set(Color.GRAY);

        Rectangle r2 = new Rectangle(0, 42, 1120, 18);
        r2.setFill(Color.YELLOW);
        r2.strokeProperty().set(Color.YELLOW);

        Rectangle r3 = new Rectangle(0, 342, 1120, 18);
        r3.setFill(Color.YELLOW);
        r3.strokeProperty().set(Color.YELLOW);

        GridPane gridpane = new GridPane(),
                gridpane2 = new GridPane(),
                gridpane3 = new GridPane();

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();

        String dateToday = (dateFormat.format(cal.getTime()));
        dateToday = "Date: " + dateToday;

        Text printText1 = new Text("Video Pipe Services, LLC"),
                printText2 = new Text("Beltsville, MD"),
                printText3 = new Text("Job Information Sheet"),
                printText4 = new Text(dateToday),
                printText5 = new Text("Customer"),
                printText6 = new Text("Company name:\t\t" + setCustName.getText()),
                printText7 = new Text("Street Address:\t\t" + setCustAddr.getText()),
                printText8 = new Text("City:\t\t\t\t\t" + setCustCity.getText()),
                printText9 = new Text("Point of contact:\t\t" + setCustPOC.getText()),
                printText10 = new Text("Phone:\t\t\t\t" + setCustPhone.getText()),
                printText11 = new Text("State:\t\t\t\t" + setCustState.getText()),
                printText12 = new Text("Zip:\t\t\t\t\t" + setCustZip.getText()),
                printText13 = new Text("Job Number:\t\t\t" + jobTitle.getText()),
                printText14 = new Text("Job Name:\t\t\t" + jobName.getText()),
                printText15 = new Text("Cust Job Number:\t\t" + custJobNum.getText()),
                printText16 = new Text("Job Start Date:\t\t\t" + startDate.getText()),
                printText17 = new Text("Special Instructions:\t\t\t\t" + sInstr.getText()),
                printText18 = new Text("Instructions (disposal):\t\t\t" + dInstr.getText()),
                printText19 = new Text("Instructions (traffic control):\t\t" + tInstr.getText()),
                printText20 = new Text("Instructions (water source):\t\t" + wInstr.getText());

        gridpane.setLayoutY(10);

        //gridlines for edit customer info
        gridpane.setGridLinesVisible(false);

        gridpane.add(printText1, 1, 1);
        gridpane.add(printText2, 1, 2);
        gridpane.add(printText3, 1, 3);
        gridpane.add(printText4, 1, 4);

        gridpane2.setLayoutY(90);

        //gridlines for edit customer info
        gridpane2.setGridLinesVisible(false);

        gridpane2.add(printText5, 1, 5);
        gridpane2.add(printText6, 1, 6);
        gridpane2.add(printText7, 1, 7);
        gridpane2.add(printText8, 1, 8);
        gridpane2.add(printText9, 1, 9);
        gridpane2.add(printText10, 1, 10);
        gridpane2.add(printText11, 1, 11);
        gridpane2.add(printText12, 1, 12);
        gridpane2.add(printText13, 1, 13);
        gridpane2.add(printText14, 1, 14);
        gridpane2.add(printText15, 1, 15);
        gridpane2.add(printText16, 1, 16);

        gridpane3.setLayoutY(300);

        //gridlines for edit customer info
        gridpane3.setGridLinesVisible(false);

        gridpane3.add(printText17, 1, 17);
        gridpane3.add(printText18, 1, 18);
        gridpane3.add(printText19, 1, 19);
        gridpane3.add(printText20, 1, 20);

        root.getChildren().add(gridpane);
        root.getChildren().add(gridpane2);
        root.getChildren().add(gridpane3);

        Scene scene2 = new Scene(root);

        stageJob = new Stage();
      //  stageJob.setX(Screen.getScreens().get(1).getVisualBounds().getMinX());
        // stageJob.setY(Screen.getScreens().get(1).getVisualBounds().getMinY());
        //stageJob.setResizable(false);    
        //stageJob.setFullScreen(true);
        //set the dimesions to the screen size:
        //stageJob.setWidth(Screen.getScreens().get(1).getVisualBounds().getWidth());
        // stageJob.setHeight(Screen.getScreens().get(1).getVisualBounds().getHeight());

        //stageJob.initStyle(StageStyle.UNDECORATED);
        stageJob.setScene(scene2);
        stageJob.setHeight(600);
        stageJob.setWidth(700);
        stageJob.show();

        //Print the root node
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);

        double scaleX = pageLayout.getPrintableWidth() / 1524;
        double scaleY = pageLayout.getPrintableHeight() / 662;

        root.getTransforms().add(new Scale(scaleX, scaleY));

        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null) {

            boolean success = job.printPage(root);

            if (success) {

                job.endJob();
                stageJob.close();
            }

        }
    }

}
