package kz.essc.qtrack.config;

import kz.essc.qtrack.core.GenericWrapper;
import kz.essc.qtrack.core.SecurityBean;
import kz.essc.qtrack.sc.user.Role;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/secure/config")
@Produces({ MediaType.APPLICATION_JSON})
@RequestScoped
public class ConfigRest {
    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse response;

    @Inject
    SecurityBean securityBean;

    @Inject
    ConfigBean configBean;

    @POST
    @Path("/")
    public GenericWrapper put(ConfigWrapper w) throws IOException {
        boolean isAdmin = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN);
        boolean isManager = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.MANAGER);
        if (isAdmin || isManager) {
            configBean.put(w);
            return GenericWrapper.wrap(true);
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @GET
    @Path("/running")
    public GenericWrapper getRunningText() {
        return GenericWrapper.wrap(configBean.get("running"));
    }

    @GET
    @Path("/org/namekz")
    public GenericWrapper getOrganizationNameKz() {
        return GenericWrapper.wrap(configBean.get("orgnamekz"));
    }

    @GET
    @Path("/org/nameen")
    public GenericWrapper getOrganizationNameEn() {
        return GenericWrapper.wrap(configBean.get("orgnameen"));
    }

    @GET
    @Path("/org/nameru")
    public GenericWrapper getOrganizationNameRu() {
        return GenericWrapper.wrap(configBean.get("orgnameru"));
    }

    @GET
    @Path("/org/textkz")
    public GenericWrapper getOrganizationTextKz() {
        return GenericWrapper.wrap(configBean.get("orgtextkz"));
    }

    @GET
    @Path("/org/texten")
    public GenericWrapper getOrganizationTextEn() {
        return GenericWrapper.wrap(configBean.get("orgtexten"));
    }

    @GET
    @Path("/org/textru")
    public GenericWrapper getOrganizationTextRu() {
        return GenericWrapper.wrap(configBean.get("orgtextru"));
    }
}
