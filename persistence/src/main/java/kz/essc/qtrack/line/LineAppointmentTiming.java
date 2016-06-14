package kz.essc.qtrack.line;

import javax.persistence.*;
import java.io.Serializable;

/*
var n = 6424;
for (var i=0; i<366; i++) { console.log((n+i) + ' ' + (i+1)); }
INSERT INTO li_line_appointment(id_, counter_, length_, day_, year_) VALUES (6424, 0, 0, 1, 2016);
*/

@Entity
@Table(name="li_appointment_timing")
public class LineAppointmentTiming implements Serializable {
    private static final long serialVersionUID = -7058306045203370057L;

    private long id;
    private int day; // 1-365(6)
    private int year; // 2016+

    @Id
    @Column(name="id_")
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name="day_")
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }

    @Column(name="year_")
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
}