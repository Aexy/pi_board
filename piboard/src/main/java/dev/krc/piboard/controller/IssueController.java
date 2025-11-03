package dev.krc.piboard.controller;

import dev.krc.piboard.dto.IssueRequestDto;
import dev.krc.piboard.dto.IssueResponseDto;
import dev.krc.piboard.service.IssueService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    //Displays the page with all the issue related options
    @GetMapping("/issues")
    @PreAuthorize("isAuthenticated()")
    public String issues(Model model) {
        return "issues/issues";
    }

    //Displays the page with all the issues
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-issues")
    public String allIssues(Model model, HttpSession session) {
        model.addAttribute("issues", issueService.getIssues(session.getAttribute("jwt").toString()).getBody());
        return "issues/all-issues";
    }

    @GetMapping("/new-issue")
    @PreAuthorize("isAuthenticated()")
    public String newIssue(Model model) {
        model.addAttribute("issueDto", new IssueRequestDto());
        return "issues/new-issue";
    }

    @PostMapping("/anew-issue")
    public String addNewIssue(@ModelAttribute("issueDto") IssueRequestDto issueDto, Model model, HttpSession session) {
        System.out.println("JWT SESH: " + session.getAttribute("jwt"));
        ResponseEntity<String> response = issueService.addIssue(issueDto, session.getAttribute("jwt").toString());
        if(!response.getStatusCode().is2xxSuccessful()){
            model.addAttribute("error", response.getBody());
            return "issues/new-issue";
        }
        model.addAttribute("success", response.getBody());
        return "issues/new-issue";
    }

    //**************Auxiliary Functions**************

    //**************Direct Endpoint Provider Methods**************

    @GetMapping("pi-all-issues")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<IssueResponseDto>> allIssues(@AuthenticationPrincipal Authentication authentication) {
        return issueService.getIssues(authentication.getCredentials().toString());
    }

    @PostMapping("pi-new-issue")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> newIssue(@AuthenticationPrincipal Authentication authentication, IssueRequestDto issueDto) {
        return issueService.addIssue(issueDto, authentication.getCredentials().toString());
    }
}
