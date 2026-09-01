package com.projeto_final.receitas.controller;

import com.projeto_final.receitas.entity.Favorito;
import com.projeto_final.receitas.service.favoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(Value = "/favoritos")
public class favoritoController {

    @Autowired
    private favoritoService favoritoService;

    @PostMapping(value = "/{usuario_id}/{receita_id}")
    public ResponseEntity<Favorito> favoritar(@PathVariable Long usuario_id, @PathVariable Long receita_id) {
       Favorito favorito = favoritoService.favoritar(usuario_id , receita_id);
       return ResponseEntity.status(HttpStatus.CREATED).body(favorito);
    }


    @DeleteMapping(value = "/{usuario_id}/{receita_id}")
    public ResponseEntity<Void> desfavoritar(@PathVariable Long usuario_id , @PathVariable Long receita_id ){
        favoritoService.desfavoritar( usuario_id , receita_id );
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{usuario_id}/{receita_id}")
    public ResponseEntity<List<Favorito>> listarFavoritoDoUsuario(@PathVariable Long usuario_id, @PathVariable Long receita_id) {
        List<Favorito> lista = favoritoService.listarFavoritoDoUsuario(usuario_id);
        return ResponseEntity.ok().body(lista);
    }

}
