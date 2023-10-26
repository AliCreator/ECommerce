package com.advance.provider;

import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import com.advance.entity.UserPrincipal;
import com.advance.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenProvider {

	private final UserRepository userRepo;

	private static final String AUTHORITIES = "AUTHORITIES";
	private static final String AUDIENCE = "Afghan Mart Users";
	private static final String ISSUER = "Afghan Mart Shop";

	@Value("${token.secret}")
	private String secret;

	public String generateToken(UserPrincipal user) {
		return JWT.create().withIssuer(ISSUER).withAudience(AUDIENCE).withIssuedAt(new Date())
				.withArrayClaim(AUTHORITIES, getClaimsFromUser(user))
				.withSubject(String.valueOf(user.getUser().getId()))
				.withExpiresAt(new Date(currentTimeMillis() + 84_600_000L)).sign(Algorithm.HMAC512(secret));
	}

	public List<GrantedAuthority> getAuthorities(String token) {
		String[] claims = getClaimsFromToken(token);
		return stream(claims).map(SimpleGrantedAuthority::new).collect(toList());
	}

	public Authentication getAuthentication(Long id, List<GrantedAuthority> authorities, HttpServletRequest request) {

		UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(
				userRepo.findById(id).get(), null, authorities);
		userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return userPassAuthToken;
	}

	public boolean isTokenValid(Long id, String token) {
		JWTVerifier verifier = getJWTVerifier();
		return !Objects.isNull(id) && !isTokenExpired(verifier, token);
	}

	public Long getSubjet(String token, HttpServletRequest requet) {
		try {
			return Long.valueOf(getJWTVerifier().verify(token).getSubject());
		} catch (TokenExpiredException e) {
			requet.setAttribute("expiredMessage", e.getMessage());
			throw e;
		} catch (InvalidClaimException e) {
			requet.setAttribute("invalidClaim", e.getMessage());
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	private boolean isTokenExpired(JWTVerifier verifier, String token) {
		Date expiration = verifier.verify(token).getExpiresAt();
		return expiration.before(new Date());
	}

	private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
		return userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
	}

	private String[] getClaimsFromToken(String token) {
		JWTVerifier verifier = getJWTVerifier();

		return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
	}

	private JWTVerifier getJWTVerifier() {
		JWTVerifier verifier;
		try {
			Algorithm algorithm = Algorithm.HMAC512(secret);
			verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
		} catch (JWTVerificationException e) {
			throw new JWTVerificationException("Token cannot be verified!");
		}
		return verifier;
	}

}