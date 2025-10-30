package com.jerezsurinmobiliaria.web.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;

@Repository
public interface PropiedadAdicionalRepository extends JpaRepository<PropiedadAdicional, Integer> {
    
    List<PropiedadAdicional> findByInmueble(Inmueble inmueble);
    
    List<PropiedadAdicional> findByInmueble_Id(Integer inmuebleId);  // ← CORRECTO
    
    List<PropiedadAdicional> findByTipo(String tipo);
    
    List<PropiedadAdicional> findByInmueble_IdOrderByTipoAsc(Integer inmuebleId);  // ← CORRECTO
    
    List<PropiedadAdicional> findByDerramaLessThanEqual(Double maxDerrama);
}