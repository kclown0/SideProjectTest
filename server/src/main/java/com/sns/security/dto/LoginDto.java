package com.sns.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

//시큐리티 필터에서 사용할 수 있도록 역직렬화 DTO 클래스

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDto {
    private String username;
    private String password;


}
