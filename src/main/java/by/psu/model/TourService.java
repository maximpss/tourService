package by.psu.model;

import by.psu.exception.TourServiceValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class TourService
{
    private Integer id;
    private String name;
    private BigDecimal price;
    private LocalDate from;
    private LocalDate to;

    public TourService()
    {}

    public TourService(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to)
    {
        this.id = id;
        this.name = name;
        this.price = price;

        // Валидация: дата окончания не может быть раньше даты начала
        if (from != null && to != null && from.isAfter(to)) {
            throw new TourServiceValidationException(
                    "from date cannot be after to date: from=" + from + ", to=" + to
            );
        }

        // Валидация: дата начала не может быть в прошлом
        if (from != null && from.isBefore(LocalDate.now())) {
            throw new TourServiceValidationException(
                    "from date cannot be in the past: " + from
            );
        }

        this.from = from;
        this.to = to;
    }

    public abstract BigDecimal calculateTotalPrice(int participants);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public boolean isAvailableOn(LocalDate date) {
        // Защита от null
        if (date == null || from == null || to == null) {
            return false;
        }
        return !(from.isAfter(date) || to.isBefore(date));
    }

    @Override
    public String toString()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return "TourService{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", from=" + (from != null ? from.format(formatter) : "null") +
                ", to=" + (to != null ? to.format(formatter) : "null") +
                '}';
    }
}