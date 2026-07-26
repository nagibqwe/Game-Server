package com.gm.project.system.user.mapper;

import java.util.List;
import com.gm.project.system.user.domain.UserPost;

/**
 * 用户与岗位关联表 Данные层
 * 
 * @author ruoyi
 */
public interface UserPostMapper
{
    /**
     * 通过ID пользователяУдалить用户和岗位关联
     * 
     * @param userId ID пользователя
     * @return Результат
     */
    public int deleteUserPostByUserId(Long userId);
    
    /**
     * 通过岗位ID查询岗位使用数量
     * 
     * @param postId 岗位ID
     * @return Результат
     */
    public int countUserPostById(Long postId);
    
    /**
     * 批量Удалить用户和岗位关联
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteUserPost(Long[] ids);

    /**
     * 批量Добавить用户岗位Информация
     * 
     * @param userPostList 用户角色列表
     * @return Результат
     */
    public int batchUserPost(List<UserPost> userPostList);
}
