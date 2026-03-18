package com.fenix_sport.back.application.port.output;

public interface PasswordEncoderPort {
	String encode(String rawPassword);

	boolean matches(String rawPassword, String encodedPassword);
}

