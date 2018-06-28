package vehicle.inventory.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import vehicle.inventory.model.Vehicle;

/**
 * This class implements all the CRUD operations.
 * 
 * @author Durga Pathak
 *
 */
@Component
public class VehicleServiceImpl {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  private NamedParameterJdbcTemplate namedJdbcTemplate;

  private static final String SELECT_BY_TYPE = "SELECT * FROM VEHICLE WHERE VEHICLE_TYPE = ?";

  private static final String SELECT_BY_ID = "SELECT COUNT(ID) FROM VEHICLE WHERE ID = ?";

  private static final String REMOVE_BY_ID = "DELETE FROM VEHICLE WHERE ID = ?";

  private static final String UPDATE_BY_ID = "UPDATE VEHICLE SET VEHICLE_TYPE = :vehicle_type, NAME = :name, MAKE = :make, MODEL = :model, COLOR = :color WHERE ID = :id";

  public int addVehicleInventory(Vehicle vehicle) {
    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(
        jdbcTemplate.getDataSource()).withTableName("VEHICLE");

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("NAME", vehicle.getName());
    parameters.put("VEHICLE_TYPE", vehicle.getVehicleType());
    parameters.put("MAKE", vehicle.getMake());
    parameters.put("MODEL", vehicle.getModel());
    parameters.put("COLOR", vehicle.getColor());

    return simpleJdbcInsert.execute(parameters);

  }

  /**
   * @param vehicleType
   */
  public List<Vehicle> getVehicleByType(String vehicleType) {
    return getAllVecicles(vehicleType);
  }

  /**
   * Gets all the vehicle for given vehicle type
   * 
   * @param vehicleType
   */
  private List<Vehicle> getAllVecicles(String vehicleType) {
    return jdbcTemplate.query(SELECT_BY_TYPE, new Object[] { vehicleType },
        new ResultSetExtractor<List<Vehicle>>() {
          @Override
          public List<Vehicle> extractData(ResultSet rs) throws SQLException {
            List<Vehicle> vehicles = new ArrayList<>();
            while (rs.next()) {
              Vehicle vehicle = new Vehicle();

              vehicle.setId(rs.getString("ID"));
              vehicle.setColor(rs.getString("COLOR"));
              vehicle.setMake(rs.getString("MAKE"));
              vehicle.setModel(rs.getString("MODEL"));
              vehicle.setName(rs.getString("NAME"));
              vehicle.setVehicleType(rs.getString("VEHICLE_TYPE"));

              vehicles.add(vehicle);
            }
            return vehicles;
          }
        });
  }

  public boolean vehicleIdExists(String vehicleId) {
    int count = jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { vehicleId }, Integer.class);
    if (count >= 1)
      return true;
    return false;
  }

  /**
   * Removes the vehicle for given id.
   * 
   * @param vehicleId
   * @return rowAffected
   */
  public int deleteVehicleById(String vehicleId) {

    return jdbcTemplate.update(REMOVE_BY_ID, new Object[] { vehicleId });
  }

  /**
   * Updates vehicle information for given vehicle ID.
   * 
   * @param vehicleId
   * @param vehicle
   * @return rowAffectged
   */
  public int updateVehicleById(String vehicleId, Vehicle vehicle) {
    return namedJdbcTemplate.update(UPDATE_BY_ID,
        new MapSqlParameterSource("id", vehicleId)
            .addValue("color", vehicle.getColor())
            .addValue("make", vehicle.getMake())
            .addValue("model", vehicle.getModel())
            .addValue("name", vehicle.getName())
            .addValue("vehicle_type", vehicle.getVehicleType()));
  }

}
