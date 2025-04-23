package co.pragma.scaffold.infra.driven.webclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CityInfoDto {

    private String name;
    private Double latitude;
    private Double longitude;
    private String country;
    private Long population;

    @JsonProperty("is_capital")
    private boolean isCapital;
}
