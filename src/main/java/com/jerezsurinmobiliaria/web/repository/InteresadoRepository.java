package com.jerezsurinmobiliaria.web.repository;

import com.jerezsurinmobiliaria.web.model.Interesado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InteresadoRepository extends JpaRepository<Interesado, Integer> {
    
    Optional<Interesado> findByNif(String nif);
    
    boolean existsByNif(String nif);
}