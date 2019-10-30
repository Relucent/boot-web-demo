#!/bin/bash
############################################################
# Server Startup Script :: (start|stop|restart|status)
# @author YYL
############################################################

WORKING_DIR=$(cd $(dirname $0) && pwd)
JARFILE="boot-web-demo.jar"
LOGFILE=${JARFILE%.jar}.log
STOP_WAIT_TIME=60

get_server_pid() {
  echo `ps -ef | grep java | grep $JARFILE | awk '{print $2}'`
}

start() {
  if [[ -n $pid ]]; then
    isRunning "$pid" && { echo "Server Already Running [$pid]"; return 0; }
  fi
  nohup java -jar -Xms128m -Xmx1024m ${WORKING_DIR}/${JARFILE} > ${LOGFILE} 2>&1 &
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
  tail -F -n 35 $LOGFILE
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