package fr.fms.service;

import fr.fms.dao.UserRepository;
import fr.fms.entities.Role;
import fr.fms.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUserDetailsService.class);

    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Tentative de chargement de l'utilisateur avec l'email: {}", email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.error("Utilisateur non trouvé avec l'email: {}", email);
            throw new UsernameNotFoundException("Utilisateur non trouvé: " + email);
        }

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(Role::getRole)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        logger.info("Utilisateur trouvé: {} avec rôles: {}", user.getEmail(), authorities);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
