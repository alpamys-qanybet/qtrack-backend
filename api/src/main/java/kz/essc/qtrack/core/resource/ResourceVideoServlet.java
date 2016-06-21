package kz.essc.qtrack.core.resource;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Properties;

@WebServlet(urlPatterns={"/download/resource/video/*"})
public class ResourceVideoServlet extends HttpServlet{

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            String pi = request.getPathInfo();

            ServletOutputStream sos = response.getOutputStream();
            response.setContentType("video/mp4");
            sos.write(getByte(ResourceBean.getDir("video") + pi));

            Thread.sleep(10000);                 //1000 milliseconds is one second.
            sos.flush();
            Thread.sleep(8000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public byte[] getByte(String filePath) {
        try {
            File file = new File(filePath);
            final int BUFFER = 20480*20;
            byte data[] = new byte[BUFFER];

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            BufferedInputStream origin = new BufferedInputStream(new FileInputStream(file), BUFFER);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                dos.write(data, 0, count);
            }
            count = 0;
            origin.close();
            dos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}