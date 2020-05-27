package com.npht.springtodo.controller.client;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.npht.springtodo.model.ListOrder;
import com.npht.springtodo.model.Project;
import com.npht.springtodo.model.ProjectList;
import com.npht.springtodo.model.Task;
import com.npht.springtodo.model.TaskOrder;
import com.npht.springtodo.model.User;
import com.npht.springtodo.repository.ListOrderRepository;
import com.npht.springtodo.repository.ProjectListRepository;
import com.npht.springtodo.repository.ProjectRepository;
import com.npht.springtodo.repository.TaskOrderRepository;
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
    private UserRepository userRepo;
    @Autowired
    private ProjectRepository projectRepo;
    @Autowired
    private ProjectListRepository listRepo;
    @Autowired
    private TaskRepository taskRepo;
    @Autowired
    private ListOrderRepository listOrderRepo;
    @Autowired
    private TaskOrderRepository taskOrderRepo;

    // TODO: make all add new function add id to order automatically (no need to save order again)


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
            // Re-order task and list
            List<ProjectList> lists = reorder(project.getLists(), project.getId());
            // Result
            model.addAttribute("project", project);
            model.addAttribute("lists", lists);
            model.addAttribute("cate", "project");
            return "view/client/project";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/dashboard";
        }
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "newProject", method = RequestMethod.POST)
    public @ResponseBody String doAddNewProject(@RequestParam(name = "title") String title, Principal principal) {
        try {
            title = title.trim();
            User u = userRepo.findByEmail(principal.getName());
            if (u != null) {
                Project p = new Project();
                p.setUser(u);
                p.setTitle(title.isEmpty() ? "New Project" : title);
                projectRepo.save(p);
                return p.getId().toString();
            } else {
                throw new Exception("Cannot find logged-in user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "newList", method = RequestMethod.POST)
    public @ResponseBody ProjectList doAddNewList(@RequestParam(name = "id") String strId,
            @RequestParam(name = "title") String title, Principal principal) {
        try {
            Long id = Long.parseLong(strId);
            Project project = projectRepo.findById(id).orElse(null);
            if (project == null) {
                throw new Exception("Cannot find project (id=" + id + ")");
            }
            if (project.getUser().getEmail().equals(principal.getName())) {
                ProjectList list = new ProjectList();
                list.setTitle(title);
                list.setProject(project);
                listRepo.save(list);
                return list.toJsonList();
            } else {
                throw new Exception("Logged-in user not own this");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "getTaskInfo", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody Task getTaskInfo(@RequestParam(name = "id") String id) {
        // TODO: Check if user own this or not
        String preId = id.substring(0, 1);
        String sufIdstr = id.substring(1, id.length());
        System.out.println("id: \"" + id + "\" | preId: \"" + preId + "\" | sufId: \"" + sufIdstr + "\"");
        try {
            if (!"t".equals(preId)) {
                throw new Exception("Wrong id.");
            }
            Long sufId = Long.parseLong(sufIdstr);
            Task t = taskRepo.findById(sufId).orElse(null);
            if (null != t) {
                return t.toJsonTask();
            } else {
                throw new Exception("Cannot find the task (id = " + sufId + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "newTask", method = RequestMethod.POST)
    public @ResponseBody Task doAddNewTask(@RequestParam(name = "id") String id) {
        // TODO: Check if user own this list or not
        String preId = id.substring(0, 1);
        String sufIdstr = id.substring(1, id.length());
        System.out.println("id: \"" + id + "\" | preId: \"" + preId + "\" | sufId: \"" + sufIdstr + "\"");
        try {
            Long sufId = Long.parseLong(sufIdstr);
            Task t = new Task();
            if ("l".equals(preId)) {
                ProjectList l = listRepo.findById(sufId).orElse(null);
                if (l == null) {
                    throw new Exception("Cannot find the list (id = " + sufId + ")");
                }
                t.setList(l);
            } else if ("t".equals(preId)) {
                Task task = taskRepo.findById(sufId).orElse(null);
                if (task == null) {
                    throw new Exception("Cannot find the task (id = " + sufId + ")");
                }
                t.setListTask(task);
            } else {
                throw new Exception("Wrong id");
            }
            t.setType("item");
            t.setTitle("New Task");
            t.setIsDone(false);
            taskRepo.save(t);
            return t.toJsonTask();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            e.printStackTrace();
            return null;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "saveToggle", method = RequestMethod.POST)
    public @ResponseBody String doSaveToggle(@RequestParam(name = "id") String id,
            @RequestParam(name = "isDone") Boolean isDone) {
        String preId = id.substring(0, 1);
        String sufIdstr = id.substring(1, id.length());
        System.out.println("id: \"" + id + "\" | preId: \"" + preId + "\" | sufId: \"" + sufIdstr + "\"");
        try {
            Long sufId = Long.parseLong(sufIdstr);
            Task t = taskRepo.findById(sufId).orElse(null);
            if (t == null) {
                throw new Exception("Cannot find the task (id = " + sufId + ")");
            }
            t.setIsDone(isDone);
            taskRepo.save(t);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "remove")
    public @ResponseBody String doRemove(@RequestParam(name = "id") String id) {
        String preId = id.substring(0, 1);
        String sufIdstr = id.substring(1, id.length());
        System.out.println("id: \"" + id + "\" | preId: \"" + preId + "\" | sufId: \"" + sufIdstr + "\"");
        // TODO: compelete remove function
        return null;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "reorder", method = RequestMethod.POST)
    public @ResponseBody String doReorder(@RequestParam(name = "id") String id,
            @RequestParam(name = "order") String order) {
        // TODO: Check if user own this or not
        String preId = id.substring(0, 1);
        String sufIdstr = id.substring(1, id.length());
        System.out.println(
                "id: \"" + id + "\" | preId: \"" + preId + "\" | sufId: \"" + sufIdstr + "\" order: '" + order + "'");
        try {
            Long sufId = Long.parseLong(sufIdstr);
            if ("l".equals(preId)) {
                ListOrder lOrder = listOrderRepo.findByProjectId(sufId);
                if (lOrder == null) {
                    lOrder = new ListOrder();
                    lOrder.setProject(new Project(sufId));
                }
                lOrder.setOrder(order);
                listOrderRepo.save(lOrder);
            } else if ("t".equals(preId)) {
                TaskOrder tOrder = taskOrderRepo.findByListId(sufId);
                if (tOrder == null) {
                    tOrder = new TaskOrder();
                    tOrder.setList(new ProjectList(sufId));
                }
                tOrder.setOrder(order);
                taskOrderRepo.save(tOrder);
            } else if ("i".equals(preId)) {
                TaskOrder iOrder = taskOrderRepo.findByTaskId(sufId);
                if (iOrder == null) {
                    iOrder = new TaskOrder();
                    iOrder.setTask(new Task(sufId));
                }
                iOrder.setOrder(order);
                taskOrderRepo.save(iOrder);
            } else {
                throw new Exception("Wrong id format");
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<ProjectList> reorder(List<ProjectList> lists, Long projectId) {
        ListOrder lOrder = listOrderRepo.findByProjectId(projectId);
        if (lOrder != null) {
            try {
                StringTokenizer token = new StringTokenizer(lOrder.getOrder(), " ");
                List<Long> listOrder = new ArrayList<>();
                while (token.hasMoreTokens()) {
                    listOrder.add(Long.parseLong(token.nextToken()));
                }
                lists.sort(Comparator.comparing(l -> listOrder.indexOf(l.getId())));

                for (ProjectList projectList : lists) {
                    TaskOrder tOrder = taskOrderRepo.findByListId(projectList.getId());
                    if (tOrder != null) {
                        token = new StringTokenizer(tOrder.getOrder(), " ");
                        List<Long> taskOrder = new ArrayList<>();
                        while (token.hasMoreTokens()) {
                            taskOrder.add(Long.parseLong(token.nextToken()));
                        }
                        projectList.getTasks().sort(Comparator.comparing(t -> taskOrder.indexOf(t.getId())));
                    }
                    for (Task task : projectList.getTasks()) {
                        TaskOrder iOrder = taskOrderRepo.findByTaskId(task.getId());
                        if (task.getTaskItems().size() > 1 && iOrder != null) {
                            token = new StringTokenizer(iOrder.getOrder(), " ");
                            List<Long> itemOrder = new ArrayList<>();
                            while (token.hasMoreTokens()) {
                                itemOrder.add(Long.parseLong(token.nextToken()));
                            }
                            task.getTaskItems().sort(Comparator.comparing(i -> itemOrder.indexOf(i.getId())));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lists;
    }
}