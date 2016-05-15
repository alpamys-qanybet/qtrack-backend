package kz.essc.qtrack.sc;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="sc_lang")
public class Language implements Serializable {
    private static final long serialVersionUID = 8541018276024179372L;

    private long id;
    private String name;
    private String code;
    private List<Message> messages;

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

    @Column(name="code_", nullable=false)
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    @OneToMany(mappedBy = "lang")
    public List<Message> getMessages() {
        return messages;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
