package br.com.saviorodrigues.gestao_vagas.modules.company.controllers;

import br.com.saviorodrigues.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.saviorodrigues.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.saviorodrigues.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.saviorodrigues.gestao_vagas.modules.company.useCases.AuthCompanyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@Tag(name = "Autenticação Empresa", description = "Informações de Autenticação da Empresa")
@RestController
@RequestMapping("/company")
public class AuthCompanyController {

    @Autowired
    private AuthCompanyUseCase authCompanyUseCase;

    @PostMapping("/auth")
    @Operation(summary = "Autenticação de Empresas", description = "Essa função é responsável por autenticar uma empresa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema = @Schema(implementation = AuthCompanyResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Company not found")
    })
    public ResponseEntity<Object> create (@RequestBody  AuthCompanyDTO authCompanyDTO){
        try{
            var result = authCompanyUseCase.execute(authCompanyDTO);
            return ResponseEntity.ok().body(result);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
