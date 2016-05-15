package kz.essc.qtrack.user;


import kz.essc.qtrack.sc.user.User;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserWrapper {

	private long id;
	private String login;
	private String name;
	private String password;
	private String old;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getOld() {
		return old;
	}
	public void setOld(String old) {
		this.old = old;
	}
	
	public static UserWrapper wrap(User user){
		UserWrapper wrapper = new UserWrapper();

		try {
			wrapper.setId(user.getId());
		}
		catch(NullPointerException npe) {
//			npe.printStackTrace();
		}

		try {
			wrapper.setLogin(user.getLogin());
			wrapper.setName(user.getName());
//			wrapper.setPassword(user.getPassword());
		}
		catch (Exception e) {
//			e.printStackTrace();
		}
		return wrapper;
	}
	
	public static List<UserWrapper> wrap(List<User> users){
		List<UserWrapper> list = new ArrayList<UserWrapper>();
		for (User user: users)
			list.add(wrap(user));
		
		return list;
	}
}
