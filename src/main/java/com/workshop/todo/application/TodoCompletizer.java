package com.workshop.todo.application;

import com.workshop.todo.domain.Todo;
import com.workshop.todo.domain.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TodoCompletizer {

    private TodoRepository todoRepository;

    @Autowired
    public TodoCompletizer(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoCompletizerStatus setCompleted(UUID id, boolean completed) {
        Optional<Todo> todo = todoRepository.get(id);

        if (!todo.isPresent()) {
            return TodoCompletizerStatus.NOT_FOUND;
        }

        Todo todoUpdated = handleCompletion(completed, todo.get());

        todoRepository.update(todoUpdated);

        return TodoCompletizerStatus.SUCCESS;
    }

    private Todo handleCompletion(boolean completed, Todo todo) {
        if (completed) {
            return todo.complete();
        } else {
            return todo.uncomplete();
        }
    }
}
