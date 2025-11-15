package _inc.studentApp.service;

import _inc.studentApp.Impl.MyUserDetails;
import _inc.studentApp.model.User;
import _inc.studentApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository u_repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = u_repository.findByUserName(username);
        return user.map(MyUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException(username + " not found."));
    }

    public boolean existsByUsername(String username) {
        Optional<User> user = u_repository.findByUserName(username);
        return user.isPresent();
    }
}
