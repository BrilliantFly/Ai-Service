/**
 * Copyright &copy; 2017-2020 <a href="#">huaxinSoft</a> All rights reserved.
 */
package com.know.knowboot.utils.file;

import freemarker.core.Environment;
import freemarker.core.InvalidReferenceException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * FreeMarkers工具类
 *
 * 可以用户模板下载成word
 */
public class FreeMarkers {
	public static String renderString(String templateString, Map<String, ?> model) {
		try {
			StringWriter result = new StringWriter();
			Template t = new Template("name", new StringReader(templateString), new Configuration());
			t.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public static String renderTemplate(Template template, Object model) {
		try {
			StringWriter result = new StringWriter();
			template.process(model, result);
			return result.toString();
		} catch (Exception e) {
            throw new RuntimeException();
		}
	}

	public static Configuration buildConfigByResPath(String directory) throws IOException {
		//directory = "C:/Tomcat 8.0/webapps/slj/WEB-INF/classes/templates/word/doc/ftl/";
		Configuration cfg = new Configuration();
		cfg.setClassicCompatible(true);
		cfg.setDefaultEncoding("UTF-8");
		//Resource path = new DefaultResourceLoader().getResource(directory);
		cfg.setDirectoryForTemplateLoading(new File(directory));
		return cfg;
	}
	
	public static Configuration buildConfigByOtherPath(String directory) throws IOException {
		Configuration cfg = new Configuration();
		cfg.setDefaultEncoding("UTF-8");
		cfg.setDirectoryForTemplateLoading(new File(directory));
		return cfg;
	}
	
	
	/**
	 * 替换ftl模板变量生成word文档
	 * @param directory ftl模板文件加载路径
	 * @param outFileName 输出文件路径名称(word2007前版本为doc文件,后版本为xml文件)
	 * @param dataMap 模板中需替换数据
	 * @param ftlTmplName ftl模板文件名称
	 * @return 替换后的word文档
	 */
//     public static File createWordZG(String directory, String outFileName, Map<?, ?> dataMap, String ftlTmplName, HttpServletResponse response) {
//         Writer wr = null;
//         File outFile = new File(outFileName);
//         FileOutputStream fos =null;
//         try {
//        	 Configuration configuration = buildConfigByResPath(directory);
//
//        	 response.setContentType("application/msword");
//             response.setHeader("Content-Disposition", "attachment;filename=\"" + new String("青岛市水利安全生产检查反馈报告单.doc".getBytes("GBK"), "iso8859-1") + "\"");
//             response.setCharacterEncoding("utf-8");
//             PrintWriter out = response.getWriter();
//             Template t =  configuration.getTemplate(ftlTmplName,"utf-8");
//            // t.process(dataMap, out);
//             Environment env = t.createProcessingEnvironment(dataMap, out);
//             env.setClassicCompatible(true);
//             env.process();
//             out.close();
//
//         } catch (InvalidReferenceException ife) {
//        	 ife.printStackTrace();
//        	 System.out.println("模板变量没有赋值，详细错误参照如下：");
//        	 System.out.println(ife.getLocalizedMessage());
//         }  catch (TemplateException e) {
//             e.printStackTrace();
//         } catch (IOException e) {
//
//
//         	e.printStackTrace();
//         } finally {
//             IOUtils.closeQuietly(wr);
//             IOUtils.closeQuietly(fos);
//         }
//         return outFile;
//     }
     
     /**
 	 * 替换ftl模板变量生成word文档
 	 * @param directory ftl模板文件加载路径
 	 * @param outFileName 输出文件路径名称(word2007前版本为doc文件,后版本为xml文件)
 	 * @param dataMap 模板中需替换数据
 	 * @param ftlTmplName ftl模板文件名称
 	 * @return 替换后的word文档
 	 */
      public static File createWordDoc(String directory, String outFileName, Map<?, ?> dataMap, String ftlTmplName, HttpServletResponse response) {
          Writer wr = null;
          File outFile = new File(outFileName);
          FileOutputStream fos =null;
          try {
         	 Configuration configuration = buildConfigByResPath(directory);
         	 
         	 response.setContentType("application/msword");
              response.setHeader("Content-Disposition", "attachment;filename=\"" + new String(outFileName.getBytes("GBK"), "iso8859-1") + "\"");
              response.setCharacterEncoding("utf-8");
              PrintWriter out = response.getWriter();
              Template t =  configuration.getTemplate(ftlTmplName,"utf-8"); 
             // t.process(dataMap, out); 
              Environment env = t.createProcessingEnvironment(dataMap, out);
              env.setClassicCompatible(true);
              env.process();
              out.close(); 

          } catch (InvalidReferenceException ife) {  
         	 ife.printStackTrace();  
         	 System.out.println("模板变量没有赋值，详细错误参照如下：");
         	 System.out.println(ife.getLocalizedMessage());
          }  catch (TemplateException e) {  
              e.printStackTrace();  
          } catch (IOException e) {


          	e.printStackTrace();
          } finally {
              IOUtils.closeQuietly(wr);
              IOUtils.closeQuietly(fos);
          }
          return outFile;
      }
      
     public static File createWord(String directory, String outFileName, Map<?, ?> dataMap, String ftlTmplName, HttpServletResponse response) {
         Writer wr = null;
         File outFile = new File(outFileName);
         FileOutputStream fos =null;
         try {
        	 Configuration configuration = buildConfigByResPath(directory);
        	 
        	 response.setContentType("application/msword");     
             response.setHeader("Content-Disposition", "attachment;filename=\"" + new String("青岛市水利安全生产检查通知单.doc".getBytes("GBK"), "iso8859-1") + "\"");
             response.setCharacterEncoding("utf-8");
             PrintWriter out = response.getWriter();
             Template t =  configuration.getTemplate(ftlTmplName,"utf-8");
             t.process(dataMap, out);  
             out.close(); 

         } catch (InvalidReferenceException ife) {  
        	 ife.printStackTrace();  
        	 System.out.println("模板变量没有赋值，详细错误参照如下：");
        	 System.out.println(ife.getLocalizedMessage());
         }  catch (TemplateException e) {  
             e.printStackTrace();  
         } catch (IOException e) {
             e.printStackTrace();  
         } finally {
             IOUtils.closeQuietly(wr);
             IOUtils.closeQuietly(fos);
         }
         return outFile;
     }
     
 	/**
 	 * 替换ftl模板变量生成word文档
 	 * @param directory ftl模板文件加载路径
 	 * @param outFileName 输出文件路径名称(word2007前版本为doc文件,后版本为xml文件)
 	 * @param dataMap 模板中需替换数据
 	 * @param ftlTmplName ftl模板文件名称
 	 * @return 替换后的word文档
 	 */
      public static File createWord(String directory, String outFileName, Map<?, ?> dataMap, String ftlTmplName) {
          Writer wr = null;
          File outFile = new File(outFileName);
          FileOutputStream fos =null;
          try {
         	 Configuration configuration = buildConfigByResPath(directory);
              Template tmpl =configuration.getTemplate(ftlTmplName);
              fos =new FileOutputStream(outFile);
              wr = new OutputStreamWriter(fos, "utf-8");
              tmpl.process(dataMap, wr);
              wr.close();
          } catch (InvalidReferenceException ife) {  
         	 ife.printStackTrace();  
         	 System.out.println("模板变量没有赋值，详细错误参照如下：");
         	 System.out.println(ife.getLocalizedMessage());
          }  catch (TemplateException e) {  
              e.printStackTrace();  
          } catch (IOException e) {
              e.printStackTrace();  
          } finally {
              IOUtils.closeQuietly(wr);
              IOUtils.closeQuietly(fos);
          }
          return outFile;
      }
      
	public static void main(String[] args) throws IOException {


	}
	
}
