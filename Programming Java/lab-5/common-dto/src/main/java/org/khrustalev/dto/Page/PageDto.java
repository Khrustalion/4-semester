package org.khrustalev.dto.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {
    private int page;
    private int size;
    private String sortBy;
    private boolean descending;
}
