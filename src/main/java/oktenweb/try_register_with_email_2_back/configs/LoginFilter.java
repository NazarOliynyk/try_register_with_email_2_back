package oktenweb.try_register_with_email_2_back.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import oktenweb.try_register_with_email_2_back.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;


public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private User user;
    private UserDetailsService userDetailsService;

    public LoginFilter(String url, AuthenticationManager authManager, UserDetailsService userDetailsService) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        this.userDetailsService = userDetailsService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        user = new ObjectMapper()
                .readValue(httpServletRequest.getInputStream(), User.class);

        System.out.println(user);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        // Collections.emptyList()  // it did not work
                        Collections.<GrantedAuthority>emptyList()
                )
        );


    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {


        String jwtoken = Jwts.builder()
                .setSubject(auth.getName()) // this auth goes from the previous method
                // returned from that method
                .signWith(SignatureAlgorithm.HS512, "yes".getBytes())
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .compact();

        res.addHeader("Authorization", "Bearer " + jwtoken);
        User userLogged= (User) userDetailsService.loadUserByUsername(auth.getName());
        System.out.println(userLogged.toString());
        res.addHeader("UserClass", String.valueOf(userLogged.getClass()));
        res.addHeader("UserLogged", new ObjectMapper().writeValueAsString(userLogged));
    }
}
