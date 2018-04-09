package com.workshop.todo.api.http;

public class CreatedTodoResponse {
    private String todoId;

    public CreatedTodoResponse() {
    }

    public CreatedTodoResponse(final String todoId) {
        this.todoId = todoId;
    }

    public String getTodoId() {
        return todoId;
    }
}