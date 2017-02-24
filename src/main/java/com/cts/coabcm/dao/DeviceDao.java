package com.cts.coabcm.dao;


import java.util.List;

import com.cts.coabcm.model.Device;

public interface DeviceDao{ 
	
	public Device findByDeviceId(String deviceId);
	public void save(Device device);
	public List<Device> findAll();
}
