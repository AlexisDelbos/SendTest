package fr.fms.web;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SecurityController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public SecurityController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            System.out.println("Tentative de connexion pour l'email: " + email);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            Instant instant = Instant.now();

            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(10, ChronoUnit.MINUTES))
                    .subject(email)
                    .claim("scope", scope)
                    .build();

            JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSet);

            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

            return Map.of("access-token", jwt);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "Invalid email or password");
        }
    }

    // Endpoint pour récupérer les infos de l'utilisateur authentifié via son token
    @GetMapping("/infos")
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }
}
