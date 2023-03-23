package com.javarush.service;

import com.javarush.dao.TaskDAO;
import com.javarush.domain.Status;
import com.javarush.domain.Task;
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

    @Test
    void should_return_all_tasks() {
        when(taskDAO.getAll(1, 10)).thenReturn(Arrays.asList(new Task(), new Task(), new Task()));

        var actualList = taskService.getAll(1, 10);
        assertNotNull(actualList);

        verify(taskDAO, times(1)).getAll(1, 10);
    }

    @Test
    void should_return_total_count_of_tasks() {
        when(taskDAO.getAllCount()).thenReturn(Arrays.asList(new Task(), new Task()).size());

        var actualTaskCount = taskService.getAllCount();

        assertEquals(taskDAO.getAllCount(), actualTaskCount);
    }

    @Test
    void should_save_and_return_edited_task() {
        int id = 1;
        String description = "description";
        Status status = Status.IN_PROGRESS;

        Task expectedTask = new Task();
        expectedTask.setId(id);
        expectedTask.setDescription(description);
        expectedTask.setStatus(status);

        when(taskDAO.getById(id)).thenReturn(expectedTask);

        var actualTask = taskService.edit(id, description, status);

        assertNotNull(expectedTask);
        assertEquals(description, actualTask.getDescription());
        assertEquals(status, actualTask.getStatus());

        verify(taskDAO, times(1)).saveOrUpdate(expectedTask);
    }

    @Test
    void should_create_newTask() {
        String description = "description";
        Status status = Status.IN_PROGRESS;

        Task task = taskService.create(description, status);

        verify(taskDAO).saveOrUpdate(task);

        assertEquals(description, task.getDescription());
        assertEquals(status, task.getStatus());
    }

    @Test
    void should_delete_task_by_id() {
        int id = 1;
        String description = "description";
        Status status = Status.IN_PROGRESS;

        Task expectedTask = new Task();

        expectedTask.setId(1);
        expectedTask.setDescription(description);
        expectedTask.setStatus(status);

        when(taskDAO.getById(id)).thenReturn(expectedTask);

        taskService.delete(id);

        verify(taskDAO).delete(expectedTask);
    }

    @Test
    public void when_task_not_found_by_id() {
        int id = 1;

        when(taskDAO.getById(id)).thenReturn(null);

        try {
            taskService.delete(id);
        } catch (RuntimeException e) {
            assertEquals("Task with id=" + id + " not found", e.getMessage());
            verify(taskDAO, times(1)).getById(id);
            verifyNoMoreInteractions(taskDAO);
            return;
        }
        fail("Expected RuntimeException was not thrown");
    }
}
