package com.gm.project.system.role.domain;

import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.gm.framework.web.domain.BaseEntity;

/**
 * 角色表 sys_role
 * 
 * @author ruoyi
 */
public class Role extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID персонажа */
    @Excel(name = "角色№", cellType = ColumnType.NUMERIC)
    private Long roleId;

    /** Имя персонажа */
    @Excel(name = "Имя персонажа")
    private String roleName;

    /** 角色权限 */
    @Excel(name = "角色权限")
    private String roleKey;

    /** 角色Сортировка */
    @Excel(name = "角色Сортировка")
    private String roleSort;

    /** Данные范围（1：所有Данные权限；2：自定义Данные权限；3：本部门Данные权限；4：本部门及以下Данные权限） */
    @Excel(name = "Данные范围", readConverterExp = "1=所有Данные权限,2=自定义Данные权限,3=本部门Данные权限,4=本部门及以下Данные权限")
    private String dataScope;

    /** 角色Статус（0Норма 1Отключено） */
    @Excel(name = "角色Статус", readConverterExp = "0=Норма,1=Отключено")
    private String status;

    /** Удалить标志（0代表存在 2代表Удалить） */
    private String delFlag;

    /** 用户ДаНет存在此角色标识 默认不存在 */
    private boolean flag = false;

    /** 菜单组 */
    private Long[] menuIds;

    /** 部门组（Данные权限） */
    private Long[] deptIds;

    public Role()
    {

    }

    public Role(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.roleId);
    }

    public static boolean isAdmin(Long roleId)
    {
        return roleId != null && 1L == roleId;
    }

    public String getDataScope()
    {
        return dataScope;
    }

    public void setDataScope(String dataScope)
    {
        this.dataScope = dataScope;
    }

    @NotBlank(message = "Имя персонажа不能为空")
    @Size(min = 0, max = 30, message = "Имя персонажа长度不能超过30个字符")
    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    public String getRoleKey()
    {
        return roleKey;
    }

    public void setRoleKey(String roleKey)
    {
        this.roleKey = roleKey;
    }

    @NotBlank(message = "Порядок отображения обязателен")
    public String getRoleSort()
    {
        return roleSort;
    }

    public void setRoleSort(String roleSort)
    {
        this.roleSort = roleSort;
    }

    public String getStatus()
    {
        return status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    public Long[] getMenuIds()
    {
        return menuIds;
    }

    public void setMenuIds(Long[] menuIds)
    {
        this.menuIds = menuIds;
    }

    public Long[] getDeptIds()
    {
        return deptIds;
    }

    public void setDeptIds(Long[] deptIds)
    {
        this.deptIds = deptIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("roleKey", getRoleKey())
            .append("roleSort", getRoleSort())
            .append("dataScope", getDataScope())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
