package com.udacity.jdnd.course3.critter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BusinessException extends Throwable {
    private String code;
    private String message;
}
