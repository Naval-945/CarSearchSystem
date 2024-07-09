# 2.0.1更新变动

## 前端部分：

更新wifi模块、采用前台服务(介于后台服务的wifi扫描功能耗费资源过多很容易被系统枪毙)、更新wifiData（wifi通信的固定格式）。  
为了开发便利将位置信息location定义为了int类型、为了适配需求可以自定义位置类（如 pair<int, int>）并加以改动。  
响应体传回int值、**后续需要调整**。

## 后端部分：

算法适配部分请看AlgorithmA.java（**适配需求而调整**），WifiDataProcessor.java  
其中WifiDataProcessor调用AlgorithmA（如此设计是为了降低耦合度），由wificontroller调用processer并给出响应体int值（location）。




