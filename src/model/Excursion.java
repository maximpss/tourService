package model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;

public final class Excursion extends TourService {
    String where;
    int day;

    @Override
    public String toString() {
        return "User{Где экскурсия=\"" + where + "\", Сколько дней экскурсия=\"" + day + "\", X=\"" + getId() + "\", Name=\"" + getName() + "\", From=\"" + getFrom() + "\", To=\"" + getTo() + "\", Price=\"" + getPrice() + "\"}";
    }
    public String getWhere() {
        return where;
    }

    public int getDay() {
        return day;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setDay(int day) {
        this.day = day;
    }
    public Excursion(Integer x, String name, BigDecimal price, LocalDate from, LocalDate to, String where, int day) {
        super(x, name, price, from, to);
        this.where = where;
        this.day = day;
    }

    public Excursion() {
        super();
        this.where = null;
        this.day = 0;
    }
    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        var discount = getPrice().multiply(BigDecimal.valueOf(participants));
        return participants > 10
                ? discount.subtract(discount.divide(BigDecimal.valueOf(10), MathContext.DECIMAL128))
                : discount;
    }
}
