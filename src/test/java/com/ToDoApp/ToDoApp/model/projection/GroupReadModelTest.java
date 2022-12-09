package com.ToDoApp.ToDoApp.model.projection;

import com.ToDoApp.ToDoApp.model.Task;
import com.ToDoApp.ToDoApp.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GroupReadModelTest {
    @Test
    @DisplayName(" should create null dedline for group when no task deadines")
    void constructor_noDeadline_createNulldeadline(){
        //given
        var source = new TaskGroup();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("foo", null)));
        //when
        var result = new GroupReadModel(source);

        //then
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }

}