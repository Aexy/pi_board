package dev.krc.piboard.service;

import dev.krc.piboard.repository.IssuesRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class IssueService {

    private final IssuesRepository repository;

    public IssueService(IssuesRepository repository) {
        this.repository = repository;
    }
}
