package kz.essc.qtrack.user;

import kz.essc.qtrack.core.PasswordManager;
import kz.essc.qtrack.core.SecurityBean;
import kz.essc.qtrack.sc.user.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class UserBean {
	@PersistenceContext
	EntityManager em;

	@Inject
	SecurityBean securityBean;
	
	@Inject
	PasswordManager passwordManager;

	public List<User> get() {
		
		try {
			return em.createQuery("select u from User u").getResultList();
		}
		catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}
	
	public User get(Long id) {
		try {
			return (User) em.find(User.class, id);
		}
		catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}
	
	public User add(UserWrapper userWrapper) {
		try {
			User user = new User();
			user.setLogin(userWrapper.getLogin());
			user.setName(userWrapper.getName());
			
			String password = passwordManager.generate();
			user.setPassword(securityBean.hash(password));
			
			em.persist(user);
			
			em.createNativeQuery("INSERT INTO sc_user_group(group_id_, user_id_) " +
								 "VALUES ('authenticated', ?);")
				.setParameter(1, user.getLogin())
				.executeUpdate();

			return user;
		}
		catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}
	
	public User edit(Long id, UserWrapper userWrapper) {
		try {
			
			User user = (User) em.find(User.class, id);
			user.setName(userWrapper.getName());
			
			return em.merge(user);
		}
		catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}
	
	public boolean delete(Long id) {
		try {
			User user = (User) em.find(User.class, id);
			em.createNativeQuery("DELETE FROM sc_user_group " +
								 "WHERE user_id_ = '" + user.getLogin() + "';")
				.executeUpdate();
			
			em.remove(user);
			
			return true;
		}
		catch (Exception e) {
//			e.printStackTrace();
			return false;
		}
	}
	
	public User getUserByLogin(String login) {
		try {
			return (User) em.createQuery("select u from User u where u.login = :login")
							.setParameter("login", login)
							.getSingleResult();
		}
		catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}

	public boolean changePassword(Long id, UserWrapper userWrapper) {
		try {
			User user = (User) em.find(User.class, id);
			
			if (user.getPassword().equals( securityBean.hash(userWrapper.getOld()) )) {
				user.setPassword(securityBean.hash(userWrapper.getPassword()));
				em.merge(user);

				return true;
			}
			
			return false;
		}
		catch (Exception e) {
//			e.printStackTrace();
			return false;
		}
	}
	
	public boolean resetPassword(Long id) {
		try {
			User user = (User) em.find(User.class, id);
			
			String password = passwordManager.generate();
			user.setPassword(securityBean.hash(password));
			
			em.merge(user);
			return true;
		}
		catch (Exception e) {
//			e.printStackTrace();
			return false;
		}
	}
}
