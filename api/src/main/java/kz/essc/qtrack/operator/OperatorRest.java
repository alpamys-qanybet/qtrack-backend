package kz.essc.qtrack.operator;

import kz.essc.qtrack.core.SecurityBean;
import kz.essc.qtrack.sc.user.Role;
import kz.essc.qtrack.user.UserBean;
import kz.essc.qtrack.user.UserWrapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/secure/operators")
@Produces({ MediaType.APPLICATION_JSON})
@RequestScoped
public class OperatorRest {

    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse response;

    @Inject
    SecurityBean securityBean;

    @Inject
    OperatorBean operatorBean;

    @Inject
    UserBean userBean;

    @GET
    @Path("/")
    public List<OperatorWrapper> get() {
        return OperatorWrapper.wrapInherited(operatorBean.get());
    }

    @GET
    @Path("/{id}")
    public OperatorWrapper get(@PathParam("id") Long id) {
        return OperatorWrapper.wrapInherited(userBean.get(id));
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public OperatorWrapper edit(@PathParam("id") Long id, OperatorWrapper userWrapper) throws IOException {
        boolean isAdmin = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN);
        boolean isManager = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.MANAGER);
        if (isAdmin || isManager) {
            return OperatorWrapper.wrapInherited(operatorBean.edit(id, userWrapper) );
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @GET
    @Path("/available")
    public List<OperatorWrapper> getAvailable() {
        return OperatorWrapper.wrapInherited(operatorBean.getAvailable());
    }

}