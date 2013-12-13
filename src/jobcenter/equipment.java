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
public class equipment {
     private final SimpleStringProperty veh;
    private final SimpleStringProperty type;
    private final SimpleStringProperty stat; 

    public equipment(String vname, String vtype, String vstat) {
        this.veh = new SimpleStringProperty(vname);
        this.type = new SimpleStringProperty(vtype);
        this.stat = new SimpleStringProperty(vstat); 
    }

    public String getVeh() {
        return veh.get();
    }

    public void setVeh(String vehVar) {
        veh.set(vehVar);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String typestrvar) {
        type.set(typestrvar);
    }

    public String getStat() {
        return stat.get();
    }

    public void setStat(String statVar) {
        stat.set(statVar);
    }
 
}
