package org.khrustalev.dto.Page;

import java.util.List;

public class PageResponseDto<T> {
    private List<T> objects;
    private int page;
    private int size;
    private int total;

    public PageResponseDto() {}

    public PageResponseDto(List<T> objects, int page, int size, int total) {
        this.objects = objects;
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public List<T> getObjects() {
        return objects;
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
