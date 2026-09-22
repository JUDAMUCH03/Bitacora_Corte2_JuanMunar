package co.edu.eci.dosw.restaurant.model.domain.enums;

/**
 * Categorías oficiales de la carta en Blue Velvet.
 */
public enum CategoriaBebida {
    COCTEL_AUTOR(true),
    DESTILADO_PREMIUM(true),
    MOCKTAIL(false),
    BOTANICO(false);

    private final boolean alcoholico;

    CategoriaBebida(boolean alcoholico) {
        this.alcoholico = alcoholico;
    }

    public boolean esAlcoholico() {
        return this.alcoholico;
    }
}