#!/bin/bash

# 定义全局路径
GLOBAL_PATH="/apps/yiiu"

# 定义应用名称和路径
APP_NAME="yiiu.jar"
APP_PATH="$GLOBAL_PATH/$APP_NAME"  # 使用全局路径
PID_FILE="$GLOBAL_PATH/tmp/yiiu.pid"
JVM_OPTS="-Xms3G -Xmx3G -XX:PermSize=128m -XX:MaxPermSize=256m"  # 设置JVM参数

# 日志文件配置
DUMP_LOG="$GLOBAL_PATH/logs/dump.log"  # 堆转储日志文件路径
GC_LOG="$GLOBAL_PATH/logs/gc.log"      # GC日志文件路径

# 启动应用
start() {
    if [ -f $PID_FILE ]; then
        echo "$APP_NAME is already running (PID: $(cat $PID_FILE))."
    else
        echo "Starting $APP_NAME with JVM options: $JVM_OPTS..."
        nohup java $JVM_OPTS \
            -XX:+HeapDumpOnOutOfMemoryError \
            -XX:HeapDumpPath=$DUMP_LOG \
            -Xlog:gc*:time,uptime:file=$GC_LOG,level=info \
            -XX:+PrintGCDetails \
            -XX:+PrintGCDateStamps \
            -XX:+PrintGCTimeStamps \
            -XX:+UseG1GC \  # 使用 G1 垃圾回收器
            -jar $APP_PATH > /dev/null 2>&1 &
        echo $! > $PID_FILE
        echo "$APP_NAME started with PID: $(cat $PID_FILE)."
    fi
}

# 停止应用
stop() {
    if [ -f $PID_FILE ]; then
        echo "Stopping $APP_NAME (PID: $(cat $PID_FILE))..."
        kill $(cat $PID_FILE)
        rm $PID_FILE
        echo "$APP_NAME stopped."
    else
        echo "$APP_NAME is not running."
    fi
}

# 重启应用
restart() {
    stop
    sleep 2
    start
}

# 查看状态
status() {
    if [ -f $PID_FILE ]; then
        echo "$APP_NAME is running (PID: $(cat $PID_FILE))."
    else
        echo "$APP_NAME is not running."
    fi
}

# 处理命令行参数
case "\$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    status)
        status
        ;;
    *)
        echo "Usage: \$0 {start|stop|restart|status}"
        exit 1
        ;;
esac