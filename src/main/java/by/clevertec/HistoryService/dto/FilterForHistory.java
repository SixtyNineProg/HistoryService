package by.clevertec.HistoryService.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FilterForHistory {
    private String[] userNames;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timestampFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timestampTo;
    private String[] operatingType;
    private String[] entityType;
    private Boolean[] isWaslStatus;
    private Boolean[] isWialonStatus;
}