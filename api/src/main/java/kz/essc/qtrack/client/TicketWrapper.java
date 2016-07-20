package kz.essc.qtrack.client;

import kz.essc.qtrack.line.Line;
import kz.essc.qtrack.sc.user.User;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class TicketWrapper {
	private long id;
	private Date date;
	private String code;
	private String status;
	private int order;
	private Long lineId;
	private Long operatorId;
	private String lang;
	private String lineNameKz;
	private String lineNameEn;
	private String lineNameRu;
	private List<String> cabinets;
	private List<String> operators;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
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

	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLineNameKz() {
		return lineNameKz;
	}
	public void setLineNameKz(String lineNameKz) {
		this.lineNameKz = lineNameKz;
	}

	public String getLineNameEn() {
		return lineNameEn;
	}
	public void setLineNameEn(String lineNameEn) {
		this.lineNameEn = lineNameEn;
	}

	public String getLineNameRu() {
		return lineNameRu;
	}
	public void setLineNameRu(String lineNameRu) {
		this.lineNameRu = lineNameRu;
	}

	public List<String> getCabinets() {
		return cabinets;
	}
	public void setCabinets(List<String> cabinets) {
		this.cabinets = cabinets;
	}

	public List<String> getOperators() {
		return operators;
	}
	public void setOperators(List<String> operators) {
		this.operators = operators;
	}

	public static TicketWrapper wrap(Client client){
		TicketWrapper wrapper = new TicketWrapper();

		try {
			wrapper.setOperatorId(client.getOperator().getId());
		}
		catch(NullPointerException npe) {
//			npe.printStackTrace();
			wrapper.setOperatorId(null);
		}

		try {
			Line line = client.getLine();

			wrapper.setId(client.getId());
			wrapper.setDate(client.getDate());
			wrapper.setCode(client.getCode().toUpperCase());
			wrapper.setStatus(client.getStatus());
			wrapper.setOrder(client.getOrder());
			wrapper.setLineId(line.getId());
			wrapper.setLang(client.getLang());

			List<String> operators = new ArrayList<>();
			List<String> cabinets = new ArrayList<>();
			for (User operator: line.getOperators()) {
				operators.add(operator.getShortname());
				if (operator.getCabinet() != null)
					cabinets.add(operator.getCabinet());
			}

			wrapper.setOperators(operators);
			wrapper.setCabinets(cabinets);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return wrapper;
	}
	
	public static List<TicketWrapper> wrap(List<Client> clients){
		List<TicketWrapper> list = new ArrayList<>();
		for (Client client: clients)
			list.add(wrap(client));
		
		return list;
	}
}
