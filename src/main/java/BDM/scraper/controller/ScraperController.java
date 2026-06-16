package BDM.scraper.controller;

import BDM.scraper.dto.ScrapeRequestDto;
import BDM.scraper.dto.ScrapeResponseDto;
import BDM.scraper.service.ScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scrape")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ScraperController {

    private final ScraperService scraperService;

    @PostMapping
    public ResponseEntity<ScrapeResponseDto> scrape(
            @RequestBody ScrapeRequestDto request) {

        ScrapeResponseDto response = scraperService.scrape(request);

        return ResponseEntity.ok(response);
    }
}