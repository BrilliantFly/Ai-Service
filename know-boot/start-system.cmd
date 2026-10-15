@echo off
cd /d E:\Ai-Project\Ai-Service\know-boot
java -Xms256m -Xmx512m -XX:MaxMetaspaceSize=128m ^
  -Dspring.profiles.active=dev ^
  "-Dspring.datasource.url=jdbc:mysql://127.0.0.1:3306/know_boot_v1?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true" ^
  -Dspring.datasource.username=root ^
  -Dspring.datasource.password=123456aA@ ^
  -Dspring.redis.host=127.0.0.1 ^
  -Dspring.redis.port=6379 ^
  -Dspring.redis.database=6 ^
  -Dknow.mode=standalone ^
  -Dseata.enabled=false ^
  -Dfeign.circuitbreaker.enabled=false ^
  -Dfeign.loadbalancer.enabled=false ^
  "-Dspring.autoconfigure.exclude=com.alibaba.cloud.seata.feign.SeataFeignAutoConfiguration,com.alibaba.cloud.seata.feign.SeataFeignClientAutoConfiguration" ^
  -jar "E:\Ai-Project\Ai-Service\know-boot\know-boot-system\target\know-boot-system-0.0.1-SNAPSHOT.jar"
