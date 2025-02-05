package ru.taustudio.duckview.control.screenshotcontrol.job;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JobNotFoundException extends Exception{
}
