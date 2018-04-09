package com.workshop.todo.domain;

import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TodoTest {

    @Test
    public void shouldCreateUncompletedTodo() {
        // when
        Todo todo = new Todo(UUID.randomUUID(), "desc", Optional.empty());

        // expect
        assertThat(todo.isCompleted()).isFalse();
    }

    @Test
    public void shouldCompleteTodo() {
        // given
        Todo todo = new Todo(UUID.randomUUID(), "desc", Optional.empty());

        // when
        Todo todoCompleted = todo.complete();

        // expect
        assertThat(todoCompleted.isCompleted()).isTrue();
    }
}