package kz.essc.qtrack.operator;

import kz.essc.qtrack.sc.user.User;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class OperatorWrapper {
//	id login name password old
	private long id;
	private String login;
	private String name;
	private String shortname;
	private String position;
	private String cabinet;
	private int floor;
	private String status;
	private Boolean online;
	private Boolean enabled;
	private int display;
	private int infotable;
	private Long lineId;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

	public String getCabinet() {
		return cabinet;
	}
	public void setCabinet(String cabinet) {
		this.cabinet = cabinet;
	}

	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getOnline() {
		return online;
	}
	public void setOnline(Boolean online) {
		this.online = online;
	}

	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public int getDisplay() {
		return display;
	}
	public void setDisplay(int display) {
		this.display = display;
	}

	public int getInfotable() {
		return infotable;
	}
	public void setInfotable(int infotable) {
		this.infotable = infotable;
	}

	public Long getLineId() {
		return lineId;
	}
	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	//	@Override
	public static OperatorWrapper wrapInherited(User user){
		OperatorWrapper wrapper = new OperatorWrapper();

		try {
			wrapper.setId(user.getId());
			wrapper.setPosition(user.getPosition());
			wrapper.setCabinet(user.getCabinet());
			wrapper.setFloor(user.getFloor());
			wrapper.setEnabled(user.getEnabled());
			wrapper.setOnline(user.getOnline());
			wrapper.setStatus(user.getStatus());
			wrapper.setDisplay(user.getDisplay());
			wrapper.setInfotable(user.getInfotable());
			wrapper.setShortname(user.getShortname());
			wrapper.setLineId(user.getLine().getId());
		}
		catch(NullPointerException npe) {
//			npe.printStackTrace();
		}

		try {
			wrapper.setLogin(user.getLogin());
			wrapper.setName(user.getName());
		}
		catch (Exception e) {
//			e.printStackTrace();
		}
		return wrapper;
	}

	public static List<OperatorWrapper> wrapInherited(List<User> users){
		List<OperatorWrapper> list = new ArrayList<>();
		for (User user: users)
			list.add(wrapInherited(user));

		return list;
	}

	@Override
	public String toString() {
		return "{" +
				"id:" + id +
				", login:'" + login + '\'' +
				", name:'" + name + '\'' +
				", shortname:'" + shortname + '\'' +
				", position:'" + position + '\'' +
				", cabinet:'" + cabinet + '\'' +
				", floor:" + floor +
				", status:'" + status + '\'' +
				", online:" + online +
				", enabled:" + enabled +
				", display:" + display +
				", infotable:" + infotable +
				", lineId:" + lineId +
				'}';
	}
}
