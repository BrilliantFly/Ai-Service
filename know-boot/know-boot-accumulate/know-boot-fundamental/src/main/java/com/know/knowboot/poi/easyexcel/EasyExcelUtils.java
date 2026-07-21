package com.know.knowboot.poi.easyexcel;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.merge.OnceAbsoluteMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class EasyExcelUtils {

    // ************************************************************* excel导出 ***********************************************************************
    /**
     * 导出excel - 根据条件重置单元格内容字体颜色
     * @param objects 数据列表
     * @param clazz 数据对应实体类
     * @param fileName 文件名称
     * @param sheetName sheet名称
     * @param startRowIndex 标注开始行
     * @param columnIndexs 标注列集合
     * @param response
     * @throws IOException
     */
    public static void exportExcel(List<?> objects, Class clazz, String fileName, String sheetName,Integer startRowIndex,List<Integer> columnIndexs, HttpServletResponse response) throws IOException {

        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        // 设置防止中文名乱码
        String exportName = URLEncoder.encode(fileName, "utf-8");

        response.setHeader("Content-Type", "video/x-msvideo");
        // 文件下载方式(附件下载还是在当前浏览器打开)
        response.setHeader("Content-disposition", "attachment;filename=" + exportName + ".xlsx");

        // 向Excel中写入数据
        EasyExcel
                .write(response.getOutputStream(), clazz)
                .sheet(sheetName).registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        if (CollectionUtils.isNotEmpty(columnIndexs)){
                            Cell cell = context.getCell();
                            // 获取行号 context.getRowIndex()
                            // 获取列号 cell.getColumnIndex()
                            if (context.getRowIndex() > startRowIndex && columnIndexs.contains(cell.getColumnIndex())) {
                                WriteCellStyle writeCellStyle = context.getFirstCellData().getOrCreateStyle();
                                if (writeCellStyle.getWriteFont() != null){
                                    WriteFont writeFont = writeCellStyle.getWriteFont();
                                    String stringCellValue = cell.getStringCellValue();
                                    if (stringCellValue.equals("wwww")) {
                                        writeFont.setColor((short) 10);
                                    }
                                }else {
                                    WriteFont writeFont = new WriteFont();
                                    String stringCellValue = cell.getStringCellValue();
                                    if (stringCellValue.equals("wwww")) {
                                        writeFont.setColor((short) 10);
                                        writeCellStyle.setWriteFont(writeFont);
                                    }
                                }

                            }
                        }
                    }
                })
        .doWrite(objects);
    }


    /**
     * 导出excel - 根据条件重置单元格部分内容字体颜色
     * @param objects 数据列表
     * @param clazz 数据对应实体类
     * @param fileName 文件名称
     * @param sheetName sheet名称
     * @param startRowIndex 标注开始行
     * @param columnIndexs 标注列集合
     * @param response
     * @throws IOException
     */
    public static void exportExcel(List<?> objects, Class clazz, String fileName, String sheetName,Integer startRowIndex,HttpServletResponse response) throws IOException {

        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        // 设置防止中文名乱码
        String exportName = URLEncoder.encode(fileName, "utf-8");

        response.setHeader("Content-Type", "video/x-msvideo");
        // 文件下载方式(附件下载还是在当前浏览器打开)
        response.setHeader("Content-disposition", "attachment;filename=" + exportName + ".xlsx");

        // 向Excel中写入数据
        EasyExcel
                .write(response.getOutputStream(), clazz)
                //富文本得把inMemory设置为true,不然部分字体颜色无法设置
                .inMemory(true)
                .sheet(sheetName).registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

                        // 隐藏列，一般对exce的控制，可以使用常用的Sheet、cell等方法
//                        Sheet sheet1 = writeSheetHolder.getSheet();
//                        sheet1.setColumnHidden(0,true);

                        if (cell.getRowIndex()  > 1){
                            if (cell.getColumnIndex() == 3){
                                Sheet sheet = writeSheetHolder.getSheet();
                                Workbook workbook = sheet.getWorkbook();

                                String stringCellValue = cell.getStringCellValue();
                                if (stringCellValue.contains("(") || stringCellValue.contains(")")){
                                    // xlsx格式，如果是老版本格式的话就用 HSSFRichTextString
                                    XSSFRichTextString richString = new XSSFRichTextString(cell.getStringCellValue());
                                    Font font = workbook.createFont();
                                    // IndexedColors.RED.getIndex() 颜色参考
                                    font.setColor(Font.COLOR_RED);
                                    // 从哪到哪，你想设置成什么样的字体都行startIndex，endIndex
                                    // 给[0, 2)位置设置以上格式
                                    richString.applyFont(0, 2, font);
                                    // 如果多个，一个单元格设置不同颜色
//                                    richString.applyFont(2, 5, font);
                                    // 再设置回每个单元格里
                                    cell.setCellValue(richString);
                                }
                            }
                        }

                    }
                })
                .doWrite(objects);
    }
    /**
     * 导出excel
     * @param data 数据列表
     * @param response
     * @throws Exception
     */
    public static void exportExcel(List<ExcelVo> data, HttpServletResponse response)throws Exception{
        //1、设置数据表格的样式
        //  ---------- 头部样式 ----------
        WriteCellStyle headStyle = new WriteCellStyle();
        // 字体样式
        WriteFont headFont = new WriteFont();
        headFont.setFontHeightInPoints((short) 11);
        headFont.setFontName("宋体");
        headFont.setColor(IndexedColors.BLACK.index);
        headStyle.setWriteFont(headFont);

        WriteCellStyle contentStyle = new WriteCellStyle();
        //垂直居中
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //水平居中
        contentStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 设置边框
        // bodyStyle.setBorderTop(BorderStyle.DOUBLE);
        contentStyle.setBorderLeft(BorderStyle.THIN);
        contentStyle.setBorderRight(BorderStyle.THIN);
        contentStyle.setBorderBottom(BorderStyle.THIN);
        WriteFont writeFont = new WriteFont();
        //加粗
        //字体大小为11
        writeFont.setFontHeightInPoints((short) 11);
        writeFont.setFontName("宋体");
        writeFont.setColor(IndexedColors.BLACK.index);
        contentStyle.setWriteFont(writeFont);

        // 创建单元格策略1 参数1为头样式【不需要头部，设置为null】，参数2位表格内容样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headStyle, contentStyle);

        // 创建策略2
//        HorizontalCellStyleStrategy dataTableStrategy = new HorizontalCellStyleStrategy(headStyle,bodyStyle);

        // 设置数据表格的行高   null表示使用原来的行高
        // SimpleRowHeightStyleStrategy rowHeightStrategy3 = new SimpleRowHeightStyleStrategy( null, (short) 18);

        //循环合并策略
//     LoopMergeStrategy loopMergeStrategy = new LoopMergeStrategy(2, 0);

        //一次绝对合并策略
        OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy1 = new OnceAbsoluteMergeStrategy(0, 1, 1, 2); //0，1表示第1行到第2行 1,2表示第2列到第3列
        OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy2 = new OnceAbsoluteMergeStrategy(0, 1, 3, 4);
        OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy3 = new OnceAbsoluteMergeStrategy(0, 1, 5, 6);
        OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy4 = new OnceAbsoluteMergeStrategy(0, 1, 7, 8);
        OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy5 = new OnceAbsoluteMergeStrategy(0, 1, 9, 10);

//      response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("文件名", "UTF-8");
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream(), ExcelVo.class)
//              .registerWriteHandler(loopMergeStrategy)  // 循环合并策略
                .registerWriteHandler(onceAbsoluteMergeStrategy1)
                .registerWriteHandler(onceAbsoluteMergeStrategy2)
                .registerWriteHandler(onceAbsoluteMergeStrategy3)
                .registerWriteHandler(onceAbsoluteMergeStrategy4)
                .registerWriteHandler(onceAbsoluteMergeStrategy5)
