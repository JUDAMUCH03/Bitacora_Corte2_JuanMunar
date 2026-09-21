package co.edu.eci.dosw.restaurant.service.impl;

import co.edu.eci.dosw.restaurant.model.domain.Plato;
import co.edu.eci.dosw.restaurant.service.IPlatoService;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlatoServiceImpl implements IPlatoService {

    @Override
    public List<Plato> listar() {
        return Collections.emptyList();
    }
}
