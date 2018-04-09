package com.workshop.todo.api.http;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PatchTodoRequest {
    private boolean completed = false;

    public PatchTodoRequest() {
    }

    public PatchTodoRequest(final boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }
}
