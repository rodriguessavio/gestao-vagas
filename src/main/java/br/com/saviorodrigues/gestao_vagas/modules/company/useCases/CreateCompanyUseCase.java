package br.com.saviorodrigues.gestao_vagas.modules.company.useCases;

import br.com.saviorodrigues.gestao_vagas.exceptions.CompanyFoundException;
import br.com.saviorodrigues.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.saviorodrigues.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateCompanyUseCase {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CompanyEntity execute(CompanyEntity companyEntity) {
        this.companyRepository
                .findByUsernameOrCnpj(companyEntity.getUsername(), companyEntity.getCnpj())
                .ifPresent((company) -> {
                    throw new CompanyFoundException();
                });

        var password = passwordEncoder.encode(companyEntity.getPassword());
        companyEntity.setPassword(password);

        return this.companyRepository.save(companyEntity);
    }
}
