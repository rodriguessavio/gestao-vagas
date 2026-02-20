package br.com.saviorodrigues.gestao_vagas.modules.candidate.controllers;

import br.com.saviorodrigues.gestao_vagas.exceptions.UserFoundException;
import br.com.saviorodrigues.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.saviorodrigues.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.saviorodrigues.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import br.com.saviorodrigues.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.saviorodrigues.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import br.com.saviorodrigues.gestao_vagas.modules.candidate.useCases.ListAllJobsByFilterUseCase;
import br.com.saviorodrigues.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import br.com.saviorodrigues.gestao_vagas.modules.company.entities.JobEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Candidato", description = "Informações do Candidato")
@RestController
@RequestMapping("/candidate")
public class CandidateController {
    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private ProfileCandidateUseCase profileCandidateUseCase;

    @Autowired
    private ListAllJobsByFilterUseCase listAllJobsByFilterUseCase;

    @PostMapping("/")
    @Operation(summary = "Cadastro de Candidato", description = "Essa função é responsável por cadastrar as informações do candidato")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema = @Schema(implementation = AuthCandidateResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Usuário Já Existe")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidate ) {
        try{
            var result = this.createCandidateUseCase.execute(candidate);
            return ResponseEntity.ok().body(result);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Perfil do Candidato", description = "Essa função é responsável por buscar as informações do perfil candidato")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema = @Schema(implementation = ProfileCandidateResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "User Not Found")
    })
    @SecurityRequirement(name="jwt_auth")
    public ResponseEntity<Object> get(HttpServletRequest request) {

        var idCandidate = request.getParameter("idCandidate");
        try{
            var profile = this.profileCandidateUseCase.execute(UUID.fromString(idCandidate.toString()));
            return ResponseEntity.ok().body(profile);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/job")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Listagem de Vagas Disponíveis para o candidato", description = "Essa função é responsável por listar todas as vagas disponíveis, com base em um filtro")
    @ApiResponses({
                    @ApiResponse(responseCode = "200", content = {
                            @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = JobEntity.class))
                            )
                    })
            })
    @SecurityRequirement(name="jwt_auth")
    public List<JobEntity> findJobByFilter(@RequestParam String filter) {
        return this.listAllJobsByFilterUseCase.execute(filter);
    }

}
