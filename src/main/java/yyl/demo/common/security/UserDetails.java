package yyl.demo.common.security;

import java.io.Serializable;

import com.github.relucent.base.common.constant.StringConstants;

/**
 * 用户登录对象
 */
@SuppressWarnings("serial")
public class UserDetails implements Serializable {

    // ========================================Fields=========================================
    /** 用户ID */
    private String id;
    /** 用户名 */
    private String username;
    /** 用户姓名 */
    private String realname;
    /** 所属机构 */
    private String organizationId;
    /** 所属部门 */
    private String departmentId;
    /** 所属角色 */
    private String[] roleIds = new String[0];
    /** 拥有权限 */
    private String[] permissionIds = new String[0];

    // ========================================Constants======================================
    /** 未登录用户 */
    public static final UserDetails NONE;
    static {
        NONE = new UserDetails();
        NONE.setId(StringConstants.EMPTY);
        NONE.setUsername(StringConstants.EMPTY);
        NONE.setRealname(StringConstants.EMPTY);
        NONE.setOrganizationId(StringConstants.EMPTY);
        NONE.setDepartmentId(StringConstants.EMPTY);
    }

    // ========================================Methods========================================
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    public String[] getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(String[] permissionIds) {
        this.permissionIds = permissionIds;
    }
}
