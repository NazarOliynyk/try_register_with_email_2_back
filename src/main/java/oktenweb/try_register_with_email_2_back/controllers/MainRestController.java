package oktenweb.try_register_with_email_2_back.controllers;

import oktenweb.try_register_with_email_2_back.models.ResponseTransfer;
import oktenweb.try_register_with_email_2_back.models.User;
import oktenweb.try_register_with_email_2_back.services.impl.MailServiceImpl;
import oktenweb.try_register_with_email_2_back.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainRestController {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    MailServiceImpl mailServiceImpl;

    String responseOnConfirmPass = "";

    @CrossOrigin(origins = "*")
    @PostMapping("/saveUser")
    public ResponseTransfer saveUser(@RequestBody User user){

        return userServiceImpl.save(user);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/verification/{jwt}")
    public String verification(@PathVariable String jwt){

        return userServiceImpl.verification(jwt);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getUsers")
    public List<User> getUsers(){
        return userServiceImpl.findAll();
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseTransfer deleteUser(@PathVariable("id") int id) {

        return userServiceImpl.deleteById(id);
    }

}
