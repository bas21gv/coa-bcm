package com.cts.coabcm.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cts.coabcm.model.Device;

@Repository
public class DeviceDaoImpl implements DeviceDao{
	
	@Autowired
	private SessionFactory sessionFactory;

	
	public Device findByDeviceId(String deviceId) {
		return (Device) sessionFactory.getCurrentSession().get(Device.class, deviceId);
	}

	
	public void save(Device device) {
		sessionFactory.getCurrentSession().saveOrUpdate(device);
	}

	
	@SuppressWarnings("unchecked")
	public List<Device> findAll() {
		return (List<Device>) sessionFactory.getCurrentSession().createCriteria(Device.class).list();
	}

}
