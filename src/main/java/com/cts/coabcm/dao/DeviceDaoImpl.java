package com.cts.coabcm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cts.coabcm.model.Device;

@Repository
public class DeviceDaoImpl implements DeviceDao{
	
	@Autowired
	private SessionFactory sessionFactory;

	
	public Device findByDeviceId(String deviceId) {
		Query query = sessionFactory.getCurrentSession().createQuery("from Device where deviceId='"+deviceId+"'");
		if(query.list().isEmpty()){
			return null;
		}
		List<Device> result = query.list();
		return result.get(0);
	}

	
	public void save(Device device) {
		sessionFactory.getCurrentSession().saveOrUpdate(device);
	}

	
	@SuppressWarnings("unchecked")
	public List<Device> findAll() {
		return (List<Device>) sessionFactory.getCurrentSession().createCriteria(Device.class).list();
	}

}
