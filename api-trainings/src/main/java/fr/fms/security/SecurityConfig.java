package fr.fms.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import fr.fms.service.DatabaseUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private DatabaseUserDetailsService userDetailsService;


//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        PasswordEncoder passwordEncoder = passwordEncoder();
//        return new InMemoryUserDetailsManager(
//                User.withUsername("aymen").password(passwordEncoder.encode("12345")).authorities("USER").build(),
//                User.withUsername("elbab@gmail.com").password(passwordEncoder.encode("12345")).authorities("ADMIN").build()
//        );
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Désactiver l'authentification basée sur les sessions
                .csrf(csrf -> csrf.disable()) // Désactiver la protection CSRF
                .cors(Customizer.withDefaults()) // Configuration CORS par défaut
                .authorizeHttpRequests(ahr -> ahr.requestMatchers(HttpMethod.POST, "/login/**").permitAll()) // Autoriser les requêtes POST vers /login pour l'authentification
                .authorizeHttpRequests(ahr -> ahr.anyRequest().authenticated()) // Toutes les autres requêtes nécessitent une authentification
                .oauth2ResourceServer(ors -> ors.jwt(Customizer.withDefaults())) // Configurer la gestion des tokens JWT
                .build();
    }

    @Bean
    JwtEncoder jwtEncoder() throws IOException, URISyntaxException {
        Path path = Paths.get(getClass().getClassLoader().getResource("jwt-secret.txt").toURI());
        String secretKey = Files.readString(path); // Lire la clé secrète pour signer les tokens
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
    }

    @Bean
    JwtDecoder jwtDecoder() throws IOException, URISyntaxException {
        Path path = Paths.get(getClass().getClassLoader().getResource("jwt-secret.txt").toURI());
        String secretKey = Files.readString(path); // Lire la clé secrète pour vérifier les tokens
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        System.out.println("AuthenticationManager est en train d'utiliser UserDetailsServiceImpl !");
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }
}
