package by.clevertec.HistoryService.service.impl;


import by.clevertec.HistoryService.constants.Constants;
import by.clevertec.HistoryService.dto.History;
import by.clevertec.HistoryService.repository.HistoryRepository;
import by.clevertec.HistoryService.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final ObjectMapper objectMapper;

    public HistoryServiceImpl(HistoryRepository historyRepository, ObjectMapper objectMapper) {
        this.historyRepository = historyRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public String save(History history) {
        historyRepository.save(history);
        log.info(Constants.HISTORY_SAVED, toJson(history));
        return history.getId();
    }

    @Override
    public Optional<History> find(String id) {
        Optional<History> history = historyRepository.findById(id);
        history.ifPresent(data -> log.info(Constants.HISTORY_RECEIVED, id, toJson(history)));
        return history;
    }

    @Override
    public Page<History> getHistory(Integer pageSize, Integer pageNumber) {
        Page<History> histories = historyRepository.findAllByOrderByTimestampDesc(PageRequest.of(pageNumber, pageSize));
        log.info(Constants.HISTORY_PAGEABLE, toJson(histories));
        return histories;
    }

    private synchronized String toJson(Object o) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.warn(Constants.ERROR_PARSING_OF_OBJECT, o.getClass().getSimpleName(), e.toString());
        }
        return json;
    }
}