package com.sveabilar.api.features.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.sveabilar.api.features.user.dto.UserResponse;
import com.sveabilar.api.features.user.entity.User;
import com.sveabilar.api.features.user.exception.UserNotFoundException;
import com.sveabilar.api.features.user.mapper.UserMapper;
import com.sveabilar.api.features.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; 
    private final UserMapper userMapper; 

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Användaren hittades inte: " + email)); 

            return userMapper.toResponse(user);
        
    }
    
}
