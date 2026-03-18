package com.fenix_sport.back.application.port.output;

import com.fenix_sport.back.domain.model.User;

public interface JwtTokenPort {
	String generateToken(User user);
}

