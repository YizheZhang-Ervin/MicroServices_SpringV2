# Neo4j Demo

## Usage
```
create (n:myNode {name:'李四',sex:'男',age:'22'})	

Match(n:myNode) RETURN n LIMIT 25
```

## 部署neo4j
```
默认账号密码：neo4j/neo4j

#解压缩

tar -zxvf neo4j-community-3.5.31-unix.tar.gz
#修改配置文件
vim ./conf/neo4j.conf
# 允许远程访问
dbms.connectors.default_listen_address=0.0.0.0
# 开启bolt服务，默认端口7687
dbms.connector.bolt.listen_address=:7687
# 开启http服务，默认端口7474
dbms.connector.http.listen_address=:7474
# 开启https服务，默认端口7473
dbms.connector.https.listen_address=:7473

cd neo4j-community-3.5.25/bin

#前台启动命令：
./neo4j console

#后台启动命令
./neo4j start
#状态
./neo4j status
#停止
./neo4j stop
#重启
./neo4j restart

#http访问
http://yourip:7474

#重置密码
connect:bolt://yourip:7687/
username:*
password:*
```