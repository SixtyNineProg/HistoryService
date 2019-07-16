package by.clevertec.HistoryService.constants;

public class Constants {
    public static final String HISTORY = "/history";
    public static final String ID_PATH_VARIABLE = "/{id}";
    public static final String FILTER_PATH_VARIABLE = "/filter";
    public static final String PATH_GET_ALL = "/getAll";
    public static final String PATH_UPDATE = "/update";

    public static final String PAGESIZE_KEY = "pagesize";
    public static final String PAGESIZE_VALUE = "15";
    public static final String PAGENUMBER_KEY = "pagenumber";
    public static final String PAGENUMBER_VALUE = "0";

    public static final String HISTORY_SAVED = "History saved successful : {}";
    public static final String HISTORY_DELETED = "History deleted successful : {}";
    public static final String HISTORIES_RECEIVED = "All histories with received successful";
    public static final String HISTORY_UPDATED = "History with id={} updated successful : {}";
    public static final String HISTORY_RECEIVED = "History with id={} received successful : {}";
    public static final String HISTORY_PAGEABLE = "History pageable received successful : {}";
    public static final String ERROR_PARSING_OF_OBJECT = "can't represent object of class {} in json form for logging: {}";
    public static final String ERROR_GET_FIELD = "can't get feild of class {} exception: {}";
}