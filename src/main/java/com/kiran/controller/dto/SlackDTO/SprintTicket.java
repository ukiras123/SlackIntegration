package com.kiran.controller.dto.SlackDTO;

/**
 * @author Kiran
 * @since 11/22/17
 */
public class SprintTicket implements Comparable<SprintTicket>{

    private String ticket;
    private String summary;
    private String assigneeName;
    private String status;
    private Integer order;

    public SprintTicket(String ticket, String summary, String assigneeName, String status) {
        this.ticket = ticket;
        this.summary = summary;
        this.assigneeName = assigneeName;
        this.status = status;
        if (status.equalsIgnoreCase("Done")) {
            this.order = 1;
        } else if (status.equalsIgnoreCase("In Qa")) {
            this.order = 2;
        }else if (status.equalsIgnoreCase("In Review")) {
            this.order = 3;
        }else if (status.equalsIgnoreCase("In Development")) {
            this.order = 4;
        } else if (status.equalsIgnoreCase("In Progress")) {
            this.order = 4;
        } else {
            this.order = 5;
        }
    }

    @Override
    public int compareTo(SprintTicket o) {
        int last = this.order.compareTo(o.order);
        if (last!=0) {
            return last;
        }
        last = this.status.compareTo(o.status);
        return last;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SprintTicket that = (SprintTicket) o;

        if (ticket != null ? !ticket.equals(that.ticket) : that.ticket != null) return false;
        if (summary != null ? !summary.equals(that.summary) : that.summary != null) return false;
        if (assigneeName != null ? !assigneeName.equals(that.assigneeName) : that.assigneeName != null) return false;
        return status != null ? status.equals(that.status) : that.status == null;
    }

    @Override
    public int hashCode() {
        int result = ticket != null ? ticket.hashCode() : 0;
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (assigneeName != null ? assigneeName.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
