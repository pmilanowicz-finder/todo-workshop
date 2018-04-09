package com.workshop.todo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Preconditions;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class Todo {

    private UUID id;
    private String description;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Optional<LocalDateTime> deadline;
    private boolean completed = false;

    public Todo(final UUID id, final String description, final Optional<LocalDateTime> deadline) {
        this.id = Preconditions.checkNotNull(id, "Id cannot be null");
        this.description = Preconditions.checkNotNull(description);
        this.deadline = Preconditions.checkNotNull(deadline);
    }

    private Todo(final UUID id, final String description, final Optional<LocalDateTime> deadline, boolean completed) {
        this(id, description, deadline);
        this.completed = completed;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Optional<LocalDateTime> getDeadline() {
        return deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Todo complete() {
        return new Todo(id, description, deadline, true);
    }

    public Todo uncomplete() {
        return new Todo(id, description, deadline, false);
    }
}
