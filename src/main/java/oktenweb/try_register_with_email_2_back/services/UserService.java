package oktenweb.try_register_with_email_2_back.services;

import oktenweb.try_register_with_email_2_back.models.ResponseTransfer;
import oktenweb.try_register_with_email_2_back.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    ResponseTransfer save(User user);

    boolean existsByEmail(String email);

    List<User> findAll();

    ResponseTransfer deleteById(int id);
}
