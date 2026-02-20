package br.com.saviorodrigues.gestao_vagas.modules.candidate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCandidateResponseDTO {
    @Schema(example = "Desenvolvedor Java")
    private String description;
    @Schema(example = "desenvolvedorjava@email.com")
    private String email;
    @Schema(example = "Fernando")
    private String username;
    private UUID id;
    @Schema(example = "Fernando de Souza")
    private String name;

}
