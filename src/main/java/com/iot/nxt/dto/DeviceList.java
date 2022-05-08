package com.iot.nxt.dto;


public class DeviceList {

	private String device_id;
	private Double current_temperature;
	private Double pressure;
	private Double supply_voltage_level;
	private Double full_scale;
	private Double frequency;
	private Double accuracy;
	private Double resolution;
	private Double drift;
	private Integer sensor_type;

	

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public Double getCurrent_temperature() {
		return current_temperature;
	}

	public void setCurrent_temperature(Double current_temperature) {
		this.current_temperature = current_temperature;
	}

	public Double getPressure() {
		return pressure;
	}

	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}

	public Double getSupply_voltage_level() {
		return supply_voltage_level;
	}

	public void setSupply_voltage_level(Double supply_voltage_level) {
		this.supply_voltage_level = supply_voltage_level;
	}

	public Double getFull_scale() {
		return full_scale;
	}

	public void setFull_scale(Double full_scale) {
		this.full_scale = full_scale;
	}

	public Double getFrequency() {
		return frequency;
	}

	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

	public Double getResolution() {
		return resolution;
	}

	public void setResolution(Double resolution) {
		this.resolution = resolution;
	}

	public Double getDrift() {
		return drift;
	}

	public void setDrift(Double drift) {
		this.drift = drift;
	}

	public Integer getSensor_type() {
		return sensor_type;
	}

	public void setSensor_type(Integer sensor_type) {
		this.sensor_type = sensor_type;
	}

	@Override
	public String toString() {
		return "DeviceList [device_id=" + device_id + ", current_temperature=" + current_temperature + ", pressure="
				+ pressure + ", supply_voltage_leve=" + supply_voltage_level + ", full_scale=" + full_scale
				+ ", frequency=" + frequency + ", accuracy=" + accuracy + ", resolution=" + resolution + ", drift="
				+ drift + ", sensor_type=" + sensor_type + "]";
	}

}
