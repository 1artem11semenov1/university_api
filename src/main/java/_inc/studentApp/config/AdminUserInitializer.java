package _inc.studentApp.config;

import _inc.studentApp.model.User;
import _inc.studentApp.repository.UserRepository;
import _inc.studentApp.service.MyUserDetailsService;
import _inc.studentApp.service.UnivService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final MyUserDetailsService u_details;
    private final UnivService service;

    @Override
    public void run(String... args) throws Exception {
        if (!u_details.existsByUsername("INIT_ADMIN")){
            User initAdmin = new User();
            initAdmin.setUserName("INIT_ADMIN");
            initAdmin.setPassword("INIT_ADMIN");
            initAdmin.setEmail("TEST_EMAIL@gmail.com");
            initAdmin.setRole("ROLE_ADMIN");

            System.out.println(service.saveUser(initAdmin));
        }
    }
}
