package com.npht.springtodo.controller.client;

import java.security.Principal;
import java.util.Date;

import com.npht.springtodo.model.Project;
import com.npht.springtodo.model.ProjectList;
import com.npht.springtodo.model.Task;
import com.npht.springtodo.repository.ProjectListRepository;
import com.npht.springtodo.repository.ProjectRepository;
import com.npht.springtodo.repository.TaskRepository;
import com.npht.springtodo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "project")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepo;
    @Autowired
    private ProjectListRepository listRepo;
    @Autowired
    private TaskRepository taskRepo;
    @Autowired
    private UserRepository userRepo;

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "{projectId}", method = RequestMethod.GET)
    public String getHome(@PathVariable("projectId") Long id, Principal principal, Model model) {
        Project project = projectRepo.findById(id).orElse(null);
        try {
            // Error: Cannot find project
            if (project == null) {
                throw new Exception("Cannot find this project (id = " + id + ")");
            }
            // Error: User don't own the project
            if (!project.getUser().getEmail().equals(principal.getName())) {
                throw new Exception("Logged-in user don't own this project.");
            }

            // Result
            model.addAttribute("project", project);
            return "view/client/project";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/dashboard";
        }
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public @ResponseBody String doSave(@RequestParam(name = "id") String id, @RequestParam(name = "value") String value,
            Principal principal) {
        String preId = id.substring(0, 1);
        String sufIdstr = id.substring(1, id.length());
        System.out.println("id: \"" + id + "\" | preId: \"" + preId + "\" | sufId: \"" + sufIdstr + "\"");
        try {
            if (value == null || value.isEmpty()) {
                throw new Exception("Value is null!");
            }
            Long sufId = Long.parseLong(sufIdstr);
            if ("p".equals(preId)) {
                System.out.println("p chosen.");
                Project p = projectRepo.findById(sufId).orElse(null);
                if (p != null) {
                    // check if logged-in user own it or not.
                    if (p.getUser().getEmail().equals(principal.getName())) {
                        p.setTitle(value);
                        p.setUpdatedDate(new Date());
                        projectRepo.save(p);
                        return value;
                    } else {
                        throw new Exception("The user is not own this");
                    }
                } else {
                    throw new Exception("Cannot find project (id = " + sufId + ")");
                }
            } else if ("l".equals(preId)) {
                System.out.println("l chosen.");
                ProjectList l = listRepo.findById(sufId).orElse(null);
                if (l != null) {
                    if (l.getProject().getUser().getEmail().equals(principal.getName())) {
                        l.setTitle(value);
                        l.setUpdatedDate(new Date());
                        listRepo.save(l);
                        return value;
                    } else {
                        throw new Exception("The user is not own this");
                    }
                } else {
                    throw new Exception("Cannot find list (id = " + sufId + ")");
                }
            } else {
                System.out.println("t chosen.");
                Task t = taskRepo.findById(sufId).orElse(null);
                if (t != null) {
                    if ((t.getList() != null
                            && t.getList().getProject().getUser().getEmail().equals(principal.getName()))
                            || (t.getListTask() != null && t.getListTask().getList().getProject().getUser().getEmail()
                                    .equals(principal.getName()))) {
                        t.setTitle(value);
                        t.setUpdatedDate(new Date());
                        taskRepo.save(t);
                        return value;
                    } else {
                        throw new Exception("The user is not own this");
                    }
                } else {
                    throw new Exception("Cannot find task (id = " + sufId + ")");
                }
            }
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}