/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jobcenter;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author vangfc
 */
public class users {
    
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty userName;
    private final SimpleStringProperty password;

    public users(String fName, String lName, String uname, String pwd) {
        this.firstName = new SimpleStringProperty(fName);
        this.lastName = new SimpleStringProperty(lName);
        this.userName = new SimpleStringProperty(uname);
        this.password = new SimpleStringProperty(pwd);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String fName) {
        firstName.set(fName);
    }

    public String getPwd() {
        return password.get();
    }

    public void setPwd(String phoneStrVar) {
        password.set(phoneStrVar);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String fName) {
        lastName.set(fName);
    }

    public String getUsername() {
        return userName.get();
    }

    public void setUsername(String unameset) {
        userName.set(unameset);
    }
}
