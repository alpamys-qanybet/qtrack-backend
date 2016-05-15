package kz.essc.qtrack.process;

import kz.essc.qtrack.client.Process;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class ProcessWrapper {
	private long id;
	private String clientCode;
	private Long clientId;
	private Long lineId;
	private Long operatorId;
	private Date begin;
	private Date end;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getLineId() {
		return lineId;
	}
	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Date getBegin() {
		return begin;
	}
	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}

	public static ProcessWrapper wrap(Process process){
		ProcessWrapper wrapper = new ProcessWrapper();
		try {
			wrapper.setId(process.getId());
			wrapper.setClientCode(process.getClientCode());
			wrapper.setClientId(process.getClientId());
			wrapper.setLineId(process.getLineId());
			wrapper.setOperatorId(process.getOperatorId());
			wrapper.setBegin(process.getBegin());
			wrapper.setEnd(process.getEnd());
		}
		catch (Exception e) {
//			e.printStackTrace();
		}
		return wrapper;
	}
	
	public static List<ProcessWrapper> wrap(List<Process> processes){
		List<ProcessWrapper> list = new ArrayList<>();
		for (Process process: processes)
			list.add(wrap(process));
		
		return list;
	}

	@Override
	public String toString() {
		String result = "{" +
				"id:" + id +
				", clientCode:'" + clientCode + '\'' +
				", clientId:" + clientId +
				", lineId:" + lineId +
				", operatorId:" + operatorId +
				", begin:" + begin.getTime();

		if (end == null)
			result += ", end: null";
		else
			result += ", end:" + end.getTime();

		result += '}';
		return result;
	}
}
