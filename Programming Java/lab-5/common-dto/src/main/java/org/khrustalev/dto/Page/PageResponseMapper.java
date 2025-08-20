package org.khrustalev.dto.Page;

import org.springframework.data.domain.Page;

public class PageResponseMapper {
    public static <T> PageResponseDto<T> map(Page<T> page) {
        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                (int) page.getTotalElements() // если точно не больше Integer.MAX_VALUE
        );
    }
}