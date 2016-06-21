package kz.essc.qtrack.core.resource;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns={"/download/resource/image/*"})
public class ResourceImageServlet extends HttpServlet{
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            String pi = request.getPathInfo();
            ServletOutputStream sos = response.getOutputStream();
            response.setContentType("image/png");
            sos.write(getByte(ResourceBean.getDir("img")+pi));
            sos.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public byte[] getByte(String filePath) {
        try {
            java.io.File file = new java.io.File(filePath);
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