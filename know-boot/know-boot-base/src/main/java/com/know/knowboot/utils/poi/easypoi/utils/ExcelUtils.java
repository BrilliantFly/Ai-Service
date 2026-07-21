package com.know.knowboot.utils.poi.easypoi.utils;

import cn.afterturn.easypoi.cache.manager.POICacheManager;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.ExcelXorHtmlUtil;
import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.afterturn.easypoi.word.parse.ParseWord07;
import com.know.knowboot.utils.poi.easypoi.enums.ExcelEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Excel导入导出工具类
 */

public class ExcelUtils {

    //************************************************************************** Excel导入开始 *********************************************************************************

    /**
     * excel 导入
     *
     * @param file       excel文件
     * @param sheetIndex sheet索引（从0开始）
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, int sheetIndex, Class<T> pojoClass) throws IOException {
        return importExcel(file, sheetIndex, ExcelEnum.TITLE_ROWS.getValue(), ExcelEnum.HEADER_ROWS.getValue(), pojoClass);
    }

    /**
     * excel 导入
     *
     * @param file       上传的文件
     * @param sheetIndex sheet索引（从0开始）
     * @param titleRows  表格内数据标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, int sheetIndex, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (file == null) {
            return null;
        }
        try {
            return importExcel(file.getInputStream(), sheetIndex, titleRows, headerRows, pojoClass);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param inputStream 文件输入流
     * @param sheetIndex  sheet索引（从0开始）
     * @param titleRows   表格内数据标题行
     * @param headerRows  表头行
     * @param pojoClass   pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(InputStream inputStream, int sheetIndex, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setStartSheetIndex(sheetIndex);
        params.setHeadRows(headerRows);
        params.setSaveUrl("/excel/");
        params.setNeedSave(true);
        try {
            return ExcelImportUtil.importExcel(inputStream, pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("excel文件不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param filePath   excel文件路径
     * @param sheetIndex sheet索引（从0开始）
     * @param titleRows  表格内数据标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(String filePath, int sheetIndex, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedSave(true);
        params.setSaveUrl("/excel/");
        try {
            return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("模板不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入，有错误信息
     *
     * @param file      上传的文件
     * @param pojoClass pojo类型
     * @param <T>
     * @return
     */
    public static <T> ExcelImportResult<T> importExcelMore(MultipartFile file, Class<T> pojoClass) throws IOException {
        if (file == null) {
            return null;
        }
        try {
            return importExcelMore(file.getInputStream(), pojoClass);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param inputStream 文件输入流
     * @param pojoClass   pojo类型
     * @param <T>
     * @return
     */
    private static <T> ExcelImportResult<T> importExcelMore(InputStream inputStream, Class<T> pojoClass) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(1);//表格内数据标题行
        params.setHeadRows(1);//表头行
        params.setSaveUrl("/excel/");
        params.setNeedSave(true);
        params.setNeedVerify(true);
        try {
            return ExcelImportUtil.importExcelMore(inputStream, pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("excel文件不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    //************************************************************************** Excel导入结束 *********************************************************************************

    //************************************************************************** Excel导出开始 *********************************************************************************

    /**
     * 默认的 excel 导出
     *
     * @param list     数据列表
     * @param fileName 导出时的excel名称
     * @param response
     */
    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        //把数据添加到excel表格中
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        DownLoadUtils.downLoadExcel(fileName, response, workbook);
    }

    /**
     * 根据模板生成excel后导出
     *
     * @param templatePath 模板路径
     * @param map          数据集合
     * @param fileName     文件名
     * @param response
     * @throws IOException
     */
    public static void exportExcel(TemplateExportParams templatePath, Map<String, Object> map, String fileName, HttpServletResponse response) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(templatePath, map);
        DownLoadUtils.downLoadExcel(fileName, response, workbook);
    }

    /**
     * excel 导出
     *
     * @param list     数据列表
     * @param fileName 导出时的excel名称
     * @param response
     */
    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, fileName, response);
    }


    /**
     * excel 导出
     *
     * @param list         数据列表
     * @param pojoClass    pojo类型
     * @param fileName     导出时的excel名称
     * @param response
     * @param exportParams 导出参数（标题、sheet名称、是否创建表头，表格类型）
     */
    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) throws IOException {
        //把数据添加到excel表格中
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        DownLoadUtils.downLoadExcel(fileName, response, workbook);
    }

    /**
     * excel 导出
     *
     * @param list         数据列表
     * @param pojoClass    pojo类型
     * @param fileName     导出时的excel名称
     * @param exportParams 导出参数（标题、sheet名称、是否创建表头，表格类型）
     * @param response
     */
    public static void exportExcel(List<?> list, Class<?> pojoClass, String fileName, ExportParams exportParams, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    /**
     * excel 导出
     *
     * @param list      数据列表
     * @param title     表格内数据标题
     * @param sheetName sheet名称
     * @param pojoClass pojo类型
     * @param fileName  导出时的excel名称
     * @param response
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName, ExcelType.XSSF));
    }

    /**
     * excel 导出
     *
     * @param list           数据列表
     * @param title          表格内数据标题
     * @param sheetName      sheet名称
     * @param pojoClass      pojo类型
     * @param fileName       导出时的excel名称
     * @param isCreateHeader 是否创建表头
     * @param response
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    //************************************************************************** Excel导出结束 *********************************************************************************


    //************************************************************************** Word导出开始 *********************************************************************************

    /**
     * word模板导出
     *
     * @param map
     * @param templatePath
     * @param fileName
     * @param response
     * @throws Exception
     */
    public static void WordTemplateExport(Map<String, Object> map, String templatePath, String fileName, HttpServletResponse response) throws Exception {
        XWPFDocument doc = WordExportUtil.exportWord07(templatePath, map);
        DownLoadUtils.downLoadWord(fileName, response, doc);
    }

    /**
     * word模板导出多页
     *
     * @param list
     * @param templatePath
     * @param fileName
     * @param response
     * @throws Exception
     */
    public static void WordTemplateExportMorePage(List<Map<String, Object>> list, String templatePath, String fileName, HttpServletResponse response) throws Exception {
        XWPFDocument doc = new ParseWord07().parseWord(templatePath, list);
        DownLoadUtils.downLoadWord(fileName, response, doc);
    }

    //************************************************************************** Word结束开始 *********************************************************************************


    /**
     * excel转html预览
     *
     * @param filePath 文件路径
     * @param response
     * @throws Exception
     */
    public static void excelToHtml(String filePath, HttpServletResponse response) throws Exception {
        ExcelToHtmlParams params = new ExcelToHtmlParams(WorkbookFactory.create(POICacheManager.getFile(filePath)), true);
        response.getOutputStream().write(ExcelXorHtmlUtil.excelToHtml(params).getBytes());
    }


}
