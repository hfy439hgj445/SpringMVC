package cn.edu.guet.dao.impl;

import cn.edu.guet.bean.Permission;
import cn.edu.guet.bean.User;
import cn.edu.guet.dao.IUserDao;
import cn.edu.guet.util.PasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements IUserDao {
    @Override
    public User login(String username, String password) {
        Connection conn = null;
        String user = "root";// mysql的用户名
        String pwd = "Mysql_123456";
        String url = "jdbc:mysql://118.190.148.144:3306/huangengyu?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String sql = "SELECT id,nick_name,password,salt FROM sys_user WHERE name=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pwd);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);// 给占位符赋值
            rs = pstmt.executeQuery();
            if (rs.next()) {
                // 如果满足这个条件，说明什么？说明：用户名是正确的
                String encPass = rs.getString("password");
                String salt = rs.getString("salt");

                PasswordEncoder encoderMd5 = new PasswordEncoder(salt, "MD5");
                boolean result = encoderMd5.matches(encPass, password);
                if (result) {
                    User loginUser=new User();
                    loginUser.setId(rs.getString("id"));
                    loginUser.setNickName(rs.getString("nick_name"));
                    return loginUser;
                } else {
                    return null;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<Permission> getMenuByUserId(String userId) {
        Connection conn = null;
        String user = "root";// mysql的用户名
        String pwd = "Mysql_123456";
        String url = "jdbc:mysql://118.190.148.144:3306/huangengyu?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String sql = "SELECT p.*\n" +
                "FROM user_role ur,role_permission rp,permission p\n" +
                "WHERE ur.role_id=rp.role_id\n" +
                "AND rp.permission_id=p.id\n" +
                "AND user_id=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Permission> permissionList=new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pwd);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);// 给占位符赋值
            rs = pstmt.executeQuery();
            // rs.next：游标向下移动
            while(rs.next()){
                Permission permission=new Permission();
                permission.setId(rs.getString("id"));
                permission.setpId(rs.getString("pid"));
                permission.setName(rs.getString("name"));
                permission.setUrl(rs.getString("url"));
                permission.setIcon(rs.getString("icon"));
                permission.setTarget(rs.getString("target"));
                permission.setIsParent(rs.getString("isParent"));

                permissionList.add(permission);//把芒果放入麻袋
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return permissionList;//返回集合（把装满芒果的麻袋，扛到车上）
    }
}
