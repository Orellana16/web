package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.model.VendedorInmueble;
import com.jerezsurinmobiliaria.web.repository.VendedorInmuebleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendedorInmuebleService {
    
    private final VendedorInmuebleRepository vendedorInmuebleRepository;
    
    @Transactional(readOnly = true)
    public List<VendedorInmueble> findByInmuebleId(Integer inmuebleId) {
        return vendedorInmuebleRepository.findByInmuebleIdWithVendedor(inmuebleId);
    }
    
    @Transactional(readOnly = true)
    public Long countByInmuebleId(Integer inmuebleId) {
        return vendedorInmuebleRepository.countByInmueble_Id(inmuebleId);
    }
    
    @Transactional
    public VendedorInmueble save(VendedorInmueble vendedorInmueble) {
        return vendedorInmuebleRepository.save(vendedorInmueble);
    }
}