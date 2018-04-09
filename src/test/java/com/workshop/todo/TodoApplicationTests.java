package com.workshop.todo;

import com.jayway.jsonpath.JsonPath;
import com.workshop.todo.domain.Todo;
import com.workshop.todo.domain.TodoRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TodoApplicationTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TodoRepository todoRepository;

    private MockMvc mvc;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    private final JsonPath idJsonPath = JsonPath.compile("$.todoId");

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void shouldReturnEmptyTodos() throws Exception {
        //when
        mvc.perform(get("/todo")
                .accept(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.empty()));
    }

    @Test
    public void shouldReturnGivenTodos() throws Exception {
        //given
        Todo todo = new Todo(UUID.randomUUID(), "desc", Optional.empty());
        todoRepository.save(todo);

        //when
        mvc.perform(get("/todo")
                .accept(MediaType.APPLICATION_JSON))

                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(todo.getId().toString())))
                .andExpect(jsonPath("$[0].description", is(todo.getDescription())))
                .andExpect(jsonPath("$[0].completed", is(todo.isCompleted())))
                .andExpect(jsonPath("$[0].deadline", isEmptyOrNullString()));
    }

    @Test
    public void shouldCreateNewTodo() throws Exception {
        //when
        final String desc = "some description";
        final Long deadline = 1522157553467L;

        final MvcResult mvcResult = mvc.perform(post("/todo")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"" + desc + "\",\"deadline\":" + deadline + "}"))

                //then
                .andExpect(status().isCreated())
                .andReturn();

        final String content = mvcResult.getResponse().getContentAsString();
        final String id = idJsonPath.read(content);

        //when
        mvc.perform(get("/todo")
                .accept(MediaType.APPLICATION_JSON))

                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(id)))
                .andExpect(jsonPath("$[0].description", is(desc)))
                .andExpect(jsonPath("$[0].completed", is(false)))
                .andExpect(jsonPath("$[0].deadline", is(dateFormat.format(new Date(deadline)))));
    }

    @Test
    public void shouldCompleteTodo() throws Exception {
        //when
        final String desc = "some description";
        final Long deadline = 1522157553467L;


        final MvcResult mvcResult = mvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"" + desc + "\",\"deadline\":" + deadline + "}"))

                //then
                .andExpect(status().isCreated())
                .andReturn();

        //given
        final String content = mvcResult.getResponse().getContentAsString();
        final String id = idJsonPath.read(content);

        //when
        mvc.perform(patch("/todo/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"completed\":\"true\"}"))

                //then
                .andDo(print())
                .andExpect(status().isOk());

        //when
        mvc.perform(get("/todo/{id}", id)
                .accept(MediaType.APPLICATION_JSON))

                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.description", is(desc)))
                .andExpect(jsonPath("$.completed", is(true)))
                .andExpect(jsonPath("$.deadline", is(dateFormat.format(new Date(deadline)))));
    }

    @Test
    public void shouldNotCompleteTodo() throws Exception {
        UUID id = UUID.randomUUID();

        //when
        mvc.perform(patch("/todo/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"completed\":\"true\"}"))

                //then
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnTodoNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        // when
        mvc.perform(get("/todo/{id}", id)
                .accept(MediaType.APPLICATION_JSON))

                //then
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnTodoOk() throws Exception {
        //given
        UUID id = UUID.randomUUID();

        Todo todo = new Todo(id, "desc", Optional.empty());
        todoRepository.save(todo);

        // when
        mvc.perform(get("/todo/{id}", id)
                .accept(MediaType.APPLICATION_JSON))

                //then
                .andDo(print())
                .andExpect(status().isOk());
    }
}
