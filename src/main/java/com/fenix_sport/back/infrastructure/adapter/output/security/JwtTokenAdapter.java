package com.fenix_sport.back.infrastructure.adapter.output.security;

import com.fenix_sport.back.application.port.output.JwtTokenPort;
import com.fenix_sport.back.domain.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtTokenAdapter implements JwtTokenPort {
	private final SecretKey key;
	private final long expirationMinutes;

	public JwtTokenAdapter(String secret, long expirationMinutes) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMinutes = expirationMinutes;
	}

	@Override
	public String generateToken(User user) {
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(expirationMinutes * 60);

		var roles = user.roles() == null ? "" : user.roles().stream().map(r -> "ROLE_" + r.name().name()).collect(Collectors.joining(","));

		return Jwts.builder()
				.subject(user.email())
				.claims(Map.of(
						"uid", user.id(),
						"roles", roles
				))
				.issuedAt(Date.from(now))
				.expiration(Date.from(exp))
				.signWith(key)
				.compact();
	}
}

