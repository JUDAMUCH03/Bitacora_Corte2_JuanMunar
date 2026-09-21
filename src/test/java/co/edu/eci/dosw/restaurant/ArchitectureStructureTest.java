package co.edu.eci.dosw.restaurant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class ArchitectureStructureTest {

    @Test
    void shouldHaveExpectedPackageStructure() {
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.service.impl.PlatoServiceImpl"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.controller.docs.PlatoApi"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.dto.request.PlatoRequestDTO"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.dto.response.PlatoResponseDTO"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.mapper.in.PlatoMapper"));
        assertDoesNotThrow(() -> Class.forName("co.edu.eci.dosw.restaurant.mapper.out.PlatoMapper"));
    }
}
