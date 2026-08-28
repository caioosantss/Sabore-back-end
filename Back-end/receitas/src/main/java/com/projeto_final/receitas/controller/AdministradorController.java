package com.projeto_final.receitas.controller;

import com.example.demo.entity.Administrador;
import com.example.demo.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/administrador")
public class AdministradorController {
    @Autowired

    private AdministradorService service;

    @PostMapping
    public ResponseEntity<Administrador> create(@RequestBody Administrador obj) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(obj));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Administrador> getId(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.getId(id));
    }

    @GetMapping
    public ResponseEntity<List<Administrador>> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }


    @PutMapping(value = "/{id}")

    public ResponseEntity<Administrador> update(@PathVariable Long id, @RequestBody Administrador obj) {
        obj.setId(id);
        return ResponseEntity.ok().body(service.update(obj));
    }
}