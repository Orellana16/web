package com.jerezsurinmobiliaria.web.repository;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InmuebleRepository extends JpaRepository<Inmueble, Integer> {
    
    // Búsquedas básicas
    List<Inmueble> findByValido(Byte valido);
    List<Inmueble> findByTipoOperacion(Byte tipo);
    List<Inmueble> findByTipoVivienda(String tipoVivienda);
    
    // Búsquedas combinadas útiles
    List<Inmueble> findByValidoAndTipoOperacion(Byte valido, Byte tipoOperacion);
    List<Inmueble> findByValidoAndComunidad(Byte valido, String comunidad);
    
    // Búsquedas por rango de precio
    List<Inmueble> findByPrecioBetween(Double min, Double max);
    List<Inmueble> findByPrecioLessThanEqual(Double maxPrecio);
    List<Inmueble> findByPrecioGreaterThanEqual(Double minPrecio);
}