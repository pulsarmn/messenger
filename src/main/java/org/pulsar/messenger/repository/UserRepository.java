package org.pulsar.messenger.repository;


import org.pulsar.messenger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);
}
