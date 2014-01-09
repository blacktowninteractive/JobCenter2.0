/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobcenter;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author vangfc
 */
public class editHistClass {

    private final SimpleStringProperty firstName5;
    private final SimpleStringProperty lastName5;
    private final SimpleStringProperty descrNow5;
    private final SimpleStringProperty jobNameStr5;
    private final SimpleStringProperty dateMod5;

    public editHistClass(String firstName11, String lastName, String descrNow, String jobNameStr,String dateMod) {
        this.firstName5 = new SimpleStringProperty(firstName11);
        this.lastName5 = new SimpleStringProperty(lastName);
        this.descrNow5 = new SimpleStringProperty(descrNow);
        this.jobNameStr5 = new SimpleStringProperty(jobNameStr);
        this.dateMod5 = new SimpleStringProperty(dateMod);
    }

    //some gd bs, you have to capitalize this letter "F" to get this working properly, must be in their dumb api
    public String getFirstName() {
        return firstName5.get();
    }

    public void setFirstName5(String fn) {
        firstName5.set(fn);
    }

    public String getLastName() {
        return lastName5.get();
    }

    public void setLastName(String ln) {
        lastName5.set(ln);
    }

    public String getDescrNow() {
        return descrNow5.get();
    }

    public void setDescrNow(String d) {
        descrNow5.set(d);
    }

    public String getJobName() {
        return jobNameStr5.get();
    }

    public void setJobName(String jobNameString) {
        jobNameStr5.set(jobNameString);
    }
    
        public String getDate() {
        return dateMod5.get();
    }

    public void setDate(String jobNameString) {
        dateMod5.set(jobNameString);
    }
}
