package com.khebra.whatsappclone.interceptor;

import com.khebra.whatsappclone.mappers.UserMapper;
import com.khebra.whatsappclone.models.User;
import com.khebra.whatsappclone.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizer {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public void synchronizeWithIdp(Jwt token) {
      log.info("Synchronizing user with idp");
      getUserEmail(token).ifPresent(userEmail ->
      {
          log.info("Synch user having email {}",userEmail);
          Optional<User> optUser=userRepository.findByEmail(userEmail);
          User user=userMapper.fromTokenAttribute(token.getClaims());
          optUser.ifPresent(val -> user.setId(optUser.get().getId()));
          userRepository.save(user);
      });
    }
    private Optional<String> getUserEmail(Jwt token){
        Map<String,Object> attributes=token.getClaims();
        if (attributes.containsKey("email"))
            return Optional.of(attributes.get("email").toString());

        return Optional.empty();
    }
}
