package kz.essc.qtrack.sc;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="sc_message")
public class Message implements Serializable {
	private static final long serialVersionUID = 307727022444619397L;

	private long id;
	private Language lang;
	private String name;
	private String value;

	@Id
	@Column(name="id_")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="name_", nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="value_", nullable=false)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne
	@JoinColumn(name = "lang_")
	public Language getLang() {
		return lang;
	}
	public void setLang(Language lang) {
		this.lang = lang;
	}
}
