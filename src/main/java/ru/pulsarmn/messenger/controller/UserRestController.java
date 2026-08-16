package ru.pulsarmn.messenger.controller;

import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pulsarmn.messenger.dto.response.UserSearchResponse;
import ru.pulsarmn.messenger.service.UserService;


@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    ResponseEntity<Page<UserSearchResponse>> findUsersByName(
            @RequestParam @Size(min = 2) String query,
            @PageableDefault(size = 10, sort = "username") Pageable pageable) {
        Page<UserSearchResponse> usersPage = userService.findUsers(query, pageable);
        return ResponseEntity.ok(usersPage);
    }
}
