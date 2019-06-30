package by.clevertec.HistoryService.repository;

import by.clevertec.HistoryService.dto.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoryRepository extends MongoRepository<History, String> {
    Page<History> findAllByOrderByTimestampDesc(Pageable pageable);
}