package kz.essc.qtrack.config;

import javax.ejb.Stateless;

@Stateless
public class ConfigWrapper {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
