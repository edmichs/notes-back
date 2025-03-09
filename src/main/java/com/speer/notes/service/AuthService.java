package com.speer.notes.service;

import com.speer.notes.dto.request.LoginRequest;
import com.speer.notes.dto.request.RegisterRequest;
import com.speer.notes.dto.response.LoginResponse;
import com.speer.notes.dto.response.MessageResponse;

public interface AuthService {
    LoginResponse authenticateUser(LoginRequest loginRequest);
    MessageResponse registerUser(RegisterRequest registerRequest);

}
