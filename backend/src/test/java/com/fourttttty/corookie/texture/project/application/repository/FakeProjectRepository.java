package com.fourttttty.corookie.texture.project.application.repository;

import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import jakarta.persistence.EntityNotFoundException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeProjectRepository implements ProjectRepository {

    private long autoIncrementId = 1L;
    private final Map<Long, Project> store = new HashMap<>();

    @Override
    public Project save(Project project) {
        if (project.getId() == null) {
            setIdInEntity(project);
        }
        store.put(autoIncrementId, project);
        autoIncrementId++;
        return project;
    }

    private void setIdInEntity(Project project) {
        try {
            Class<Project> projectClass = Project.class;
            Field id = projectClass.getDeclaredField("id");
            id.setAccessible(true);
            id.set(project, autoIncrementId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Project> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Project> findByManagerId(Long managerId) {
        return store.values().stream()
                .filter(project -> project.isManager(managerId))
                .toList();
    }

    @Override
    public Optional<Project> findByInvitationLink(String invitationLink) {
        for (Map.Entry<Long, Project> item : store.entrySet()) {
            Project project = item.getValue();
            if(project.getInvitationLink().equals(invitationLink)){
                return Optional.of(project);
            }
        }
       throw new EntityNotFoundException();
    }

    @Override
    public List<Project> findAll() {
        return store.values().stream()
                .toList();
    }
}
