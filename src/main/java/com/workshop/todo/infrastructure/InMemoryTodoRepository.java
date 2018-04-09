package com.workshop.todo.infrastructure;

import com.workshop.todo.domain.Todo;
import com.workshop.todo.domain.TodoRepository;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

@Component
public class InMemoryTodoRepository implements TodoRepository {

    private HashMap<UUID, Todo> store = new HashMap<>();

    @Override
    public Try<UUID> update(Todo todo) {

        if (!store.containsKey(todo.getId())) {
            return Try.failure(new Exception("Todo already found"));
        }

        store.put(todo.getId(), todo);

        return Try.success(todo.getId());
    }

    @Override
    public Try<UUID> save(Todo todo) {

        if (!store.containsKey(todo.getId())) {
            store.put(todo.getId(), todo);

            return Try.success(todo.getId());
        }

        return Try.failure(new Exception("Todo already found"));

    }

    @Override
    public Optional<Todo> get(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Todo> todoList() {
        return new ArrayList<>(store.values());
    }
}
