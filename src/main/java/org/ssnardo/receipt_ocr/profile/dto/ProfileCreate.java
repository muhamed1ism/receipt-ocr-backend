package org.ssnardo.receipt_ocr.profile.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.ssnardo.receipt_ocr.profile.enums.CurrencyEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileCreate {

    @NotBlank
    @Size(min = 2)
    private String firstName;

    @NotBlank
    @Size(min = 2)
    private String lastName;

    @NotNull
    @DateTimeFormat
    private LocalDate dateOfBirth;

    private String phoneNumber;
    private String country;
    private String address;
    private String city;

    @NotNull
    private CurrencyEnum currencyPreference;
}
