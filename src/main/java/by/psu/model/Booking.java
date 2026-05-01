package by.psu.model;

import by.psu.exception.TourServiceValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Booking {
    private final String bookingId;
    private final Client client;
    private final Map<TourService, Integer> serviceParticipants;
    private final LocalDate bookingDate;
    private BookingStatus status;

    public enum BookingStatus {
        PENDING, CONFIRMED, COMPLETED, CANCELLED
    }

    public Booking(Client client, Map<TourService, Integer> serviceParticipants) {
        if (client == null) {
            throw new TourServiceValidationException("client cannot be null");
        }
        if (serviceParticipants == null) {
            throw new TourServiceValidationException("serviceParticipants cannot be null");
        }
        if (serviceParticipants.isEmpty()) {
            throw new TourServiceValidationException("serviceParticipants cannot be empty");
        }

        LocalDate today = LocalDate.now();

        for (Map.Entry<TourService, Integer> entry : serviceParticipants.entrySet()) {
            TourService service = entry.getKey();
            Integer participants = entry.getValue();

            if (service == null) {
                throw new TourServiceValidationException("Service cannot be null");
            }

            if (participants == null || participants <= 0) {
                throw new TourServiceValidationException("Participants count must be positive for service: " +
                        service.getName());
            }

            if (!service.isAvailableOn(today)) {
                throw new TourServiceValidationException("Service " + service.getName() + " is not available today");
            }

            if (service instanceof HotelStay hotelStay) {
                RoomType roomType = hotelStay.getRoomType();
                int maxParticipants = getMaxParticipantsForRoomType(roomType);

                if (participants > maxParticipants) {
                    throw new TourServiceValidationException(
                            "For HotelStay with room type " + roomType +
                                    ", maximum participants is " + maxParticipants +
                                    ", but got " + participants
                    );
                }
            }
        }

        this.client = client;
        this.serviceParticipants = new HashMap<>(serviceParticipants);
        this.bookingDate = today;
        this.status = BookingStatus.PENDING;
        this.bookingId = generateBookingId();
    }

    // ИСПРАВЛЕНО: теперь поддерживаются все типы номеров
    private int getMaxParticipantsForRoomType(RoomType roomType) {
        return switch (roomType) {
            case SINGLE -> 1;
            case DOUBLE -> 2;
            case TWIN -> 2;      // ДОБАВЛЕНО: двухместный номер с раздельными кроватями
            case SUITE -> 3;     // ДОБАВЛЕНО: люкс — до 3 человек
            case FAMILY -> 4;
        };
    }

    private String generateBookingId() {
        return "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public String getBookingId() {
        return bookingId;
    }

    public Client getClient() {
        return client;
    }

    public Map<TourService, Integer> getServiceParticipants() {
        return new HashMap<>(serviceParticipants);
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    // НОВЫЙ МЕТОД: возвращает общее количество участников бронирования
    public int getTotalParticipants() {
        return serviceParticipants.values().stream().mapToInt(Integer::intValue).sum();
    }

    // НОВЫЙ МЕТОД: безопасная проверка наличия услуги (без исключений)
    public boolean hasService(TourService service) {
        return service != null && serviceParticipants.containsKey(service);
    }

    // НОВЫЙ МЕТОД: очистить все услуги из бронирования
    public void removeAllServices() {
        serviceParticipants.clear();
    }

    public void addService(TourService service, int participants) {
        if (service == null) {
            throw new TourServiceValidationException("Service cannot be null");
        }

        if (participants <= 0) {
            throw new TourServiceValidationException("Participants count must be positive");
        }

        if (!service.isAvailableOn(LocalDate.now())) {
            throw new TourServiceValidationException("Service " + service.getName() + " is not available today");
        }

        if (service instanceof HotelStay hotelStay) {
            RoomType roomType = hotelStay.getRoomType();
            int maxParticipants = getMaxParticipantsForRoomType(roomType);

            if (participants > maxParticipants) {
                throw new TourServiceValidationException(
                        "For HotelStay with room type " + roomType +
                                ", maximum participants is " + maxParticipants +
                                ", but got " + participants
                );
            }
        }

        serviceParticipants.put(service, participants);
    }

    public void removeService(TourService service) {
        if (service == null) {
            throw new TourServiceValidationException("Service cannot be null");
        }

        if (!serviceParticipants.containsKey(service)) {
            throw new TourServiceValidationException("Service " + service.getName() + " not found in booking");
        }

        serviceParticipants.remove(service);
    }

    public void updateParticipants(TourService service, int participants) {
        if (service == null) {
            throw new TourServiceValidationException("Service cannot be null");
        }

        if (participants <= 0) {
            throw new TourServiceValidationException("Participants count must be positive");
        }

        if (!serviceParticipants.containsKey(service)) {
            throw new TourServiceValidationException("Service " + service.getName() + " not found in booking");
        }

        if (service instanceof HotelStay hotelStay) {
            RoomType roomType = hotelStay.getRoomType();
            int maxParticipants = getMaxParticipantsForRoomType(roomType);

            if (participants > maxParticipants) {
                throw new TourServiceValidationException(
                        "For HotelStay with room type " + roomType +
                                ", maximum participants is " + maxParticipants +
                                ", but got " + participants
                );
            }
        }

        serviceParticipants.put(service, participants);
    }

    public BigDecimal calculateTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<TourService, Integer> entry : serviceParticipants.entrySet()) {
            TourService service = entry.getKey();
            int participants = entry.getValue();

            BigDecimal servicePrice = service.calculateTotalPrice(participants);
            total = total.add(servicePrice);
        }

        BigDecimal discountRate = client.getDiscountRate();
        BigDecimal discount = total.multiply(discountRate);

        return total.subtract(discount);
    }

    public void confirm() {
        if (status != BookingStatus.PENDING) {
            throw new TourServiceValidationException(
                    "Cannot confirm booking: current status is " + status +
                            ", expected PENDING"
            );
        }
        status = BookingStatus.CONFIRMED;
    }

    public void complete() {
        if (status != BookingStatus.CONFIRMED) {
            throw new TourServiceValidationException(
                    "Cannot complete booking: current status is " + status +
                            ", expected CONFIRMED"
            );
        }

        BigDecimal totalPrice = calculateTotalPrice();
        int pointsToAdd = totalPrice.multiply(new BigDecimal("0.1")).intValue();
        client.addLoyaltyPoints(pointsToAdd);

        status = BookingStatus.COMPLETED;
    }

    public void cancel() {
        if (status != BookingStatus.PENDING && status != BookingStatus.CONFIRMED) {
            throw new TourServiceValidationException(
                    "Cannot cancel booking: current status is " + status +
                            ", expected PENDING or CONFIRMED"
            );
        }
        status = BookingStatus.CANCELLED;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Booking{\n");
        sb.append("  bookingId='").append(bookingId).append("'\n");
        sb.append("  client=").append(client.getFullName()).append("\n");
        sb.append("  bookingDate=").append(bookingDate).append("\n");
        sb.append("  status=").append(status).append("\n");
        sb.append("  services:\n");

        for (Map.Entry<TourService, Integer> entry : serviceParticipants.entrySet()) {
            sb.append("    - ").append(entry.getKey().getName())
                    .append(": ").append(entry.getValue()).append(" participants\n");
        }

        sb.append("  totalPrice=").append(calculateTotalPrice()).append("\n");
        sb.append("  totalParticipants=").append(getTotalParticipants()).append("\n");  // ДОБАВЛЕНО
        sb.append("}");

        return sb.toString();
    }
}