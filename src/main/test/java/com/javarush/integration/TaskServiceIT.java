package com.javarush.integration;

import com.javarush.config.AppConfig;
import com.javarush.config.WebConfig;
import com.javarush.domain.Status;
import com.javarush.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, WebConfig.class})
public class TaskServiceIT {
    @Autowired
    private TaskService taskService;

    @Test
    void should_save_new_task() {
        var expectedTask = taskService.create("test decr", Status.IN_PROGRESS);

        assertNotNull(expectedTask);
    }
}
