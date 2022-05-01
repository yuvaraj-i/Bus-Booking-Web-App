package com.app.BookingApp.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Orders {
    @Id
    private Long id;
    private Long userId;
    private Long ticketId;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTicketId() {
        return this.ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

}
