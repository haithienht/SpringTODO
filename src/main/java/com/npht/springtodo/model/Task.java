package com.npht.springtodo.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "detail", length = 255, nullable = true)
    private String detail;

    @Column(name = "`order`", nullable = true)
    private Integer order;

    @Column(name = "type", length = 20, nullable = false)
    private String type;

    @Column(name = "is_done")
    private Boolean isDone;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", referencedColumnName = "id")
    private ProjectList list; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_list_id", referencedColumnName = "id")
    private Task listTask; 

    @OneToMany(mappedBy = "listTask", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Task> taskItems;
  
    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return String return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return String return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * @return Integer return the order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * @return String return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Boolean return the isDone
     */
    public Boolean getIsDone() {
        return isDone;
    }

    /**
     * @param isDone the isDone to set
     */
    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * @return Date return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return Date return the updatedDate
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * @param updatedDate the updatedDate to set
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * @return ProjectList return the list
     */
    public ProjectList getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(ProjectList list) {
        this.list = list;
    }

    /**
     * @return Task return the listTask
     */
    public Task getListTask() {
        return listTask;
    }

    /**
     * @param listTask the listTask to set
     */
    public void setListTask(Task listTask) {
        this.listTask = listTask;
    }

    /**
     * @return List<Task> return the taskItems
     */
    public List<Task> getTaskItems() {
        return taskItems;
    }

    /**
     * @param taskItems the taskItems to set
     */
    public void setTaskItems(List<Task> taskItems) {
        this.taskItems = taskItems;
    }

}