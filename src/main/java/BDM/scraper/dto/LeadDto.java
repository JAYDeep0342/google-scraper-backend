package BDM.scraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LeadDto {

    private String name;
    private String category;

    private Double rating;
    private Integer reviews;

    private String address;
    private String phone;
    private String website;

    private String emails;

    @JsonProperty("social_links")
    private String socialLinks;

    @JsonProperty("plus_code")
    private String plusCode;

    private Double latitude;
    private Double longitude;

    @JsonProperty("maps_url")
    private String mapsUrl;
}