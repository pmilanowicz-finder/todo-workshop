package com.workshop.todo.api.http;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Date;
import java.util.Optional;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateTodoRequest {
    private String description;
    private Optional<Date> deadline;

    private CreateTodoRequest() {
    }

    public CreateTodoRequest(final String description, final Optional<Date> deadline) {
        this.description = description;
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public Optional<Date> getDeadline() {
        return deadline;
    }
}