package co.yiiu.web.admin;

import javax.servlet.http.HttpServletResponse;

import co.yiiu.core.base.BaseController;
import co.yiiu.module.security.model.Permission;
import co.yiiu.module.security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Controller
@RequestMapping("/admin/permission")
public class PermissionAdminController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 角色列表
     *
     * @param pid
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Integer pid, Model model) {
        if (pid == null || pid == 0) {
            model.addAttribute("childPermissions", permissionService.findAll(true));
            model.addAttribute("permissions", permissionService.findByPid(0));
        }
        else {
            model.addAttribute("childPermissions", permissionService.findByPid(pid));
            model.addAttribute("permissions", permissionService.findByPid(0));
        }
        model.addAttribute("pid", pid);
        return "admin/permission/list";
    }

    /**
     * 跳转添加页面
     *
     * @return
     */
    @GetMapping("/add")
    public String add(Integer pid, Model model) {
        model.addAttribute("pid", pid);
        model.addAttribute("permissions", permissionService.findByPid(0));
        return "admin/permission/add";
    }

    /**
     * 保存添加的权限
     *
     * @param pid
     * @param name
     * @param description
     * @param url
     * @return
     */
    @PostMapping("/add")
    public String save(Integer pid, String name, String description, String url, HttpServletResponse response) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setDescription(description);
        permission.setUrl(url);
        permission.setPid(pid == null ? 0 : pid);
        permissionService.save(permission);
        return redirect(response, "/admin/permission/list?pid=" + pid);
    }

    /**
     * 跳转编辑页面
     *
     * @return
     */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("permission", permissionService.findById(id));
        model.addAttribute("permissions", permissionService.findByPid(0));
        return "admin/permission/edit";
    }

    /**
     * 更新权限
     *
     * @param id
     * @param pid
     * @param name
     * @param description
     * @param url
     * @return
     */
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Integer id, Integer pid, String name, String description, String url,
            HttpServletResponse response) {
        Permission permission = permissionService.findById(id);
        permission.setName(name);
        permission.setDescription(description);
        permission.setUrl(url);
        permission.setPid(pid == null ? 0 : pid);
        permissionService.save(permission);
        return redirect(response, "/admin/permission/list?pid=" + pid);
    }

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, HttpServletResponse response) {
        permissionService.deleteById(id);
        return redirect(response, "/admin/permission/list");
    }
}
