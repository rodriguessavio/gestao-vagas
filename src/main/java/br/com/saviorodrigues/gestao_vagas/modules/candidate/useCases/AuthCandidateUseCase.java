package br.com.saviorodrigues.gestao_vagas.modules.candidate.useCases;


import br.com.saviorodrigues.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.saviorodrigues.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.saviorodrigues.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public class AuthCandidateUseCase {
    @Value("${security.token.secret.candidate}")
    private String secretKey;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO) throws AuthenticationException {
        //verificar se o usuário existe
        var candidate = this.candidateRepository.findByUsername(authCandidateRequestDTO.username())
                .orElseThrow(() ->{
                    throw new UsernameNotFoundException("Username/password incorrect");
                });

        var passwordMatches = this.passwordEncoder.matches(authCandidateRequestDTO.password(), candidate.getPassword());

        if(!passwordMatches) {
            throw new AuthenticationException();
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiration = Instant.now().plus(Duration.ofHours(2));
        var token = JWT.create()
                .withIssuer("javagas")
                .withSubject(candidate.getId().toString())
                .withClaim("roles", Arrays.asList("CANDIDATE"))
                .withExpiresAt(expiration)
                .sign(algorithm);

        var authCandidateResponse = AuthCandidateResponseDTO.builder()
                .acess_token(token)
                .expiration(expiration.toEpochMilli())
                .build();

        return authCandidateResponse;
    }
}
