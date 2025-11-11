package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.model.Cita;
import com.jerezsurinmobiliaria.web.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {
    
    private final CitaRepository citaRepository;
    
    @Transactional(readOnly = true)
    public List<Cita> findAll() {
        return citaRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Cita findById(Integer id) {
        return citaRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public Cita save(Cita cita) {
        return citaRepository.save(cita);
    }
    
    @Transactional
    public void deleteById(Integer id) {
        citaRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Cita> findByInmuebleId(Integer inmuebleId) {
        return citaRepository.findByInmueble_IdOrderByFechaAscHoraAsc(inmuebleId);
    }
    
    @Transactional(readOnly = true)
    public List<Cita> findCitasFuturas(Integer inmuebleId) {
        return citaRepository.findCitasFuturasInmueble(inmuebleId, LocalDate.now());
    }
    
    @Transactional(readOnly = true)
    public Long countByInmuebleId(Integer inmuebleId) {
        return citaRepository.countByInmueble_Id(inmuebleId);
    }
}