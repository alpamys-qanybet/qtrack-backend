package kz.essc.qtrack.line;

import kz.essc.qtrack.client.Client;
import kz.essc.qtrack.sc.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="li_line")
public class Line implements Serializable {
    private static final long serialVersionUID = -1833134119141940745L;

    private long id;
    private String name;
    private String prefix;
    private List<User> operators;
    private List<Client> clients;
    private int length = 0; // set zero when clients end
    private int counter = 0; // 0-999
    private Boolean enabled;
    private LineHierarchy lineHierarchy;
    private Date begin;
    private Date end;

    @Id
    @Column(name="id_")
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name="name_", nullable=false)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name="prefix_")
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @OneToMany(mappedBy = "line")
    public List<User> getOperators() {
        return operators;
    }
    public void setOperators(List<User> operators) {
        this.operators = operators;
    }

    @OneToMany(mappedBy = "line")
    public List<Client> getClients() {
        return clients;
    }
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    @Column(name="length_")
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }

    @Column(name="counter_")
    public int getCounter() {
        return counter;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Column(name="enabled_")
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @ManyToOne
    @JoinColumn(name = "line_hierarchy_")
    public LineHierarchy getLineHierarchy() {
        return lineHierarchy;
    }
    public void setLineHierarchy(LineHierarchy lineHierarchy) {
        this.lineHierarchy = lineHierarchy;
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
}