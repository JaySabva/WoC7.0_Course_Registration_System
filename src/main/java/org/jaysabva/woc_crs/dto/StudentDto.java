package org.jaysabva.woc_crs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record StudentDto(
        @Size(min = 3, max = 15) @NotBlank @NotEmpty(message = "Name cannot be empty") String name,
        @Email @NotEmpty(message = "Email cannot be empty") String email,
        @Size(min = 8) @NotEmpty(message = "Password cannot be empty") String password) {
}
