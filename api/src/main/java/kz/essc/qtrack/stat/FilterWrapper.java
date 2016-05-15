package kz.essc.qtrack.stat;

import javax.ejb.Stateless;
import java.util.Date;

@Stateless
public class FilterWrapper {
    private Date begin;
    private Date end;
    private Long line;
    private Long operator;

    public Date getEnd() {
        return end;
    }
    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getBegin() {
        return begin;
    }
    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Long getLine() {
        return line;
    }
    public void setLine(Long line) {
        this.line = line;
    }

    public Long getOperator() {
        return operator;
    }
    public void setOperator(Long operator) {
        this.operator = operator;
    }
}
