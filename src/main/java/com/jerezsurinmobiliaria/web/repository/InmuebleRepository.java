package com.jerezsurinmobiliaria.web.repository;

import com.jerezsurinmobiliaria.web.dto.InmuebleListDTO;
import com.jerezsurinmobiliaria.web.model.Inmueble;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InmuebleRepository extends JpaRepository<Inmueble, Integer> {
    
    // ========================================================================
    // FILTRADO DINÁMICO CON MÚLTIPLES CRITERIOS (Sin cambios)
    // ========================================================================
    
    // ⭐ CONSULTA MODIFICADA PARA CONSTRUIR EL DTO DIRECTAMENTE
    @Query("SELECT new com.jerezsurinmobiliaria.web.dto.InmuebleListDTO(" +
           "i.id, i.direccion, i.tipoVivienda, i.tipoOperacion, i.precio, " +
           "i.numHab, i.metrosCuadrados, SIZE(i.propiedadesAdicionales)) " +
           "FROM Inmueble i WHERE " +
           "(:direccion IS NULL OR LOWER(i.direccion) LIKE LOWER(CONCAT('%', :direccion, '%'))) AND " +
           "(:tipoVivienda IS NULL OR i.tipoVivienda IN :tipoVivienda) AND " +
           "(:tipoOperacion IS NULL OR i.tipoOperacion IN :tipoOperacion) AND " +
           "(:precioMin IS NULL OR i.precio >= :precioMin) AND " +
           "(:precioMax IS NULL OR i.precio <= :precioMax) AND " +
           "(:numHabMin IS NULL OR i.numHab >= :numHabMin) AND " +
           "(:numHabMax IS NULL OR i.numHab <= :numHabMax) AND " +
           "(:metrosMin IS NULL OR i.metrosCuadrados >= :metrosMin) AND " +
           "(:metrosMax IS NULL OR i.metrosCuadrados <= :metrosMax) AND " +
           "(:comunidad IS NULL OR i.comunidad IN :comunidad) AND " +
           "(:estado IS NULL OR i.estado IN :estado) AND " +
           "(:valido IS NULL OR i.valido = :valido)")
    Page<InmuebleListDTO> buscarConFiltros( // ⭐ AHORA DEVUELVE Page<InmuebleListDTO>
        @Param("direccion") String direccion,
        @Param("tipoVivienda") List<String> tipoVivienda,
        @Param("tipoOperacion") List<Byte> tipoOperacion,
        @Param("precioMin") Double precioMin,
        @Param("precioMax") Double precioMax,
        @Param("numHabMin") Integer numHabMin,
        @Param("numHabMax") Integer numHabMax,
        @Param("metrosMin") Double metrosMin,
        @Param("metrosMax") Double metrosMax,
        @Param("comunidad") List<String> comunidad,
        @Param("estado") List<String> estado,
        @Param("valido") Byte valido,
        Pageable pageable
    );
    
    // ========================================================================
    // MÉTODOS PARA OBTENER VALORES ÚNICOS (Sin cambios)
    // ========================================================================
    
    @Query("SELECT DISTINCT i.tipoVivienda FROM Inmueble i ORDER BY i.tipoVivienda")
    List<String> findDistinctTipoVivienda();
    
    @Query("SELECT DISTINCT i.comunidad FROM Inmueble i ORDER BY i.comunidad")
    List<String> findDistinctComunidad();
    
    @Query("SELECT DISTINCT i.estado FROM Inmueble i ORDER BY i.estado")
    List<String> findDistinctEstado();
    
    @Query("SELECT MIN(i.precio), MAX(i.precio) FROM Inmueble i")
    Object[] obtenerRangoPrecio();
    
    // ========================================================================
    // CONTEO Y ESTADÍSTICAS
    // ========================================================================
    
    Long countByValido(Byte valido);
    Long countByTipoOperacion(Byte tipoOperacion);

    // Contar inmuebles por tipo de vivienda
    @Query("SELECT i.tipoVivienda, COUNT(i) FROM Inmueble i GROUP BY i.tipoVivienda")
    Integer countInmueblesByTipoVivienda();
    
    // Contar interesados por inmueble
    @Query("SELECT COUNT(int) FROM Inmueble i JOIN i.interesados int WHERE i.id = :inmuebleId")
    Long countInteresadosByInmuebleId(@Param("inmuebleId") Integer inmuebleId);

    // Obtener los 5 inmuebles con más interesados
    @Query(value = "SELECT i FROM Inmueble i LEFT JOIN i.interesados interesados GROUP BY i ORDER BY COUNT(interesados) DESC")
    List<Inmueble> findTop5ByOrderByInteresados();
    
    // ========================================================================
    // TOP INMUEBLES
    // ========================================================================
    
    List<Inmueble> findTop3ByValidoOrderByPrecioAsc(Byte valido);
}