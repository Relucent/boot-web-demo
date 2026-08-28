#!/bin/bash
############################################################
# Server Startup Script :: (start|stop|restart|status|log)
# @author YYL
############################################################

PROJECT="boot-web-demo"

XMS="128m"
XMX="512m"

WORKING_DIR=$(cd "$(dirname "$0")" && pwd)
JARFILE_PATH="${WORKING_DIR}/${PROJECT}.jar"
LOGFILE_PATH="${WORKING_DIR}/${PROJECT}.log"

STOP_WAIT_TIME=60

cd "$WORKING_DIR" || exit 1

get_server_pid() {
    ps -ef | grep '[j]ava' | grep "$JARFILE_PATH" | awk '{print $2}'
}

isRunning() {
    ps -p "$1" > /dev/null 2>&1
}

start() {
    pid=$(get_server_pid)

    if [[ -n "$pid" ]]; then
        isRunning "$pid" && {
            echo "Server Already Running [$pid]"
            return 0
        }
    fi

    echo "java -Xms${XMS} -Xmx${XMX} -jar ${JARFILE_PATH} >${LOGFILE_PATH} 2>&1"

    nohup java \
        -Xms"${XMS}" \
        -Xmx"${XMX}" \
        -jar "$JARFILE_PATH" \
        > "$LOGFILE_PATH" 2>&1 &

    sleep 1

    pid=$(get_server_pid)

    if [[ -n "$pid" ]] && isRunning "$pid"; then
        echo "Server Started [$pid]"
        return 0
    fi

    echo "Server Start Failed"
    return 1
}

stop() {
    pid=$(get_server_pid)

    [[ -n "$pid" ]] || {
        echo "Server Not Running!"
        return 0
    }

    echo "Stopping Server [$pid]..."

    kill "$pid" 2>/dev/null

    for i in $(seq 1 "$STOP_WAIT_TIME"); do
        if ! isRunning "$pid"; then
            echo "Server Stopped [$pid]"
            return 0
        fi

        if [[ "$i" -eq $((STOP_WAIT_TIME / 2)) ]]; then
            echo "Server still running, force killing [$pid]..."
            kill -9 "$pid" 2>/dev/null
        fi

        sleep 1
    done

    echo "Unable to kill process [$pid]"
    return 1
}

restart() {
    stop && start
}

status() {
    pid=$(get_server_pid)

    if [[ -n "$pid" ]] && isRunning "$pid"; then
        echo "Server Running [$pid]"
        return 0
    fi

    echo "Server Not Running"
    return 1
}

log() {
    tail -F -n 35 "$LOGFILE_PATH"
}

action="$1"

case "$action" in
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
    log)
        log
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status|log}"
        exit 1
        ;;
esac