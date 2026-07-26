package com.gm.project.system.post.service;

import java.util.List;
import com.gm.project.system.post.domain.Post;

/**
 * 岗位Информация 服务层
 * 
 * @author ruoyi
 */
public interface IPostService
{
    /**
     * 查询岗位Информация集合
     * 
     * @param post 岗位Информация
     * @return 岗位Информация集合
     */
    public List<Post> selectPostList(Post post);

    /**
     * 查询所有岗位
     * 
     * @return 岗位列表
     */
    public List<Post> selectPostAll();

    /**
     * 根据ID пользователя查询岗位
     * 
     * @param userId ID пользователя
     * @return 岗位列表
     */
    public List<Post> selectPostsByUserId(Long userId);

    /**
     * 通过岗位ID查询岗位Информация
     * 
     * @param postId 岗位ID
     * @return 角色对象Информация
     */
    public Post selectPostById(Long postId);

    /**
     * 批量Удалить岗位Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     * @throws Exception 异常
     */
    public int deletePostByIds(String ids) throws Exception;

    /**
     * ДобавитьСохранить岗位Информация
     * 
     * @param post 岗位Информация
     * @return Результат
     */
    public int insertPost(Post post);

    /**
     * ИзменитьСохранить岗位Информация
     * 
     * @param post 岗位Информация
     * @return Результат
     */
    public int updatePost(Post post);

    /**
     * 通过岗位ID查询岗位使用数量
     * 
     * @param postId 岗位ID
     * @return Результат
     */
    public int countUserPostById(Long postId);

    /**
     * 校验岗位Название
     * 
     * @param post 岗位Информация
     * @return Результат
     */
    public String checkPostNameUnique(Post post);

    /**
     * 校验岗位编码
     * 
     * @param post 岗位Информация
     * @return Результат
     */
    public String checkPostCodeUnique(Post post);
}
