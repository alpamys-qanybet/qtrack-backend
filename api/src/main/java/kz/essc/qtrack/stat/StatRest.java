package kz.essc.qtrack.stat;


import kz.essc.qtrack.operator.OperatorWrapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/secure/stat")
@Produces({ MediaType.APPLICATION_JSON})
@RequestScoped
public class StatRest {

    @Inject
    private StatBean statBean;

    @POST
    @Path("/client/count/line")
    public List<ProcessWrapper> countByLine(FilterWrapper w) {
        return statBean.countByLine(w);
    }

    @POST
    @Path("/client/count/operator")
    public List<ProcessWrapper> countByOperator(FilterWrapper w) {
        return statBean.countByOperator(w);
    }

    @POST
    @Path("/client/count/time")
    public List<ProcessWrapper> countByTime(FilterWrapper w) {
        return statBean.countByTime(w);
    }

    @POST
    @Path("/client/waiting/line")
    public List<ProcessWrapper> waitingByLine(FilterWrapper w) {
        return statBean.avgWaitingByLine(w);
    }

    @POST
    @Path("/client/waiting/operator")
    public List<ProcessWrapper> waitingByOperator(FilterWrapper w) {
        return statBean.avgWaitingByOperator(w);
    }

    @POST
    @Path("/client/waiting/time")
    public List<ProcessWrapper> waitingByTime(FilterWrapper w) {
        return statBean.avgWaitingByTime(w);
    }

    @POST
    @Path("/client/handling/line")
    public List<ProcessWrapper> handlingByLine(FilterWrapper w) {
        return statBean.avgHandlingByLine(w);
    }

    @POST
    @Path("/client/handling/operator")
    public List<ProcessWrapper> handlingByOperator(FilterWrapper w) {
        return statBean.avgHandlingByOperator(w);
    }

    @POST
    @Path("/client/handling/time")
    public List<ProcessWrapper> handlingByTime(FilterWrapper w) {
        return statBean.avgHandlingByTime(w);
    }

    @POST
    @Path("/line/work/operator")
    /** filtered by time */
    public List<OperatorWrapper> getWorkedLineOperator(FilterWrapper w) {
        return  OperatorWrapper.wrapInherited(statBean.getWorkedLineOperator(w));
    }

}