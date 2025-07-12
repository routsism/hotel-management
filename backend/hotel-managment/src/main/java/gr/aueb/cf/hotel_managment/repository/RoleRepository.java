package gr.aueb.cf.hotel_managment.repository;

import gr.aueb.cf.hotel_managment.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}

