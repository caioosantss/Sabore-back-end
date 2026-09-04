package com.projeto_final.receitas.dto;

import com.projeto_final.receitas.entity.Usuario;

public record LoginResponse(String token, Usuario user) {
}
