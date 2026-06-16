package BDM.scraper.controller;
import BDM.scraper.dto.LoginRequestDto;
import BDM.scraper.dto.LoginResponseDto;
import BDM.scraper.dto.SignupRequestDto;
import BDM.scraper.dto.SignupResponseDto;
import BDM.scraper.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@CrossOrigin(origins = "http://localhost:5173")

@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> register(@RequestBody SignupRequestDto signUpRequestDto) {
        SignupResponseDto response = authService.signup (signUpRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        LoginResponseDto response =authService.login(dto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
