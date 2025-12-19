package com.smartexpense.budgetservice.exception;

import java.time.Instant;

public record ErrorResponse(Instant timestamp,int status, String error, String path) {
}
