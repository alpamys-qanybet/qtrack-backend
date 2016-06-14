package kz.essc.qtrack.client;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class ClientWrapper {
	private long id;
	private Date date;
	private String code;
	private String status;
	private int order;
	private Long lineId;
	private Long operatorId;
	private String lang;

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

	public static ClientWrapper wrap(Client client){
		ClientWrapper wrapper = new ClientWrapper();

		try {
			wrapper.setOperatorId(client.getOperator().getId());
		}
		catch(NullPointerException npe) {
//			npe.printStackTrace();
			wrapper.setOperatorId(null);
		}

		try {
			wrapper.setLineId(client.getLine().getId());
			wrapper.setId(client.getId());
			wrapper.setDate(client.getDate());
			wrapper.setCode(client.getCode());
			wrapper.setStatus(client.getStatus());
			wrapper.setOrder(client.getOrder());
			wrapper.setLang(client.getLang());
		}
		catch (Exception e) {
//			e.printStackTrace();
		}
		return wrapper;
	}
	
	public static List<ClientWrapper> wrap(List<Client> clients){
		List<ClientWrapper> list = new ArrayList<>();
		for (Client client: clients)
			list.add(wrap(client));
		
		return list;
	}

	@Override
	public String toString() {
		String result = "{" +
				"id:" + id;

		if (date== null)
			result += ", date: null";
		else
			result += ", date:" + date.getTime();

		result += ", code:'" + code + '\'' +
				", status:'" + status + '\'' +
				", order:" + order +
				", lineId:" + lineId +
				", operatorId:" + operatorId +
				", lang:'" + lang + '\'' +
				'}';

		return result;
	}
}
