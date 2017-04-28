package kz.essc.qtrack.config;

import kz.essc.qtrack.core.resource.ResourceBean;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

@RequestScoped
public class ConfigBean {
    private static final String path = ResourceBean.getConfigPath();

    public static String get(String key) {
        return getProperty(key);
    }

    public static void put(ConfigWrapper w) {
        putProperty(w.getKey(), w.getValue());
    }

    private static JSONObject read() {
        try {
            /*File file = new File(path);
            Scanner in = new Scanner(file);

            StringBuilder sb = new StringBuilder();

            do
                sb.append(in.nextLine());
            while (in.hasNextLine());

            System.out.println("fuck you");
            System.out.println(sb.toString());

            in.close();
            */

            String s = ""; //String.valueOf(Files.readAllLines(Paths.get(path), Charset.forName("UTF-8")));

            String line;
            try (
                    InputStream fis = new FileInputStream(path);
                    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                    BufferedReader br = new BufferedReader(isr);
            ) {
                while ((line = br.readLine()) != null) {
                    // Deal with the line
                    s += line;
                }
            }

//            System.out.println(s);

            return new JSONObject(s);
        }
        catch(IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getProperty(String key) {
        try {
            JSONObject json = read();
            return (String) json.get(key);
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void putProperty(String key, String value) {
        try {
//            System.out.println(value);
            JSONObject json = read();
            if (json.has(key))
                json.remove(key);
            json.put(key, value);

            /*
            PrintWriter out = new PrintWriter(new File(path));
            out.write(json.toString());
            out.close();*/

            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path), "UTF-8"));

            out.write(json.toString());
            out.close();
        }
        catch(IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
