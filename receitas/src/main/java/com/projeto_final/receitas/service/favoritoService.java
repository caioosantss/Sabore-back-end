package com.projeto_final.receitas.service;

import com.projeto_final.receitas.entity.Favorito;
import com.projeto_final.receitas.entity.Receita;
import com.projeto_final.receitas.entity.Usuario;
import com.projeto_final.receitas.exception.businessException;
import com.projeto_final.receitas.exception.resourceNotFoundException;
import com.projeto_final.receitas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final receitaRepository receitaRepository;

    @Autowired
    public FavoritoService(
            FavoritoRepository favoritoRepository,
            UsuarioRepository usuarioRepository,
            receitaRepository receitaRepository) {

        this.favoritoRepository = favoritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.receitaRepository = receitaRepository;
    }

    @Transactional
    public Favorito favoritar(Long usuario_id, Long receita_id) {

        Usuario usuario = usuarioRepository.findById(usuario_id)
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Usuario não encontrado com ID: " + usuario_id));

        Receita receita = receitaRepository.findById(receita_id)
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Receita não encontrada com o ID: " + receita_id));

        if (favoritoRepository.existsByUsuarioIdAndReceitaId(
                usuario_id, receita_id)) {

            throw new businessException(
                    "Esta receita já está nos favoritos do usuário");
        }

        Favorito favorito = new Favorito(usuario, receita);

        return favoritoRepository.save(favorito);
    }

    @Transactional
    public void desfavoritar(Long usuario_id, Long receita_id) {

        if (!favoritoRepository.existsByUsuarioIdAndReceitaId(
                usuario_id, receita_id)) {

            throw new resourceNotFoundException(
                    "Favorito não encontrado para remoção!");
        }

        favoritoRepository.deleteByUsuarioIdAndReceitaId(
                usuario_id, receita_id);
    }

    @Transactional(readOnly = true)
    public List<Favorito> listarFavoritoDoUsuario(Long usuario_id) {

        usuarioRepository.findById(usuario_id)
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Usuario não encontrado com ID: " + usuario_id));

        return favoritoRepository.findByUsuarioId(usuario_id);
    }
}
