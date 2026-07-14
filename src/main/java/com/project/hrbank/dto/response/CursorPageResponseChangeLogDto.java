package com.project.hrbank.dto.response;

import java.util.List;

public record CursorPageResponseChangeLogDto(
        List<ChangeLogDto> content,
        String nextCursor,
        Long nextIdAfter,
        Long size,
        Long totalElements,
        Boolean hasNext
) {
}
