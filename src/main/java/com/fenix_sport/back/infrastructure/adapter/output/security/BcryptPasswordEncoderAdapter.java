package com.fenix_sport.back.infrastructure.adapter.output.security;

import com.fenix_sport.back.application.port.output.PasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BcryptPasswordEncoderAdapter implements PasswordEncoderPort {
	private final PasswordEncoder delegate;

	public BcryptPasswordEncoderAdapter(PasswordEncoder delegate) {
		this.delegate = delegate;
	}

	@Override
	public String encode(String rawPassword) {
		return delegate.encode(rawPassword);
	}

	@Override
	public boolean matches(String rawPassword, String encodedPassword) {
		return delegate.matches(rawPassword, encodedPassword);
	}
}

