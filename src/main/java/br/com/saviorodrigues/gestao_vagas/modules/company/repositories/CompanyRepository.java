package br.com.saviorodrigues.gestao_vagas.modules.company.repositories;

import br.com.saviorodrigues.gestao_vagas.modules.company.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {
    Optional<CompanyEntity> findByUsernameOrCnpj(String username, String cnpj);
    Optional<CompanyEntity> findByUsername(String username);
}
