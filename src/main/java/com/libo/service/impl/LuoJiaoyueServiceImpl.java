package com.libo.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libo.entity.LuoJiaoyue;
import com.libo.enums.R;
import com.libo.excelexport.LiboExcelExport;
import com.libo.excelimport.LiboExcel;
import com.libo.mapper.LuoJiaoyueMapper;
import com.libo.service.LuoJiaoyueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.libo.util.EasyExcelUtil;
import com.libo.util.ValidatorUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author junji
 * @since 2023-01-28
 */
@Service
public class LuoJiaoyueServiceImpl extends ServiceImpl<LuoJiaoyueMapper, LuoJiaoyue> implements LuoJiaoyueService {

    @Resource
    LuoJiaoyueService luoJiaoyueService;

    @Resource
    LuoJiaoyueMapper luoJiaoyueMapper;

    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param luoJiaoyue 查询实体
     * @return 所有数据
     */
    @Override
    public Page<LuoJiaoyue> selectAll(Page<LuoJiaoyue> page, LuoJiaoyue luoJiaoyue) {
        LambdaQueryWrapper<LuoJiaoyue> queryWrapper = new LambdaQueryWrapper<LuoJiaoyue>();
        // 根据姓名模糊查询
        queryWrapper.like(ObjectUtil.isNotEmpty(luoJiaoyue.getName()), LuoJiaoyue::getName, luoJiaoyue.getName());
        // 根据礼金模糊查询
        queryWrapper.like(ObjectUtil.isNotEmpty(luoJiaoyue.getMoney()), LuoJiaoyue::getMoney, luoJiaoyue.getMoney());
        // 根据地址模糊查询
        queryWrapper.like(ObjectUtil.isNotEmpty(luoJiaoyue.getAddress()), LuoJiaoyue::getAddress, luoJiaoyue.getAddress());
        // 根据礼品模糊查询
        queryWrapper.like(ObjectUtil.isNotEmpty(luoJiaoyue.getPresent()), LuoJiaoyue::getPresent, luoJiaoyue.getPresent());
        // 根据类型模糊查询
        queryWrapper.like(ObjectUtil.isNotEmpty(luoJiaoyue.getType()), LuoJiaoyue::getType, luoJiaoyue.getType());
        // 根据 地址，礼金，姓名 排序
        queryWrapper.orderByDesc(LuoJiaoyue::getAddress, LuoJiaoyue::getMoney, LuoJiaoyue::getName);

        Page<LuoJiaoyue> list = luoJiaoyueService.page(page, queryWrapper);

        return list;
    }

    /**
     * 导出
     *
     * @param
     * @return
     */
    @Override
    public void myExport(HttpServletResponse response, HttpServletRequest request) {
        String fileName = "礼簙信息";
        String sheetName="礼簙信息";
        Page<LuoJiaoyue> page = new Page<>();
        page.setSize(99999);
        LuoJiaoyue luoJiaoyue = new LuoJiaoyue();
        List<LiboExcelExport> teacherExcelList = new ArrayList<>();
        List<LuoJiaoyue> records = luoJiaoyueService.selectAll(page, luoJiaoyue).getRecords();
        for (LuoJiaoyue record : records) {
            LiboExcelExport liboExcelExport = new LiboExcelExport();
            BeanUtils.copyProperties(record,liboExcelExport);
            teacherExcelList.add(liboExcelExport);
        }

        try {
            EasyExcelUtil.writeExcel(response,teacherExcelList,fileName,sheetName,LiboExcelExport.class);
        } catch (Exception e) {
            System.out.println(e.getCause());

        }
    }

    @Override
    public void downloadExcel(HttpServletResponse response, HttpServletRequest request){
        String fileName = "礼簙信息模板";
        String sheetName="礼簙信息模板";
        List<LiboExcelExport> teacherExcelList = new ArrayList<>();
        LiboExcelExport liboExcelExport = new LiboExcelExport();
        teacherExcelList.add(liboExcelExport);
        try {
            EasyExcelUtil.writeExcel(response,teacherExcelList,fileName,sheetName,LiboExcelExport.class);
        } catch (Exception e) {
            System.out.println(e.getCause());

        }
    }

