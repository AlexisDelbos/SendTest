package fr.fms.dao;

import fr.fms.entities.Role;
import fr.fms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // List<Role> findByUserId(long userId);

    Role findByRole(String role);
}
