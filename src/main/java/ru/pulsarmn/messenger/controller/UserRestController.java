package ru.pulsarmn.messenger.controller;

import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pulsarmn.messenger.dto.request.BirthdateUpdateRequest;
import ru.pulsarmn.messenger.dto.request.DisplayNameUpdateRequest;
import ru.pulsarmn.messenger.dto.request.UsernameUpdateRequest;
import ru.pulsarmn.messenger.dto.response.UserProfileResponse;
import ru.pulsarmn.messenger.dto.response.UserSearchResponse;
import ru.pulsarmn.messenger.security.UserPrincipal;
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

    @GetMapping("/me")
    ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserProfileResponse response = userService.getUserProfile(userPrincipal.getUserId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/username")
    ResponseEntity<UserProfileResponse> updateUsername(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                       @Validated @RequestBody UsernameUpdateRequest request) {
        UserProfileResponse response = userService.updateUsername(userPrincipal.getUserId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/name")
    ResponseEntity<UserProfileResponse> updateName(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @Validated @RequestBody DisplayNameUpdateRequest request) {
        UserProfileResponse response = userService.updateDisplayName(userPrincipal.getUserId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/birthdate")
    ResponseEntity<UserProfileResponse> updateBirthdate(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                        @Validated @RequestBody BirthdateUpdateRequest request) {
        UserProfileResponse response = userService.updateBirthdate(userPrincipal.getUserId(), request);
        return ResponseEntity.ok(response);
    }
}
