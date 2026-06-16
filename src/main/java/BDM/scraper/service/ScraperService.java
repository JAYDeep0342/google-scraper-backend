package BDM.scraper.service;

import BDM.scraper.dto.LeadDto;
import BDM.scraper.dto.ScrapeRequestDto;
import BDM.scraper.dto.ScrapeResponseDto;
import BDM.scraper.entity.Lead;
import BDM.scraper.entity.ScrapeJob;
import BDM.scraper.repository.LeadRepository;
import BDM.scraper.repository.ScrapeJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ScraperService {

    private final RestTemplate restTemplate;
    private final ScrapeJobRepository scrapeJobRepository;
    private final LeadRepository leadRepository;

    public ScrapeResponseDto scrape(ScrapeRequestDto request) {

        String url = "http://localhost:8000/scrape/sync";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ScrapeRequestDto> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<ScrapeResponseDto> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        ScrapeResponseDto.class
                );

        ScrapeResponseDto scrapeResponse = response.getBody();

        if (scrapeResponse == null) {
            throw new RuntimeException("Python API returned null response");
        }

        ScrapeJob job = ScrapeJob.builder()
                .keyword(request.getKeyword())
                .location(request.getLocation())
                .totalLeads(scrapeResponse.getTotalLeads())
                .status("COMPLETED")
                .build();

        job = scrapeJobRepository.save(job);

        for (LeadDto dto : scrapeResponse.getLeads()) {

            Lead lead = Lead.builder()
                    .name(dto.getName())
                    .category(dto.getCategory())
                    .rating(dto.getRating())
                    .reviews(dto.getReviews())
                    .address(dto.getAddress())
                    .phone(dto.getPhone())
                    .website(dto.getWebsite())
                    .emails(dto.getEmails())
                    .socialLinks(dto.getSocialLinks())
                    .plusCode(dto.getPlusCode())
                    .latitude(dto.getLatitude())
                    .longitude(dto.getLongitude())
                    .mapsUrl(dto.getMapsUrl())
                    .job(job)
                    .build();

            leadRepository.save(lead);
        }

        return scrapeResponse;
    }
}