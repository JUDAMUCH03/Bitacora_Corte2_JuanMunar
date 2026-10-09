package co.edu.eci.dosw.restaurant.exception;

/**
 * Excepción lanzada cuando los datos de una solicitud violan reglas de validación de negocio.
 * Se traduce en una respuesta HTTP 400 Bad Request.
 */
public class ValidacionException extends RuntimeException {

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
