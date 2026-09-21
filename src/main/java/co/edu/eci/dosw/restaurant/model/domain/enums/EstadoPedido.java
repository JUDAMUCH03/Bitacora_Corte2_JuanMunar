package co.edu.eci.dosw.restaurant.model.domain.enums;

/**
 * Máquina de estados para el KDS de Blue Velvet.
 * Modela las transiciones admisibles en el ciclo de vida de una comanda.
 */
public enum EstadoPedido {
    RECIBIDO,
    EN_PREPARACION,
    LISTO,
    ENTREGADO,
    CANCELADO;

    /**
     * Evalúa si una transición de estado es válida según el flujo unidireccional.
     */
    public boolean puedeTransicionarA(EstadoPedido siguiente) {
        if (siguiente == null) {
            return false;
        }
        return switch (this) {
            case RECIBIDO -> siguiente == EN_PREPARACION || siguiente == CANCELADO;
            case EN_PREPARACION -> siguiente == LISTO;
            case LISTO -> siguiente == ENTREGADO;
            default -> false;
        };
    }

    /**
     * Regla de negocio Blue Velvet: Solo se cancela el trago si no ha entrado a coctelera.
     */
    public boolean esCancelable() {
        return this == RECIBIDO;
    }

    /**
     * Regla de negocio Blue Velvet (Invariante 1):
     * La mutación del destilado solo es válida mientras la orden esté en RECIBIDO.
     */
    public boolean permiteModificacionDestilado() {
        return this == RECIBIDO;
    }
}