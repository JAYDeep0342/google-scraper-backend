package BDM.scraper.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "leads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private Double rating;

    private Integer reviews;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String website;

    private Double latitude;

    private Double longitude;

    @Column(columnDefinition = "TEXT")
    private String mapsUrl;

    @Column(columnDefinition = "TEXT")
    private String emails;

    @Column(columnDefinition = "TEXT")
    private String socialLinks;

    private String plusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private ScrapeJob job;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}