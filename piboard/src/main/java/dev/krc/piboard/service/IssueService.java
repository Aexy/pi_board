package dev.krc.piboard.service;

import dev.krc.piboard.annotation.LocalJwtPresent;
import dev.krc.piboard.dto.IssueRequestDto;
import dev.krc.piboard.dto.IssueResponseDto;
import dev.krc.piboard.model.IssueState;
import dev.krc.piboard.model.Issues;
import dev.krc.piboard.model.Users;
import dev.krc.piboard.repository.IssuesRepository;
import dev.krc.piboard.repository.UsersRepository;
import dev.krc.piboard.util.JWTutil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class IssueService {

    private final IssuesRepository repository;
    private final UsersRepository usersRepository;
    private final JWTutil jwtutil;

    public IssueService(IssuesRepository repository, JWTutil jwtutil, UsersRepository usersRepository) {
        this.repository = repository;
        this.jwtutil = jwtutil;
        this.usersRepository = usersRepository;
    }

    public ResponseEntity<List<IssueResponseDto>> getIssues(String jwt) {
        if(usersRepository.findByEmail(jwtutil.extractEmailFromToken(jwt)).orElse(null) instanceof Users user) {
            return new ResponseEntity<>(repository.findAllByUserId(user.getId()).stream().map(this::mapToIssueDto).toList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<String> addIssue(IssueRequestDto issueDto, String jwt) {
        if(usersRepository.findByEmail(jwtutil.extractEmailFromToken(jwt)).isEmpty()) {
            return new ResponseEntity<>("Session Authentication Failed", HttpStatus.UNAUTHORIZED);
        }
        if(issueDto.getTitle().isEmpty() || issueDto.getDescription().isEmpty()) {
            return new ResponseEntity<>("Fields can not be empty", HttpStatus.BAD_REQUEST);
        }
        if(issueDto.getDueDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("The due date is before the current date", HttpStatus.BAD_REQUEST);
        }
        Issues issue = new Issues();
        issue.setTitle(issueDto.getTitle());
        issue.setDescription(issueDto.getDescription());
        issue.setDueDate(issueDto.getDueDate());
        issue.setState(IssueState.Open);
        issue.setUser(usersRepository.findByEmail(jwtutil.extractEmailFromToken(jwt)).get());
        repository.save(issue);
        return new ResponseEntity<>("Issue has been added", HttpStatus.CREATED);
    }

    //**************Auxiliary Functions**************
    private IssueResponseDto mapToIssueDto(Issues is) {
        return new IssueResponseDto(is.getTitle(),is.getDescription(),is.getDueDate(),is.getState(),is.getUser().getEmail());
    }

    //**************MCP Related Functions**************
    @Tool(name = "all-issues", description = "Returns all issues and their details")
    @LocalJwtPresent
    public String issues(){
        if(jwtutil.getLocalJWT().isEmpty()){
            return "Local JWT could not be read, make sure the user is logged in. ";
        }
        var optional = usersRepository.findByEmail(jwtutil.extractEmailFromToken(jwtutil.getLocalJWT()));
        if(optional.isEmpty()){
            return "No user could be found using the local JWT";
        }
        Users found = optional.get();
        List<Issues> issues = repository.findAllByUserId(found.getId());
        if(issues.isEmpty()){
            return "No issues found, to add an issue use add issue and specify (Title, Description, Status and User)";
        }
        List<IssueResponseDto> issueDtoList = issues.stream().map(this::mapToIssueDto).toList();
        return  "All the issues found for the user are: " + issueDtoList;
    }

    @Tool(name = "add-issue", description = "Adds an issue item to the local user. Required parameters are title, description and due date")
    @LocalJwtPresent
    public String addIssue(IssueRequestDto issueDto) {
        if(jwtutil.getLocalJWT().isEmpty()){
            return "Local JWT could not be read, make sure the user is logged in. ";
        }
        ResponseEntity<String> response = addIssue(issueDto,jwtutil.extractEmailFromToken(jwtutil.getLocalJWT()));
        if(response.getStatusCode().is2xxSuccessful()){
            return "An issue with the title: " + issueDto.getTitle() + " has been added";
        }
        if(response.getStatusCode().is4xxClientError()){
            return "The request could not be processed, make sure the due date is not before the current date and the fields (Title, Description and Due Date) are not empty";
        }
        return "Unidentified error, please try logging in again.";
    }
}
