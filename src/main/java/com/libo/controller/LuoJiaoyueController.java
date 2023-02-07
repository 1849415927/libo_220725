package com.libo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libo.entity.LuoJiaoyue;
import com.libo.enums.R;
import com.libo.service.LuoJiaoyueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author junji
 * @since 2023-01-28
 */
@RestController
@Api(tags = "礼簙管理")
@RequestMapping("/luoJiaoyue")
public class LuoJiaoyueController {

    @Autowired
    private LuoJiaoyueService luoJiaoyueService;

    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param luoJiaoyue 查询实体
     * @return 所有数据
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页列表查询")
    public R selectAll(Page<LuoJiaoyue> page, @RequestBody LuoJiaoyue luoJiaoyue) {
        Page<LuoJiaoyue> pages = this.luoJiaoyueService.selectAll(page, luoJiaoyue);
        return R.ok().data("list", pages);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过主键查询单条数据")
    public R selectOne(@PathVariable Serializable id) {
        LuoJiaoyue byId = this.luoJiaoyueService.getById(id);
        return R.ok().data("detail", byId);
    }

    /**
     * 新增数据
     *
     * @param luoJiaoyue 实体对象
     * @return 新增结果
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    public R insert(@RequestBody LuoJiaoyue luoJiaoyue) {
        boolean save = this.luoJiaoyueService.save(luoJiaoyue);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 修改数据
     *
     * @param luoJiaoyue 实体对象
     * @return 修改结果
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改数据")
    public R update(@RequestBody LuoJiaoyue luoJiaoyue) {
        boolean updateById = this.luoJiaoyueService.updateById(luoJiaoyue);
        if (updateById) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 批量删除
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除")
    public R delete(@RequestParam("idList") List<Long> idList) {
        boolean removeByIds = this.luoJiaoyueService.removeByIds(idList);
        if (removeByIds) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 导出并下载文件
     *
     * @param
     * @return 是否成功
     */
    @ApiOperation(value = "导出并下载文件")
    @PostMapping("/export_driver_excel")
    public void exportEmployeeExcel(@RequestBody LuoJiaoyue luoJiaoyue, HttpServletResponse response) throws IOException {
        luoJiaoyueService.myExport(luoJiaoyue, response);
    }

    /**
     * 通过Excel批量导入数据
     *
     * @param file MultipartFile对象
     * @return 单条数据
     */
    @ApiOperation(value = "批量导入")
    @PostMapping("/importData")
    public R importData(@RequestBody MultipartFile file) {
        R data = luoJiaoyueService.importData(file);
        return data;
    }

    /**
     * excel模板下载
     *
     * @param
     * @return
     */
    @ApiOperation(value = "excel模板下载")
    @RequestMapping(value = "/templateDownload", method = RequestMethod.GET)
    public void downloadExcel(HttpServletResponse response) throws IOException{
        luoJiaoyueService.downloadExcel(response);
    }

}

