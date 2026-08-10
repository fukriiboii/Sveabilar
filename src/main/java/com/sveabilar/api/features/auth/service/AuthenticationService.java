package com.sveabilar.api.features.auth.service;

import com.sveabilar.api.features.auth.dto.LoginRequest;
import com.sveabilar.api.features.auth.dto.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request); 

}
