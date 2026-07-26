package com.gm.project.system.user.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gm.common.constant.UserConstants;
import com.gm.common.exception.BusinessException;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.common.utils.text.Convert;
import com.gm.framework.aspectj.lang.annotation.DataScope;
import com.gm.framework.shiro.service.PasswordService;
import com.gm.project.system.config.service.IConfigService;
import com.gm.project.system.post.domain.Post;
import com.gm.project.system.post.mapper.PostMapper;
import com.gm.project.system.role.domain.Role;
import com.gm.project.system.role.mapper.RoleMapper;
import com.gm.project.system.user.domain.User;
import com.gm.project.system.user.domain.UserPost;
import com.gm.project.system.user.domain.UserRole;
import com.gm.project.system.user.mapper.UserMapper;
import com.gm.project.system.user.mapper.UserPostMapper;
import com.gm.project.system.user.mapper.UserRoleMapper;

/**
 * 用户 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class UserServiceImpl implements IUserService
{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserPostMapper userPostMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IConfigService configService;

    @Autowired
    private PasswordService passwordService;

    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户Информация
     * @return 用户Информация集合Информация
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<User> selectUserList(User user)
    {
        // 生成Данные权限过滤条件
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user 用户Информация
     * @return 用户Информация集合Информация
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<User> selectAllocatedList(User user)
    {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户Информация
     * @return 用户Информация集合Информация
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<User> selectUnallocatedList(User user)
    {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过Имя пользователя查询用户
     * 
     * @param userName Имя пользователя
     * @return 用户对象Информация
     */
    @Override
    public User selectUserByLoginName(String userName)
    {
        return userMapper.selectUserByLoginName(userName);
    }

    /**
     * 通过手机号码查询用户
     * 
     * @param phoneNumber 手机号码
     * @return 用户对象Информация
     */
    @Override
    public User selectUserByPhoneNumber(String phoneNumber)
    {
        return userMapper.selectUserByPhoneNumber(phoneNumber);
    }

    /**
     * 通过邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象Информация
     */
    @Override
    public User selectUserByEmail(String email)
    {
        return userMapper.selectUserByEmail(email);
    }

    /**
     * 通过ID пользователя查询用户
     * 
     * @param userId ID пользователя
     * @return 用户对象Информация
     */
    @Override
    public User selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * 通过ID пользователя查询用户和角色关联
     * 
     * @param userId ID пользователя
     * @return 用户和角色关联列表
     */
    @Override
    public List<UserRole> selectUserRoleByUserId(Long userId)
    {
        return userRoleMapper.selectUserRoleByUserId(userId);
    }

    /**
     * 通过ID пользователяУдалить用户
     * 
     * @param userId ID пользователя
     * @return Результат
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId)
    {
        // Удалить用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // Удалить用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量Удалить用户Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    @Transactional
    public int deleteUserByIds(String ids)
    {
        Long[] userIds = Convert.toLongArray(ids);
        for (Long userId : userIds)
        {
            checkUserAllowed(new User(userId));
        }
        // Удалить用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        // Удалить用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * ДобавитьСохранить用户Информация
     * 
     * @param user 用户Информация
     * @return Результат
     */
    @Override
    @Transactional
    public int insertUser(User user)
    {
        user.randomSalt();
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(ShiroUtils.getLoginName());
        // Добавить用户Информация
        int rows = userMapper.insertUser(user);
        // Добавить用户岗位关联
        insertUserPost(user);
        // Добавить用户与Управление персонажами
        insertUserRole(user.getUserId(), user.getRoleIds());
        return rows;
    }

    /**
     * Зарегистрироваться用户Информация
     * 
     * @param user 用户Информация
     * @return Результат
     */
    @Override
    public boolean registerUser(User user)
    {
        user.setUserType(UserConstants.REGISTER_USER_TYPE);
        user.randomSalt();
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        return userMapper.insertUser(user) > 0;
    }

    /**
     * ИзменитьСохранить用户Информация
     * 
     * @param user 用户Информация
     * @return Результат
     */
    @Override
    @Transactional
    public int updateUser(User user)
    {
        Long userId = user.getUserId();
        user.setUpdateBy(ShiroUtils.getLoginName());
        // Удалить用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // Добавить用户与Управление персонажами
        insertUserRole(user.getUserId(), user.getRoleIds());
        // Удалить用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // Добавить用户与Должности
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * Изменить用户个人详细Информация
     * 
     * @param user 用户Информация
     * @return Результат
     */
    @Override
    public int updateUserInfo(User user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 用户授权角色
     * 
     * @param userId ID пользователя
     * @param roleIds 角色组
     */
    @Override
    public void insertUserAuth(Long userId, Long[] roleIds)
    {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * Изменить用户Пароль
     * 
     * @param user 用户Информация
     * @return Результат
     */
    @Override
    public int resetUserPwd(User user)
    {
        user.randomSalt();
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        return updateUserInfo(user);
    }

    /**
     * Добавить用户角色Информация
     * 
     * @param user 用户对象
     */
    public void insertUserRole(Long userId, Long[] roleIds)
    {
        if (StringUtils.isNotNull(roleIds))
        {
            // Добавить用户与Управление персонажами
            List<UserRole> list = new ArrayList<UserRole>();
            for (Long roleId : roleIds)
            {
                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * Добавить用户岗位Информация
     * 
     * @param user 用户对象
     */
    public void insertUserPost(User user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts))
        {
            // Добавить用户与Должности
            List<UserPost> list = new ArrayList<UserPost>();
            for (Long postId : user.getPostIds())
            {
                UserPost up = new UserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0)
            {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 校验ЛогинДаНет唯一
     * 
     * @param loginName Имя пользователя
     * @return
     */
    @Override
    public String checkLoginNameUnique(String loginName)
    {
        int count = userMapper.checkLoginNameUnique(loginName);
        if (count > 0)
        {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }

    /**
     * 校验手机号码ДаНет唯一
     *
     * @param user 用户Информация
     * @return
     */
    @Override
    public String checkPhoneUnique(User user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        User info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.USER_PHONE_NOT_UNIQUE;
        }
        return UserConstants.USER_PHONE_UNIQUE;
    }

    /**
     * 校验emailДаНет唯一
     *
     * @param user 用户Информация
     * @return
     */
    @Override
    public String checkEmailUnique(User user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        User info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.USER_EMAIL_NOT_UNIQUE;
        }
        return UserConstants.USER_EMAIL_UNIQUE;
    }

    /**
     * 校验用户ДаНет允许Действия
     * 
     * @param user 用户Информация
     */
    @Override
    public void checkUserAllowed(User user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new BusinessException("不允许Действия超级管理员用户");
        }
    }

    /**
     * 查询用户所属角色组
     * 
     * @param userId ID пользователя
     * @return Результат
     */
    @Override
    public String selectUserRoleGroup(Long userId)
    {
        List<Role> list = roleMapper.selectRolesByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (Role role : list)
        {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     * 
     * @param userId ID пользователя
     * @return Результат
     */
    @Override
    public String selectUserPostGroup(Long userId)
    {
        List<Post> list = postMapper.selectPostsByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (Post post : list)
        {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * Импорт用户Данные
     * 
     * @param userList 用户Данные列表
     * @param isUpdateSupport ДаНет更新支持，如果已存在，则进行更新Данные
     * @return Результат
     */
    @Override
    public String importUser(List<User> userList, Boolean isUpdateSupport)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new BusinessException("Импорт用户Данные不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String operName = ShiroUtils.getLoginName();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (User user : userList)
        {
            try
            {
                // 验证ДаНет存在这个用户
                User u = userMapper.selectUserByLoginName(user.getLoginName());
                if (StringUtils.isNull(u))
                {
                    user.setPassword(password);
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " ИмпортУспешно");
                }
                else if (isUpdateSupport)
                {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 更新Успешно");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getLoginName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getLoginName() + " ИмпортОшибка：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，ИмпортОшибка！共 " + failureNum + " 条Данные格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，Данные已全部ИмпортУспешно！共 " + successNum + " 条，Данные如下：");
        }
        return successMsg.toString();
    }

    /**
     * Статус пользователяИзменить
     * 
     * @param user 用户Информация
     * @return Результат
     */
    @Override
    public int changeStatus(User user)
    {
        return userMapper.updateUser(user);
    }
}
