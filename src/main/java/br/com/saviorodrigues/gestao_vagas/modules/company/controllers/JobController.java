package br.com.saviorodrigues.gestao_vagas.modules.company.controllers;

import br.com.saviorodrigues.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.saviorodrigues.gestao_vagas.modules.company.entities.JobEntity;
import br.com.saviorodrigues.gestao_vagas.modules.company.useCases.CreateJobUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/company/job")
public class JobController {
    @Autowired

    private CreateJobUseCase createJobUseCase;

    @PostMapping("/")
    @PreAuthorize("hasRole('COMPANY')")
    public JobEntity create(@Valid @RequestBody CreateJobDTO createJobDTO, HttpServletRequest request) {

        // aqui está sendo recuperado o atributo company_id que está sendo passada no requisição
        var companyId = request.getAttribute("company_id");
        //convertendo string para UUID, para poder setar como fk
        var jobEntity = JobEntity.builder()
                .benefits(createJobDTO.getBenefits())
                .companyId(UUID.fromString(companyId.toString()))
                .description(createJobDTO.getDescription())
                .level(createJobDTO.getLevel())
                .build();
        return this.createJobUseCase.execute(jobEntity);
    }
}
