package com.projeto_final.receitas.config;

import com.projeto_final.receitas.entity.Administrador;
import com.projeto_final.receitas.entity.Usuario;
import com.projeto_final.receitas.repository.AdministradorRepository;
import com.projeto_final.receitas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final AdministradorRepository administradorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private final String nome;
    private final String email;
    private final String senha;

    public AdminSeeder(
            AdministradorRepository administradorRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.name:Administrador}") String nome,
            @Value("${app.admin.email}") String email,
            @Value("${app.admin.password}") String senha) {

        this.administradorRepository = administradorRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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

        Optional<Usuario> existente = usuarioRepository.findByEmail(email);

        if (existente.isEmpty()) {
            Administrador admin = new Administrador();
            admin.setName(nome);
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(senha));

            administradorRepository.save(admin);

            System.out.println("[AdminSeeder] Administrador criado: " + email);
            return;
        }

        Usuario usuario = existente.get();

        if (administradorRepository.existsById(usuario.getId())) {
            System.out.println("[AdminSeeder] Ja e administrador: " + email);
            return;
        }

        administradorRepository.promover(usuario.getId());

        System.out.println(
                "[AdminSeeder] Conta existente promovida a administrador: " + email
                        + " (a senha continua sendo a que ela ja tinha, "
                        + "ADMIN_PASSWORD nao a substitui)");
    }
}