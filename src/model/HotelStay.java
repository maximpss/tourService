package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class HotelStay extends TourService {

    private int stars;
    private int nights;
    private RoomType roomType;

    public HotelStay() {
    }

    public HotelStay(Integer x, String name, BigDecimal price, LocalDate from, LocalDate to, int stars, int nights, RoomType roomType) {
        super(x, name, price, from, to);
        this.stars = stars;
        this.nights = nights;
        this.roomType = roomType;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        var newPr = getPrice().multiply(BigDecimal.valueOf(participants));
        double hadN;
        double starsMulti = 1.0 + ((double) stars) / 10;
        hadN = switch (nights) {
            case 0 -> 1.0;
            case 1 -> 1.2;
            case 2 -> 1.4;
            case 3 -> 1.6;
            default -> 2.0;
        };
        return newPr.multiply(new BigDecimal(hadN)).multiply(BigDecimal.valueOf(starsMulti));
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getStars() {
        return stars;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void nights(int nights) {
        this.nights = nights;
    }

    public void setRoomType(RoomType room) {
        this.roomType = room;
    }

    @Override
    public String toString() {
        return "User{Сколько звезд=\"" + stars + "\", Сколько ночей=\"" + nights + "\", Тип комнаты=\""
                + (roomType != null ? roomType.name() : "null") + "\", X=\"" + getId() + "\", Name=\""
                + getName() + "\", From=\"" + getFrom() + "\", To=\"" + getTo() + "\", Price=\"" + getPrice() + "\"}";
    }
}
