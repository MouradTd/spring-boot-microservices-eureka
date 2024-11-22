package com.example.userservice.Seeder;

import com.example.userservice.model.Role;
import com.example.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RoleSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            // Define initial roles
            Role adminRole = new Role();
            adminRole.setRole("admin");

            Role secRole = new Role();
            secRole.setRole("secrétaire");

            Role dentistRole = new Role();
            dentistRole.setRole("dentiste");

            Role infRole = new Role();
            infRole.setRole("infermiére");

            // Save roles to the database
            roleRepository.save(adminRole);
            roleRepository.save(secRole);
            roleRepository.save(dentistRole);
            roleRepository.save(infRole);
        }
    }
}