package com.projeto_final.receitas.service;

import com.projeto_final.receitas.entity.Favorito;
import com.projeto_final.receitas.repository.*;
import jakarta.transaction.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class FavoritoService {

    @Autowired
    private favoritoRepository favoritoRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private receitaRepository receitaRepository;


    @Transactional
    public void desfavoritar (Long usuario_id , Long receita_id){
        if (!favoritoRepository.existsByUsuarioIdAndReceitaId(usuario_id , receita_id)) {
            throw new RuntimeException("Favorito não encontrado para remoção!");
        }

        favoritoRepository.deleteByUsuarioIdAndReceitaId(usuario_id , receita_id);
    }

    @Transactional(readOnly = true)
    public List<Favorito> listarFavoritoDoUsuario (Long usuario_id) {
        return favoritoRepository.findByUsuarioId(usuario_id);
    }
}
