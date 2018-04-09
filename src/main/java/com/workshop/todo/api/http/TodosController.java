package com.workshop.todo.api.http;

import com.workshop.todo.application.TodoCompletizer;
import com.workshop.todo.application.TodoCompletizerStatus;
import com.workshop.todo.domain.Todo;
import com.workshop.todo.domain.TodoRepository;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/todo")
public class TodosController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private TodoRepository todoRepository;

    private TodoCompletizer todoCompletizer;

    @Autowired
    public TodosController(final TodoRepository todoRepository, final TodoCompletizer todoCompletizer) {
        this.todoRepository = todoRepository;
        this.todoCompletizer = todoCompletizer;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Iterable<Todo>> todosList() {
        return new ResponseEntity<>(todoRepository.todoList(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<CreatedTodoResponse> createTodo(@RequestBody CreateTodoRequest createTodoRequest) {
        Todo newTodo = new Todo(UUID.randomUUID(), createTodoRequest.getDescription(), createTodoRequest.getDeadline().map(date -> LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())));
        Try<UUID> tryUuid = todoRepository.save(newTodo);

        return tryUuid.map(uuid ->
                new ResponseEntity<>(new CreatedTodoResponse(uuid.toString()), HttpStatus.CREATED)
        ).getOrElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
    public ResponseEntity<HttpStatus> patchTodo(@PathVariable("id") String id, @RequestBody PatchTodoRequest patchTodoRequest) {

        TodoCompletizerStatus completizerStatus = todoCompletizer.setCompleted(UUID.fromString(id), patchTodoRequest.isCompleted());

        return new ResponseEntity<>(completizerStatus.equals(TodoCompletizerStatus.SUCCESS) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Optional<Todo>> todo(@PathVariable("id") String id) {
        Optional<Todo> todo = todoRepository.get(UUID.fromString(id));

        return new ResponseEntity<>(todo, todo.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
