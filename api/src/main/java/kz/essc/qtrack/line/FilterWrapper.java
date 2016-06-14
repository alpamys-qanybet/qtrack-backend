package kz.essc.qtrack.line;

import javax.ejb.Stateless;
import java.util.Date;

@Stateless
public class FilterWrapper {
    private Date date;

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
