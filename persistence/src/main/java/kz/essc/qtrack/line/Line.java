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
//    private String prefix;
    private List<User> operators;
    private List<Client> clients;
    private int length = 0; // set zero when clients end
    private int size = 0; // increases till next day
    private int counter = 0; // 1-9999
    private int counterBegin = 1;
    private int counterEnd = 9999;
    private int limit = 20;
    private int limitAdditional = 0;
    private Boolean enabled = false;
    private LineHierarchy lineHierarchy;
    private Date begin;
    private Date end;
    private Date suBegin;
    private Date suEnd;
    private Date moBegin;
    private Date moEnd;
    private Date tuBegin;
    private Date tuEnd;
    private Date weBegin;
    private Date weEnd;
    private Date thBegin;
    private Date thEnd;
    private Date frBegin;
    private Date frEnd;
    private Date stBegin;
    private Date stEnd;
    private Boolean isRaw = true; // raw - just queue, not row - by writing down the time

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

//    @Column(name="prefix_")
//    public String getPrefix() {
//        return prefix;
//    }
//    public void setPrefix(String prefix) {
//        this.prefix = prefix;
//    }

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

    @Column(name="size_")
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    @Column(name="counter_")
    public int getCounter() {
        return counter;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Column(name="counter_begin_")
    public int getCounterBegin() {
        return counterBegin;
    }
    public void setCounterBegin(int counterBegin) {
        this.counterBegin = counterBegin;
    }

    @Column(name="counter_end_")
    public int getCounterEnd() {
        return counterEnd;
    }
    public void setCounterEnd(int counterEnd) {
        this.counterEnd = counterEnd;
    }

    @Column(name="limit_")
    public int getLimit() {
        return limit;
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Column(name="limit_add_")
    public int getLimitAdditional() {
        return limitAdditional;
    }
    public void setLimitAdditional(int limitAdditional) {
        this.limitAdditional = limitAdditional;
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

    @Column(name="su_begin_")
    public Date getSuBegin() {
        return suBegin;
    }
    public void setSuBegin(Date suBegin) {
        this.suBegin = suBegin;
    }

    @Column(name="su_end_")
    public Date getSuEnd() {
        return suEnd;
    }
    public void setSuEnd(Date suEnd) {
        this.suEnd = suEnd;
    }

    @Column(name="mo_begin_")
    public Date getMoBegin() {
        return moBegin;
    }
    public void setMoBegin(Date moBegin) {
        this.moBegin = moBegin;
    }

    @Column(name="mo_end_")
    public Date getMoEnd() {
        return moEnd;
    }
    public void setMoEnd(Date moEnd) {
        this.moEnd = moEnd;
    }

    @Column(name="tu_begin_")
    public Date getTuBegin() {
        return tuBegin;
    }
    public void setTuBegin(Date tuBegin) {
        this.tuBegin = tuBegin;
    }

    @Column(name="tu_end_")
    public Date getTuEnd() {
        return tuEnd;
    }
    public void setTuEnd(Date tuEnd) {
        this.tuEnd = tuEnd;
    }

    @Column(name="we_begin_")
    public Date getWeBegin() {
        return weBegin;
    }
    public void setWeBegin(Date weBegin) {
        this.weBegin = weBegin;
    }

    @Column(name="we_end_")
    public Date getWeEnd() {
        return weEnd;
    }
    public void setWeEnd(Date weEnd) {
        this.weEnd = weEnd;
    }

    @Column(name="th_begin_")
    public Date getThBegin() {
        return thBegin;
    }
    public void setThBegin(Date thBegin) {
        this.thBegin = thBegin;
    }

    @Column(name="th_end_")
    public Date getThEnd() {
        return thEnd;
    }
    public void setThEnd(Date thEnd) {
        this.thEnd = thEnd;
    }

    @Column(name="fr_begin_")
    public Date getFrBegin() {
        return frBegin;
    }
    public void setFrBegin(Date frBegin) {
        this.frBegin = frBegin;
    }

    @Column(name="fr_end_")
    public Date getFrEnd() {
        return frEnd;
    }
    public void setFrEnd(Date frEnd) {
        this.frEnd = frEnd;
    }

    @Column(name="st_begin_")
    public Date getStBegin() {
        return stBegin;
    }
    public void setStBegin(Date stBegin) {
        this.stBegin = stBegin;
    }

    @Column(name="st_end_")
    public Date getStEnd() {
        return stEnd;
    }
    public void setStEnd(Date stEnd) {
        this.stEnd = stEnd;
    }

    @Column(name="raw_")
    public Boolean getIsRaw() {
        return isRaw;
    }
    public void setIsRaw(Boolean isRaw) {
        this.isRaw = isRaw;
    }
}