package vehicle.inventory.common;

import org.springframework.stereotype.Component;

/**
 * @author Durga Pathak
 *
 */
@Component
public class VehicleResponse {

  private int code;

  private String message;

  private String description;

  /**
   * Gets the code.
   *
   * @return the code
   */
  public int getCode() {
    return code;
  }

  /**
   * Sets the code
   *
   * @param code
   *          the code to set the new code
   */
  public void setCode(int code) {
    this.code = code;
  }

  /**
   * Gets the message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the message
   *
   * @param message
   *          the message to set the new message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Gets the description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description
   *
   * @param description
   *          the description to set the new description
   */
  public void setDescription(String description) {
    this.description = description;
  }

}
