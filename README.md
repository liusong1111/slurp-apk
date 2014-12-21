# 假定是mac或linux环境

# 准备开发环境

* 安装jdk
* 安装lein:

    下载这个文件到$PATH下（例如/usr/bin)，并赋于执行权限(chmod a+x lein)：
    [https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein]

# 在开发环境中运行

    下载工程代码
    cd 工程目录
    # 这步第一次执行较慢，因为需要下载依赖包，以后就快了。执行完成后，会提示监听3000端口
    lein ring server-headless

# 为生产环境打包
    cd 工程目录
    # 本步会在"工程目录/target/"下生成文件"slurp-apk-0.1.0-SNAPSHOT-standalone.jar"
    lein ring uberjar

# 准备生产环境

* 确保生产环境安装jdk
* 将如下三个文件复制到生产环境的一个目录下（如~/apk-parser)：
    * slurp-apk-0.1.0-SNAPSHOT-standalone.jar  #server执行文件
    * 原始工程下的apk.db  #解析apk的结果存放到该数据库
    * apk-parser.sh   #解析apk得到包名的可执行文件,由立新提供

# 在生产环境运行：

    cd ~/apk-parser
    nohup java -jar slurp-apk-0.1.0-SNAPSHOT-standalone.jar &


# 客户端使用方法
    # POST一个url
    curl -X POST http://127.0.0.1:3000/api/accept-urls -d $'http://d1.apk8.com:8020/game_m/zhaotonglei.apk'
    # POST两个或多个url(注意post数据有\n，在bash里，前面标上$符号用来允许\n生效)
    curl -X POST http://127.0.0.1:3000/api/accept-urls -d $'http://d1.apk8.com:8020/game_m/zhaotonglei.apk\nhttp://api.gfan.com/market/api/apk?type=WAP&cid=99&uid=-1&pid=uUWTTatSARTvZZrl2I+hTzXDIKUL+vn4&sid=zT23ftOwZD9YDH7pKPlyIw=='

# 查看日志
工程下的production.log、nohup.out

# 查看缓存的apk文件
在工程下的apks目录下

# 查看解析结果
    cd 工程下
    sqlite3 apk.db
    select * from apks;
    .quit

