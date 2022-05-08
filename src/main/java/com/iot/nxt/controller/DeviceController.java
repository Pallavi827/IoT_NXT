package com.iot.nxt.controller;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iot.nxt.dto.DeviceList;


@RestController
public class DeviceController {

	@PostMapping("/read/device_data")
	public void addDevices() {
		DeviceList deviceList = null;

		try {

			File directory = new File(".\\jsonFiles");
			int fileCount = directory.list().length;
			System.out.println("Total Files in JsonFiles is " + fileCount);
			List<DeviceList> deviceObjList = new ArrayList<>();
			List<String> deviceIdList  = new ArrayList<>();

			for (int i = 1; i <= fileCount; i++) {
				String fileName = ".\\jsonFiles\\Device_" + i + ".json";
				 //System.out.println(fileName);

				FileReader file = new FileReader(fileName);
				Gson gson = new Gson();
				deviceList = gson.fromJson(file, DeviceList.class);

				
				deviceIdList.add(deviceList.getDevice_id());

				deviceObjList.add(deviceList);

			}

			System.out.println(deviceIdList);

			for (DeviceList device : deviceObjList) {

				System.out.print("current_temperature " + deviceList.getCurrent_temperature() + " ");
				System.out.print("pressure " + device.getPressure() + " ");
				System.out.print("supply_voltage_level " + device.getSupply_voltage_level() + " ");
				System.out.print("frequency " + device.getFrequency() + " ");
				System.out.print("accuracy " + device.getAccuracy() + " ");
				System.out.print("resolution " + device.getResolution() + " ");
				System.out.print("drift " + device.getDrift() + " ");
				System.out.print("sensor_type " + device.getSensor_type());
				System.out.println();

			}

		} catch (FileNotFoundException e) {

			System.out.println("No file found");
		}

	}
}
