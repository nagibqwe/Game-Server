package com.gm.project.system.notice.mapper;

import com.gm.project.system.notice.domain.Notice;
import java.util.List;

/**
 * 公告 Данные层
 * 
 * @author ruoyi
 */
public interface NoticeMapper
{
    /**
     * 查询公告Информация
     * 
     * @param noticeId 公告ID
     * @return 公告Информация
     */
    public Notice selectNoticeById(Long noticeId);

    /**
     * 查询公告列表
     * 
     * @param notice 公告Информация
     * @return 公告集合
     */
    public List<Notice> selectNoticeList(Notice notice);

    /**
     * Добавить公告
     * 
     * @param notice 公告Информация
     * @return Результат
     */
    public int insertNotice(Notice notice);

    /**
     * Изменить公告
     * 
     * @param notice 公告Информация
     * @return Результат
     */
    public int updateNotice(Notice notice);

    /**
     * 批量Удалить公告
     * 
     * @param noticeIds 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteNoticeByIds(String[] noticeIds);
}