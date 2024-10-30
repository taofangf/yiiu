#!/bin/bash

# 公共基础路径
BASE_DIR="/apps/yiiu"
# Spring Boot 应用的 JAR 文件名
APP_NAME="yiiu.jar"
# 应用路径
APP_PATH="$BASE_DIR/$APP_NAME"
# 日志路径
LOG_DIR="$BASE_DIR/logs"
# GC 日志路径
GC_LOG_DIR="$LOG_DIR/gc"
# Heap Dump 文件路径
DUMP_DIR="$LOG_DIR/dump"
# PID 文件路径
PID_FILE="$LOG_DIR/app.pid"

# 创建日志、GC 日志、和 Heap Dump 文件目录（如果不存在）
mkdir -p "$LOG_DIR" "$GC_LOG_DIR" "$DUMP_DIR"

# JVM 和 GC 日志参数（适用于 Java 8）
JAVA_OPTS="-Xms6G -Xmx6G -XX:+UseG1GC"
JAVA_OPTS="$JAVA_OPTS -Xloggc:$GC_LOG_DIR/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M"
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$DUMP_DIR"

# 检查 PID 文件是否存在且进程是否在运行
is_running() {
    [ -f "$PID_FILE" ] && kill -0 $(cat "$PID_FILE") 2>/dev/null
}

# 启动函数
start() {
    if is_running; then
        echo "应用已在运行中，PID: $(cat $PID_FILE)"
    else
        echo "启动 $APP_NAME ..."
        nohup java $JAVA_OPTS -jar $APP_PATH > "$LOG_DIR/app.log" 2>&1 &
        echo $! > "$PID_FILE"
        echo "应用已启动，PID: $(cat $PID_FILE)"
    fi
}

# 停止函数
stop() {
    if is_running; then
        echo "停止应用，PID: $(cat $PID_FILE)"
        kill $(cat "$PID_FILE")
        rm -f "$PID_FILE"
        echo "应用已停止"
    else
        echo "应用未运行或 PID 文件不存在"
    fi
}

# 重启函数
restart() {
    echo "重启应用..."
    stop
    sleep 2  # 停止后稍等几秒以确保释放资源
    start
}

# 查看状态函数
status() {
    if is_running; then
        echo "应用正在运行，PID: $(cat $PID_FILE)"
    else
        echo "应用未运行"
    fi
}

# 帮助函数
help() {
    echo "用法: $0 {start|stop|restart|status|help}"
    echo "  start      启动应用"
    echo "  stop       停止应用"
    echo "  restart    重启应用"
    echo "  status     查看应用状态"
    echo "  help       显示帮助信息"
}

# 根据用户输入执行对应操作
case "$1" in
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
    help)
        help
        ;;
    *)
        echo "无效选项！"
        help
        ;;
esac