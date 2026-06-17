package by.psu.service;

import by.psu.dto.request.ExcursionCreateRequest;
import by.psu.dto.response.ExcursionResponse;
import by.psu.entity.TourService;
import by.psu.exception.TourServiceValidationException;
import by.psu.repository.TourServiceRepository; // Убедитесь, что путь верен!
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TourServiceService {

    private final TourServiceRepository tourServiceRepository;


    public ExcursionResponse createExcursion(ExcursionCreateRequest request) {

        TourService excursion = new TourService();
        excursion.setName(request.getName());
        excursion.setPrice(request.getPrice());
        excursion.setFrom(request.getFrom());
        excursion.setTo(request.getTo());


        TourService saved = tourServiceRepository.save(excursion);
        return mapToExcursionResponse(saved);
    }


    public ExcursionResponse getExcursionById(Integer id) {
        TourService excursion = tourServiceRepository.findById(id)
                .orElseThrow(() -> new TourServiceValidationException("Экскурсия с ID " + id + " не найдена"));
        return mapToExcursionResponse(excursion);
    }


    public Page<ExcursionResponse> getExcursionPage(Pageable pageable) {
        return tourServiceRepository.findAll(pageable)
                .map(this::mapToExcursionResponse);
    }


    @Transactional
    public ExcursionResponse updateExcursion(Integer id, ExcursionCreateRequest request) {
        // 1. Находим существующую экскурсию
        TourService excursion = tourServiceRepository.findById(id)
                .orElseThrow(() -> new TourServiceValidationException("Экскурсия с ID " + id + " не найдена"));

        // 2. Обновляем поля из запроса
        excursion.setName(request.getName());
        excursion.setPrice(request.getPrice());
        excursion.setFrom(request.getFrom());
        excursion.setTo(request.getTo());

        TourService updatedExcursion = tourServiceRepository.save(excursion);


        return mapToExcursionResponse(updatedExcursion);
    }


    @Transactional
    public void deleteExcursion(Integer id) {
        // 1. Проверяем, существует ли экскурсия
        if (!tourServiceRepository.existsById(id)) {
            throw new TourServiceValidationException("Экскурсия с ID " + id + " не найдена");
        }

        // 2. Удаляем
        tourServiceRepository.deleteById(id);
    }


    private ExcursionResponse mapToExcursionResponse(TourService excursion) {

        return ExcursionResponse.builder()
                .id(excursion.getId())
                .name(excursion.getName())
                .price(excursion.getPrice())
                .from(excursion.getFrom())
                .to(excursion.getTo())
                .build();
    }
}