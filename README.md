## 目录结构说明

本目录包含 Android 版 移动直播 SDK 的Demo 源代码，主要演示接口如何调用以及最基本的功能。

```
├─ MLVB-API-Example // MLVB API Example，包括直播推流，直播播放，互动直播
|  ├─ App                   // 程序入口界面
|  ├─ Basic                 // 演示 Live 基础功能示例代码
|  |  ├─ LiveLink           // 演示直播连麦示例代码
|  |  ├─ LivePK             // 演示直播 PK 示例代码
|  |  ├─ LivePlay           // 演示直播播放示例代码
|  |  ├─ LivePushCamera     // 演示摄像头推流示例代码
|  |  ├─ LivePushScreen     // 演示屏幕推流示例代码
|  ├─ Advanced 				// 演示直播高级功能示例代码
|  |  ├─ CustomVideoCapture		// 演示自定义视频采集示例代码
|  |  ├─ RTCPushAndPlay 		// 演示 RTC 推流和播放示例代码
|  |  ├─ SwitchRenderView		// 演示切换渲染 View 示例代码
|  |  ├─ ThirdBeauty			// 演示第三方美颜示例代码
|  
├─ SDK 
│  ├─ LiteAVSDK_Smart_x.y.zzzz.aar          // 如果您下载的是 Smart 版本 zip 包，解压后将出现此文件夹，其中 x.y.zzzz 表示 SDK 版本号 
|  ├─ LiteAVSDK_Live_x.y.zzzz.aar           // 如果您下载的是 Live 版本 zip 包，解压后将出现此文件夹，其中 x.y.zzzz 表示 SDK 版本号 
|  ├─ LiteAVSDK_Professional_x.y.zzzz.aar   // 如果您下载的是 Professional 版本 zip 包，解压后将出现此文件夹，其中 x.y.zzzz 表示 SDK 版本号 
```

## SDK 分类和下载

腾讯云 移动直播 SDK 基于 LiteAVSDK 统一框架设计和实现，该框架包含直播、点播、短视频、RTC、AI美颜在内的多项功能：

- 如果您追求最小化体积增量，可以下载 Smart 版：[TXLiteAVSDK_Smart_Android_latest.zip](https://cloud.tencent.com/document/product/454/7873)
- 如果您还需要 连麦PK 的功能，可以下载 Live 版：[TXLiteAVSDK_Live_Android_latest.zip](https://cloud.tencent.com/document/product/454/7873)
- 如果您需要使用多个功能而不希望打包多个 SDK，可以下载专业版：[TXLiteAVSDK_Professional_Android_latest.zip](https://cloud.tencent.com/document/product/454/7873)

## 相关文档链接

- [SDK 的版本更新历史](https://cloud.tencent.com/document/product/454/7878)
- [SDK 的 API 文档](https://cloud.tencent.com/document/product/454/34766)
- [SDK 的官方体验 App](https://cloud.tencent.com/document/product/454/6555)
- [全功能小直播 App（Demo）源代码](https://cloud.tencent.com/document/product/454/38625)
