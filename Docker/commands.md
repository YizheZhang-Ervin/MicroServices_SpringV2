# 打包准备
- /home/ervin/developing/play_spring里
    - server-a-1.0.jar、Dockerfile-ServerA
    - client-a-1.0.jar、Dockerfile-ClientA
    - client-b.jar、Dockerfile-ClientB
    - admin.sh
    
---
# admin.sh操作
```
# 打包三个镜像
sh admin.sh -b

# 运行三个镜像
sh admin.sh -r

# 停止三个镜像
sh admin.sh -s

# 删除某个容器
sh admin.sh -d xxContainer

# 进入某个容器
sh admin.sh -e xxContainer

# 查看某个容器日志
sh admin.sh -l xxContainer
```

---

# 原生容器镜像操作
## 构建
```
podman build -f Dockerfile-ServerA -t server-a .
```
## 启停
```
podman images
podman run -p 8080:8080 -d --name server-a localhost/server-a:latest
podman ps
podman stop server-a
```

## 删除镜像/容器
```
podman rmi -f localhost/server-a:latest
podman rm -f server-a
```