package co.edu.eci.dosw.restaurant.model.domain;

import co.edu.eci.dosw.restaurant.model.enums.EstadoMesa;

public class Mesa {
    private EstadoMesa estado = EstadoMesa.DISPONIBLE;

    public EstadoMesa getEstado() {
        return estado;
    }

    public void setEstado(EstadoMesa estado) {
        this.estado = estado;
    }
}
