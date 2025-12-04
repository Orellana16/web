package com.jerezsurinmobiliaria.web.repository;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.model.Vendedor;
import com.jerezsurinmobiliaria.web.model.VendedorInmueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendedorInmuebleRepository extends JpaRepository<VendedorInmueble, Long> {
    
    List<VendedorInmueble> findByInmueble(Inmueble inmueble);
    
    List<VendedorInmueble> findByVendedor(Vendedor vendedor);
    
    @Query("SELECT vi FROM VendedorInmueble vi JOIN FETCH vi.vendedor WHERE vi.inmueble.id = :inmuebleId")
    List<VendedorInmueble> findByInmuebleIdWithVendedor(@Param("inmuebleId") Integer inmuebleId);
    
    Long countByInmueble(Inmueble inmueble);
}