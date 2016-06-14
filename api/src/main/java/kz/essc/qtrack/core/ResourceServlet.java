package kz.essc.qtrack.core;

import java.io.*;
import java.util.Properties;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;

//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns={"/download/resource"})
public class ResourceServlet extends HttpServlet{

//    @javax.annotation.Resource(lookup = "java:jboss/kudosEntityManagerFactory")
//    private EntityManagerFactory emf;

//    private static Logger logger = Logger.getLogger(ResourceServlet.class.getName());

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
//        EntityManager em =  emf.createEntityManager();
        try {
//            Long resourceId;
//            try{
//                resourceId = Long.parseLong(request.getParameter("resourceId"));
//            }catch (NumberFormatException e){
//                logger.log(Level.WARNING, "ResourceServlet : resourceId is not Numeric");
//                return;
//            }
//            Resource res = em.find(Resource.class, resourceId);
//            if(res!=null){
//                File file = new File();
//                String ext = "";
//                if (res.getContentType().equals("application/scorm")) {
//                    file.setMimeType("application/zip");
//                    file.setData(startZip(getFileSystem() + res.getId()));
//                    ext = "zip";
//                    response.setHeader("Content-Disposition", "attachment; filename=\""+res.getId()+".zip\"");
//                } else {
//                    file.setMimeType(res.getContentType());
//                    file.setData(startOther(getFileSystem()+"/" + res.getFileName()));
//                    response.setHeader("Content-Disposition", "attachment; filename=\""+res.getFileName()+"\"");
//                }
//                ServletOutputStream sos = response.getOutputStream();
//                response.setContentType(file.getMimeType());
//
//                sos.write(file.getData());
//                sos.flush();
//            }else{
////                logger.log(Level.WARNING, "Resource not found: resourceId="+resourceId);
//            }

            ServletOutputStream sos = response.getOutputStream();
            response.setContentType("video/mp4");
//            File file = new File("/home/alpamys/Videos/a.mp4");
            sos.write(getByte("/home/alpamys/Videos/a.mp4"));
            sos.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
//            if (em!=null)
//                em.close();
        }
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public String getFileSystem(){
        Properties property = System.getProperties();
        String filesDir = (String) property.get("scorm.base.dir");
        if (filesDir == null) {
            filesDir = System.getProperty("java.io.tmpdir")+"/";
        } else {
            java.io.File fd = new java.io.File(filesDir);
            if (!fd.isDirectory()) {
                filesDir = System.getProperty("java.io.tmpdir")+"/";
            }
        }
        return filesDir;
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