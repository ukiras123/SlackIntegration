package com.kiran.model.response;

import com.kiran.controller.dto.RetroDTO.RetroDTO;

import java.util.List;

/**
 * @author Kiran
 * @since 11/8/17
 */
public class ReadAllRetroResponse {

    private List<RetroDTO> retros;

    public ReadAllRetroResponse(List<RetroDTO> retros) {
        this.retros = retros;
    }

    public void setRetros(List<RetroDTO> retros) {
        this.retros = retros;
    }

    public List<RetroDTO> getRetros() {
        return retros;
    }

}
