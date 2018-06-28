package vehicle.inventory.service;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

import vehicle.inventory.common.VehicleResponse;
import vehicle.inventory.model.Vehicle;
import vehicle.inventory.service.impl.VehicleServiceImpl;

@Path("/vehicles")
public class VehicleService {
  @Autowired
  VehicleServiceImpl vehicleServiceImpl;

  @Autowired
  VehicleResponse vehicleResponse;

  @POST
  @Path("/vehicle")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addVehicleInventory(@RequestBody Vehicle vehicle) {

    if (!isValidRequest(vehicle)) {
      throw new BadRequestException(build400Response());
    }
    int rowAffected = vehicleServiceImpl.addVehicleInventory(vehicle);

    return buildInsertResponse(rowAffected, vehicle);
  }

  @Path("/vehicle/{vehicleType}/search")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<Vehicle> getVehicleInventoryByType(
      @NotNull @PathParam("vehicleType") String vehicleType) {
    if (StringUtils.isEmpty(vehicleType)) {
      throw new BadRequestException(build400Response());
    }
    return vehicleServiceImpl.getVehicleByType(vehicleType);
  }

  @Path("/vehicle/{vehicleId}/remove")
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getVehicleInventoryById(
      @NotNull @PathParam("vehicleId") @Pattern(regexp = "[0-9]+", message = "Item id must be valid positive number") String vehicleId) {
    if (StringUtils.isEmpty(vehicleId)) {
      throw new BadRequestException(build400Response());
    }

    if (!vehicleServiceImpl.vehicleIdExists(vehicleId)) {
      return build444Response();
    }
    int rowAffectd = vehicleServiceImpl.deleteVehicleById(vehicleId);
    return buildRemoveResponse(rowAffectd, vehicleId);
  }
  
  @POST
  @Path("/vehicle/{vehicleId}/update")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateVehicleInventoryById(
      @NotNull @PathParam("vehicleId") @Pattern(regexp = "[0-9]+", message = "Item id must be valid positive number") String vehicleId,
      @RequestBody Vehicle vehicle) {
    if (!isValidRequest(vehicle) || StringUtils.isEmpty(vehicleId)) {
      throw new BadRequestException(build400Response());
    }
    
    if (!vehicleServiceImpl.vehicleIdExists(vehicleId)) {
      return build444Response();
    }
    int rowAffected = vehicleServiceImpl.updateVehicleById(vehicleId, vehicle);
    return buildUpdateResponse(rowAffected, vehicleId);
  }

  /**
   * @param rowAffected
   * @param vehicleId
   * @return
   */
  private Response buildUpdateResponse(int rowAffected, String vehicleId) {
    if (rowAffected >= 1) {
      vehicleResponse.setCode(HttpStatus.OK.value());
      vehicleResponse.setMessage(HttpStatus.OK.getReasonPhrase());
      vehicleResponse.setDescription(String.format(
          "Successfully updated vehicle with ID %s", vehicleId));
    }
    
    return Response.status(vehicleResponse.getCode()).entity(vehicleResponse)
        .build();
  }

  /**
   * @param rowAffectd
   * @return
   */
  private Response buildRemoveResponse(int rowAffectd, String vehicleId) {
    if (rowAffectd >= 1) {
      vehicleResponse.setCode(HttpStatus.OK.value());
      vehicleResponse.setMessage(HttpStatus.OK.getReasonPhrase());
      vehicleResponse.setDescription(
          String.format("Successfully removed Vehicle with ID %s.", vehicleId));
    } else {
      throw new WebApplicationException();
    }

    return Response.status(vehicleResponse.getCode()).entity(vehicleResponse)
        .build();
  }

  /**
   * Builds the response when there is bad request from client.
   * 
   * @return Response
   */
  private Response build400Response() {
    vehicleResponse.setCode(HttpStatus.BAD_REQUEST.value());
    vehicleResponse.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
    vehicleResponse.setDescription(
        "Bad Client Request. [type, name, make, model, color] cannot be empty/null.");
    return Response.status(vehicleResponse.getCode()).entity(vehicleResponse)
        .build();
  }

  /**
   * Builds the response when there is bad request from client.
   * 
   * @return Response
   */
  private Response build444Response() {
    vehicleResponse.setCode(444);
    vehicleResponse.setMessage("Vehicle Not Found");
    vehicleResponse.setDescription("Cannot find vehicle for given Vehicle ID.");
    return Response.status(vehicleResponse.getCode()).entity(vehicleResponse)
        .build();
  }

  /**
   * Builds the response for insert operation.
   * 
   * @param Response
   */
  private Response buildInsertResponse(int rowAffected, Vehicle vehicle) {
    if (rowAffected >= 1) {
      vehicleResponse.setCode(HttpStatus.CREATED.value());
      vehicleResponse.setMessage(HttpStatus.CREATED.getReasonPhrase());
      vehicleResponse.setDescription(String.format(
          "Successfully added new %s to inventory.", vehicle.getVehicleType()));
    } else {
      vehicleResponse.setCode(HttpStatus.NOT_MODIFIED.value());
      vehicleResponse.setMessage(HttpStatus.NOT_MODIFIED.getReasonPhrase());
      vehicleResponse.setDescription(
          String.format("Could not add %s to inventory.", vehicle.getVehicleType()));
    }
    return Response.status(vehicleResponse.getCode()).entity(vehicleResponse)
        .build();
  }

  /**
   * Validates if the client request is valid.
   * 
   * @param vehicle
   * @return boolean
   */
  private boolean isValidRequest(Vehicle vehicle) {
    if (StringUtils.isEmpty(vehicle.getName())
        || StringUtils.isEmpty(vehicle.getVehicleType())
        || StringUtils.isEmpty(vehicle.getMake())
        || StringUtils.isEmpty(vehicle.getModel())
        || StringUtils.isEmpty(vehicle.getColor())) {
      return false;
    }
    return true;

  }

}
