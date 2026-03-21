package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Booking {
    // Поля
    private final String bookingId;
    private final Client client;
    private final Map<TourService, Integer> serviceParticipants;
    private final LocalDateTime bookingDate;
    private BookingStatus status;

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    // Конструктор с валидацией
    public Booking(Client client, Map<TourService, Integer> serviceParticipants, String bookingId, Client client1, Map<TourService, Integer> serviceParticipants1, LocalDateTime bookingDate) {
        // реализация
        this.bookingId = bookingId;
        this.client = client1;
        this.serviceParticipants = serviceParticipants1;
        this.bookingDate = bookingDate;
    }

    // Генерация ID: BK + timestamp + 4 случайные цифры
    private String generateBookingId(LocalDateTime timestamp) {
        String timestampStr = timestamp.format(TIMESTAMP_FORMATTER);
        int randomSuffix = (int) (Math.random() * 10000);
        return String.format("BK%s%04d", timestampStr, randomSuffix);
    }

    // Getters
    public String getBookingId() { return bookingId; }
    public Client getClient() { return client; }
    public Map<TourService, Integer> getServiceParticipants() {
        return new HashMap<>(serviceParticipants);
    }
    public LocalDateTime getBookingDate() { return bookingDate; }
    public BookingStatus getStatus() { return status; }

    // Методы управления услугами
    public void addService(TourService service, int participants) { /* ... */ }
    public void removeService(TourService service) { /* ... */ }
    public void updateParticipants(TourService service, int newParticipants) { /* ... */ }

    // Вычисление итоговой суммы
    public double calculateTotalAmount() { /* ... */
        return 0;
    }

    // Методы перехода между статусами
    public void confirm() { /* ... */ }
    public void complete() { /* ... */ }
    public void cancel() { /* ... */ }
}
