package kz.essc.qtrack.line;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="li_hierarchy")
public class LineHierarchy implements Serializable {
    private static final long serialVersionUID = -4290667626696244407L;

    private long id;
    private LineHierarchy parent;
    private List<LineHierarchy> children = new ArrayList<>();
    private List<Line> lines;
    private Boolean enabled = false;

    @Id
    @Column(name="id_")
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "parent_")
    public LineHierarchy getParent() {
        return parent;
    }
    public void setParent(LineHierarchy parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent")
    public List<LineHierarchy> getChildren() {
        return children;
    }
    public void setChildren(List<LineHierarchy> children) {
        this.children = children;
    }

    @OneToMany(mappedBy = "lineHierarchy")
    public List<Line> getLines() {
        return lines;
    }
    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    @Column(name="enabled_")
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
