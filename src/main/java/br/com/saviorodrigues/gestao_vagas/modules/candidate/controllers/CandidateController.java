package br.com.saviorodrigues.gestao_vagas.modules.candidate.controllers;

import br.com.saviorodrigues.gestao_vagas.modules.candidate.CandidateEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/candidate")
public class CandidateController {


    @PostMapping("/")
    public void create(@RequestBody CandidateEntity candidate) {
        System.out.println("Candidato criado com sucesso");
        System.out.println(candidate.getEmail());
    }

}
