package vehicle.inventory.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Vehicle {
	
	@JsonProperty
	private String id;
	
	@JsonProperty
	private String vehicleType;
	
	@JsonProperty
    private String name;
	
	@JsonProperty
	private String make;
	
	@JsonProperty
	private String model;
	
	@JsonProperty
	private String color;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getMake() {
		return make;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

}
