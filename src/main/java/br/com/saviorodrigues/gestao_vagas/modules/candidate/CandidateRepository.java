package br.com.saviorodrigues.gestao_vagas.modules.candidate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CandidateRepository extends JpaRepository<CandidateEntity, UUID> {
//para validar a criação de usuário
    Optional<CandidateEntity> findByUsernameOrEmail(String username, String email);
    Optional<CandidateEntity> findByUsername(String username);
//    Optional permite utilizar o ifPresent, sem ele caso não houvesse dado correspondete a resposta seria apenas null
}
