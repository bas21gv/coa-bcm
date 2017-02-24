package com.cts.coabcm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cts.coabcm.model.Notification;

@Repository
public class NotificationDaoImpl implements NotificationDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	
	@SuppressWarnings("unchecked")
	public List<Notification> findAll() {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Notification.class);
		criteria.addOrder(Order.desc("notifyId"));
		criteria.setMaxResults(10);
		return criteria.list();
	}

	public void save(Notification notification) {
		sessionFactory.getCurrentSession().saveOrUpdate(notification);
	}

}
