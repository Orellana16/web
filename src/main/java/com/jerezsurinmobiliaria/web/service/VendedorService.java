package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.model.Vendedor;
import com.jerezsurinmobiliaria.web.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendedorService {
    
    private final VendedorRepository vendedorRepository;
    
    @Transactional(readOnly = true)
    public List<Vendedor> findAll() {
        return vendedorRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Vendedor findById(Integer id) {
        return vendedorRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public Vendedor save(Vendedor vendedor) {
        return vendedorRepository.save(vendedor);
    }
    
    @Transactional
    public void deleteById(Integer id) {
        vendedorRepository.deleteById(id);
    }
}