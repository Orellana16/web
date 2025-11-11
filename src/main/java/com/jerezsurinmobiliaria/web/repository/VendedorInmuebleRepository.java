package com.jerezsurinmobiliaria.web.repository;

import com.jerezsurinmobiliaria.web.model.VendedorInmueble;
import com.jerezsurinmobiliaria.web.model.VendedorInmuebleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendedorInmuebleRepository extends JpaRepository<VendedorInmueble, VendedorInmuebleId> {
    
    List<VendedorInmueble> findByInmueble_Id(Integer inmuebleId);
    
    List<VendedorInmueble> findByVendedor_Id(Integer vendedorId);
    
    @Query("SELECT vi FROM VendedorInmueble vi JOIN FETCH vi.vendedor WHERE vi.inmueble.id = :inmuebleId")
    List<VendedorInmueble> findByInmuebleIdWithVendedor(@Param("inmuebleId") Integer inmuebleId);
    
    Long countByInmueble_Id(Integer inmuebleId);
}