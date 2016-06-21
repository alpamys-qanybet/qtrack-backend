package kz.essc.qtrack.core.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResourceBean {
    static final private String dir = "/home/alpamys/Videos";
    static final private String host = "192.168.0.103:8080";
//    static final private String host = "alpamys-samsung:8080";

    public static String getDir() {
        return dir;
    }
    public static String getDir(String name) {
        return dir+"/"+name;
    }

    public static String getHost() {
        return host;
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
