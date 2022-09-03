#!/bin/bash

# 构建镜像
doBuild(){
  podman build -f Dockerfile-ServerA -t server-a:latest .
  podman build -f Dockerfile-ClientA -t client-a:latest .
  podman build -f Dockerfile-ClientB -t client-b:latest .
}

# 运行镜像
doRun(){
  #podman network rm -f mynet
  #podman network create mynet
  #podman run -p 8000:8000 --name server-a -d --network mynet --network-alias servera server-a:latest
  #podman run -p 9000:9000 --name client-a -d --network mynet --network-alias clienta client-a:latest
  #podman run -p 9001:8080 --name client-b -d --network mynet --network-alias clientb client-b:latest
  podman run -p 8000:8000 --name server-a -d server-a:latest
  podman run -p 9000:9000 --name client-a -d --add-host server-a:192.168.137.22 client-a:latest
  podman run -p 9001:8080 --name client-b -d --add-host server-a:192.168.137.22 client-b:latest
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

# 检查在线状态
doCheck(){
  curl localhost:8000/check
  curl localhost:9000/check
  curl localhost:9001/check
}

while getopts ":brsd:e:l:c" opt
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
  c)
  doCheck
  ;;
  ?)
  echo "未知参数"
  exit 1;;
 esac
done