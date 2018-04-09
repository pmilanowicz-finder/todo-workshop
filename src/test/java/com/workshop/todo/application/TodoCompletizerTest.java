package com.workshop.todo.application;

import com.workshop.todo.api.http.PatchTodoRequest;
import com.workshop.todo.domain.Todo;
import com.workshop.todo.domain.TodoRepository;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static com.workshop.todo.application.TodoCompletizerStatus.NOT_FOUND;
import static com.workshop.todo.application.TodoCompletizerStatus.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TodoCompletizerTest {

    private TodoRepository todoRepository = mock(TodoRepository.class);
    private TodoCompletizer todoCompletizer = new TodoCompletizer(todoRepository);

    @Test
    public void shouldComplete() {
        // given
        UUID id = UUID.randomUUID();
        Todo todo = new Todo(id, "desc", Optional.empty());

        PatchTodoRequest patchTodoRequest = new PatchTodoRequest(true);

        when(todoRepository.get(id)).thenReturn(Optional.of(todo));

        // when
        TodoCompletizerStatus completedStatus = todoCompletizer.setCompleted(id, patchTodoRequest.isCompleted());

        // then
        assertThat(completedStatus).isEqualTo(SUCCESS);
    }

    @Test
    public void shouldNotComplete() {
        // given
        UUID id = UUID.randomUUID();
        PatchTodoRequest patchTodoRequest = new PatchTodoRequest(true);

        when(todoRepository.get(id)).thenReturn(Optional.empty());

        // when
        TodoCompletizerStatus completedStatus = todoCompletizer.setCompleted(id, patchTodoRequest.isCompleted());

        // then
        assertThat(completedStatus).isEqualTo(NOT_FOUND);
    }
}