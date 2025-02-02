package fr.fms.service;

import fr.fms.entities.Role;
import fr.fms.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String email, String role);

    User findUserByUsername(String email);

    ResponseEntity<List<User>> listUsers();
}
