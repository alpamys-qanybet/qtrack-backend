//package kz.essc.qtrack.line;
//
//import kz.essc.qtrack.sc.user.User;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.List;
//
//@Entity
//@Table(name="li_infotable")
//public class Infotable implements Serializable {
//    private static final long serialVersionUID = -6729523911997672971L;
//
//    private long id;
//    private int display = 0;
//    private List<User> operators;
//
//    @Id
//    @Column(name="id_")
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    public long getId() {
//        return id;
//    }
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    @Column(name="display_")
//    public int getDisplay() {
//        return display;
//    }
//    public void setDisplay(int display) {
//        this.display = display;
//    }
//
//    @OneToMany(mappedBy = "infotable")
//    public List<User> getOperators() {
//        return operators;
//    }
//    public void setOperators(List<User> operators) {
//        this.operators = operators;
//    }
//}
