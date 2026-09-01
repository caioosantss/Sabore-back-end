package com.projeto_final.receitas.controller;

import com.projeto_final.receitas.entity.Receita;
import com.projeto_final.receitas.service.receitasService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class receitaController {

    @Autowired
    private receitasService service;

}
