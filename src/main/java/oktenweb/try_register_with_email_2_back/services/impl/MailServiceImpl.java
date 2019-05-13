package oktenweb.try_register_with_email_2_back.services.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import oktenweb.try_register_with_email_2_back.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
@PropertySource("classpath:application.properties")
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    Environment env;

    public String send(String email) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        String jwtoken = Jwts.builder()
                .setSubject(email)
                .signWith(SignatureAlgorithm.HS512, "yes".getBytes())
                .setExpiration(new Date(System.currentTimeMillis() + 200000))
                .compact();

        System.out.println("jwtoken: "+jwtoken);
        try {
            mimeMessage.setFrom(new InternetAddress(env.getProperty("spring.mail.username")));
            helper.setTo(email);
            helper.setText("http://localhost:8080/verification/" + jwtoken, true);
            helper.setSubject("CONFIRMATION MESSAGE");
        } catch (MessagingException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
        javaMailSender.send(mimeMessage);
        return "Message was sent";}

}
