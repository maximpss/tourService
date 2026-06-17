package by.psu.service;

import by.psu.dto.request.ExcursionCreateRequest;
import by.psu.dto.response.ExcursionResponse;
import by.psu.exception.ExcursionNotFoundException;
import by.psu.mapper.ExcursionMapper;
import by.psu.repository.ExcursionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TourServiceService {
    @Autowired
    private ExcursionRepository excursionRepository;
    @Autowired
    private ExcursionMapper excursionMapper;

    public ExcursionResponse createExcursion(ExcursionCreateRequest request) {
        var excursion = excursionMapper.toEntity(request);
        var savedExcursion = excursionRepository.save(excursion);
        return excursionMapper.toResponse(savedExcursion);
    }

    public ExcursionResponse getExcursionById(Integer id) {
        var excursion = excursionRepository.findById(id)
                .orElseThrow(ExcursionNotFoundException::new);
        return excursionMapper.toResponse(excursion);
    }

    public Page<ExcursionResponse> getExcursionPage(Pageable pageable) {
        return excursionRepository.findAll(pageable).map(excursionMapper::toResponse);
    }
}
