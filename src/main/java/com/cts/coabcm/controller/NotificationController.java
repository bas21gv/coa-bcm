package com.cts.coabcm.controller;

import java.net.URI;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cts.coabcm.bean.DeviceValue;
import com.cts.coabcm.bean.FCMValue;
import com.cts.coabcm.model.Device;
import com.cts.coabcm.model.Notification;
import com.cts.coabcm.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/rest")
public class NotificationController extends CorsController{
	
	private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	FCMValue fcmValue;
	
	@Autowired
	NotificationService notificationService;
	
	@RequestMapping(value = "/getnotify", method = RequestMethod.GET)
	public ResponseEntity<List<Notification>> getNotifications() {
		log.info("<---- Started Fetch Notification ---->");
		List<Notification> notifyList = notificationService.notifyMsg();
		log.info("Notification count from DB ::: " + notifyList.size());
		if (notifyList.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		log.info("<---- Ended Fetch Notification ---->");
		return new ResponseEntity<List<Notification>>(notifyList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/sendnotify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> pushNotification(@RequestBody String notifyMsg) {

		log.info("<---- Stared the pushNotification ---->");
		log.info("Request JSON value ::: " + notifyMsg);
		ResponseEntity<?> response = null;
	    final ObjectMapper mapper = new ObjectMapper();
		JSONObject dataJson = new JSONObject();
		JSONObject notificationJson = new JSONObject();
		JSONObject responseJson = new JSONObject();

		DeviceValue fromClient = null;
		try {
			fromClient = mapper.readValue(notifyMsg, DeviceValue.class);
		
		log.info("Google Cloud Messaging ::: FCM URL ::: " + fcmValue.getUrl());
		log.info("Google Cloud Messaging ::: FCM API KEY ::: " + fcmValue.getApiKey());

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "key=" +fcmValue.getApiKey());
		
		dataJson.put("title", fromClient.getNotification().getTopic());
		dataJson.put("description", fromClient.getNotification().getDescription());
		dataJson.put("details", fromClient.getNotification().getDetails());
		dataJson.put("date", fromClient.getNotification().getDatevalue());
		dataJson.put("time", fromClient.getNotification().getTimevalue());
		
		notificationJson.put("title", fromClient.getNotification().getTopic());
		notificationJson.put("body", fromClient.getNotification().getDescription());

		
		Set<String> registerIds = notificationService.deviceIds();
		registerIds.remove(fromClient.getDeviceid());
		//registerIds.add(fromClient.getDeviceid());//notificationService.deviceIds();
		log.info("Registered Id from DB ::: " + registerIds.toString());

		if (!registerIds.isEmpty()) {
			JSONArray idList = new JSONArray(registerIds);
			JSONObject requestJson = new JSONObject();
			requestJson.put("priority", "high");
			requestJson.put("registration_ids", idList);
			requestJson.put("notification", notificationJson);
			requestJson.put("data", dataJson);/** {"registration_ids":"","notification":{"topic":"","description":""},"data":{"details":"","date":"","time":""}} **/

			HttpEntity<String> entity = new HttpEntity<String>(requestJson.toString(), headers);
			log.info("Json to send FCM ::: " + entity.getBody());

			response = restTemplate.exchange(new URI(fcmValue.getUrl()), HttpMethod.POST, entity,
						Object.class);

			log.info("Json received from FCM ::: " + response.getBody());

			LinkedHashMap<String, Object> responsePayload = (LinkedHashMap<String, Object>) response.getBody();
			if ((Integer) responsePayload.get("success") != 0) {
				Notification notification = new Notification();
				notification.setDeviceId(fromClient.getDeviceid());
				notification.setTopic(fromClient.getNotification().getTopic());
				notification.setDescription(fromClient.getNotification().getDescription());
				notification.setConferenceDetails(fromClient.getNotification().getDetails());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
				java.util.Date dateValue;
				java.util.Date timeValue;
				try {
					dateValue = sdf.parse(fromClient.getNotification().getDatevalue());
					notification.setConferenceDate(new Date(dateValue.getTime()));
					timeValue = sdfTime.parse(fromClient.getNotification().getTimevalue());
					notification.setConferenceTime(new Time(timeValue.getTime()));
				} catch (Exception e) {
					log.error(e.getMessage());
					response = new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
				}

				boolean result = notificationService.saveNotification(notification);
				if (result) {
					responseJson.put("status", 200);
					responseJson.put("statusText", "Notification sent");
					response = new ResponseEntity(responseJson.toString(), HttpStatus.OK);
				}
			}else{
				responseJson.put("status", 500);
				responseJson.put("statusText", "Notification not sent");
				response = new ResponseEntity(responseJson.toString(), HttpStatus.INTERNAL_SERVER_ERROR);				
			}
		} else {
			response =  new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		} catch (Exception e) {
			log.error(e.getMessage());
			response = new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("<---- Ended the pushNotification ---->");
		return response;
	}

	@RequestMapping(value = "/saveid", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveDeviceId(@RequestBody String userDeviceId) {

		log.info("<---- Started Register Ids ---->");
		log.info("Request JSON value ::: " + userDeviceId);

		ResponseEntity<?> response = null;
		ObjectMapper mapper = new ObjectMapper();
		JSONObject responseJson = new JSONObject();
		Map<String, Object> map = null;
		try {
			map = mapper.readValue(userDeviceId, Map.class);

		if(map.get("deviceid")!=null){
			Device device = new Device();
			device.setDeviceId(map.get("deviceid").toString());
		boolean result = notificationService.saveDeviceId(device);
		if (result) {
			responseJson.put("status", 200);
			responseJson.put("statusText", "DeviceId registered");
			response = new ResponseEntity(responseJson.toString(), HttpStatus.OK);
		}
		} else {
			responseJson.put("status", 500);
			responseJson.put("statusText", "DeviceId not registered");
			response = new ResponseEntity(responseJson.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		} catch (Exception e) {
			log.error(e.getMessage());
			response = new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("<---- Ended Register Ids ---->");
		return response;
	}
}
