#!/bin/bash

# 构建镜像
doBuild(){
  podman build -f Dockerfile-ServerA -t server-a:latest .
  podman build -f Dockerfile-ClientA -t client-a:latest .
  podman build -f Dockerfile-ClientB -t client-b:latest .
}

# 运行镜像
doRun(){
  podman network create mynet
  podman run -p 8000:8000 --name server-a -d --net mynet server-a:latest
  podman run -p 9000:9000 --name client-a -d --net mynet client-a:latest
  podman run -p 9001:9001 --name client-b -d --net mynet client-b:latest
}

# 停止镜像
doStop(){
  podman stop server-a
  podman stop client-a
  podman stop client-b
}

# 删除容器
doDelete(){
  if [[ $1 = "server-a" ]];
  then
    podman rm -f server-a
  elif [[ $1 = "client-a" ]];
  then
    podman rm -f client-a
  elif [[ $1 = "client-b" ]];
  then
    podman rm -f client-b
  fi
}

# 进入容器
doExec(){
  podman exec -it "$1" /bin/bash
}

# 容器日志
doLog(){
  podman logs "$1"
}

while getopts ":brsd:e:l:" opt
do
 case $opt in
  b)
  doBuild
  ;;
  r)
  doRun
  ;;
  s)
  doStop
  ;;
  d)
  doDelete "$OPTARG"
  ;;
  e)
  doExec "$OPTARG"
  ;;
  l)
  doLog "$OPTARG"
  ;;
  ?)
  echo "未知参数"
  exit 1;;
 esac
done