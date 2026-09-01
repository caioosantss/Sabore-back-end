package com.projeto_final.receitas.service;

import com.projeto_final.receitas.entity.Receita;
import com.projeto_final.receitas.repository.receitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class receitasService {
    @Autowired
    private receitaRepository repository;

    public Receita create(Receita obj){
        return repository.save(obj);
    }
    public void delete(Receita obj){
        repository.delete(obj);
    }
    public List<Receita> getall(){
        return repository.findAll();
    }

    public Receita update(Receita obj) {

        Receita newObj = repository.findById(obj.getId())
                .orElseThrow(() -> new RuntimeException("nao encontrado"));

        updateReceita(newObj, obj);

        return repository.save(newObj);
    }

    private void updateReceita(Receita newObj, Receita obj) {

        if (obj.getNome() != null) {
            newObj.setNome(obj.getNome());
        }

        if (obj.getDesc() != null) {
            newObj.setDesc(obj.getDesc());
        }

        if (obj.getTempo() != null) {
            newObj.setTempo(obj.getTempo());
        }

        if (obj.getImg() != null) {
            newObj.setImg(obj.getImg());
        }
    }
}