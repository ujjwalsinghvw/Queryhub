package com.vwits.qaplatform.service;

import com.vwits.qaplatform.dto.RegistrationRequestDto;
import com.vwits.qaplatform.dto.ResponseUserDto;
import com.vwits.qaplatform.entity.User;
import com.vwits.qaplatform.exception.EntityNotFoundException;
import com.vwits.qaplatform.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    public ResponseUserDto getUserById(long id){
        User user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User Not Found"));
        ResponseUserDto userDto = new ResponseUserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User saveUser(RegistrationRequestDto user){
        User newUser = new User();
        BeanUtils.copyProperties(user, newUser);
        newUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(newUser);
    }

    public void updateUser(User user){
        userRepository.save(user);

    }


    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return findByEmail(username);
            }
        };
    }


}
