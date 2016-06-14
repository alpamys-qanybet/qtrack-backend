package kz.essc.qtrack.line;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class LineWrapper {
	private long id;
	private String name;
	private int limit;
	private int counter;
	private int length;
	private String prefix;
	private Boolean enabled;
	private String nameKz;
	private String nameEn;
	private String nameRu;
	private long lineHierarchyId;
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
	private Boolean isRaw;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}

	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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

	public long getLineHierarchyId() {
		return lineHierarchyId;
	}
	public void setLineHierarchyId(long lineHierarchyId) {
		this.lineHierarchyId = lineHierarchyId;
	}

	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getBegin() {
		return begin;
	}
	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getSuBegin() {
		return suBegin;
	}
	public void setSuBegin(Date suBegin) {
		this.suBegin = suBegin;
	}

	public Date getSuEnd() {
		return suEnd;
	}
	public void setSuEnd(Date suEnd) {
		this.suEnd = suEnd;
	}

	public Date getMoBegin() {
		return moBegin;
	}
	public void setMoBegin(Date moBegin) {
		this.moBegin = moBegin;
	}

	public Date getMoEnd() {
		return moEnd;
	}
	public void setMoEnd(Date moEnd) {
		this.moEnd = moEnd;
	}

	public Date getTuBegin() {
		return tuBegin;
	}
	public void setTuBegin(Date tuBegin) {
		this.tuBegin = tuBegin;
	}

	public Date getTuEnd() {
		return tuEnd;
	}
	public void setTuEnd(Date tuEnd) {
		this.tuEnd = tuEnd;
	}

	public Date getWeBegin() {
		return weBegin;
	}
	public void setWeBegin(Date weBegin) {
		this.weBegin = weBegin;
	}

	public Date getWeEnd() {
		return weEnd;
	}
	public void setWeEnd(Date weEnd) {
		this.weEnd = weEnd;
	}

	public Date getThBegin() {
		return thBegin;
	}
	public void setThBegin(Date thBegin) {
		this.thBegin = thBegin;
	}

	public Date getThEnd() {
		return thEnd;
	}
	public void setThEnd(Date thEnd) {
		this.thEnd = thEnd;
	}

	public Date getFrBegin() {
		return frBegin;
	}
	public void setFrBegin(Date frBegin) {
		this.frBegin = frBegin;
	}

	public Date getFrEnd() {
		return frEnd;
	}
	public void setFrEnd(Date frEnd) {
		this.frEnd = frEnd;
	}

	public Date getStBegin() {
		return stBegin;
	}
	public void setStBegin(Date stBegin) {
		this.stBegin = stBegin;
	}

	public Date getStEnd() {
		return stEnd;
	}
	public void setStEnd(Date stEnd) {
		this.stEnd = stEnd;
	}

	public Boolean getIsRaw() {
		return isRaw;
	}
	public void setIsRaw(Boolean isRaw) {
		this.isRaw = isRaw;
	}

	public static LineWrapper wrap(Line line){
		LineWrapper wrapper = new LineWrapper();

		try {
			wrapper.setId(line.getId());
		}
		catch(NullPointerException npe) {
//			npe.printStackTrace();
		}

		try {
			wrapper.setName(line.getName());
			wrapper.setPrefix(line.getPrefix());
//			wrapper.setLimit(line.getLimit());
			wrapper.setCounter(line.getCounter());
			wrapper.setLength(line.getLength());
			wrapper.setEnabled(line.getEnabled());
			wrapper.setBegin(line.getBegin());
			wrapper.setEnd(line.getEnd());
			wrapper.setSuBegin(line.getSuBegin());
			wrapper.setSuEnd(line.getSuEnd());
			wrapper.setMoBegin(line.getMoBegin());
			wrapper.setMoEnd(line.getMoEnd());
			wrapper.setTuBegin(line.getTuBegin());
			wrapper.setTuEnd(line.getTuEnd());
			wrapper.setWeBegin(line.getWeBegin());
			wrapper.setWeEnd(line.getWeEnd());
			wrapper.setThBegin(line.getThBegin());
			wrapper.setThEnd(line.getThEnd());
			wrapper.setFrBegin(line.getFrBegin());
			wrapper.setFrEnd(line.getFrEnd());
			wrapper.setStBegin(line.getStBegin());
			wrapper.setStEnd(line.getStEnd());
			wrapper.setIsRaw(line.getIsRaw());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return wrapper;
	}
	
	public static List<LineWrapper> wrap(List<Line> lines){
		List<LineWrapper> list = new ArrayList<>();
		for (Line line: lines)
			list.add(wrap(line));
		
		return list;
	}

	@Override
	public String toString() {
		String result = "{" +
				"id:" + id +
				", name:'" + name + '\'' +
				", limit:" + limit +
				", counter:" + counter +
				", length:" + length +
				", prefix:'" + prefix + '\'' +
				", enabled:" + enabled +
				", nameKz:'" + nameKz + '\'' +
				", nameEn:'" + nameEn + '\'' +
				", nameRu:'" + nameRu + '\'' +
				", isRaw:"+isRaw;

		// TODO add isRaw


		if (begin == null)
			result += ", begin: null";
		else
			result += ", begin:" + begin.getTime();

		if (end == null)
			result += ", end: null";
		else
			result += ", end:" + end.getTime();

		result += '}';
		return result;
	}
}
