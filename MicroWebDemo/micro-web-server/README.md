# micro-web-server

## 逻辑整理
1. token拦截 (Jwt.sql)
   JwtInterceptor—>JwtHandler->JwtServiceImpl->JwtService->JwtMapper->Jwt/JwtVo/JwtTransformer
   
2. 用户登录/注册/更新/登出 (Jwt.sql,JwtMapper.xml)
   JwtController->JwtServiceImpl->JwtService->JwtMapper->Jwt/JwtVo/JwtTransformer
   
3. 全局配置 
   MybatisMetaObjectHandler: 插入更新时自动更新字段
   MybatisPlusConfig: 分页&乐观锁
   WebInterceptorConfig: 拦截器配置&eureka静态资源&默认返回json格式
   ErrorControllerAdvice->ResultData->ReturnCode: 全局异常处理
   application.properties

4. User相关 (User.sql,UserMapper.xml)
   test->UserServiceImpl->UserService->UserMapper->User->SexEnum
   
   







