package ru.pulsarmn.messenger.dto.response;

import java.util.List;
import java.util.UUID;


public record CursorPageResponse<T>(
        List<T> items,
        UUID nextCursor,
        boolean hasNext
        ) {
}
