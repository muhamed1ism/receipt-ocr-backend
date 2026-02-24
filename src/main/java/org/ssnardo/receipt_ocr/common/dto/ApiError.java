package org.ssnardo.receipt_ocr.common.dto;

import java.time.LocalDateTime;

public record ApiError(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path) {
}
