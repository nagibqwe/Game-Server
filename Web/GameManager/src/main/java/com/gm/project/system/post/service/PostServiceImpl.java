package com.gm.project.system.post.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.common.constant.UserConstants;
import com.gm.common.exception.BusinessException;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.common.utils.text.Convert;
import com.gm.project.system.post.domain.Post;
import com.gm.project.system.post.mapper.PostMapper;
import com.gm.project.system.user.mapper.UserPostMapper;

/**
 * 岗位Информация 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class PostServiceImpl implements IPostService
{
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserPostMapper userPostMapper;

    /**
     * 查询岗位Информация集合
     * 
     * @param post 岗位Информация
     * @return 岗位Информация集合
     */
    @Override
    public List<Post> selectPostList(Post post)
    {
        return postMapper.selectPostList(post);
    }

    /**
     * 查询所有岗位
     * 
     * @return 岗位列表
     */
    @Override
    public List<Post> selectPostAll()
    {
        return postMapper.selectPostAll();
    }

    /**
     * 根据ID пользователя查询岗位
     * 
     * @param userId ID пользователя
     * @return 岗位列表
     */
    @Override
    public List<Post> selectPostsByUserId(Long userId)
    {
        List<Post> userPosts = postMapper.selectPostsByUserId(userId);
        List<Post> posts = postMapper.selectPostAll();
        for (Post post : posts)
        {
            for (Post userRole : userPosts)
            {
                if (post.getPostId().longValue() == userRole.getPostId().longValue())
                {
                    post.setFlag(true);
                    break;
                }
            }
        }
        return posts;
    }

    /**
     * 通过岗位ID查询岗位Информация
     * 
     * @param postId 岗位ID
     * @return 角色对象Информация
     */
    @Override
    public Post selectPostById(Long postId)
    {
        return postMapper.selectPostById(postId);
    }

    /**
     * 批量Удалить岗位Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @throws Exception
     */
    @Override
    public int deletePostByIds(String ids) throws BusinessException
    {
        Long[] postIds = Convert.toLongArray(ids);
        for (Long postId : postIds)
        {
            Post post = selectPostById(postId);
            if (countUserPostById(postId) > 0)
            {
                throw new BusinessException(String.format("%1$s已分配,不能Удалить", post.getPostName()));
            }
        }
        return postMapper.deletePostByIds(postIds);
    }

    /**
     * ДобавитьСохранить岗位Информация
     * 
     * @param post 岗位Информация
     * @return Результат
     */
    @Override
    public int insertPost(Post post)
    {
        post.setCreateBy(ShiroUtils.getLoginName());
        return postMapper.insertPost(post);
    }

    /**
     * ИзменитьСохранить岗位Информация
     * 
     * @param post 岗位Информация
     * @return Результат
     */
    @Override
    public int updatePost(Post post)
    {
        post.setUpdateBy(ShiroUtils.getLoginName());
        return postMapper.updatePost(post);
    }

    /**
     * 通过岗位ID查询岗位使用数量
     * 
     * @param postId 岗位ID
     * @return Результат
     */
    @Override
    public int countUserPostById(Long postId)
    {
        return userPostMapper.countUserPostById(postId);
    }

    /**
     * 校验岗位НазваниеДаНет唯一
     * 
     * @param post 岗位Информация
     * @return Результат
     */
    @Override
    public String checkPostNameUnique(Post post)
    {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        Post info = postMapper.checkPostNameUnique(post.getPostName());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue())
        {
            return UserConstants.POST_NAME_NOT_UNIQUE;
        }
        return UserConstants.POST_NAME_UNIQUE;
    }

    /**
     * 校验岗位编码ДаНет唯一
     * 
     * @param post 岗位Информация
     * @return Результат
     */
    @Override
    public String checkPostCodeUnique(Post post)
    {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        Post info = postMapper.checkPostCodeUnique(post.getPostCode());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue())
        {
            return UserConstants.POST_CODE_NOT_UNIQUE;
        }
        return UserConstants.POST_CODE_UNIQUE;
    }
}
