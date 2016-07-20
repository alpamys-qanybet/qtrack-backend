package kz.essc.qtrack.kiosk;

import kz.essc.qtrack.client.ClientBean;
import kz.essc.qtrack.client.ClientRest;
import kz.essc.qtrack.client.ClientWrapper;
import kz.essc.qtrack.client.TicketWrapper;
import kz.essc.qtrack.config.ConfigRest;
import kz.essc.qtrack.core.GenericWrapper;
//import kz.essc.qtrack.core.resource.ResourceBean;
import kz.essc.qtrack.core.resource.ResourceBean;
import kz.essc.qtrack.kiosk.display.DisplayBean;
import kz.essc.qtrack.kiosk.display.DisplayWrapper;
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

    @Inject
    DisplayBean displayBean;

//    @Inject
//    ResourceBean resourceBean;

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

    @POST
    @Path("/line/{id}/clients/reserved")
    public List<ClientWrapper> getClientsReservedFiltered(@PathParam("id") Long id, FilterWrapper w) throws IOException {
        return lineRest.getClientsReservedFiltered(id, w);
    }

    @POST
    @Path("/line/{id}/clients/filtered")
    public List<ClientWrapper> getClientsFiltered(@PathParam("id") Long id, FilterWrapper w) throws IOException {
        return lineRest.getClientsFiltered(id, w);
    }

    @GET
    @Path("/genlp")
    @Transactional
    public GenericWrapper getLP() {
        return GenericWrapper.wrap(kioskBean.genLP());
    }

    /*@GET
    @Path("/line/prefix")
    public GenericWrapper getLP() {
        return GenericWrapper.wrap(kioskBean.genLP());
    }*/

    @GET
    @Path("/client/{id}")
    public TicketWrapper getClient(@PathParam("id") Long id) {
        TicketWrapper wrap = TicketWrapper.wrap(clientBean.get(id));
        return kioskBean.translated(wrap);
    }

    @GET
    @Path("/client/{id}/exists")
    public GenericWrapper isClientExists(@PathParam("id") Long id) {
        return GenericWrapper.wrap(clientBean.get(id) != null);
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

    @GET
    @Path("/config/org/textkz")
    public GenericWrapper getConfigOrgtextKz() {
        return configRest.getOrganizationTextKz();
    }

    @GET
    @Path("/config/org/texten")
    public GenericWrapper getConfigOrgtextEn() {
        return configRest.getOrganizationTextEn();
    }

    @GET
    @Path("/config/org/textru")
    public GenericWrapper getConfigOrgtextRu() {
        return configRest.getOrganizationTextRu();
    }

    @GET
    @Path("/display/listfiles/{name}")
    public List<GenericWrapper> getKioskListFiles(@PathParam("name") String name) {
        return GenericWrapper.wrap(ResourceBean.listFiles(name));
    }

    @GET
    @Path("/display/{display}")
    public DisplayWrapper getDisplayInfo(@PathParam("display") int display) {
        return displayBean.get(display);
    }

    @GET
    @Path("/infotable")
    public GenericWrapper getInfotableInfo() {
        try {
            return GenericWrapper.wrap(kioskBean.messageOnLaunchInfotable());
        }
        catch(JSONException e) {
            return GenericWrapper.wrap("{}");
        }
    }

    @GET
    @Path("/operator/{operatorId}")
    public GenericWrapper getOperatorInfo(@PathParam("operatorId") Long operatorId) {
        try {
            return GenericWrapper.wrap(kioskBean.noWsOperatorRest(operatorId));
        }
        catch(JSONException e) {
            return GenericWrapper.wrap("{}");
        }
    }

    @GET
    @Path("/monitoring")
    public GenericWrapper getMonitoring() {
        try {
            return GenericWrapper.wrap(kioskBean.messageOnLaunchOperator());
        }
        catch(JSONException e) {
            return GenericWrapper.wrap("{}");
        }
    }
}
