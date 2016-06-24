package kz.essc.qtrack.sc.user;

import kz.essc.qtrack.client.*;
import kz.essc.qtrack.client.Process;
import kz.essc.qtrack.line.Line;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="sc_user")
public class User implements Serializable {
	private static final long serialVersionUID = 1696932357727252111L;

	private long id;
	private String login;
	private String name;
	private String shortname;
	private String password;
	private Set<Role> roles = new HashSet<>();

	private Line line;
	public enum Status {
		AVAILABLE, WAITING, PROCESSING, BUSY
	}
	private String status;
	private Boolean online;
	private Client client;

	private String position;
	private String cabinet;
	private int display = 0;
	private Boolean enabled;
	private int floor = 1;

	@Id
	@Column(name="id_")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@Column(name="login_", nullable=false, unique=true)
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}

	@Column(name="name_", nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="shortname_")
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	@Column(name="password_", nullable=false)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="sc_user_role",
			joinColumns=@JoinColumn(name="user_"),
			inverseJoinColumns=@JoinColumn(name="role_"))
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "line_")
	public Line getLine() {
		return line;
	}
	public void setLine(Line line) {
		this.line = line;
	}

	@Column(name="status_")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name="online_")
	public Boolean getOnline() {
		return online;
	}
	public void setOnline(Boolean online) {
		this.online = online;
	}

	@OneToOne(mappedBy = "operator")
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Column(name="position_")
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name="cabinet_")
	public String getCabinet() {
		return cabinet;
	}
	public void setCabinet(String cabinet) {
		this.cabinet = cabinet;
	}

	@Column(name="display_")
	public int getDisplay() {
		return display;
	}
	public void setDisplay(int display) {
		this.display = display;
	}

	@Column(name="floor_")
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}

	@Column(name="enabled_")
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}