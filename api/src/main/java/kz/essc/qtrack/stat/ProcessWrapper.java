package kz.essc.qtrack.stat;

import kz.essc.qtrack.client.Process;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class ProcessWrapper {
    private String client;
    private Long line;
    private Long operator;
    private Date begin;
    private String sBegin;
    private Date end;
    private int wait;
    private int handling;
    private long count;
    private double average;

    public String getClient() {
        return client;
    }
    public void setClient(String client) {
        this.client = client;
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

    public Date getBegin() {
        return begin;
    }
    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public String getsBegin() {
        return sBegin;
    }
    public void setsBegin(String sBegin) {
        this.sBegin = sBegin;
    }

    public Date getEnd() {
        return end;
    }
    public void setEnd(Date end) {
        this.end = end;
    }

    public int getWait() {
        return wait;
    }
    public void setWait(int wait) {
        this.wait = wait;
    }

    public int getHandling() {
        return handling;
    }
    public void setHandling(int handling) {
        this.handling = handling;
    }

    public long getCount() {
        return count;
    }
    public void setCount(long count) {
        this.count = count;
    }

    public double getAverage() {
        return average;
    }
    public void setAverage(double average) {
        this.average = average;
    }

    public static ProcessWrapper wrap(Process p){
        ProcessWrapper w = new ProcessWrapper();

        try {
            w.setClient(p.getClientCode());
            w.setLine(p.getLineId());
            w.setOperator(p.getOperatorId());
            w.setBegin(p.getBegin());
            w.setEnd(p.getEnd());
            w.setWait(p.getWait());
            w.setHandling(p.getHandling());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return w;
    }

    public static List<ProcessWrapper> wrap(List<Process> processes){
        List<ProcessWrapper> list = new ArrayList<>();
        for (Process p: processes)
            list.add(wrap(p));

        return list;
    }
}
