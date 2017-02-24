package com.cts.coabcm.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.coabcm.dao.DeviceDao;
import com.cts.coabcm.dao.NotificationDao;
import com.cts.coabcm.model.Device;
import com.cts.coabcm.model.Notification;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
	
	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

	@Autowired
	private NotificationDao notificationDao;
	@Autowired
	private DeviceDao deviceDao;

	@Override
	public boolean saveNotification(Notification notification) {
		if (notification != null) {
			notificationDao.save(notification);
			log.info("<---- Notification message stored in Database ---->");
			return true;
		}
		return false;
	}

	@Override
	public boolean saveDeviceId(Device device) {
		if(device !=null){
			Device deviceAvail = deviceDao.findByDeviceId(device.getDeviceId());
			if (deviceAvail == null) {
				deviceDao.save(device);
				log.info("<---- DeviceId registered in Database ---->");
			}else{
				log.info("<---- DeviceId already registered ---->");
			}
			return true;
		}
		return false;
	}

	@Override
	public Set<String> deviceIds() {
		Set<String> ids = new HashSet<String>();
		for (Device device : deviceDao.findAll()) {
			ids.add(device.getDeviceId());
		}
		log.info("<---- fetched all registered Ids ---->");
		return ids;
	}

	@Override
	public List<Notification> notifyMsg() {
		List<Notification> msgList = new ArrayList<Notification>();
		//for (Notification notification : notificationDao.findWithPageable(new PageRequest(0, 10, Direction.DESC, "id"))) {
		for (Notification notification : notificationDao.findAll()) {
			msgList.add(notification);
		}
		log.info("<---- fetched all Notification messages ---->");
		return msgList;
	}

}
