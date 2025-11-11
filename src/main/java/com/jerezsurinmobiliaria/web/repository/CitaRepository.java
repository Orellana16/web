package com.jerezsurinmobiliaria.web.repository;

import com.jerezsurinmobiliaria.web.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    
    List<Cita> findByInmueble_Id(Integer inmuebleId);
    
    List<Cita> findByInmueble_IdOrderByFechaAscHoraAsc(Integer inmuebleId);
    
    List<Cita> findByTrabajador_Id(Integer trabajadorId);
    
    List<Cita> findByInteresado_Id(Integer interesadoId);
    
    List<Cita> findByFecha(LocalDate fecha);
    
    @Query("SELECT c FROM Cita c WHERE c.inmueble.id = :inmuebleId AND c.fecha >= :fechaDesde ORDER BY c.fecha ASC, c.hora ASC")
    List<Cita> findCitasFuturasInmueble(@Param("inmuebleId") Integer inmuebleId, @Param("fechaDesde") LocalDate fechaDesde);
    
    Long countByInmueble_Id(Integer inmuebleId);
}