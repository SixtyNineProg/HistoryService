package by.clevertec.HistoryService.service.impl;

import by.clevertec.HistoryService.constants.Constants;
import by.clevertec.HistoryService.dto.FilterForHistory;
import by.clevertec.HistoryService.dto.History;
import by.clevertec.HistoryService.repository.HistoryRepository;
import by.clevertec.HistoryService.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;

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
    public Boolean delete(String id) {
        historyRepository.deleteById(id);
        log.info(Constants.HISTORY_DELETED, id);
        return true;
    }

    @Override
    public List<History> getAll() {
        List<History> users = historyRepository.findAll();
        log.info(Constants.HISTORIES_RECEIVED);
        return users;
    }

    @Override
    public Boolean update(History history) {
        historyRepository.save(history);
        Optional<History> optionalUser = Optional.ofNullable(history);
        optionalUser.ifPresent(data -> log.info(Constants.HISTORY_UPDATED, history.getId(), toJson(history)));
        return true;
    }

    @Override
    public Page<History> getHistory(Integer pageSize, Integer pageNumber) {
        Page<History> histories = historyRepository.findAll(PageRequest.of(pageNumber, pageSize));
        log.info(Constants.HISTORY_PAGEABLE, toJson(histories));
        return histories;
    }

    @Override
    public Page<History> findAllWithFilter(Integer pageSize, Integer pageNumber, FilterForHistory filterForHistory) throws IOException {
        final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize);

        Query query = new Query();

        Class filterForHistoryClass = FilterForHistory.class;

        convertFilterForHistoryToMap(filterForHistory).forEach((key, value) ->
        {
            if (value != null) {
                try {
                    String fieldType = filterForHistoryClass.getDeclaredField(key).getType().toString();
                    if (fieldType.contains("String")) {
                        String valueToString = value.toString();
                        String[] dataStrings = (valueToString.substring(valueToString.indexOf("[") + 1, valueToString.indexOf("]"))).split(",");
                        query.addCriteria(Criteria.where("userName").in((Object[]) dataStrings));
                    }
                } catch (NoSuchFieldException e) {
                    log.warn(Constants.ERROR_GET_FIELD, filterForHistoryClass.getSimpleName(), e.toString());
                }
            }
        });

/*
        if (filterForHistory.getUserNames() != null) {
            query.addCriteria(Criteria.where("userName").in((Object[]) filterForHistory.getUserNames()));
            System.out.println(Arrays.toString(filterForHistory.getUserNames()));
        }
        if (filterForHistory.getTimestampFrom() != null && filterForHistory.getTimestampTo() != null) {
            query.addCriteria(Criteria.where("timestamp").gte(filterForHistory.getTimestampFrom()).lte(filterForHistory.getTimestampTo()));
        } else if (filterForHistory.getTimestampFrom() != null) {
            query.addCriteria(Criteria.where("timestamp").gte(filterForHistory.getTimestampFrom()));
        } else if (filterForHistory.getTimestampTo() != null) {
            query.addCriteria(Criteria.where("timestamp").lte(filterForHistory.getTimestampTo()));
        }
        if (filterForHistory.getOperatingType() != null) {
            query.addCriteria(Criteria.where("operatingType").in((Object[]) filterForHistory.getOperatingType()));
        }
        if (filterForHistory.getEntityType() != null) {
            query.addCriteria(Criteria.where("entityType").in((Object[]) filterForHistory.getEntityType()));
        }
        if (filterForHistory.getIsWaslStatus() != null) {
            query.addCriteria(Criteria.where("isWaslStatus").in((Object[]) filterForHistory.getIsWaslStatus()));
        }
        if (filterForHistory.getIsWialonStatus() != null) {
            query.addCriteria(Criteria.where("isWialonStatus").in((Object[]) filterForHistory.getIsWialonStatus()));
        }
*/

        query.with(pageableRequest);

        List<History> list = mongoTemplate.find(query, History.class);
        return new PageImpl<>(list, pageableRequest, list.size());
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

    private Map<String, Object> convertFilterForHistoryToMap(FilterForHistory filterForHistory) throws IOException {
        return objectMapper.readValue(objectMapper.writeValueAsString(filterForHistory),
                new TypeReference<LinkedHashMap<String, Object>>() {
                });
    }
}