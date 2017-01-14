package kz.essc.qtrack.core.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResourceBean {
    static final private String dir = "/home/alpamys/Videos";
//    static final private String dir = "D:/server/files/resource";
    public static String getDir() {
        return dir;
    }
    public static String getDir(String name) {
        return dir+"/"+name;
    }

    static final private String soundPath = "/home/alpamys/Downloads/numbers";
//    static final private String soundPath = "D:/server/numbers";
    public static String getSoundPath() {
        return soundPath;
    }

/*
    static final private String host = "192.168.1.110:8080";
    static final private String host = "192.168.1.200:8080";
    static final private String host = "alpamys-samsung:8080";
    public static String getHost() {
        return host;
    }
*/

    static final String configPath = "/home/alpamys/dev/qtrack.config.json";
//    static final String configPath = "D:/server/files/qtrack.config.json";
    public static String getConfigPath() {
        return configPath;
    }

    public static List<String> listFiles(String name) {
        File folder = new File(getDir(name));
        List<String> list = new ArrayList<>();
        for (File fileEntry : folder.listFiles())
            if (fileEntry.isFile())
                list.add(fileEntry.getName());

        return list;
    }
 }
