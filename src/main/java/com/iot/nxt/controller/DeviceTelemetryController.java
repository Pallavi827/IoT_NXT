package com.iot.nxt.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iot.nxt.dto.DeviceList;
import com.iot.nxt.helper.TelemetryHelper;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.TwinPropertyCallBack;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwin;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwinDevice;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

@RestController
public class DeviceTelemetryController {

	static IotHubClientProtocol protocol = IotHubClientProtocol.AMQPS;
	static DeviceClient client;
//	public static final String iotHubOwnerConnectionString = "HostName=IoT-NxT-Hub.azure-devices.net;SharedAccessKeyName=iothubowner;DeviceId=NxTDevice1000;SharedAccessKey=GHgKuy6Ie3IGHKliLQ9IHRJhwRuPzYhITn4s4PXZTjk=";
	public static final String iotHubOwnerConnectionString2 = "HostName=IoT-NxT-Hub.azure-devices.net;SharedAccessKeyName=iothubowner;DeviceId=NxTDevice1000;SharedAccessKey=Gu9lyR4z1jVWcvdIKMYBsCQ0Vlg+dgZOp+vpvOx3//0=";
	public static final String iotHubReadConnectionString = "HostName=IoT-NxT-Hub.azure-devices.net;SharedAccessKeyName=ServiceAndRegistryRead;SharedAccessKey=BeGXClv7fL7f5x5fGVH2I/Oge7MRlNXe2ajOQCnc2dQ=";
	public static final String deviceId = "NxTDevice1000";
//	DeviceTwin twinClient;
	
	
	@GetMapping("/sendTele")
	public boolean doStuff() {
		try{
			sendMessages();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	private void sendMessages() throws Exception {
		
		//twinClient = DeviceTwin.createFromConnectionString(iotHubOwnerConnectionString2);
	
		/*READ JSON FILES STARTS*/
		//COUNT NO OF JSON FILES IN jsonFiles Folder
		File directory = new File(".\\jsonFiles");
		int fileCount = directory.list().length;
		System.out.println("Total Files in JsonFiles is " + fileCount);
		DeviceList deviceSpecification = null;
		for(int i=1;i<=fileCount;i++) {
			client = new DeviceClient(iotHubOwnerConnectionString2, protocol);
			client.open();
			String fileName = ".\\jsonFiles\\Device_" + i + ".json";
			//To Read File
			FileReader file = new FileReader(fileName);
			//Convert file Object to JSON
			Gson gson = new Gson();//Creating new gson Object
			deviceSpecification = gson.fromJson(file, DeviceList.class);
			//String deviceId = deviceSpecification.getDevice_id();
			//send(deviceId);
			send(deviceSpecification);
			
		}
		
		/* READ JSON FILES ENDS */
		/*
		 * for (int i=0; i<4; i++) { System.out.println("i = "+i); send(deviceId); }
		 */
		client.closeNow();
	}
	private void send(DeviceList deviceSpecification) {
			
		Gson gson = new Gson();
		String msgStr = gson.toJson(deviceSpecification);
		Message msg = new Message(msgStr);
		System.out.println("Sending: " + msgStr);
		
		System.out.println("Reported props ");
		Set<Property> reportedProp = new HashSet<Property>();
		reportedProp.add(new Property("current_temperature", deviceSpecification.getCurrent_temperature()));
		reportedProp.add(new Property("full_scale", deviceSpecification.getFull_scale()));
		reportedProp.add(new Property("device_id", deviceSpecification.getDevice_id()));
		reportedProp.add(new Property("supply_voltage_level", deviceSpecification.getSupply_voltage_level()));
		reportedProp.add(new Property("accuracy", deviceSpecification.getAccuracy()));
		reportedProp.add(new Property("pressure", deviceSpecification.getPressure()));
		reportedProp.add(new Property("resolution", deviceSpecification.getResolution()));
		reportedProp.add(new Property("sensor_type", deviceSpecification.getSensor_type()));
		reportedProp.add(new Property("frequency", deviceSpecification.getFrequency()));
		reportedProp.add(new Property("drift", deviceSpecification.getDrift()));
		EventCallback callback = new EventCallback();
		ContextCallback conCallback = new ContextCallback();
		
		try {
			client.startDeviceTwin(callback, null, conCallback, null);
			client.sendReportedProperties(reportedProp); //-----------------------------------
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
		Object lockobj = new Object();
        
        client.sendEventAsync(msg, callback, lockobj);
        
        synchronized (lockobj) {
        	try {
				lockobj.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
		
		
		
		
	}
	private void send(String deviceName) throws IOException, IotHubException  {
        double maxval = 12.0;
        double minval = 8.0;
        
//        DeviceTwinDevice device = new DeviceTwinDevice(deviceId);
//        twinClient.getTwin(device);
        Integer temp = (int) ((Math.random() * (maxval- minval)) + minval);
		Integer humid = (int) ((Math.random() * (maxval- minval)) + minval);
		
		//  Generate data object
		//{"deviceId": id, "windSpeed": currWindSpeed,
		// "powerOutput": currPowerOutput, "payerId": "chris@microsoft", "eventDate": now}
		TelemetryHelper telemetryDataPoint = new TelemetryHelper();
		telemetryDataPoint.setDeviceId(deviceName);
		telemetryDataPoint.setTemp(temp.toString());
		telemetryDataPoint.setHumid(humid.toString());

		String msgStr = telemetryDataPoint.serialize();
		Message msg = new Message(msgStr);
		System.out.println("Sending: " + msgStr);
		
		System.out.println("Reported props ");
		Set<Property> reportedProp = new HashSet<Property>();
		reportedProp.add(new Property("temp", temp));
		reportedProp.add(new Property("humid", humid));
		EventCallback callback = new EventCallback();
		ContextCallback conCallback = new ContextCallback();
		try {
			client.startDeviceTwin(callback, null, conCallback, null);
			client.sendReportedProperties(reportedProp); //-----------------------------------
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
		Object lockobj = new Object();
        
        client.sendEventAsync(msg, callback, lockobj); //----------------------------

        synchronized (lockobj) {
        	try {
				lockobj.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
	
	@GetMapping("/getProps")
	public String getDeviceProps() throws IOException, IotHubException {
		DeviceTwin twinClient = DeviceTwin.createFromConnectionString(iotHubOwnerConnectionString2);
		DeviceTwinDevice device = new DeviceTwinDevice(deviceId);
		twinClient.getTwin(device);
//		DeviceTwinDevice device = new DeviceTwinDevice(deviceId);
		System.out.println("device.getConnectionState() "+device.getConnectionState());
		System.out.println("device.desiredPropertiesToString() "+device.desiredPropertiesToString());
		System.out.println("device.reportedPropertiesToString() "+device.reportedPropertiesToString());
		System.out.println("DesiredPropertiesVersion() : "+device.getDesiredPropertiesVersion());
		System.out.println("DesiredProperties() : "+device.getDesiredProperties());
		System.out.println("ReportedPropertiesVersion() : "+device.getReportedPropertiesVersion());
		System.out.println("device.getReportedProperties() : "+device.getReportedProperties());
		
		return "success";
	}
	
	
	
	private class EventCallback implements IotHubEventCallback {
        public void execute(IotHubStatusCode status, Object context) {
            System.out.println("IoT Hub responded to message with status: " + status.name());

            if (context != null) {
                synchronized (context) {
                    context.notify();
                }
            }
        }
    }
	private class ContextCallback implements TwinPropertyCallBack {
        @Override
		public void TwinPropertyCallBack(Property property, Object context) {
			System.out.println("IoT Hub responded to TwinProperty with update at: " + property.getLastUpdated());
            if (context != null) {
                synchronized (context) {
                    context.notify();
                }
            }
		}
    }
	
}