    /**
     * 通过Excel批量导入数据
     *
     * @param multipartFile excel
     * @return 是否成功
     */
    @Override
    public R importData(MultipartFile multipartFile) {
        // 校验文件类型合法性
        boolean pass = fileTypeCheck(multipartFile);
        if (!pass) {
            return R.error().code(500).message("文件格式错误！");
        }
        File file = transferToFile(multipartFile);

        // 解析excel数据
        ExcelReaderBuilder readerBuilder = EasyExcel.read(file);
        List<LiboExcel> liboExcels = readerBuilder.head(LiboExcel.class).sheet("礼簙信息模板").doReadSync();

        // 消除空白行
        liboExcels.removeIf(liboExcel -> isEmptyRow(liboExcel));
        if (CollUtil.isEmpty(liboExcels)) {
            return R.error().code(500).message("数据为空，导入失败！");
        }

        // 校验数据合法性
        Map<Integer, List<String>> errorMap = check(liboExcels);
        if (errorMap.size() > 0) {
            return R.error().code(500).message("文件解析错误！").data("errormap", errorMap);
        }

        // 将List<DriverExcel>转为List<Driver>
        List<LuoJiaoyue> luoJiaoyues = transferToOrderVo(liboExcels);

        // 批量添加
        luoJiaoyues.forEach(driver -> add(driver));

        return R.ok().data("luoJiaoyues", luoJiaoyues);
    }

    // 校验文件类型合法性
    private boolean fileTypeCheck(MultipartFile multipartFile) {
        String extName = FileNameUtil.extName(multipartFile.getOriginalFilename());

        List<String> excelTypeEnums = Arrays.asList("xls", "xlsx");
        boolean contains = excelTypeEnums.contains(extName.toLowerCase());

        return contains;
    }

    /**
     * description: MultipartFile 转 File <br>
     * date: 2022/5/11 14:20 <br>
     *
     * @param multipartFile MultipartFile类对象
     * @return
     */
    private File transferToFile(MultipartFile multipartFile) {
        // 选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file = File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * description: 消除空白行 <br>
     * date: 2022/5/23 18:07 <br>
     *
     * @return
     */
    private boolean isEmptyRow(LiboExcel liboExcel) {
        return ObjectUtil.isAllEmpty(
                liboExcel.getName(),
                liboExcel.getMoney(),
                liboExcel.getAddress(),
                liboExcel.getPresent(),
                liboExcel.getType()
        );
    }

    /**
     * description: 报错字段 为空提示信息排序规则 <br>
     *
     * @return 报错字段提示信息排序规则
     * @NotNull(message = "姓名为空")
     */
    private List<String> getRank() {
        return Arrays.asList(
                "name",
                "money",
                "address",
                "present",
                "type"
        );
    }

    /**
     * description: excel数据校验 <br>
     *
     * @param liboExcels excel表对象
     * @return
     */
    private Map<Integer, List<String>> check(List<LiboExcel> liboExcels) {
        Map errorMap = new HashMap<Integer, List<String>>(10);
        for (int i = 0; i < liboExcels.size(); i++) {
            LiboExcel driverExcel = liboExcels.get(i);

            LuoJiaoyue luoJiaoyue = new LuoJiaoyue();
            try {
                List<String> reason = new ArrayList<>(10);

                List<String> rank = getRank();
                List<ConstraintViolation<LiboExcel>> constraintViolations = ValidatorUtil.validateAll(driverExcel)
                        .stream()
                        .sorted((o1, o2) -> rank.indexOf(o1.getPropertyPath().toString()) > rank.indexOf(o2.getPropertyPath().toString()) ? 1 : -1)
                        .collect(Collectors.toList());
                Iterator<ConstraintViolation<LiboExcel>> iterator = constraintViolations.iterator();
                while (iterator.hasNext()) {
                    ConstraintViolation<LiboExcel> next = iterator.next();
                    reason.add(next.getMessage());
                }

                if (reason.size() > 0) {
                    // <单元格行数, 错误原因>
                    // 单元格行数：i + 1
                    errorMap.put(i + 1, reason);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return errorMap;
    }

    /**
     * 通过Driver对象插入单条数据
     *
     * @param luoJiaoyue Driver对象
     * @return 单条数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(LuoJiaoyue luoJiaoyue) {
        if (ObjectUtil.isEmpty(luoJiaoyue)) {
            return;
        }

        luoJiaoyueMapper.insert(luoJiaoyue);
    }

    /**
     * description: 将List<DriverExcel>转为List<Driver> <br>
     *
     * @param liboExcels excel表对象集合
     * @return OrderVo对象集合
     */
    private List<LuoJiaoyue> transferToOrderVo(List<LiboExcel> liboExcels) {
        List<LuoJiaoyue> luoJiaoyues = new ArrayList<>();
        for (LiboExcel liboExcel : liboExcels) {
            LuoJiaoyue luoJiaoyue = new LuoJiaoyue();
            BeanUtils.copyProperties(liboExcel,luoJiaoyue);
            luoJiaoyues.add(luoJiaoyue);
        }

        return luoJiaoyues;
    }

}
