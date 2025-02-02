package fr.fms.service;

import fr.fms.dao.RoleRepository;
import fr.fms.dao.UserRepository;
import fr.fms.entities.Role;
import fr.fms.entities.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public User saveUser(User user) {

        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving role: " + role);
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String role) {
        Role role1 = roleRepository.findByRole(role);
        User user = userRepository.findByEmail(email);
        user.getRoles().add(role1);
        log.info("Adding role: " + role);
    }

    @Override
    public User findUserByUsername(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}

