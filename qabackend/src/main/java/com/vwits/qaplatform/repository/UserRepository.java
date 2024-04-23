package com.vwits.qaplatform.repository;

import com.vwits.qaplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
