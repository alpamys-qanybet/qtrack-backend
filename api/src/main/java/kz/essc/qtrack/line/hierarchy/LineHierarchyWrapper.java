package kz.essc.qtrack.line.hierarchy;

import kz.essc.qtrack.line.LineHierarchy;

import java.util.ArrayList;
import java.util.List;

public class LineHierarchyWrapper {

    private long id;
    private String nameKz;
    private String nameEn;
    private String nameRu;
    private long parentId;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getNameKz() {
        return nameKz;
    }
    public void setNameKz(String nameKz) {
        this.nameKz = nameKz;
    }

    public String getNameEn() {
        return nameEn;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameRu() {
        return nameRu;
    }
    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public long getParentId() {
        return parentId;
    }
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public static LineHierarchyWrapper wrap(LineHierarchy hierarchy){
        LineHierarchyWrapper wrapper = new LineHierarchyWrapper();

        try {
            wrapper.setId(hierarchy.getId());
            wrapper.setParentId(hierarchy.getParent().getId());
        }
        catch(NullPointerException npe) {
//			npe.printStackTrace();
        }

        return wrapper;
    }

    public static List<LineHierarchyWrapper> wrap(List<LineHierarchy> hierarchies){
        List<LineHierarchyWrapper> list = new ArrayList<>();
        for (LineHierarchy hierarchy: hierarchies)
            list.add(wrap(hierarchy));

        return list;
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", nameKz:'" + nameKz + '\'' +
                ", nameEn:'" + nameEn + '\'' +
                ", nameRu:'" + nameRu + '\'' +
                ", parentId:" + parentId +
                '}';
    }
}
