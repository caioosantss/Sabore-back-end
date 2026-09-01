package com.projeto_final.receitas.repository;

public interface usuarioRepository {
    <T> ScopedValue<T> findById(Long usuarioId);
}
