package com.projeto_final.receitas.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.engine.internal.Cascade;

import java.util.*;

@Entity
@Table(name = "receita")
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", unique = true, nullable = false)
    private String nome;

    @Column(name = "descricao", nullable = false)
    private String desc;

    @Column(name = "tempo", nullable = false)
    private Integer tempo;

    @Column(name = "url", nullable = false)
    private String img;


    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorito> favoritadaPor = new ArrayList<>();

    public Receita(){}

    public List<Favorito> getFavoritadaPor() {
        return favoritadaPor;
    }
    public void setFavoritadaPor(ArrayList<Favorito> favoritadaPor) {
        this.favoritadaPor = favoritadaPor;
    }

}
