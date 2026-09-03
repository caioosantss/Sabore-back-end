package com.projeto_final.receitas.dto;

import com.projeto_final.receitas.entity.Receita;

/**
 * Traduz a entidade Receita para os nomes de campo que o front consome
 * (types/recipe.ts). Serve tambem para nao serializar a lista
 * favoritadaPor, que causaria recursao infinita Receita -> Favorito.
 */
public record RecipeResponse(
        Long id,
        String title,
        String description,
        String imageUrl,
        Integer prepTime
) {
    public static RecipeResponse from(Receita receita) {
        return new RecipeResponse(
                receita.getId(),
                receita.getNome(),
                receita.getDesc(),
                receita.getImg(),
                receita.getTempo()
        );
    }
}
