package com.iot.nxt.dto;



import java.util.List;

public class Devices {

	private List<DeviceList> device_list;

	public List<DeviceList> getDevice_list() {
		return device_list;
	}

	public void setDevice_list(List<DeviceList> device_list) {
		this.device_list = device_list;
	}

	@Override
	public String toString() {
		return "Devices [device_list=" + device_list + "]";
	}

}
