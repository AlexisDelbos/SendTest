package fr.fms.dao;

import fr.fms.entities.Role;
import fr.fms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // List<Role> findByUserId(long userId);

    Role findByRole(String role);
}
