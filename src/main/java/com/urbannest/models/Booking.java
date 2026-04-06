package com.urbannest.models;

public class Booking {
    private int id;
    private String customerName;
    private String customerContact;
    private String roomNumber;
    private String bookingDate;

    public Booking() {}

    public Booking(int id, String customerName, String customerContact, String roomNumber, String bookingDate) {
        this.id = id;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.roomNumber = roomNumber;
        this.bookingDate = bookingDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerContact() { return customerContact; }
    public void setCustomerContact(String customerContact) { this.customerContact = customerContact; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
}
