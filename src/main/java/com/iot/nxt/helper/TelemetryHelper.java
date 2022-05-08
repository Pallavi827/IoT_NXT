package com.iot.nxt.helper;

import com.google.gson.Gson;

public class TelemetryHelper {
	
	private String deviceId;
    private String temp;
    private String humid;
    
    public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getHumid() {
		return humid;
	}


	public void setHumid(String humid) {
		this.humid = humid;
	}


	public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
