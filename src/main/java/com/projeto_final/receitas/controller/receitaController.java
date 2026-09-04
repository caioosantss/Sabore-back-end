package com.projeto_final.receitas.controller;

import com.projeto_final.receitas.dto.RecipeResponse;
import com.projeto_final.receitas.entity.Receita;
import com.projeto_final.receitas.service.receitasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
// import com.projeto_final.receitas.service.AdministradorService;

@RestController
@RequestMapping("/recipes")
public class receitaController {

    @Autowired
    private receitasService service;

    // O front chama GET /recipes na home. O parametro "filter" ainda nao
    // e aplicado, mas e aceito para nao quebrar a chamada existente.
    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getAll(
            @RequestParam(value = "filter", required = false) String filter
    ) {
        List<RecipeResponse> receitas = service.getall()
                .stream()
                .map(RecipeResponse::from)
                .toList();

        return ResponseEntity.ok(receitas);
    }

    @PostMapping
    public ResponseEntity<Receita> create(
            @RequestParam("nome") String nome,
            @RequestParam("desc") String desc,
            @RequestParam("tempo") Integer tempo,
            @RequestParam("imagem") MultipartFile imagem
    ) {

        Receita obj = new Receita();

        obj.setNome(nome);
        obj.setDesc(desc);
        obj.setTempo(tempo);

        // Próximo passo:
        // enviar "imagem" para o Supabase Storage
        // e obter a URL.

        // obj.setImg(url);

        Receita receitaSalva = service.create(obj);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(receitaSalva);
    }
}
