package vehicle.inventory;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import vehicle.inventory.service.VehicleService;

@Component
@ApplicationPath("/rest")
public class RestConfig extends ResourceConfig{
	
	public RestConfig() {
		register(VehicleService.class);
	}

}
