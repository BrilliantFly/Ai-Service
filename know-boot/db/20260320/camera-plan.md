# 摄像头设备模块 - 完整实施计划

## 一、需求确认

| 需求 | 确认 |
|------|------|
| 设备扫描 | ✅ 自动扫描局域网摄像头 |
| 设备管理 | ✅ 最多8路 |
| 视频查看 | ✅ 实时视频 + 截图 |
| 录像存储 | ✅ 本地存储 + 全时录像 |
| 存储位置 | 本地存储 |
| 最大设备数 | 8路 |
| 录像策略 | 全时录像 |

## 二、模块命名

| 层级 | 模块名 | 说明 |
|------|--------|------|
| **后端** | `know-boot-camera` | 摄像头设备模块 |
| **数据库表前缀** | `camera_` | 设备表、录像表 |
| **API路径前缀** | `/adminapi/camera.` | 接口前缀 |

## 三、数据库设计

### 3.1 表结构

#### camera_device - 摄像头设备表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 主键ID |
| name | varchar(100) | 设备名称 |
| brand | varchar(50) | 品牌(haikang/dahua/yushi/tplink/general) |
| ip | varchar(50) | IP地址 |
| port | int | RTSP端口(默认554) |
| http_port | int | HTTP端口(默认80) |
| username | varchar(100) | 用户名 |
| password | varchar(100) | 密码(加密) |
| channel | int | 通道号 |
| stream_type | varchar(20) | 码流(main主码流/sub子码流) |
| status | tinyint | 在线状态(0离线1在线) |
| last_online_time | int | 最后在线时间 |
| create_by | varchar(32) | 创建人 |
| create_time | datetime | 创建时间 |
| update_by | varchar(32) | 更新人 |
| update_time | datetime | 更新时间 |
| del_flag | tinyint | 删除标志(0正常1删除) |

#### camera_record - 摄像头录像表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 主键ID |
| device_id | int | 设备ID |
| file_name | varchar(255) | 文件名 |
| file_path | varchar(255) | 文件路径 |
| file_size | bigint | 文件大小(字节) |
| duration | int | 时长(秒) |
| start_time | int | 开始时间戳 |
| end_time | int | 结束时间戳 |
| record_type | varchar(20) | 录像类型(manual手动/timing定时/motion移动侦测) |
| status | tinyint | 状态(0生成中1已完成) |
| create_time | datetime | 创建时间 |

#### camera_screenshot - 摄像头截图表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 主键ID |
| device_id | int | 设备ID |
| file_path | varchar(255) | 文件路径 |
| file_size | bigint | 文件大小 |
| snapshot_time | int | 截图时间戳 |
| create_time | datetime | 创建时间 |

#### camera_config - 摄像头配置表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 主键ID |
| config_key | varchar(50) | 配置键 |
| config_value | varchar(500) | 配置值 |
| remark | varchar(255) | 备注 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |

## 四、后端模块设计

### 4.1 Maven 模块结构
```
know-boot-camera/
├── pom.xml
└── src/main/java/com/know/knowboot/
    ├── controller/
    │   ├── CameraDeviceController.java
    │   ├── CameraScanController.java
    │   └── CameraRecordController.java
    ├── service/
    │   ├── ICameraService.java
    │   ├── ICameraScanService.java
    │   ├── ICameraRecordService.java
    │   └── impl/
    │       ├── CameraServiceImpl.java
    │       ├── CameraScanServiceImpl.java
    │       └── CameraRecordServiceImpl.java
    ├── entity/
    │   ├── CameraDevice.java
    │   ├── CameraRecord.java
    │   ├── CameraScreenshot.java
    │   └── CameraConfig.java
    ├── mapper/
    │   ├── CameraDeviceMapper.java
    │   ├── CameraRecordMapper.java
    │   └── CameraScreenshotMapper.java
    ├── vo/
    │   ├── CameraDeviceVo.java
    │   └── CameraScanResultVo.java
    ├── validate/
    │   └── CameraDeviceValidate.java
    ├── utils/
    │   ├── CameraScanner.java
    │   └── StreamProxy.java
    └── enums/
        ├── CameraBrandEnum.java
        └── RecordTypeEnum.java
```

### 4.2 API 接口设计

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 扫描局域网 | POST | `/adminapi/camera.scan/scan` | 扫描摄像头 |
| 设备列表 | GET | `/adminapi/camera.device/list` | 获取设备列表 |
| 设备详情 | GET | `/adminapi/camera.device/{id}` | 获取设备详情 |
| 添加设备 | POST | `/adminapi/camera.device` | 添加设备 |
| 编辑设备 | PUT | `/adminapi/camera.device` | 编辑设备 |
| 删除设备 | DELETE | `/adminapi/camera.device/{id}` | 删除设备 |
| 设备检测 | POST | `/adminapi/camera.device/check` | 检测在线状态 |
| 获取截图 | GET | `/adminapi/camera.screenshot/{id}` | 获取截图 |
| 获取流地址 | GET | `/adminapi/camera.stream/{id}` | 获取视频流地址 |
| 开始录像 | POST | `/adminapi/camera.record/start/{id}` | 开始录像 |
| 停止录像 | POST | `/adminapi/camera.record/stop/{id}` | 停止录像 |
| 录像列表 | GET | `/adminapi/camera.record/list` | 录像列表 |
| 删除录像 | DELETE | `/adminapi/camera.record/{id}` | 删除录像 |
| 播放录像 | GET | `/adminapi/camera.record/play/{id}` | 播放录像 |

