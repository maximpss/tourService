Изменения :

1.Client.java — исправление логики скидок
Проблема: Условия проверялись от меньшего к большему, из-за чего при 600 баллах возвращалось 5% вместо 10%.
Что изменено: Порядок условий в методе getDiscountRate() изменён с возрастающего на убывающий.
Зачем: Чтобы клиенты с большим количеством баллов получали бóльшую скидку (прогрессивная шкала).

Было :
      public BigDecimal getDiscountRate() {
    if (loyaltyPoints >= 100) {
        return new BigDecimal("0.05");
    } else if (loyaltyPoints >= 500) {
        return new BigDecimal("0.1");
    } // ...
}

Стало :

       public BigDecimal getDiscountRate() {
    if (loyaltyPoints >= 5000) {
        return new BigDecimal("0.2");
    } else if (loyaltyPoints >= 1000) {
        return new BigDecimal("0.15");
    } else if (loyaltyPoints >= 500) {
        return new BigDecimal("0.1");
    } else if (loyaltyPoints >= 100) {
        return new BigDecimal("0.05");
    } else {
        return BigDecimal.ZERO;
    }
}


2.TourService.java — валидация дат в конструкторе
Проблема: Можно было создать услугу с датой окончания раньше даты начала или с датой в прошлом.
Что изменено: В конструктор добавлены проверки дат.
Зачем: Предотвращение создания некорректных услуг и раннее обнаружение ошибок.

Было:
public TourService(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.from = from;
    this.to = to;
}

Стало:

public TourService(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to) {
    this.id = id;
    this.name = name;
    this.price = price;
    
    if (from != null && to != null && from.isAfter(to)) {
        throw new TourServiceValidationException(
            "from date cannot be after to date: from=" + from + ", to=" + to
        );
    }
    
    if (from != null && from.isBefore(LocalDate.now())) {
        throw new TourServiceValidationException(
            "from date cannot be in the past: " + from
        );
    }
    
    this.from = from;
    this.to = to;
}



3.TourService.java — защита от NullPointerException в isAvailableOn()
Проблема: Если from, to или параметр date равен null, метод падал с NPE.
Что изменено: Добавлена проверка на null с безопасным возвратом false.
Зачем: Предотвращение падения программы при неполных данных об услуге.

Было:
public boolean isAvailableOn(LocalDate date) {
    return !(from.isAfter(date) || to.isBefore(date));
}

Стало:
public boolean isAvailableOn(LocalDate date) {
    if (date == null || from == null || to == null) {
        return false;
    }
    return !(from.isAfter(date) || to.isBefore(date));
}



4.Booking.java — исправление getMaxParticipantsForRoomType()
Проблема: Метод поддерживал только типы SINGLE, DOUBLE, FAMILY, а для TWIN и SUITE выбрасывал исключение.
Что изменено: Добавлены недостающие типы номеров.
Зачем: Полная поддержка всех типов номеров, объявленных в RoomType.java.

Было:
private int getMaxParticipantsForRoomType(RoomType roomType) {
    return switch (roomType) {
        case SINGLE -> 1;
        case DOUBLE -> 2;
        case FAMILY -> 4;
        default -> throw new TourServiceValidationException(...);
    };
}


Стало:
private int getMaxParticipantsForRoomType(RoomType roomType) {
    return switch (roomType) {
        case SINGLE -> 1;
        case DOUBLE -> 2;
        case TWIN -> 2;
        case SUITE -> 3;
        case FAMILY -> 4;
    };
}




5. Booking.java — добавлен метод getTotalParticipants()
Что добавлено:


public int getTotalParticipants() {
    return serviceParticipants.values().stream().mapToInt(Integer::intValue).sum();
}

Зачем: Удобное получение общего количества участников бронирования без ручного суммирования.

Booking.java — добавлен метод hasService():

public boolean hasService(TourService service) {
    return service != null && serviceParticipants.containsKey(service);
}

Зачем: Безопасная проверка наличия услуги в бронировании без необходимости ловить исключения.



Booking.java — добавлен метод removeAllServices():

public void removeAllServices() {
    serviceParticipants.clear();
}

Зачем: Быстрая очистка всех услуг из бронирования (например, когда клиент хочет пересмотреть весь набор услуг).

Booking.java — улучшен метод toString()
Что изменено: Добавлен вывод общего количества участников.
Зачем: Повышение информативности вывода бронирования.

Было:
sb.append("  totalPrice=").append(calculateTotalPrice()).append("\n");
sb.append("}");

Стало:
sb.append("  totalPrice=").append(calculateTotalPrice()).append("\n");
sb.append("  totalParticipants=").append(getTotalParticipants()).append("\n");
sb.append("}");
