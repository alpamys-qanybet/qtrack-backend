package kz.essc.qtrack.line;

import kz.essc.qtrack.client.ClientWrapper;
import kz.essc.qtrack.core.GenericWrapper;
import kz.essc.qtrack.core.SecurityBean;
import kz.essc.qtrack.operator.OperatorWrapper;
import kz.essc.qtrack.sc.user.Role;
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

@Path("/secure/line")
@Produces({ MediaType.APPLICATION_JSON})
@RequestScoped
public class LineRest {
    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse response;

    @Inject
    SecurityBean securityBean;

    @Inject
    LineBean lineBean;

    @GET
    @Path("/")
    public List<LineWrapper> get() {
        List<LineWrapper> list = LineWrapper.wrap( lineBean.get() );
        return lineBean.translated(list);
    }

    @GET
    @Path("/{id}")
    public LineWrapper get(@PathParam("id") Long id) {
        LineWrapper wrap = LineWrapper.wrap( lineBean.get(id) );
        return lineBean.translated(wrap);
    }

    @GET
    @Path("/root")
    public List<LineWrapper> getRoot() {
        List<LineWrapper> list = LineWrapper.wrap( lineBean.root() );
        return lineBean.translated(list);
    }

    @POST
    @Path("/")
    @Transactional
    public LineWrapper add(LineWrapper LineWrapper) throws IOException {
        if (securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN)) {
            return LineWrapper.wrap( lineBean.add(LineWrapper) );
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public LineWrapper edit(@PathParam("id") Long id, LineWrapper wrapper) throws IOException {
        if (securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN)) {
            return LineWrapper.wrap( lineBean.edit(id, wrapper) );
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @GET
    @Path("/{id}/operators")
    public List<OperatorWrapper> getOperators(@PathParam("id") Long id) throws IOException {
        if (securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN)) {
            return OperatorWrapper.wrapInherited(lineBean.getOperators(id));
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @POST
    @Path("/{id}/operators")
    @Transactional
    public GenericWrapper addOperator(@PathParam("id") Long id, OperatorWrapper operatorWrapper) throws IOException {
        if (securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN)) {
            return GenericWrapper.wrap(lineBean.addOperator(id, operatorWrapper));
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return GenericWrapper.wrap(false);
        }
    }

    @DELETE
    @Path("/{id}/operators/{operatorId}")
    @Transactional
    public GenericWrapper deleteOperator(@PathParam("id") Long id, @PathParam("operatorId") Long operatorId) throws IOException {
        if (securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN)) {
            return GenericWrapper.wrap(lineBean.removeOperator(id, operatorId));
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return GenericWrapper.wrap(false);
        }
    }

    /* NOT USED */
    @GET
    @Path("/{id}/clients")
    public List<ClientWrapper> getClients(@PathParam("id") Long id) throws IOException {
        if (securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN)) {
            return ClientWrapper.wrap(lineBean.getClients(id));
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @POST
    @Path("/{id}/clients/available")
    public List<ClientWrapper> getClientsAvailableFiltered(@PathParam("id") Long id, FilterWrapper w) throws IOException {
        return ClientWrapper.wrap(lineBean.getAvailableClients(id, w));
    }

    @GET
    @Path("/available")
    public List<LineWrapper> getAvailable() {
        List<LineWrapper> list = LineWrapper.wrap(lineBean.getAvailable());
        return lineBean.translated(list);
    }

    @GET
    @Path("/available/prefix")
    public List<LinePrefixWrapper> getAvailablePrefix() {
        return LinePrefixWrapper.wrap(lineBean.getAvailableLinePrefixes());
    }
}
