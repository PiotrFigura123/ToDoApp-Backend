package com.ToDoApp.ToDoApp.adapter;

import com.ToDoApp.ToDoApp.model.Project;
import com.ToDoApp.ToDoApp.model.ProjectRepository;
import com.ToDoApp.ToDoApp.model.TaskGroup;
import com.ToDoApp.ToDoApp.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    //zapytania na encjach JPQL albo HQL
    @Query("select distinct p from Project p join fetch p.steps")
    List<Project> findAll();

}
