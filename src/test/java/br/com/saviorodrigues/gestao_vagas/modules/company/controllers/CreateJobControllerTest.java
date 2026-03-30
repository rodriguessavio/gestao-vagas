package br.com.saviorodrigues.gestao_vagas.modules.company.controllers;

import br.com.saviorodrigues.gestao_vagas.exceptions.CompanyNotFoundException;
import br.com.saviorodrigues.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.saviorodrigues.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.saviorodrigues.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.saviorodrigues.gestao_vagas.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static br.com.saviorodrigues.gestao_vagas.utils.TestUtils.objectToJSON;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateJobControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void should_be_able_to_create_a_new_job() throws Exception {

        var company = CompanyEntity.builder()
                        .description("COMPANY_DESCRIPTION")
                        .email("email@company.com")
                .password("1234567890")
                .username("COMPANY_USERNAME")
                .name("COMPANY_NAME")
                .build();

        company = companyRepository.saveAndFlush(company);

        var createJobDTO = CreateJobDTO.builder()
                        .benefits("BENEFITS_TEST")
                        .description("DESCRIPTION_TEST")
                        .level("LEVEL_TEST").build();

        var result = mockMvc.perform(
                MockMvcRequestBuilders.post("/company/job/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJSON(createJobDTO))
                        .header("Authorization", TestUtils.generateToken(company.getId(), "JAVAGAS@777")))
                        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shoud_not_be_able_to_create_a_new_job_if_company_not_found() throws Exception{
        var createJobDTO = CreateJobDTO.builder()
                .benefits("BENEFITS_TEST")
                .description("DESCRIPTION_TEST")
                .level("LEVEL_TEST").build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/company/job/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJSON(createJobDTO))
                        .header("Authorization", TestUtils.generateToken(UUID.randomUUID(), "JAVAGAS@777")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
