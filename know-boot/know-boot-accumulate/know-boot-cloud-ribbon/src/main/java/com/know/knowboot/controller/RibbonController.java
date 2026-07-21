package com.know.knowboot.controller;//package com.know.knowboot.controller;
//
//
//import com.know.knowboot.example.tree.entity.SysUserInfo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//@Slf4j
//@RestController
//@RequestMapping("/sys/ribbon")
//public class RibbonController {
//
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
//    public List<SysUserInfo> getUserInfo() {
//
//       ResponseEntity<List> responseEntity = restTemplate.getForEntity("http://localhost:9001/mybatis/sys/userInfo/queryAll?id=1", List.class);
//       List<SysUserInfo> list = responseEntity.getBody();
//       return list;
//
//    }
//
//}
