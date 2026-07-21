package com.know.knowboot.controller;//package com.know.knowboot.controller;
//
//
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequestMapping("/sys/hystrix")
//public class HystrixController {
//
//
//    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
//    @HystrixCommand(fallbackMethod = "hystrixFail")
//    public void getUserInfo(@RequestParam("param") int param) {
//        log.info("----------------------失败的请求-----------------------------");
//        if (param == 1){
//            int a = 1 / 0;
//        }
//
//    }
//
//    public void hystrixFail(int param){
//        log.error("hystrixFail");
//    }
//
//    @RequestMapping(value = "/getUserInfo1", method = RequestMethod.GET)
//    @HystrixCommand(fallbackMethod = "hystrixFail")
//    public void getUserInfo1() {
//       log.info("----------------------正常的请求-----------------------------");
//    }
//
//
//}
