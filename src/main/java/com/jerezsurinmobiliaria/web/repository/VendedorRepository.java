package com.jerezsurinmobiliaria.web.repository;

import com.jerezsurinmobiliaria.web.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Integer> {
    
    Optional<Vendedor> findByNif(String nif);
    
    boolean existsByNif(String nif);
}