package com.gm.project.system.post.mapper;

import java.util.List;
import com.gm.project.system.post.domain.Post;

/**
 * 岗位Информация Данные层
 * 
 * @author ruoyi
 */
public interface PostMapper
{
    /**
     * 查询岗位Данные集合
     * 
     * @param post 岗位Информация
     * @return 岗位Данные集合
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
     */
    public int deletePostByIds(Long[] ids);

    /**
     * Изменить岗位Информация
     * 
     * @param post 岗位Информация
     * @return Результат
     */
    public int updatePost(Post post);

    /**
     * Добавить岗位Информация
     * 
     * @param post 岗位Информация
     * @return Результат
     */
    public int insertPost(Post post);

    /**
     * 校验岗位Название
     * 
     * @param postName 岗位Название
     * @return Результат
     */
    public Post checkPostNameUnique(String postName);

    /**
     * 校验岗位编码
     * 
     * @param postCode 岗位编码
     * @return Результат
     */
    public Post checkPostCodeUnique(String postCode);
}
