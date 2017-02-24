package com.cts.coabcm.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="BCM_Notification")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Notification implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5350509038298233112L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="Id")
	private Integer notifyId;
	
	@Column(name="Device_Id")
	@JsonIgnore
	private String deviceId;
	
	@Column(name="Topic")
	private String topic;
	
	@Column(name="Description")
	private String description;
	
	@Column(name="Conference_Details")
	private String conferenceDetails;
	
	@Column(name="Conference_Date")
	private Date conferenceDate;
	
	@Column(name="Conference_Time")
	private Time conferenceTime;
	
	@Column(name="Is_Read")
	private Integer isRead=0;
	
	public Notification() {
	}


	public Integer getNotifyId() {
		return notifyId;
	}


	public void setNotifyId(Integer notifyId) {
		this.notifyId = notifyId;
	}


	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getConferenceDetails() {
		return conferenceDetails;
	}

	public void setConferenceDetails(String conferenceDetails) {
		this.conferenceDetails = conferenceDetails;
	}

	public Date getConferenceDate() {
		return conferenceDate;
	}

	public void setConferenceDate(Date conferenceDate) {
		this.conferenceDate = conferenceDate;
	}

	public Time getConferenceTime() {
		return conferenceTime;
	}

	public void setConferenceTime(Time conferenceTime) {
		this.conferenceTime = conferenceTime;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

}
