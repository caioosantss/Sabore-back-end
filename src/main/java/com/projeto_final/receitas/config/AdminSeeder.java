package com.projeto_final.receitas.config;

import com.projeto_final.receitas.entity.Administrador;
import com.projeto_final.receitas.repository.AdministradorRepository;
import com.projeto_final.receitas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * Cria o primeiro administrador na subida da aplicacao.
 *
 * Sem isso ninguem conseguiria cadastrar receitas: as rotas de escrita exigem
 * um admin, e a rota que cria admin tambem exige um admin — um impasse.
 * Roda so uma vez; se o e-mail ja existir, nao faz nada.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private final AdministradorRepository administradorRepository;
    private final UsuarioRepository usuarioRepository;

    private final String nome;
    private final String email;
    private final String senha;

    public AdminSeeder(
            AdministradorRepository administradorRepository,
            UsuarioRepository usuarioRepository,
            @Value("${app.admin.name:Administrador}") String nome,
            @Value("${app.admin.email:}") String email,
            @Value("${app.admin.password:}") String senha) {

        this.administradorRepository = administradorRepository;
        this.usuarioRepository = usuarioRepository;
        this.nome = nome;
        this.email = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        this.senha = senha == null ? "" : senha;
    }

    @Override
    @Transactional
    public void run(String... args) {

        if (email.isBlank() || senha.isBlank()) {
            System.out.println(
                    "[AdminSeeder] ADMIN_EMAIL/ADMIN_PASSWORD nao definidos; "
                            + "nenhum administrador foi criado.");
            return;
        }

        if (usuarioRepository.findByEmail(email).isPresent()) {
            System.out.println("[AdminSeeder] Administrador ja existe: " + email);
            return;
        }

        Administrador admin = new Administrador();
        admin.setName(nome);
        admin.setEmail(email);
        admin.setPassword(senha);

        administradorRepository.save(admin);

        System.out.println("[AdminSeeder] Administrador criado: " + email);
    }
}
