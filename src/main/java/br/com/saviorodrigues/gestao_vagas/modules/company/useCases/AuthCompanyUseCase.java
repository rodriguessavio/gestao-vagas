package br.com.saviorodrigues.gestao_vagas.modules.company.useCases;

import javax.naming.AuthenticationException;

import br.com.saviorodrigues.gestao_vagas.exceptions.CompanyFoundException;
import br.com.saviorodrigues.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.saviorodrigues.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.saviorodrigues.gestao_vagas.modules.company.repositories.CompanyRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthCompanyUseCase {
    @Value("${security.token.secret}")
    private String secretKey;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
    //verificar se a empresa existe
        var company = companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(
                () -> {
                    throw new UsernameNotFoundException("Company not found");
                }
        );

        System.out.println(company);
    //verificar a senha
        //primeiro passa-se a senha que não está criptografada, e na segunda passa-se a senha do banco de dados
        var passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        if(!passwordMatches) {
            throw new AuthenticationException();
        }
        //Definição do algoritmo
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        //Caso as senhas sejam iguais, o token deve ser gerado!!
        var expiration = Instant.now().plus(Duration.ofHours(2));
        var token = JWT.create().withIssuer("javagas")
                .withExpiresAt(expiration)
                .withSubject(company.getId().toString())
                .withClaim("roles", Arrays.asList("COMPANY"))
                .sign(algorithm);
        var authCompanyResponseDTO = AuthCompanyResponseDTO.builder()
                .acess_token(token)
                .expiration(expiration.toEpochMilli())
                .build();
        return authCompanyResponseDTO;
    }
}
