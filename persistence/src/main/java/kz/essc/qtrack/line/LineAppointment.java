package kz.essc.qtrack.line;

import javax.persistence.*;
import java.io.Serializable;

/*
var n = 6424;
for (var i=0; i<366; i++) { console.log((n+i) + ' ' + (i+1)); }
INSERT INTO li_line_appointment(id_, counter_, length_, day_, year_) VALUES (6424, 0, 0, 1, 2016);
*/

@Entity
@Table(name="li_appointment")
public class LineAppointment implements Serializable {
    private static final long serialVersionUID = -1545121714832642142L;

    private long id;
    private long timingId;
    private long lineId;
    private int length = 0; // set zero when clients end
    private int counter = 0; // 0-999

    @Id
    @Column(name="id_")
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name="timing_id_")
    public long getTimingId() {
        return timingId;
    }
    public void setTimingId(long timingId) {
        this.timingId = timingId;
    }

    @Column(name="line_id_")
    public long getLineId() {
        return lineId;
    }
    public void setLineId(long lineId) {
        this.lineId = lineId;
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

}