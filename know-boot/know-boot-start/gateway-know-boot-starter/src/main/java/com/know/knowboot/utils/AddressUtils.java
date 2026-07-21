package com.know.knowboot.utils;

import com.alibaba.cloud.commons.lang.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import java.io.File;
import java.lang.reflect.Method;

@Slf4j
public class AddressUtils {

    /**
     * 根据IP地址获取地理位置
     */
    @SneakyThrows
    public static String getRealAddressByIP(String ip) {

        if (StringUtils.isBlank(ip)) {
            return "";
        }
        if ("127.0.0.1".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)) {
            return "局域网，无法获取位置";
        }

        //开发时使用的代码
        String dbPath = AddressUtils.class.getResource("/ip2region.db").getPath();
        File file = new File(dbPath);
        if ( file.exists() == false ) {
            System.out.println("Error: Invalid ip2region.db file");
        }
        //查询算法
        int algorithm = DbSearcher.BTREE_ALGORITHM; //B-tree
        //DbSearcher.BINARY_ALGORITHM //Binary
//         int algorithm =DbSearcher.MEMORY_ALGORITYM; //Memory
        try {
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, dbPath);
            //define the method
            Method method = null;
            switch ( algorithm )
            {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
            }
            DataBlock dataBlock = null;
            if (Util.isIpAddress(ip) == false ) {
                System.out.println("Error: Invalid ip address");
            }
            dataBlock  = (DataBlock) method.invoke(searcher, ip);
            return dataBlock.getRegion();
        //部署时使用的代码
//        try{
//            //        读取jar包内的配置文件信息
//            ClassPathResource resource = new ClassPathResource("/ip2region.db");
//            InputStream inputStream = resource.getInputStream();
//            byte[] dbBinStr = IoUtil.readBytes(inputStream);
//            DbConfig config = new DbConfig();
//            DbSearcher searcher = new DbSearcher(config, dbBinStr);
//            // 查询算法memory，采用二进制方式初始化DBSearcher需要使用MEMORY_ALGORITYM，
//            //否则会抛出null。
//            Method method = searcher.getClass().getMethod("memorySearch", String.class);
//            return ((DataBlock) method.invoke(searcher, ip)).getRegion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
