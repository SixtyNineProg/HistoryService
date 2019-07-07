package by.clevertec.HistoryService.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class History {
    @Id
    private String id;
    @NotNull(message = "aaa")
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