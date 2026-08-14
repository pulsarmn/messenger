package ru.pulsarmn.messenger.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.pulsarmn.messenger.dto.response.UserSearchResponse;
import ru.pulsarmn.messenger.mapper.UserMapper;
import ru.pulsarmn.messenger.repository.UserRepository;


@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public Page<UserSearchResponse> findUsers(String query, Pageable pageable) {
        return userRepository.searchUsers(query, pageable)
                .map(userMapper::mapToSearchResponse);
    }
}
