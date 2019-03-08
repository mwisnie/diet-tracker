package mw.project.diettracker.security.filter;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import mw.project.diettracker.security.UserPrincipal;
import mw.project.diettracker.security.WebSecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class CredentialsAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        // TODO: encoding of data in request

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username != null && password != null) {

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());

            return authenticationManager.authenticate(authenticationToken);

        } else {

            // TODO: proper exception
            throw new RuntimeException("Could not read login data from request.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) {

        // on successful authentication create JWT and send it in Authorization header

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        String authorityAsString = principal.getAuthorities().stream().findFirst().map((authority) -> (authority).getAuthority()).get();
        String usernameAndAuthority = principal.getUsername() + "." + authorityAsString;

        String jwtToken = Jwts.builder()
                .setSubject(usernameAndAuthority)
                .setExpiration(new Date(System.currentTimeMillis() + WebSecurityConstants.TOKEN_EXPIRATION_TIME))
                .signWith(WebSecurityConstants.SIGNATURE_ALGORITHM, WebSecurityConstants.JWT_SECRET.getBytes())
                .compact();

        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader(WebSecurityConstants.AUTHORIZATION_HEADER, WebSecurityConstants.AUTHORIZATION_PREFIX + jwtToken);
    }

}
