package com.projeto_final.receitas.repository;

import com.projeto_final.receitas.entity.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface receitaRepository extends JpaRepository<Receita, Long> {
    Optional<Receita> findByNome(String nome);
}
