package com.projeto_final.receitas.repository;

import com.projeto_final.receitas.entity.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface receitaRepository extends JpaRepository<Receita, Long> {
};