## 五、前端模块设计 (know-vue)

### 5.1 页面结构
```
src/views/camera/
├── index.vue                  # 主页面
└── components/
    ├── DeviceList.vue         # 设备列表
    ├── DeviceScan.vue         # 设备扫描
    ├── DeviceForm.vue         # 设备表单
    ├── VideoPlayer.vue        # 视频播放
    └── RecordList.vue         # 录像列表

src/api/camera/
├── device.ts                  # 设备API
├── scan.ts                    # 扫描API
└── record.ts                # 录像API

src/router/routes/modules/
└── camera.ts                  # 路由配置
```

### 5.2 页面功能

| 页面 | 功能 |
|------|------|
| DeviceList.vue | 设备列表、状态、快捷操作 |
| DeviceScan.vue | 扫描动画、发现设备列表 |
| DeviceForm.vue | 添加/编辑设备表单 |
| VideoPlayer.vue | 实时视频播放 |
| RecordList.vue | 录像列表、时间轴 |

## 六、小程序模块设计 (know-uniapp)

### 6.1 页面结构
```
pages/camera/
├── list.vue                   # 设备列表
├── scan.vue                   # 设备扫描
├── detail.vue                 # 设备详情
├── play.vue                  # 视频播放
├── record.vue                # 录像管理
└── screenshot.vue            # 截图查看

src/api/camera.ts             # API封装
src/components/
├── camera-card.vue            # 设备卡片
├── camera-player.vue          # 视频播放
└── record-item.vue          # 录像项
```

### 6.2 页面功能

| 页面 | 功能 |
|------|------|
| list.vue | 设备卡片、状态指示 |
| scan.vue | 扫描动画、一键添加 |
| detail.vue | 设备信息、实时画面 |
| play.vue | 全屏播放、云台控制 |
| record.vue | 录像列表、日期选择 |
| screenshot.vue | 截图预览、分享 |

## 七、技术选型

| 技术 | 说明 |
|------|------|
| RTSP | 实时流协议，摄像头通用 |
| FFmpeg | 视频流处理、转码、录像 |
| FLV | 视频封装，Web播放 |
| JPEG | 截图格式 |

## 八、摄像头品牌识别

| 品牌 | HTTP端口 | RTSP端口 | 识别特征 |
|------|-----------|-----------|----------|
| 海康威视 | 80, 8000 | 554 | HTTP响应 |
| 大华 | 80, 37777 | 554 | HTTP响应 |
| 宇视 | 80 | 5544 | HTTP响应 |
| TP-LINK | 80 | 554 | HTTP响应 |
| 华为 | 80 | 554 | ONVIF |
| 通用RTSP | - | 554 | RTSP响应 |

## 九、实施任务清单

| 阶段 | 序号 | 模块 | 文件 |
|------|------|------|------|
| **阶段一** | 1 | 数据库 | `db/20260320/camera.sql` |
| **阶段二** | 2 | 后端 | `know-boot-camera/pom.xml` |
| | 3 | | `entity/CameraDevice.java` |
| | 4 | | `entity/CameraRecord.java` |
| | 5 | | `entity/CameraScreenshot.java` |
| | 6 | | `entity/CameraConfig.java` |
| | 7 | | `mapper/CameraDeviceMapper.java` |
| | 8 | | `mapper/CameraRecordMapper.java` |
| | 9 | | `service/ICameraService.java` |
| | 10 | | `service/impl/CameraServiceImpl.java` |
| | 11 | | `service/ICameraScanService.java` |
| | 12 | | `service/impl/CameraScanServiceImpl.java` |
| | 13 | | `service/ICameraRecordService.java` |
| | 14 | | `service/impl/CameraRecordServiceImpl.java` |
| | 15 | | `controller/CameraDeviceController.java` |
| | 16 | | `controller/CameraScanController.java` |
| | 17 | | `controller/CameraRecordController.java` |
| | 18 | | `utils/CameraScanner.java` |
| | 19 | | `utils/StreamProxy.java` |
| | 20 | | `enums/CameraBrandEnum.java` |
| | 21 | | `vo/CameraDeviceVo.java` |
| | 22 | | `vo/CameraScanResultVo.java` |
| | 23 | | `validate/CameraDeviceValidate.java` |
| **阶段三** | 24 | 前端 | `views/camera/index.vue` |
| | 25 | | `views/camera/components/DeviceList.vue` |
| | 26 | | `views/camera/components/DeviceScan.vue` |
| | 27 | | `views/camera/components/VideoPlayer.vue` |
| | 28 | | `views/camera/components/RecordList.vue` |
| | 29 | | `api/camera/device.ts` |
| | 30 | | `api/camera/scan.ts` |
| | 31 | | `api/camera/record.ts` |
| | 32 | | `router/routes/modules/camera.ts` |
| **阶段四** | 33 | 小程序 | `pages/camera/list.vue` |
| | 34 | | `pages/camera/scan.vue` |
| | 35 | | `pages/camera/detail.vue` |
| | 36 | | `pages/camera/play.vue` |
| | 37 | | `pages/camera/record.vue` |
| | 38 | | `src/api/camera.ts` |
| | 39 | | `src/components/camera-player.vue` |
| | 40 | | `pages.json` (添加路由) |

**总计：40 个文件**
