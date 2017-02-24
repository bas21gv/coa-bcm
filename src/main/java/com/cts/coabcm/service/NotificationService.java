package com.cts.coabcm.service;

import java.util.List;
import java.util.Set;

import com.cts.coabcm.model.Device;
import com.cts.coabcm.model.Notification;

public interface NotificationService {

	public boolean saveNotification(Notification notification);
	public boolean saveDeviceId(Device device);
	public Set<String> deviceIds();
	public List<Notification> notifyMsg();
}
