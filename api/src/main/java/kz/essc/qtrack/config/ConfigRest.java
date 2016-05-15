//package kz.essc.qtrack.config;
//
//import kz.essc.qtrack.core.SecurityBean;
//
//import javax.enterprise.context.RequestScoped;
//import javax.inject.Inject;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//
//@Path("/secure/config")
//@Produces({ MediaType.APPLICATION_JSON})
//@RequestScoped
//public class ConfigRest {
//    @Context
//    HttpServletRequest request;
//
//    @Context
//    HttpServletResponse response;
//
//    @Inject
//    SecurityBean securityBean;
//
//    @Inject
//    ConfigBean configBean;
//
//    @GET
//    @Path("/")
//    public ConfigWrapper get() {
//        ConfigWrapper wrap = ConfigWrapper.wrap();
//        return configBean.translated(wrap);
//    }
//}
