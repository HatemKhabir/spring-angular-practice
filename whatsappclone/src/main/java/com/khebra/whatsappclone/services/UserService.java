package com.khebra.whatsappclone.services;

import com.khebra.whatsappclone.mappers.UserMapper;
import com.khebra.whatsappclone.repositories.UserRepository;
import com.khebra.whatsappclone.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public List<UserResponse> getAllUsersExceptSelf(Authentication authentication){
    return userRepository.findAll().stream().filter((user)-> !user.getId().equals(authentication.getName())).map(userMapper::toUserResponse).toList();


    }

}
