package kz.essc.qtrack.sc.user;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sc_role")
public class Role implements Serializable {

	private static final long serialVersionUID = -5360799851217675397L;

	private String name;

	public enum Name {
		ADMIN, HEAD, MANAGER, OPERATOR
	}

	@Id
	@Column(name="name_", nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
