package kz.essc.qtrack.client;

import kz.essc.qtrack.core.GenericWrapper;
import kz.essc.qtrack.core.SecurityBean;
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

@Path("/secure/client")
@Produces({ MediaType.APPLICATION_JSON})
@RequestScoped
public class ClientRest {
    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse response;

    @Inject
    SecurityBean securityBean;

    @Inject
    ClientBean clientBean;

    @GET
    @Path("/")
    public List<ClientWrapper> get() {
        return ClientWrapper.wrap(clientBean.get());
    }

    @GET
    @Path("/{id}")
    public ClientWrapper get(@PathParam("id") Long id) {
        return ClientWrapper.wrap(clientBean.get(id));
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public ClientWrapper edit(@PathParam("id") Long id, ClientWrapper clientWrapper) throws IOException {
        if (securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.OPERATOR)) {
            return ClientWrapper.wrap(clientBean.edit(id, clientWrapper));
        } else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public GenericWrapper delete(@PathParam("id") Long id) throws IOException {
        if (securityBean.hasRole(request.getUserPrincipal().getName(), Role.Name.OPERATOR)) {
            return GenericWrapper.wrap(clientBean.delete(id));
        } else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return GenericWrapper.wrap(false);
        }
    }

    @PUT
    @Path("/{id}/call")
    @Transactional
    public ClientWrapper call(@PathParam("id") Long id, ClientWrapper wrapper) throws IOException {
        String login = request.getUserPrincipal().getName();
        if (securityBean.hasRole(login, Role.Name.OPERATOR)) {
            return ClientWrapper.wrap(clientBean.call(id, wrapper, login));
        } else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @GET
    @Path("/{id}/skip")
    @Transactional
    public GenericWrapper skip(@PathParam("id") Long id, ClientWrapper wrapper) throws IOException {
        String login = request.getUserPrincipal().getName();
        if (securityBean.hasRole(login, Role.Name.OPERATOR)) {
            clientBean.skip(id, login);
            return GenericWrapper.wrap(true);
        } else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @PUT
    @Path("/{id}/start")
    @Transactional
    public GenericWrapper start(@PathParam("id") Long id, ClientWrapper wrapper) throws IOException {
        String login = request.getUserPrincipal().getName();
        if (securityBean.hasRole(login, Role.Name.OPERATOR)) {
            try {
                return GenericWrapper.wrap(clientBean.startProcess(id, wrapper).getId());
            }
            catch (Exception e) {
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
                return null;
            }
        } else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @GET
    @Path("/{id}/stop/{processId}")
    @Transactional
    public GenericWrapper stop(@PathParam("id") Long id, @PathParam("processId") Long processId) throws IOException {
        String login = request.getUserPrincipal().getName();
        if (securityBean.hasRole(login, Role.Name.OPERATOR)) {
            clientBean.stopProcess(processId);
            return GenericWrapper.wrap(true);
        } else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }

    @PUT
    @Path("/{id}/send")
    @Transactional
    public ClientWrapper send(@PathParam("id") Long id, ClientWrapper wrapper) throws IOException {
        String login = request.getUserPrincipal().getName();
        if (securityBean.hasRole(login, Role.Name.OPERATOR)) {
            Client client = clientBean.send(id, wrapper, login);
            if (client == null)
                return null;

            return ClientWrapper.wrap(client);
        } else {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        }
    }
}
