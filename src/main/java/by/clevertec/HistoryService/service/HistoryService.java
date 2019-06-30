package by.clevertec.HistoryService.service;

import by.clevertec.HistoryService.dto.History;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface HistoryService {

    String save(History history);

    Optional<History> find(String id);

    Page<History> getHistory(Integer pageSize, Integer pageNumber);
}