/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobcenter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Text;

/**
 *
 * @author vangfc
 */
public class JobCenterController implements Initializable, ScreenController {

    protected String user, pass;
    private static Connection conn;
    //private static String url = "jdbc:mysql://localhost/jobcenter";
    //allows access to external database connection
    //public static String url = "jdbc:mysql://192.168.1.104/jobcenter"; 
    //public static String userdb = "vangfc";//Username of database  
    //public static String passdb = "password";//Password of database
    
    private static String url = "jdbc:mysql://192.168.1.112/jobcenter";
    private static String userdb = "videoPipe";//Username of database  
    private static String passdb = "Vps1566!!";//Password of database
    
    private ScreenPane myScreenPane;
    public TextField usernameStr;
    public Text DatabaseText;
    public PasswordField passwd;
    Statement st = null;
    ResultSet rs = null;
    boolean authorized = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        //make the connection
        
       
    }

    @Override
    public void setScreenPane(ScreenPane screenPage) { 
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);
            if (conn != null && !conn.isClosed()) {
             DatabaseText.setText("Database Connected!");
        }
            else
             DatabaseText.setText("Database Not Connected!");   
            
        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
            DatabaseText.setText(ex.toString());
        }
        
        myScreenPane = screenPage;
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws SQLException, InstantiationException, IllegalAccessException, NoSuchAlgorithmException, FileNotFoundException, UnsupportedEncodingException, UnknownHostException {
        user = usernameStr.getText();
        pass = passwd.getText();
        String uid = "";
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateStr = dateFormat.format(date);
        
        System.out.println(dateFormat.format(date));

        //for secure password
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(pass.getBytes());

        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtextpw = bigInt.toString(16);

        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtextpw.length() < 32) {
            hashtextpw = "0" + hashtextpw;
        }

        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver).newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //unique identifier for different computers
        InetAddress IP = InetAddress.getLocalHost();
        System.out.println("IP of my system is := " + IP.getHostAddress());
        String ipAddr = IP.getHostAddress();

        System.out.println("username: " + user);
        System.out.println("password: " + pass);
        System.out.println("passMD5: " + hashtextpw);

        //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);
        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (conn != null && !conn.isClosed()) {
            System.out.println("Connection Established...");
        }

        st = conn.createStatement();
        rs = st.executeQuery("select userName from users;");

        while (rs.next()) {
            System.out.println(rs.getString(1));

            //check to see if username matches
            if (rs.getString(1).equals(user)) {
                String qry = "select password from users where userName = '" + user + "';";
                System.out.println(qry);
                rs = st.executeQuery(qry);

                while (rs.next()) {
                    System.out.println(rs.getString(1));
                    //check to see if password matches
                    if (rs.getString(1).equals(hashtextpw)) {
                        authorized = true;
                    }
                }
                
                	qry = "select employees_uid from users where userName = '" + user + "';";
                System.out.println(qry);
                rs = st.executeQuery(qry);

                while (rs.next()) {
                    //System.out.println(rs.getString(1));
                    //check to see if password matches
                    uid = rs.getString(1);
                    }
                
            
            }
        }

        if (authorized == true) {
            Statement updateDb = null;
            updateDb = conn.createStatement();

            String insertSess = "insert into session (userName, ipAddr, employees_uid, dateLogged) values ('" 
                    + user + "','" + ipAddr + "',"+ uid+ ",'"+dateStr+"');";
            System.out.println(insertSess);

            //set our session id and ip address in order to identify user.
            int executeUpdate = updateDb.executeUpdate(insertSess);

            myScreenPane.setScreen("mainScreen");
        } else {
            myScreenPane.setScreen("login");
        }

    }
}
