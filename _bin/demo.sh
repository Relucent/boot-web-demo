#!/bin/bash
############################################################
# Server Startup Script :: (start|stop|restart|status)
# @author YYL
############################################################

PROJECT="boot-web-demo"

XMS="128m";
XMX="512m"

WORKING_DIR=$(cd $(dirname $0) && pwd)
JARFILE_PATH="${WORKING_DIR}/${PROJECT}.jar"
LOGFILE_PATH="${WORKING_DIR}/${PROJECT}.log"

STOP_WAIT_TIME=60

cd ${WORKING_DIR}

get_server_pid() {
  echo `ps -ef | grep java | grep ${JARFILE_PATH} | awk '{print $2}'`
}

start() {
  if [[ -n $pid ]]; then
    isRunning "$pid" && { echo "Server Already Running [$pid]"; return 0; }
  fi
  echo "java -jar -Xms${XMS} -Xmx${XMX} ${JARFILE_PATH} >${LOGFILE_PATH} 2>&1"
  nohup java -jar -Xms${XMS} -Xmx${XMX} ${JARFILE_PATH} >${LOGFILE_PATH} 2>&1 &
  pid=`get_server_pid`
  echo "Server Started $pid"
}

stop() {
  [[ -n $pid ]] || { echo "Server Not Running!"; return 0; }
  kill -9 $pid
  for i in $(seq 1 $STOP_WAIT_TIME); do
    isRunning $pid || { echo "Server Stopped $pid"; return 0; }
    [[ $i -eq STOP_WAIT_TIME/2 ]] && kill $pid &> /dev/null
    sleep 1
  done
  echo "Unable to kill process $pid"
  return 1;
}

restart() {
  stop && start
}

status() {
  if [[ -n "$pid" ]]; then
    isRunning "$pid" && { echo "Server Running [$pid]"; return 0; }
  fi
  echo "Server Not Running "; return 1;
}

log(){
  tail -F -n 35 $LOGFILE_PATH
}

isRunning() {
  ps -p "$1" &> /dev/null
}

action="$1"
pid=`get_server_pid`

case "$action" in
start)
  start "$@"; exit $?;;
stop)
  stop "$@"; exit $?;;
restart)
  restart "$@"; exit $?;;
status)
  status "$@"; exit $?;;
log)
  log "$@"; exit $?;;
*)
  echo "Usage: $0 {start|stop|restart|status}"; exit 1;
esac

exit 0