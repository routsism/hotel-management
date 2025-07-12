package gr.aueb.cf.hotel_managment.model.core.exceptions;

public class AppObjectNotFoundException extends AppGenericException {
  private static final String DEFAULT_CODE = "NotFound";

  public AppObjectNotFoundException(String code, String message) {
    super(code + DEFAULT_CODE, message);
  }
}