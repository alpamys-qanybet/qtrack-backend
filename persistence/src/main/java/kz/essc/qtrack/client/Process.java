package kz.essc.qtrack.client;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="cl_process")
public class Process implements Serializable {
    private static final long serialVersionUID = 7212351262464956422L;

    private long id;
    private String clientCode;
    private Long clientId;
    private Long lineId;
    private Long operatorId;
    private Date begin;
    private Date end;
    private int wait;
    private int handling;

    @Id
    @Column(name="id_")
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name="client_")
    public String getClientCode() {
        return clientCode;
    }
    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    @Column(name="client_id_")
    public Long getClientId() {
        return clientId;
    }
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Column(name = "line_")
    public Long getLineId() {
        return lineId;
    }
    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    @Column(name = "operator_")
    public Long getOperatorId() {
        return operatorId;
    }
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    @Column(name="begin_")
    public Date getBegin() {
        return begin;
    }
    public void setBegin(Date begin) {
        this.begin = begin;
    }

    @Column(name="end_")
    public Date getEnd() {
        return end;
    }
    public void setEnd(Date end) {
        this.end = end;
    }

    @Column(name="wait_")
    public int getWait() {
        return wait;
    }
    public void setWait(int wait) {
        this.wait = wait;
    }

    @Column(name="handling_")
    public int getHandling() {
        return handling;
    }
    public void setHandling(int handling) {
        this.handling = handling;
    }
}
