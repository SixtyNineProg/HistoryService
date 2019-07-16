package by.clevertec.HistoryService.service;

import by.clevertec.HistoryService.dto.FilterForHistory;
import by.clevertec.HistoryService.dto.History;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryService {

    String save(History history);

    Optional<History> find(String id);

    Boolean delete(String id);

    List<History> getAll();

    Boolean update(History history);

    Page<History> getHistory(Integer pageSize, Integer pageNumber);

    Page<History> findAllWithFilter(Integer pageSize, Integer pageNumber, FilterForHistory filterForHistory) throws IOException;
}