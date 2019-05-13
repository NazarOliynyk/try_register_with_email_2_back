package oktenweb.try_register_with_email_2_back.services.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import oktenweb.try_register_with_email_2_back.dao.UserDAO;
import oktenweb.try_register_with_email_2_back.models.ResponseTransfer;
import oktenweb.try_register_with_email_2_back.models.User;
import oktenweb.try_register_with_email_2_back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    MailServiceImpl mailServiceImpl;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean existsByEmail(String email) {
        return userDAO.existsByEmail(email);
    }

    @Override
    public ResponseTransfer save(User user) {

        if (userDAO.existsByUsername(user.getUsername())) {
            return new ResponseTransfer("User with such login already exists!!");
        } else if(userDAO.existsByEmail(user.getEmail())){
            return new ResponseTransfer("Field email is not unique!");
        }else {
            String responseFromMailSender =
                    mailServiceImpl.send(user.getEmail());
            if(responseFromMailSender.equals("Message was sent")){
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userDAO.save(user);
                return new ResponseTransfer("Preliminary Registration is completed. Take a look into your email");
            }else {
                return new ResponseTransfer(responseFromMailSender);
            }
        }
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public ResponseTransfer deleteById(int id){
        userDAO.deleteById(id);
        return new ResponseTransfer("User was deleted successfully");
    }

    public String verification(String jwt) {

        String email;

        try {
             email = Jwts.parser().
                    setSigningKey("yes".getBytes()).
                    parseClaimsJws(jwt).getBody().getSubject();

        }catch (MalformedJwtException e){
            System.out.println(e.toString());
            return "Verification failed";
        }
        User user = userDAO.findByEmail(email);

        if(user == null){
            return "Verification failed";
        }else {
            user.setEnabled(true);
            userDAO.save(user);
            return "Verification successful!";
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByUsername(username);
    }
}

