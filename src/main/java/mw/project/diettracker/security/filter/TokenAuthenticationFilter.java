package mw.project.diettracker.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import mw.project.diettracker.entity.Authority;
import mw.project.diettracker.security.WebSecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String authHeader = request.getHeader(WebSecurityConstants.AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(WebSecurityConstants.AUTHORIZATION_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authToken = getAuthTokenFromRequest(request);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        chain.doFilter(request, response);
    }


    private UsernamePasswordAuthenticationToken getAuthTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(WebSecurityConstants.AUTHORIZATION_HEADER);

        if (authHeader != null) {
            Claims jwtClaims = Jwts.parser().setSigningKey(WebSecurityConstants.JWT_SECRET.getBytes())
                                    .parseClaimsJws(authHeader.replace(WebSecurityConstants.AUTHORIZATION_PREFIX, ""))
                                    .getBody();

            // TODO: check token expiration / throw exception
//            Date expiration = jwtClaims.getExpiration();
//            if ( ) {
//
//            }

            String subject = jwtClaims.getSubject();

            if (subject != null) {
                String[] usernameAndAuthority = subject.split("\\.");
                Authority authority = new Authority(usernameAndAuthority[0], usernameAndAuthority[1]);

                return new UsernamePasswordAuthenticationToken(usernameAndAuthority[0], null, Arrays.asList(authority));
            }
        }

        return null;
    }

}
