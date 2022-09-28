package jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
	
	private String secretCode = "ibrahim";
	
	public String getUserName(String token) {
		return getClaims(token,Claims::getSubject);
	}
	
	public Date getExpiration(String token) {
		return getClaims(token,Claims::getExpiration);
	}
	public <T> T getClaims(String token,Function<Claims,T> claimResolve) {
		final Claims claims = getClaims(token);
		return claimResolve.apply(claims);
	}
	public Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(secretCode).parseClaimsJws(token).getBody()
;	}
	
	private boolean isTokenExpired(String token) {
		return getExpiration(token).before(new Date());
	}
	
	public String generateToken(String username , String role) {
		Map<String,Object> claims = new HashMap<>();
		claims.put("role",role );
		return createToken(claims,username);
	}
	
	public String createToken(Map<String,Object> claims,String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() +1000 *60 *60*10))
				.signWith(SignatureAlgorithm.HS256,secretCode).compact();
	}
	
	public boolean tokenValidation (String token, UserDetails userDetails) {
		final String username = getUserName(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token)
;		
	}
}
