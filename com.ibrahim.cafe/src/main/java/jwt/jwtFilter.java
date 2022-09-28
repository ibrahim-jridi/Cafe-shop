package jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

@Component
public class jwtFilter extends OncePerRequestFilter{
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private CustomerUserDetailsService CustomerUserDetailsService; 
	
	Claims claims = null;
	private String username = null;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup")) {
			filterChain.doFilter(request, response);
		}else {
			String authorizationHeader = request.getHeader("Authorization");
			String token = null;
			if (authorizationHeader != null &&authorizationHeader.startsWith("Bearer ")) {
				token = authorizationHeader.substring(7);
				username = jwtUtil.getUserName(token);
				claims = jwtUtil.getClaims(token);
			}
			
			if (username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails userDetails = CustomerUserDetailsService.loadUserByUsername(username);
				if (jwtUtil.tokenValidation(token, userDetails)) {
					UsernamePasswordAuthenticationToken UsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken( userDetails,null,userDetails.getAuthorities());
					UsernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationToken);
				}
			}
		}
		filterChain.doFilter(request, response);

	}
	public boolean isAdmin() {
		return "admin".equalsIgnoreCase((String)claims.get("role") );
	}
	
	public boolean isUser() {
		return "user".equalsIgnoreCase((String)claims.get("role") );
	}
	
	public String getCurrentUser() {
		return username;
	}
}
