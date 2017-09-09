package com.kiran.model.response;

import com.kiran.controller.dto.UserLogDTO.UserLogDTO;

import java.util.List;

/**
 * @author Kiran
 * @since 9/5/17
 */
public class ReadAllLogResponse {

    private Integer totalLogNumbers;
    private List<UserLogDTO> details;


    public ReadAllLogResponse(Integer totalLogNumbers, List<UserLogDTO> details) {
        this.totalLogNumbers = totalLogNumbers;
        this.details = details;
    }

    public Integer getTotalLogNumbers() {
        return totalLogNumbers;
    }

    public void setTotalLogNumbers(Integer totalLogNumbers) {
        this.totalLogNumbers = totalLogNumbers;
    }

    public List<UserLogDTO> getDetails() {
        return details;
    }

    public void setDetails(List<UserLogDTO> details) {
        this.details = details;
    }
}
