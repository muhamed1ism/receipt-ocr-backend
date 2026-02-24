package org.ssnardo.receipt_ocr.user.dto;

import java.time.Instant;
import java.util.UUID;

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
public class UserResponse {

  private UUID id;
  private String email;
  private boolean isActive;
  private String role;
  private Instant createdAt;
  private Instant updatedAt;
}
