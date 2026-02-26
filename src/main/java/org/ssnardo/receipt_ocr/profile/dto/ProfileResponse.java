package org.ssnardo.receipt_ocr.profile.dto;

import java.time.LocalDate;

import org.ssnardo.receipt_ocr.profile.CurrencyEnum;

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
public class ProfileResponse {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String country;
    private String address;
    private String city;
    private CurrencyEnum currencyPreference;
}
