package com.jerezsurinmobiliaria.web.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;

@Repository
public interface PropiedadAdicionalRepository extends JpaRepository<PropiedadAdicional, Integer> {
    
    // ========================================================================
    // BÚSQUEDAS BÁSICAS EXISTENTES
    // ========================================================================
    
    List<PropiedadAdicional> findByInmueble_Id(Integer inmuebleId);
    List<PropiedadAdicional> findByTipo(String tipo);
    List<PropiedadAdicional> findByInmueble_IdOrderByTipoAsc(Integer inmuebleId);
    List<PropiedadAdicional> findByDerramaLessThanEqual(Double maxDerrama);
    
    // ========================================================================
    // FILTRADO DINÁMICO CON MÚLTIPLES CRITERIOS
    // ========================================================================
    
    /**
     * Búsqueda dinámica con filtros opcionales
     */
    @Query("SELECT p FROM PropiedadAdicional p WHERE " +
           "(:tipo IS NULL OR p.tipo IN :tipo) AND " +
           "(:derramaMin IS NULL OR p.derrama >= :derramaMin) AND " +
           "(:derramaMax IS NULL OR p.derrama <= :derramaMax) AND " +
           "(:direccionInmueble IS NULL OR LOWER(p.inmueble.direccion) LIKE LOWER(CONCAT('%', :direccionInmueble, '%'))) AND " +
           "(:inmuebleId IS NULL OR p.inmueble.id = :inmuebleId)")
    Page<PropiedadAdicional> buscarConFiltros(
        @Param("tipo") List<String> tipo,
        @Param("derramaMin") Double derramaMin,
        @Param("derramaMax") Double derramaMax,
        @Param("direccionInmueble") String direccionInmueble,
        @Param("inmuebleId") Integer inmuebleId,
        Pageable pageable
    );
    
    // ========================================================================
    // MÉTODOS PARA OBTENER VALORES ÚNICOS (PARA FILTROS)
    // ========================================================================
    
    @Query("SELECT DISTINCT p.tipo FROM PropiedadAdicional p ORDER BY p.tipo")
    List<String> findDistinctTipo();
    
    @Query("SELECT MIN(p.derrama), MAX(p.derrama) FROM PropiedadAdicional p")
    Object[] obtenerRangoDerrama();
    
    // ========================================================================
    // MÉTODOS PARA BORRAR (REQUERIMIENTO II.2)
    // ========================================================================
    
    @Transactional
    @Modifying
    @Query("DELETE FROM PropiedadAdicional p WHERE p.inmueble.id = :inmuebleId")
    void deleteByInmuebleId(@Param("inmuebleId") Integer inmuebleId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM PropiedadAdicional p WHERE p.tipo = :tipo")
    void deleteByTipo(@Param("tipo") String tipo);
    
    @Transactional
    void deleteByDerramaGreaterThan(Double derrama);
    
    // ========================================================================
    // BÚSQUEDA CON FILTROS (REQUERIMIENTO II.2)
    // ========================================================================
    
    List<PropiedadAdicional> findByTipoAndDerramaLessThanEqual(String tipo, Double maxDerrama);
    
    // ========================================================================
    // CONSULTAS PERSONALIZADAS @Query (REQUERIMIENTO II.2)
    // ========================================================================
    
    @Query("SELECT p FROM PropiedadAdicional p WHERE p.derrama BETWEEN :min AND :max ORDER BY p.derrama ASC")
    List<PropiedadAdicional> findByRangoDerrama(@Param("min") Double min, @Param("max") Double max);
    
    @Query("SELECT SUM(p.derrama) FROM PropiedadAdicional p WHERE p.inmueble.id = :inmuebleId")
    Double calcularDerramaTotalPorInmueble(@Param("inmuebleId") Integer inmuebleId);
    
    @Query("SELECT p.tipo, COUNT(p) FROM PropiedadAdicional p GROUP BY p.tipo")
    List<Object[]> contarPorTipo();
    
    // ========================================================================
    // CONTEO
    // ========================================================================
    
    Long countByTipo(String tipo);
    Long countByInmueble_Id(Integer inmuebleId);
    
    // ========================================================================
    // ORDENACIÓN
    // ========================================================================
    
    List<PropiedadAdicional> findAllByOrderByDerramaAsc();
    List<PropiedadAdicional> findAllByOrderByDerramaDesc();
    List<PropiedadAdicional> findByTipoOrderByDerramaAsc(String tipo);
    
    // ========================================================================
    // AGREGACIÓN
    // ========================================================================
    
    List<PropiedadAdicional> findTop10ByOrderByDerramaDesc();
    PropiedadAdicional findFirstByOrderByDerramaDesc();
    PropiedadAdicional findFirstByOrderByDerramaAsc();
}