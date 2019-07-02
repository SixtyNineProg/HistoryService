package by.clevertec.HistoryService.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

@Data
@Table
@Accessors(chain = true)
public class History {
    @Id
    private String id;
    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timestamp;
    private String operatingType;
    private String entityType;
    private Boolean isWaslStatus;
    private Boolean isWialonStatus;
    private String waslDescription;
    private String wialonDescription;
    private String entityDescription;
}