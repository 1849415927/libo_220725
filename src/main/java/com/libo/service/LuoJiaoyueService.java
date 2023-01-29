package com.libo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libo.entity.LuoJiaoyue;
import com.baomidou.mybatisplus.extension.service.IService;
import com.libo.enums.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author junji
 * @since 2023-01-28
 */
public interface LuoJiaoyueService extends IService<LuoJiaoyue> {

    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param luoJiaoyue 查询实体
     * @return 所有数据
     */
    Page<LuoJiaoyue> selectAll(Page<LuoJiaoyue> page, LuoJiaoyue luoJiaoyue);

    /**
     * 导出
     *
     * @param
     * @return
     */
    void myExport(HttpServletResponse response, HttpServletRequest request);

    /**
     * 通过Excel批量导入数据
     *
     * @param multipartFile excel
     * @return 是否成功
     */
    R importData(MultipartFile multipartFile);

    /**
     * excel模板下载
     *
     * @param
     * @return
     */
    void downloadExcel(HttpServletResponse response, HttpServletRequest request);

}
