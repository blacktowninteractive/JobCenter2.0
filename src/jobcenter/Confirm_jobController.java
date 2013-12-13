/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobcenter;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import static jobcenter.JobCenterMainController.stageJob;

/**
 * FXML Controller class
 *
 * @author vangfc
 */
public class Confirm_jobController extends JobCenterMainController 
    implements Initializable{

     
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void confirmJobGo(ActionEvent event) throws SQLException { 
        
        System.out.println("job type");
        for(int i=0;i<jobTypePicked.size();i++)
        {
            System.out.print(jobTypePicked.get(i));
            System.out.print(",");
        }
        
        System.out.println("equipment");
        for(int i=0;i<vehList.size();i++)
        {
            System.out.print(vehList.get(i));
            System.out.print(",");
        }
        
        System.out.println("employees");
        for(int i=0;i<empListSel.size();i++)
        {
            System.out.println(empListSel.get(i));
            System.out.print(",");
        }
        System.out.println("CID: "+cid);
       String qry = "INSERT INTO currentjobs (CurJobID, Customer_CID, "
            + "CustJobNum, CustJobName, JobTitle, JobName, JobWorkDate, "
            + "JobStartTime, JobType, JobEmployees, JobEandV, S_Instr, "
            + "D_Instr, T_Instr, W_Instr, billing,status, jobSiteAddr,"
               + "jobCitySite, jobStateLoc, jobZipLoc) "
            + "VALUES (NULL, '"+cid+"', '"+custJobNumStr+"', '"+custJobNameStr+"', '"+jobTitleStr+
                "', '"+jobNameStr+"', '"+startDateStr+"', '"+startTimeStr+"', '"+jobtypecompiled+
                "','"+empCompiled+"', '"+equipCompiled+"', '"+sI+"', '"+dI+"'"
        + ", '"+tI+"', '"+wI+"', '"+billing+"','"+status+"','"+ streetAddrStr +"','"+  
               cityStr+"','"+ stateStr+"','"+ zipStr+");";
        System.out.println("qry: "+qry);
        
        
        
         //make the connection
        try {
            conn = DriverManager.getConnection(url, userdb, passdb);
        } catch (SQLException ex) {
            Logger.getLogger(JobCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (conn != null && !conn.isClosed()) {
            System.out.println("Connection Established...");
        }

          
        //delete all entries associated with IP before exiting to the login screen
        Statement updateDb = null;
        updateDb = conn.createStatement();
  
        //set our session id and ip address in order to identify user.
        int executeUpdate = updateDb.executeUpdate(qry);
        //refreshList();
         stageJob.close();
    }

    @FXML
    private void cancelJobGo(ActionEvent event) {
        System.out.println("test2");
        stageJob.close();
        
        
    }
}
