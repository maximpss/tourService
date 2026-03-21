package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract sealed class TourService permits Flight, HotelStay, Excursion {
    private Integer id;
    private String name;
    private BigDecimal price;
    private LocalDate from;
    private LocalDate to;

    public TourService() {
    }

    public TourService(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.from = from;
        this.to = to;
    }

    public abstract BigDecimal calculateTotalPrice(int participants);

    public boolean isAvailableOn(LocalDate data) {
        return !(from.isAfter(data) || from.isBefore(data));
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getTo() {
        return to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
