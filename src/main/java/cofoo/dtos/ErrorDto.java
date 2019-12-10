package cofoo.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorDto {

        private Date timestamp;
        private String message;
        private String details;
}
