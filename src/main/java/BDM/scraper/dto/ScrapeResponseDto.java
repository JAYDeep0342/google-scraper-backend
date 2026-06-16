package BDM.scraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScrapeResponseDto {

    private Boolean success;

    @JsonProperty("total_leads")
    private Integer totalLeads;

    private String query;

    @JsonProperty("time_seconds")
    private Double timeSeconds;

    private List<LeadDto> leads;
}