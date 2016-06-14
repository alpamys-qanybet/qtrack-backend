package kz.essc.qtrack.kiosk;

import kz.essc.qtrack.client.ClientBean;
import kz.essc.qtrack.client.ClientRest;
import kz.essc.qtrack.client.ClientWrapper;
import kz.essc.qtrack.client.TicketWrapper;
import kz.essc.qtrack.config.ConfigRest;
import kz.essc.qtrack.core.GenericWrapper;
import kz.essc.qtrack.line.*;
import kz.essc.qtrack.line.hierarchy.LineHierarchyRest;
import kz.essc.qtrack.line.hierarchy.LineHierarchyWrapper;
import org.codehaus.jettison.json.JSONException;
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

@Path("/kiosk")
@Produces({ MediaType.APPLICATION_JSON})
@RequestScoped
public class KioskRest {

    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse response;

    @Inject
    LineRest lineRest;

    @Inject
    LineHierarchyRest lineHierarchyRest;

    @Inject
    ClientRest clientRest;

    @Inject
    ClientBean clientBean;

    @Inject
    KioskBean kioskBean;

    @Inject
    ConfigRest configRest;

    @GET
    @Path("/line")
    public List<LineWrapper> getLines() {
        return lineRest.get();
    }

    @GET
    @Path("/line/{id}")
    public LineWrapper getLine(@PathParam("id") Long id) {
        return lineRest.get(id);
    }

    @GET
    @Path("/line/root")
    public List<LineWrapper> getRootLines() {
        return lineRest.getRoot();
    }

    @POST
    @Path("/line/{id}/client")
    @Transactional
    public GenericWrapper createClient(@PathParam("id") Long lineId, ClientWrapper clientWrapper) throws IOException {
        return GenericWrapper.wrap(kioskBean.createClient(lineId, clientWrapper));
    }

    @PUT
    @Path("/line/{id}/initcounter")
    @Transactional
    public GenericWrapper initLineCounter(@PathParam("id") Long lineId) throws IOException {
        return GenericWrapper.wrap(kioskBean.initLineCounter(lineId));
    }

    @POST
    @Path("/line/{id}/clients/available")
    public List<ClientWrapper> getClientsAvailableFiltered(@PathParam("id") Long id, FilterWrapper w) throws IOException {
        return lineRest.getClientsAvailableFiltered(id, w);
    }

    @GET
    @Path("/client/{id}")
    public TicketWrapper getClient(@PathParam("id") Long id) {
        TicketWrapper wrap = TicketWrapper.wrap(clientBean.get(id));
        return kioskBean.translated(wrap);
    }

    @GET
    @Path("/linehierarchy")
    public List<LineHierarchyWrapper> getLineHierarchies() {
        return lineHierarchyRest.get();
    }

    @GET
    @Path("/linehierarchy/{id}")
    public LineHierarchyWrapper getLineHierarchy(@PathParam("id") Long id) {
        return lineHierarchyRest.get(id);
    }

    @GET
    @Path("/linehierarchy/{id}/children")
    public List<LineHierarchyWrapper> getLineHierarchyChildren(@PathParam("id") Long id) {
        return lineHierarchyRest.getChildren(id);
    }

    @GET
    @Path("/linehierarchy/{id}/lines")
    public List<LineWrapper> getLineHierarchyLines(@PathParam("id") Long id) {
        return lineHierarchyRest.getLines(id);
    }

    @GET
    @Path("/config/running")
    public GenericWrapper getConfigRunning() {
        return configRest.getRunningText();
    }

    @GET
    @Path("/config/org/namekz")
    public GenericWrapper getConfigOrgnameKz() {
        return configRest.getOrganizationNameKz();
    }

    @GET
    @Path("/config/org/nameen")
    public GenericWrapper getConfigOrgnameEn() {
        return configRest.getOrganizationNameEn();
    }

    @GET
    @Path("/config/org/nameru")
    public GenericWrapper getConfigOrgnameRu() {
        return configRest.getOrganizationNameRu();
    }
}
