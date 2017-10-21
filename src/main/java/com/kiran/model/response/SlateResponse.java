package com.kiran.model.response;

/**
 * @author Kiran
 * @since 10/20/17
 */
public class SlateResponse {
    private String report;

    public SlateResponse(String report) {
        this.report = report;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
