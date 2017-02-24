package com.cts.coabcm.dao;

import java.util.List;
import com.cts.coabcm.model.Notification;

public interface NotificationDao{ 

	public List<Notification> findAll();
	public void save(Notification notification);
}
