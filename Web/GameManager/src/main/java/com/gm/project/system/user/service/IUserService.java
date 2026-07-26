package com.gm.project.system.user.service;

import com.gm.project.system.user.domain.User;
import com.gm.project.system.user.domain.UserRole;
import java.util.List;

/**
 * 用户 业务层
 * 
 * @author ruoyi
 */
public interface IUserService
{
    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户Информация
     * @return 用户Информация集合Информация
     */
    public List<User> selectUserList(User user);

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user 用户Информация
     * @return 用户Информация集合Информация
     */
    public List<User> selectAllocatedList(User user);

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户Информация
     * @return 用户Информация集合Информация
     */
    public List<User> selectUnallocatedList(User user);

    /**
     * 通过Имя пользователя查询用户
     * 
     * @param userName Имя пользователя
     * @return 用户对象Информация
     */
    public User selectUserByLoginName(String userName);

    /**
     * 通过手机号码查询用户
     * 
     * @param phoneNumber 手机号码
     * @return 用户对象Информация
     */
    public User selectUserByPhoneNumber(String phoneNumber);

    /**
     * 通过邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象Информация
     */
    public User selectUserByEmail(String email);

    /**
     * 通过ID пользователя查询用户
     * 
     * @param userId ID пользователя
     * @return 用户对象Информация
     */
    public User selectUserById(Long userId);

    /**
     * 通过ID пользователя查询用户和角色关联
     * 
     * @param userId ID пользователя
     * @return 用户和角色关联列表
     */
    public List<UserRole> selectUserRoleByUserId(Long userId);

    /**
     * 通过ID пользователяУдалить用户
     * 
     * @param userId ID пользователя
     * @return Результат
     */
    public int deleteUserById(Long userId);

    /**
     * 批量Удалить用户Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     * @throws Exception 异常
     */
    public int deleteUserByIds(String ids);

    /**
     * Сохранить用户Информация
     * 
     * @param user 用户Информация
     * @return Результат
     */
    public int insertUser(User user);

    /**
     * Зарегистрироваться用户Информация
     * 
     * @param user 用户Информация
     * @return Результат
     */
    public boolean registerUser(User user);

    /**
     * Сохранить用户Информация
     * 
     * @param user 用户Информация
     * @return Результат
     */
    public int updateUser(User user);

    /**
     * Изменить用户详细Информация
     * 
     * @param user 用户Информация
     * @return Результат
     */
    public int updateUserInfo(User user);

    /**
     * 用户授权角色
     * 
     * @param userId ID пользователя
     * @param roleIds 角色组
     */
    public void insertUserAuth(Long userId, Long[] roleIds);

    /**
     * Изменить用户ПарольИнформация
     * 
     * @param user 用户Информация
     * @return Результат
     */
    public int resetUserPwd(User user);

    /**
     * 校验Имя пользователяДаНет唯一
     * 
     * @param loginName Логин
     * @return Результат
     */
    public String checkLoginNameUnique(String loginName);

    /**
     * 校验手机号码ДаНет唯一
     *
     * @param user 用户Информация
     * @return Результат
     */
    public String checkPhoneUnique(User user);

    /**
     * 校验emailДаНет唯一
     *
     * @param user 用户Информация
     * @return Результат
     */
    public String checkEmailUnique(User user);

    /**
     * 校验用户ДаНет允许Действия
     * 
     * @param user 用户Информация
     */
    public void checkUserAllowed(User user);

    /**
     * 根据ID пользователя查询用户所属角色组
     * 
     * @param userId ID пользователя
     * @return Результат
     */
    public String selectUserRoleGroup(Long userId);

    /**
     * 根据ID пользователя查询用户所属岗位组
     * 
     * @param userId ID пользователя
     * @return Результат
     */
    public String selectUserPostGroup(Long userId);

    /**
     * Импорт用户Данные
     * 
     * @param userList 用户Данные列表
     * @param isUpdateSupport ДаНет更新支持，如果已存在，则进行更新Данные
     * @return Результат
     */
    public String importUser(List<User> userList, Boolean isUpdateSupport);

    /**
     * Статус пользователяИзменить
     * 
     * @param user 用户Информация
     * @return Результат
     */
    public int changeStatus(User user);
}
