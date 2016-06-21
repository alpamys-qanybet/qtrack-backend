package kz.essc.qtrack.core.resource;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(urlPatterns={"/download/resource/sound"})
public class ResourceSoundServlet extends HttpServlet{

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            ServletOutputStream sos = response.getOutputStream();
            response.setContentType("audio/mp3");
            sos.write(getByte(ResourceBean.getDir("sound")+ "/notification.mp3"));
            sos.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
        }
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public byte[] getByte(String filePath) {
        try {
            File file = new File(filePath);
            final int BUFFER = 20480;
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