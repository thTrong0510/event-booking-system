/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos;

/**
 *
 * @author vthan
 */
public class ResultPaginationDTO {

    private Meta meta;
    private Object result;

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Meta getMeta() {
        return meta;
    }

    public Object getResult() {
        return result;
    }

    public ResultPaginationDTO(Meta meta, Object result) {
        this.meta = meta;
        this.result = result;
    }

    public ResultPaginationDTO() {
    }

}
