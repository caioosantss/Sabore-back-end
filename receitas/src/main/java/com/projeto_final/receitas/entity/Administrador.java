package com.projeto_final.receitas.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Setter
@Getter
@Entity
@Table(name = "tb_administrador")
public class Administrador extends Usuario implements Serializable{
    private String token;
}
