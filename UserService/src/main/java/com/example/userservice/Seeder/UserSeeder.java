// src/main/java/com/example/userservice/Seeder/UserSeeder.java
package com.example.userservice.Seeder;

import com.example.userservice.model.ModelHasRoles;
import com.example.userservice.model.Role;
import com.example.userservice.model.Users;
import com.example.userservice.repository.ModelHasRolesRepository;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class UserSeeder implements CommandLineRunner {

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelHasRolesRepository modelHasRolesRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if users already exist
        if (userRepository.count() == 0) {
            // Fetch roles from the database
            Role adminRole = roleRepository.findByRole("admin");
            Role secRole = roleRepository.findByRole("secrétaire");
            Role dentistRole = roleRepository.findByRole("dentiste");
            Role infRole = roleRepository.findByRole("infermiére");

            // Ensure roles are not null
            if (adminRole == null || secRole == null || dentistRole == null || infRole == null) {
                throw new RuntimeException("Roles not found in the database");
            }

            // Define initial users
            Users adminUser = new Users();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword("adminpassword");
            adminUser.setPoste("Administrator");

            Users secUser = new Users();
            secUser.setFirstName("Sec");
            secUser.setLastName("User");
            secUser.setEmail("sec@example.com");
            secUser.setPassword("secpassword");
            secUser.setPoste("Secretary");

            Users dentistUser = new Users();
            dentistUser.setFirstName("Dentist");
            dentistUser.setLastName("User");
            dentistUser.setEmail("dentist@example.com");
            dentistUser.setPassword("dentistpassword");
            dentistUser.setPoste("Dentist");

            Users infUser = new Users();
            infUser.setFirstName("Inf");
            infUser.setLastName("User");
            infUser.setEmail("inf@example.com");
            infUser.setPassword("infpassword");
            infUser.setPoste("Nurse");

            // Save users to the database
            userRepository.save(adminUser);
            userRepository.save(secUser);
            userRepository.save(dentistUser);
            userRepository.save(infUser);

            // Create ModelHasRoles entries
            modelHasRolesRepository.save(new ModelHasRoles(null, adminUser, adminRole));
            modelHasRolesRepository.save(new ModelHasRoles(null, secUser, secRole));
            modelHasRolesRepository.save(new ModelHasRoles(null, dentistUser, dentistRole));
            modelHasRolesRepository.save(new ModelHasRoles(null, infUser, infRole));
        }
    }
}