package br.com.saviorodrigues.gestao_vagas.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

public class TestUtils {
    public static String objectToJSON(Object ob){
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return  mapper.writeValueAsString(ob);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateToken(UUID idCompany, String secret){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        //Caso as senhas sejam iguais, o token deve ser gerado!!
        var expiration = Instant.now().plus(Duration.ofHours(2));
        var token = JWT.create().withIssuer("javagas")
                .withExpiresAt(expiration)
                .withSubject(idCompany.toString())
                .withClaim("roles", Arrays.asList("COMPANY"))
                .sign(algorithm);
        return token;
    }


}
