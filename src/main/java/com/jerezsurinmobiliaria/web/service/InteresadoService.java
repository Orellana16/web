package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.model.Interesado;
import com.jerezsurinmobiliaria.web.repository.InteresadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InteresadoService {
    
    private final InteresadoRepository interesadoRepository;
    
    @Transactional(readOnly = true)
    public List<Interesado> findAll() {
        return interesadoRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Interesado findById(Integer id) {
        return interesadoRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public Interesado save(Interesado interesado) {
        return interesadoRepository.save(interesado);
    }
    
    @Transactional
    public void deleteById(Integer id) {
        interesadoRepository.deleteById(id);
    }
}