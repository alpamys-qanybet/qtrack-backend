package kz.essc.qtrack.client;

import kz.essc.qtrack.line.Line;
import kz.essc.qtrack.sc.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="cl_client")
public class Client implements Serializable {
    private static final long serialVersionUID = -5264632160260231997L;

    private long id;
    private Date date;
    private Line line;
    private String code; // prefix+999
    private User operator;

    public enum Status {
        WAITING, CALLED, IN_PROCESS
    }

    private String status;
    private int order;
    private Date event; // last event time
    private String lang;

    @Id
    @Column(name="id_")
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name="date_")
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne
    @JoinColumn(name = "line_")
    public Line getLine() {
        return line;
    }
    public void setLine(Line line) {
        this.line = line;
    }

    @Column(name="code_")
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    @OneToOne
    @JoinColumn(name = "operator_")
    public User getOperator() {
        return operator;
    }
    public void setOperator(User operator) {
        this.operator = operator;
    }

    @Column(name="status_")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name="order_")
    public int getOrder() {
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }

    @Column(name="event_")
    public Date getEvent() {
        return event;
    }
    public void setEvent(Date event) {
        this.event = event;
    }

    @Column(name="lang_")
    public String getLang() {
        return lang;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }
}
