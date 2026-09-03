package com.projeto_final.receitas.service;
import com.projeto_final.receitas.exception.resourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.projeto_final.receitas.entity.Usuario;
import com.projeto_final.receitas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    @Autowired
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario create(Usuario obj) {
        return repository.save(obj);
    }

    public Usuario login(String email, String senha) {

        Optional<Usuario> usuario = repository.findByEmail(email);

        if (usuario.isPresent()) {

            if (usuario.get().getPassword().equals(senha)) {
                return usuario.get();
            }
        }

        return null;
    }

    public void delete(Long id) {

        repository.findById(id)
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Usuário não encontrado com ID: " + id));

        repository.deleteById(id);
    }

    public Usuario getId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Usuário não encontrado com ID: " + id));
    }

    public List<Usuario> getAll() {
        return repository.findAll();
    }

    public Usuario update(Usuario obj) {

        Usuario usuario = repository.findById(obj.getId())
                .orElseThrow(() ->
                        new resourceNotFoundException(
                                "Usuário não encontrado com ID: " + obj.getId()));

        updateUsuario(usuario, obj);

        return repository.save(usuario);
    }

    private void updateUsuario(Usuario usuario, Usuario obj) {
        usuario.setName(obj.getName());
    }
}
