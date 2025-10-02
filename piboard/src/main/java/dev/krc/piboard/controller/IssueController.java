package dev.krc.piboard.controller;

import dev.krc.piboard.service.IssueService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    //Displays the page to create a new issue
    @GetMapping("/new-issue")
    public String newIssue(Model model) {
        return "new-issue";
    }
}
