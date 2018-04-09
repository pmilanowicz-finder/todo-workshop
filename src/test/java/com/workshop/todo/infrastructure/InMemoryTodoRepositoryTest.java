package com.workshop.todo.infrastructure;

import com.workshop.todo.domain.Todo;
import com.workshop.todo.domain.TodoRepository;
import io.vavr.control.Try;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class InMemoryTodoRepositoryTest {

    private TodoRepository repository = new InMemoryTodoRepository();

    @Test
    public void shouldReturnSavedId() {
        // given
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "description", Optional.empty());

        // when
        Try<UUID> uuidTry = repository.save(todo);

        // then
        assertThat(uuidTry.isFailure()).isFalse();
    }

    @Test
    public void shouldReturnTryException() {
        // given
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "description", Optional.empty());
        repository.save(todo);

        // when
        Try<UUID> uuidTry = repository.save(todo);

        // then
        assertThat(uuidTry.isFailure()).isTrue();
    }

    @Test
    public void shouldSaveTodo() {
        // given
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "description", Optional.empty());

        // when
        repository.save(todo);

        // then
        Optional<Todo> todoFromRepo = repository.get(id);

        assertThat(todoFromRepo.isPresent()).isTrue();

        todoFromRepo.ifPresent(todo1 -> assertThat(todo1).isEqualTo(todo));
    }

    @Test
    public void shouldReturnTodoList() {
        // given
        Todo todo1 = new Todo(UUID.randomUUID(), "description", Optional.empty());
        Todo todo2 = new Todo(UUID.randomUUID(), "description #2", Optional.empty());

        // when
        repository.save(todo1);
        repository.save(todo2);

        // then
        List<Todo> todoList = repository.todoList();

        assertThat(todoList).containsOnly(todo1, todo2);

    }
}