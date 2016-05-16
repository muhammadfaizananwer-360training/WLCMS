package com.softech.ls360.lcms.contentbuilder.dao.impl;



import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.VU360UserDAO;
import com.softech.ls360.lcms.contentbuilder.model.VU360User;

@Repository
public class VU360UserDAOImpl extends GenericDAOImpl< VU360User> implements VU360UserDAO   {
	
	public VU360UserDAOImpl(){
		
	}

	
	/*	 
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private SessionFactory sessionFactory;
	
	@Override
	public VU360User getVU360User(String userName) {
		// TODO Auto-generated method stub
		System.out.println("getVU360User:"+userName);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return (VU360User)this.sessionFactory.getCurrentSession().get(VU360User.class, new Long(11));
		
		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		VU360User user = (VU360User)session.createCriteria(VU360User.class).add(Restrictions.eq("username", userName)).uniqueResult();
	 	session.getTransaction().commit();

		return user;
	}

	@Override
	public VU360User authenticateVU360User(String userName, String password) {
		// TODO Auto-generated method stub
		return null;
	}
*/
	
	@Transactional
	@Override
	public boolean updateUserFirstTimeLogin(long userId, boolean value){
		
		Query query = entityManager.createNativeQuery("UPDATE VU360USER SET FIRSTTIMELOGINTF =:FIRSTTIMELOGINTF WHERE ID =:userId");
		query.setParameter("FIRSTTIMELOGINTF", value);
		query.setParameter("userId", userId);
		
		int rowAffected = query.executeUpdate();
		if(rowAffected>0)
			return true;
		else
			return false;
	}
}
