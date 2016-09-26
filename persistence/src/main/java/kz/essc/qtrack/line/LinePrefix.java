//package kz.essc.qtrack.line;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name="li_prefix")
//public class LinePrefix implements Serializable {
//    private static final long serialVersionUID = -2348139957333290214L;
//
//    private long id;
//    private long lineId = 0;
//    private String name;
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
//    @Column(name="line_id_")
//    public long getLineId() {
//        return lineId;
//    }
//    public void setLineId(long lineId) {
//        this.lineId = lineId;
//    }
//
//    @Column(name="name_")
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//}