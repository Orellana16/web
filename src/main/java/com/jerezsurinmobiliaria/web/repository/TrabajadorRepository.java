package com.jerezsurinmobiliaria.web.repository;

import com.jerezsurinmobiliaria.web.model.Trabajador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer> {
    
    Optional<Trabajador> findByNif(String nif);
    
    boolean existsByNif(String nif);
}