//                .registerWriteHandler(dataTableStrategy)    //策略2
                .registerWriteHandler(horizontalCellStyleStrategy);  //策略1

        excelWriterBuilder.sheet("sheet名称").doWrite(data);
    }


    //-------------------------------------------------------------- 导出excel表格，设置自适应列宽配置类 start ----------------------------------------------------

    /**
     * 设置自适应列宽配置类
     */
    public static class Custemhandler extends AbstractColumnWidthStyleStrategy {

        private static final int MAX_COLUMN_WIDTH = 255;
        //因为在自动列宽的过程中，有些设置地方让列宽显得紧凑，所以做出了个判断
        private static final int COLUMN_WIDTH = 20;
        private  Map<Integer, Map<Integer, Integer>> CACHE = new HashMap(8);

        @Override
        protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
            boolean needSetWidth = isHead || !CollectionUtils.isEmpty(cellDataList);
            if (needSetWidth) {
                Map<Integer, Integer> maxColumnWidthMap = (Map)CACHE.get(writeSheetHolder.getSheetNo());
                if (maxColumnWidthMap == null) {
                    maxColumnWidthMap = new HashMap(16);
                    CACHE.put(writeSheetHolder.getSheetNo(), maxColumnWidthMap);
                }

                Integer columnWidth = this.dataLength(cellDataList, cell, isHead);
                if (columnWidth >= 0) {
                    if (columnWidth > MAX_COLUMN_WIDTH) {
                        columnWidth = MAX_COLUMN_WIDTH;
                    }else {
                        if(columnWidth<COLUMN_WIDTH){
                            columnWidth =columnWidth*2;
                        }
                    }

                    Integer maxColumnWidth = (Integer)((Map)maxColumnWidthMap).get(cell.getColumnIndex());
                    if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
                        ((Map)maxColumnWidthMap).put(cell.getColumnIndex(), columnWidth);
                        writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(),  columnWidth* 256);
                    }
                }
            }
        }


        private  Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
            if (isHead) {
                return cell.getStringCellValue().getBytes().length;
            } else {
                CellData cellData = (CellData)cellDataList.get(0);
                CellDataTypeEnum type = cellData.getType();
                if (type == null) {
                    return -1;
                } else {
                    switch(type) {
                        case STRING:
                            return cellData.getStringValue().getBytes().length;
                        case BOOLEAN:
                            return cellData.getBooleanValue().toString().getBytes().length;
                        case NUMBER:
                            return cellData.getNumberValue().toString().getBytes().length;
                        default:
                            return -1;
                    }
                }
            }
        }
    }
    //-------------------------------------------------------------- 导出excel表格，设置自适应列宽配置类 end -----------------------------------------------------


    /**
     * 不带模板输出数据到Excel，数据传输类属性用 @ExcelProperty("") 标注
     * @param response 响应对象
     * @param tClass 输出格式
     * @param datas 输出的数据
     * @param fileName
     * @param <T>
     * @throws IOException
     */
    public static <T> void writeEasyExcel(HttpServletResponse response, Class<T> tClass, List<T> datas, String fileName) throws IOException {
        TimeInterval timer = DateUtil.timer();
        if(ObjectUtil.isNull(response) || ObjectUtil.isNull(tClass)){
            return;
        }

        if(StrUtil.isBlank(fileName)){
            fileName = "excel.xlsx";
        }else{
            if(!fileName.contains(".xlsx")){
                fileName = fileName+".xlsx";
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName, "utf-8"));
        EasyExcel.write(response.getOutputStream(), tClass)
                .registerWriteHandler(new Custemhandler())
                .excelType(ExcelTypeEnum.XLSX)
                .sheet("sheet")
                .doWrite(datas);
        log.info("导出exlce数据：{}条，耗时：{}秒！", datas.size(), timer.intervalSecond());
    }

    // ************************************************************* excel导入 ***********************************************************************


    // ************************************************************* 待测试工具类方法 ***********************************************************************
    /**
     * 将列表以 Excel 响应给前端
     *
     * @param response  响应
     * @param filename  文件名
     * @param sheetName Excel sheet 名
     * @param head      Excel head 头
     * @param data      数据列表哦
     * @param <T>       泛型，保证 head 和 data 类型的一致性
     * @throws IOException 写入失败的情况
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName,
                                 Class<T> head, List<T> data) throws IOException {
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), head)
                // 不要自动关闭，交给 Servlet 自己处理
                .autoCloseStream(false)
                // 基于 column 长度，自动适配。最大 255 宽度
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet(sheetName).doWrite(data);
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    }

    /**
     * 读取excel
     * @param file
     * @param head
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
        return EasyExcel.read(file.getInputStream(), head, null)
                // 不要自动关闭，交给 Servlet 自己处理
                .autoCloseStream(false)
                .doReadAllSync();
    }

    //-------------------------------------------------------------- 读取文件解析监听类 start ----------------------------------------------------

    /**
     * 取文件解析监听类，此类供外部实例化使用需要设置为静态类
     * @param <T>
     */
    public static class ExcelListener<T> extends AnalysisEventListener<T> {

        /**
         * 存放读取后的数据
         */
        public List<T> datas = new ArrayList<>();

        /**
         * 取数据，一条一条读取
         * @param t
         * @param analysisContext
         */
        @Override
        public void invoke(T t, AnalysisContext analysisContext) {
            datas.add(t);
        }

        /**
         * 解析完毕之后执行
         * @param analysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            log.info("读取数据条数：{}条！", datas.size());
        }

        public List<T> getDatas(){
            return this.datas;
        }

    }
    //-------------------------------------------------------------- 读取文件解析监听类 end ----------------------------------------------------

    /**
     * 读取Excel文件返回数据集合，不包含表头，默认读取第一个sheet数据
     * @param inputStream 输入流
     * @param tClass 数据映射类
     * @param excelListener 读取监听类
     * @param <T> 结果集
     * @return
     */
    public static <T> List<T> readExcel(InputStream inputStream, Class<T> tClass, ExcelListener<T> excelListener){
        if(ObjectUtil.isNull(inputStream) || ObjectUtil.isNull(tClass) || ObjectUtil.isNull(excelListener)){
            return null;
        }
        ExcelReaderBuilder read = EasyExcel.read(inputStream, tClass, excelListener);
        read.sheet().doRead();
        return excelListener.getDatas();
    }

    /**
     * 读取Excel文件返回数据集合，不包含表头，读取第x个sheet数据，不设置sheet就读取全部
     * @param inputStream 输入流
     * @param sheetNo sheet编号
     * @param tClass 数据映射类
     * @param excelListener 读取监听类
     * @param <T>
     * @return
     */
    public static <T> List<T> readExcel(InputStream inputStream, Integer sheetNo, Class<T> tClass, ExcelListener<T> excelListener){
        if(ObjectUtil.isNull(inputStream) || ObjectUtil.isNull(tClass) || ObjectUtil.isNull(excelListener)){
            return null;
        }
        ExcelReaderBuilder read = EasyExcel.read(inputStream, tClass, excelListener);
        if(ObjectUtil.isNotNull(sheetNo)){
            read.sheet(sheetNo).doRead();
        }else{
            ExcelReader excelReader = read.build();
            excelReader.readAll();
            excelReader.finish();
        }
        return excelListener.getDatas();
    }



    /**
     * 带模板输出数据到Excel，数据传输类属性用 @ExcelProperty("") 标注
     * @param inputStream 输出流
     * @param outputStream
     * @param tClass 输出格式
     * @param datas 输出的数据
     * @param <T>
     */
    public static <T> void writeExcel(InputStream inputStream , OutputStream outputStream, Class<T> tClass, List<T> datas){
        TimeInterval timer = DateUtil.timer();
        if(ObjectUtil.isNull(outputStream) || ObjectUtil.isNull(tClass)){
            return;
        }
//        EasyExcel.write(outputStream, tClass).withTemplate(inputStream).sheet("sheet").doWrite(datas);
        EasyExcel.write(outputStream, tClass).excelType(ExcelTypeEnum.XLSX).withTemplate(inputStream).sheet("sheet").doFill(datas);
        log.info("导出exlce数据：{}条，耗时：{}秒！", datas.size(), timer.intervalSecond());
    }

    /**
     * 不带模板输出数据到Excel，数据传输类属性用 @ExcelProperty("") 标注
     * @param response
     * @param tClass 输出格式
     * @param datas 输出的数据
     * @param fileName
     * @param <T>
     * @throws IOException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static <T> void writeExcel(HttpServletResponse response, Class<T> tClass, List<T> datas, String fileName) throws IOException, IllegalAccessException, NoSuchFieldException {
        TimeInterval timer = DateUtil.timer();
        if(ObjectUtil.isNull(response) || ObjectUtil.isNull(tClass)){
            return;
        }

        if(StrUtil.isBlank(fileName)){
            fileName = "excel.xlsx";
        }else{
            if(!fileName.contains(".xlsx")){
                fileName = fileName+".xlsx";
            }
        }

        //编码设置成UTF-8，excel文件格式为.xlsx
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止中文乱码 和easyexcel本身没有关系
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        //处理注解转换
//        if(CollUtil.isNotEmpty(datas)){
//            for (T dataObj : datas) {
//                AnnotationUtil.resolveAnnotation(tClass, dataObj);
//            }
//        }
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), tClass).build();
        WriteSheet writeSheet = new WriteSheet();
        writeSheet.setSheetName("sheet");
        excelWriter.write(datas, writeSheet);
        excelWriter.finish();
        log.info("导出exlce数据：{}条，耗时：{}秒！", datas.size(), timer.intervalSecond());
    }

    /**
     * 单元格样式策略
     *     .registerWriteHandler(this.getHorizontalCellStyleStrategy((short) 12))
     */
    public static HorizontalCellStyleStrategy getHorizontalCellStyleStrategy(Short fontHeightInPoints) {
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.NONE);

        // 配置字体
        WriteFont contentWriteFont = new WriteFont();
        // 字体
        contentWriteFont.setFontName("宋体");
        // 字体大小
        contentWriteFont.setFontHeightInPoints(fontHeightInPoints);
        // 设置加粗
        contentWriteFont.setBold(false);
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        // 【水平居中需要使用以下两行】
        // 设置文字左右居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 设置文字上下居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置 自动换行
        contentWriteCellStyle.setWrapped(true);

        // 样式策略
        return new HorizontalCellStyleStrategy(null, contentWriteCellStyle);
    }

}
