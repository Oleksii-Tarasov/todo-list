package com.javarush.service;

import com.javarush.dao.TaskDAO;
import com.javarush.domain.Status;
import com.javarush.domain.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;
    @Mock
    private TaskDAO taskDAO;

    private static Task expectedTask;
    private static int id;
    private static String description;
    private static Status status;

    @BeforeAll
    static void setUpTask() {
        id = 1;
        description = "description";
        status = Status.IN_PROGRESS;

        expectedTask = new Task();

        expectedTask.setId(id);
        expectedTask.setDescription(description);
        expectedTask.setStatus(status);
    }

    @Test
    void should_return_all_tasks() {
        int offset = 1;
        int limit = 10;

        when(taskDAO.getAll(offset, limit)).thenReturn(Arrays.asList(new Task(), new Task(), new Task()));

        var actualList = taskService.getAll(offset, limit);
        assertNotNull(actualList);

        verify(taskDAO, times(1)).getAll(offset, limit);
    }

    @Test
    void should_return_total_count_of_tasks() {
        when(taskDAO.getAllCount()).thenReturn(Arrays.asList(new Task(), new Task()).size());

        var actualTaskCount = taskService.getAllCount();

        assertEquals(taskDAO.getAllCount(), actualTaskCount);
    }

    @Test
    void should_save_and_return_edited_task() {
        when(taskDAO.getById(id)).thenReturn(expectedTask);

        var actualTask = taskService.edit(id, description, status);

        assertNotNull(expectedTask);
        assertEquals(description, actualTask.getDescription());
        assertEquals(status, actualTask.getStatus());

        verify(taskDAO, times(1)).saveOrUpdate(expectedTask);
    }

    @Test
    void should_create_newTask() {
        Task task = taskService.create(description, status);

        verify(taskDAO).saveOrUpdate(task);

        assertEquals(description, task.getDescription());
        assertEquals(status, task.getStatus());
    }

    @Test
    void should_delete_task_by_id() {
        when(taskDAO.getById(id)).thenReturn(expectedTask);

        taskService.delete(id);

        verify(taskDAO, times(1)).delete(expectedTask);
    }

    @Test
    void should_throw_runtime_exception_when_task_not_found_by_id() {
        int nonExistentId = 123;

        when(taskDAO.getById(nonExistentId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> taskService.delete(nonExistentId));

        verify(taskDAO, never()).delete(any());
    }
}
