# Spring_IOC简版实现
# 1. 定义基础注解
```
Component: 用在被创建的对象上  ElementType.Type(用于类、接口、枚举)

Autowired: 自动注入Component对象 ElementType.Field

Qualifier: 注入Component的名 ElementType.Field

Value: 自动注入数值 Element.Field

```

# 2. 扫描传入包下的所有class
- 获取class的工具类
- 判断是否存在@Component注解，是则存入BeanDefination集合

# 3. 根据集合，利用实例化机制创建对象
拿到类进行实例化，然后根据有value注解的属性名，得到methodName,再拿到method，对方法进行调用设置属性

# 4. 自动注入对象的属性
这块需要根据Qualifier注解，判断是否写了beanName，如果没有写的话就默认用属性类名的首字母小写
从IOCCACHE中拿到bean，然后invoke set方法，注入到属性中
