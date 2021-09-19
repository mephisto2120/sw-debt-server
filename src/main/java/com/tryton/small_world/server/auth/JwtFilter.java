package com.tryton.small_world.server.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends BasicAuthenticationFilter {

    private static final String BEARER = "Bearer ";
    private final UsernamePasswordAuthenticationCreator authenticationCreator;

    public JwtFilter(AuthenticationManager authenticationManager, UsernamePasswordAuthenticationCreator authenticationCreator) {
        super(authenticationManager);
        this.authenticationCreator = authenticationCreator;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null) {
            throw new RuntimeException("No authorization header attached!");
        }
        if (!header.startsWith(BEARER)) {
            throw new RuntimeException("Wrong authorization header format!");
        }

        String token = header.replace(BEARER, "");
        Authentication authResult = authenticationCreator.getAuthenticationByToken(token);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

//    private UsernamePasswordAuthenticationToken getAuthenticationByToken(String token) {
//        String secretKey = TextCodec.BASE64URL.encode("topSecret12#");
//        Jws<Claims> claimsJws = Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token);
//
//        String username = claimsJws.getBody().get("sub").toString();
//        String rolesStr = claimsJws.getBody().get("roles").toString();
//        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = getSimpleGrantedAuthorities(rolesStr);
//        return new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
//    }
//
//    private static Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities(String rolesStr) {
//        return Arrays.stream(rolesStr.split(","))
////                .map(roleName -> "ROLE_" + roleName)
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toSet());
//    }
}
