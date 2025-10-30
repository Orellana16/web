package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.repository.InmuebleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InmuebleService {
    
    private final InmuebleRepository inmuebleRepository;
    
    /**
     * Obtener todos los inmuebles
     */
    @Transactional(readOnly = true)
    public List<Inmueble> findAll() {
        return inmuebleRepository.findAll();
    }
    
    /**
     * Buscar inmueble por ID
     * @return Inmueble o null si no existe
     */
    @Transactional(readOnly = true)
    public Inmueble findById(Integer id) {
        return inmuebleRepository.findById(id).orElse(null);
    }
    
    /**
     * Guardar o actualizar inmueble
     */
    @Transactional
    public Inmueble save(Inmueble inmueble) {
        return inmuebleRepository.save(inmueble);
    }
    
    /**
     * Eliminar inmueble por ID
     */
    @Transactional
    public void deleteById(Integer id) {
        inmuebleRepository.deleteById(id);
    }
    
    // ===============================================
    // MÉTODOS ADICIONALES ÚTILES
    // ===============================================
    
    /**
     * Obtener solo inmuebles válidos
     */
    @Transactional(readOnly = true)
    public List<Inmueble> findAllValidos() {
        return inmuebleRepository.findByValido((byte) 1);
    }
    
    /**
     * Buscar por tipo de operación
     * @param tipo 0=Venta, 1=Alquiler
     */
    @Transactional(readOnly = true)
    public List<Inmueble> findByTipoOperacion(Byte tipo) {
        return inmuebleRepository.findByTipoOperacion(tipo);
    }
    
    /**
     * Verificar si existe un inmueble
     */
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return inmuebleRepository.existsById(id);
    }
    
    /**
     * Contar total de inmuebles
     */
    @Transactional(readOnly = true)
    public long count() {
        return inmuebleRepository.count();
    }
    
    /**
     * Soft delete (marcar como no válido en lugar de eliminar)
     */
    @Transactional
    public void marcarComoNoValido(Integer id) {
        Inmueble inmueble = findById(id);
        if (inmueble != null) {
            inmueble.setValido((byte) 0);
            save(inmueble);
        }
    }
}