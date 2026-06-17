package by.psu.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExcursionUpdateRequest(
        String guideName,
        String excursionType,
        boolean lunchIncluded,
        Integer id,
        String name,
        BigDecimal price,
        LocalDate from,
        LocalDate to
) {
}
