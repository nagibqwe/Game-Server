package com.gm.project.system.user.mapper;

import com.gm.project.system.user.domain.User;
import java.util.List;

/**
 * 用户表 Данные层
 * 
 * @author ruoyi
 */
public interface UserMapper
{
    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户Информация
     * @return 用户Информация集合Информация
     */
    public List<User> selectUserList(User user);

    /**
     * 根据条件分页查询未已配用户角色列表
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
     */
    public int deleteUserByIds(Long[] ids);

    /**
     * Изменить用户Информация
     * 
     * @param user 用户Информация
     * @return Результат
     */
    public int updateUser(User user);

    /**
     * Добавить用户Информация
     * 
     * @param user 用户Информация
     * @return Результат
     */
    public int insertUser(User user);

    /**
     * 校验Имя пользователяДаНет唯一
     * 
     * @param loginName Логин
     * @return Результат
     */
    public int checkLoginNameUnique(String loginName);

    /**
     * 校验手机号码ДаНет唯一
     *
     * @param phonenumber 手机号码
     * @return Результат
     */
    public User checkPhoneUnique(String phonenumber);

    /**
     * 校验emailДаНет唯一
     *
     * @param email 用户邮箱
     * @return Результат
     */
    public User checkEmailUnique(String email);
}
