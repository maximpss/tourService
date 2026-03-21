package model;

import java.math.BigDecimal;
import java.time.LocalDate;
public final class Flight extends TourService {
    private String origin;
    private String destination;
    private String flightNumber;
    boolean baggageInclude;
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public boolean isBaggageInclude() {
        return baggageInclude;
    }

    public void setBaggageInclude(boolean baggageInclude) {
        this.baggageInclude = baggageInclude;
    }
    public Flight(Integer x, String name, BigDecimal price, LocalDate from, LocalDate to, String origin, String destination, String flightNumber, boolean baggageInclude) {
        super(x, name, price, from, to);
        this.origin = origin;
        this.destination = destination;
        this.flightNumber = flightNumber;
        this.baggageInclude = baggageInclude;
    }
    public Flight() {
        super();
        this.origin = null;
        this.destination = null;
        this.flightNumber = null;
        this.baggageInclude = false;
    }


    @Override
    public String toString() {
        return "User{Источник=\"" + origin + "\", Место назначения=\"" + destination + "\", Номер рейса=\"" + flightNumber + "\", Багаж, X=\"" + getId() + "\", Name=\"" + getName() + "\", From=\"" + getFrom() + "\", To=\"" + getTo() + "\", Price=\"" + getPrice() + "\"}";
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        var newPr = getPrice().multiply(BigDecimal.valueOf(participants));
        return baggageInclude ? newPr.multiply(new BigDecimal("1.3")) : newPr;
    }
}
