package cn.edu.guet.controller;

import cn.edu.guet.bean.Permission;
import cn.edu.guet.mvc.annotation.Controller;
import cn.edu.guet.mvc.annotation.RequestMapping;
import cn.edu.guet.service.IPermissionService;
import cn.edu.guet.service.impl.PermissionServiceImpl;

import java.util.List;

@Controller
public class PermissionController {
    @RequestMapping("/permissionList")
    public List<Permission> getPermission(){
        IPermissionService permissionService=new PermissionServiceImpl();
        return permissionService.getPermission();
    }
}
