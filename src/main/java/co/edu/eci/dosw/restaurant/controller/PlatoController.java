package co.edu.eci.dosw.restaurant.controller;

import co.edu.eci.dosw.restaurant.controller.docs.PlatoApi;
import co.edu.eci.dosw.restaurant.dto.response.PlatoResponseDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/platos")
public class PlatoController implements PlatoApi {

    @Override
    public ResponseEntity<List<PlatoResponseDTO>> listar() {
        return ResponseEntity.ok(List.of());
    }
}
