package by.clevertec.HistoryService.controller;

import by.clevertec.HistoryService.constants.Constants;
import by.clevertec.HistoryService.dto.FilterForHistory;
import by.clevertec.HistoryService.dto.History;
import by.clevertec.HistoryService.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(Constants.HISTORY)
@Slf4j
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping(value = Constants.FILTER_PATH_VARIABLE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> findHistoryWithFilter(
            @RequestParam(name = Constants.PAGESIZE_KEY, defaultValue = Constants.PAGESIZE_VALUE, required = false)
                    Integer pageSize,
            @RequestParam(name = Constants.PAGENUMBER_KEY, defaultValue = Constants.PAGENUMBER_VALUE, required = false)
                    Integer pageNumber,
            @RequestBody
                    FilterForHistory filterForHistory) {
        try {
            return ResponseEntity.ok(historyService.findAllWithFilter(pageSize, pageNumber, filterForHistory));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> saveHistory(@RequestBody History history) {
        try {
            return ResponseEntity.ok(historyService.save(history));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = Constants.ID_PATH_VARIABLE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> findHistory(@PathVariable String id) {
        Optional<History> history = historyService.find(id);
        if (history.isPresent()) {
            return ResponseEntity.ok(history);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getPageHistory(
            @RequestParam(name = Constants.PAGESIZE_KEY, defaultValue = Constants.PAGESIZE_VALUE, required = false)
                    Integer pageSize,
            @RequestParam(name = Constants.PAGENUMBER_KEY, defaultValue = Constants.PAGENUMBER_VALUE, required = false)
                    Integer pageNumber) {
        try {
            return ResponseEntity.ok(historyService.getHistory(pageSize, pageNumber));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = Constants.ID_PATH_VARIABLE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        Optional optionalUser = historyService.find(id);
        if (optionalUser.isPresent()) {
            try {
                return ResponseEntity.ok(historyService.delete(id));
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = Constants.PATH_GET_ALL,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(historyService.getAll());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = Constants.PATH_UPDATE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody History history) {
        Optional optionalUser = historyService.find(history.getId());
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(historyService.update(history));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}