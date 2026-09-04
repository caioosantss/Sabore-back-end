package com.projeto_final.receitas.service;

import java.util.List;
import com.projeto_final.receitas.entity.Administrador;
import com.projeto_final.receitas.exception.resourceNotFoundException;
import com.projeto_final.receitas.repository.AdministradorRepository;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {

    private final AdministradorRepository repository;

    public AdministradorService(AdministradorRepository repository) {
        this.repository = repository;
    }

    public Administrador create(Administrador obj) {
        return repository.save(obj);
    }

    public void delete(Long id) {

        repository.findById(id)
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Administrador não encontrado com ID: " + id));

        repository.deleteById(id);
    }

    public Administrador getId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Administrador não encontrado com ID: " + id));
    }

    public List<Administrador> getAll() {
        return repository.findAll();
    }

    public Administrador update(Administrador obj) {

        Administrador administrador = repository.findById(obj.getId())
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Administrador não encontrado com ID: " + obj.getId()));

        updateAdministrador(administrador, obj);

        return repository.save(administrador);
    }

    private void updateAdministrador(
            Administrador administrador,
            Administrador obj) {

        administrador.setName(obj.getName());
        administrador.setToken(obj.getToken());
        administrador.setEmail(obj.getEmail());
        administrador.setPassword(obj.getPassword());
    }
}
