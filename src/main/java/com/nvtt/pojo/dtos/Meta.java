/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos;

/**
 *
 * @author vthan
 */
public class Meta {

    private int page;
    private int pageSize;
    private int pages;
    private long total;

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPages() {
        return pages;
    }

    public long getTotal() {
        return total;
    }

    public Meta(int page, int pageSize, int pages, long total) {
        this.page = page;
        this.pageSize = pageSize;
        this.pages = pages;
        this.total = total;
    }

    public Meta() {
    }

}
