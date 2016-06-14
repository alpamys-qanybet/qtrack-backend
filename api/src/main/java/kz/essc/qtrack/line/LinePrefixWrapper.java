package kz.essc.qtrack.line;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class LinePrefixWrapper {
    private long id;
    private long lineId = 0;
    private String name;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getLineId() {
        return lineId;
    }
    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public static LinePrefixWrapper wrap(LinePrefix l){
        LinePrefixWrapper w = new LinePrefixWrapper();

        try {
            w.setName(l.getName());
        }
        catch (Exception e) {
//            e.printStackTrace();
        }
        return w;
    }

    public static List<LinePrefixWrapper> wrap(List<LinePrefix> prefixes){
        List<LinePrefixWrapper> list = new ArrayList<>();
        for (LinePrefix l: prefixes)
            list.add(wrap(l));

        return list;
    }
}
