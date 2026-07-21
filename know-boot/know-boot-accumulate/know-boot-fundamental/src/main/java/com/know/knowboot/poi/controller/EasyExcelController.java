package com.know.knowboot.poi.controller;

import com.know.knowboot.poi.easyexcel.EasyExcelUtils;
import com.know.knowboot.poi.easyexcel.ExcelVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/easyExcel")
public class EasyExcelController {

    /**
     * 准备测试数据 ExcelService.getExcelExportData()
     * @param response
     * @throws Exception
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<ExcelVo> data = getExcelExportData();
        EasyExcelUtils.exportExcel(data,response);
    }

    /**
     * 准备测试数据 ExcelService.getExcelExportData()
     * @param response
     * @throws Exception
     */
    @GetMapping("/export1")
    public void export1(HttpServletResponse response) throws Exception {
        List<ExcelVo> data = getExcelExportData();
        List<Integer> columnIndexs = new ArrayList<>();
        columnIndexs.add(1);
        EasyExcelUtils.exportExcel(data,ExcelVo.class,"aaaa","aaa",1,columnIndexs,response);
    }

    /**
     * 通过前端传值测试，前端传的excelVo 字段和ExcelVo类里面的字段对应
     * @param excelVo
     * @param response
     * @throws Exception
     */
    @GetMapping("/exportTwo")
    public void aliExportDetail(@RequestBody List<ExcelVo> excelVo, HttpServletResponse response) throws Exception {
        EasyExcelUtils.exportExcel(excelVo,response);
    }

    public static List<ExcelVo> getExcelExportData() {
        ExcelVo excelVo = new ExcelVo();
        List<ExcelVo> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            excelVo.setHeadOne("qqqq");
            excelVo.setHeadTwoCome("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
            excelVo.setHeadTwoOn("eee");
            excelVo.setHeadThreeCome("rrr");
            excelVo.setHeadThreeOn("bgbd");
            excelVo.setHeadFourCome("qoqo");
            excelVo.setHeadFourOn("规格");
            excelVo.setHeadFiveCome("项链");
            excelVo.setHeadFiveOn("等等");
            excelVo.setHeadSixCome("测试");
            excelVo.setHeadSixOn("测试");
            excelVo.setHeadSeven("测试");
            list.add(excelVo);
        }

        return  list;
    }

    /**
     * 导入数据
     * @param multipartFile
     * @throws IOException
     */
    @PostMapping("/importTestDatas")
    public void importTestDatas(@RequestParam("multipartFile") MultipartFile multipartFile) throws IOException {

        List<ExcelVo> ImportTestDatasReqList = EasyExcelUtils.readExcel(multipartFile.getInputStream(), ExcelVo.class, new EasyExcelUtils.ExcelListener<>());
        ImportTestDatasReqList.forEach(System.out::println);

    }

    /**
     * 导出
     * @param response
     */
    @GetMapping("/testExport")
    public void testExport(HttpServletResponse response) {
        try {
            List<ExcelVo> entities = getExcelExportData();
            EasyExcelUtils.writeEasyExcel(response, ExcelVo.class, entities, "列表导出");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}