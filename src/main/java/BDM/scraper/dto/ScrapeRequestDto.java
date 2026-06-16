package BDM.scraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScrapeRequestDto {

    private String keyword;
    private String location;
    private Integer limit;

    @JsonProperty("find_emails")
    private Boolean findEmails;
}