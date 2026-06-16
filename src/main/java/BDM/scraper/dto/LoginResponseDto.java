package BDM.scraper.dto;
import BDM.scraper.entity.type.RoleType;
import lombok.*;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private  String Jwt ;
    private Long userid ;
    private Set<RoleType> roles;
}
