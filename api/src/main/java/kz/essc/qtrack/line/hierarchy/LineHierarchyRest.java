package kz.essc.qtrack.line.hierarchy;


import kz.essc.qtrack.core.GenericWrapper;
import kz.essc.qtrack.core.SecurityBean;
import kz.essc.qtrack.line.LineBean;
import kz.essc.qtrack.line.LineWrapper;
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

@Path("/secure/linehierarchy")
@Produces({ MediaType.APPLICATION_JSON})
@RequestScoped
public class LineHierarchyRest {
    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse response;

    @Inject
    SecurityBean securityBean;

    @Inject
    LineHierarchyBean lineHierarchyBean;

    @Inject
    LineBean lineBean;

    // :TODO get / must be all, /all must be /root
    @GET
    @Path("/") // only roots
    public List<LineHierarchyWrapper> get() {
        List<LineHierarchyWrapper> list = LineHierarchyWrapper.wrap( lineHierarchyBean.get() );
        return lineHierarchyBean.translated(list);
    }

    @GET
    @Path("/all") // raw data
    public List<LineHierarchyWrapper> all() {
        List<LineHierarchyWrapper> list = LineHierarchyWrapper.wrap( lineHierarchyBean.all() );
        return lineHierarchyBean.translated(list);
    }

    @GET
    @Path("/{id}")
    public LineHierarchyWrapper get(@PathParam("id") Long id) {
        LineHierarchyWrapper wrap = LineHierarchyWrapper.wrap( lineHierarchyBean.get(id) );
        return lineHierarchyBean.translated(wrap);
    }

    @POST
    @Path("/")
    @Transactional
    public LineHierarchyWrapper add(LineHierarchyWrapper wrapper) throws IOException {
        boolean isAdmin = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN);
        boolean isManager = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.MANAGER);
        if (isAdmin || isManager) {
            return LineHierarchyWrapper.wrap( lineHierarchyBean.add(wrapper) );
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public LineHierarchyWrapper edit(@PathParam("id") Long id, LineHierarchyWrapper wrapper) throws IOException {
        boolean isAdmin = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN);
        boolean isManager = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.MANAGER);
        if (isAdmin || isManager) {
            return LineHierarchyWrapper.wrap( lineHierarchyBean.edit(id, wrapper) );
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public GenericWrapper delete(@PathParam("id") Long id) throws IOException {
        boolean isAdmin = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN);
        boolean isManager = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.MANAGER);
        if (isAdmin || isManager) {
            lineHierarchyBean.delete(id);
            return GenericWrapper.wrap(true);
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return GenericWrapper.wrap(false);
        }
    }

    @GET
    @Path("/{id}/children")
    public List<LineHierarchyWrapper> getChildren(@PathParam("id") Long id) {
        List<LineHierarchyWrapper> list = LineHierarchyWrapper.wrap( lineHierarchyBean.getChildren(id) );
        return lineHierarchyBean.translated(list);
    }

    @GET
    @Path("/{id}/lines")
    public List<LineWrapper> getLines(@PathParam("id") Long id) {
        List<LineWrapper> list = LineWrapper.wrap( lineHierarchyBean.getLines(id) );
        return lineBean.translated(list);
    }

    @POST
    @Path("/{id}/lines")
    @Transactional
    public GenericWrapper addLine(@PathParam("id") Long id, LineWrapper lineWrapper) throws IOException {
        boolean isAdmin = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN);
        boolean isManager = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.MANAGER);
        if (isAdmin || isManager) {
            return GenericWrapper.wrap(lineHierarchyBean.addLine(id, lineWrapper));
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return GenericWrapper.wrap(false);
        }
    }

    @DELETE
    @Path("/{id}/lines/{lineId}")
    @Transactional
    public GenericWrapper deleteLine(@PathParam("id") Long id, @PathParam("lineId") Long lineId) throws IOException {
        boolean isAdmin = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.ADMIN);
        boolean isManager = securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.MANAGER);
        if (isAdmin || isManager) {
            return GenericWrapper.wrap(lineHierarchyBean.removeLine(id, lineId));
        }
        else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return GenericWrapper.wrap(false);
        }
    }

}
