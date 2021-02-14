package logic;

import annotations.Autowired;
import annotations.Component;
import annotations.Qualifier;
import annotations.Value;
import tools.MyTools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class MyAnnotationConfigApplicationContext {
    private Map<String,Object> IOCCACHE = new HashMap<String, Object>();

    // 向外抛出， 在外边拿到beanName，再到IOC中拿到这个数据;
    private List<String> beanNameList = new ArrayList<String>();
    public String[] getBeanNameList() {
        return beanNameList.toArray(new String[0]);
    }
    public Object getBean(String beanName){
       return IOCCACHE.get(beanName);
    }
    public MyAnnotationConfigApplicationContext(String pack) {
        // 获取所有的有@Component注解的类，构建BeanDefinition<String,Class>Set;
        System.out.println("构建BeanDefinition<String,Class>Set");
        Set<BeanDefinition> beanDefinitionSet = getBeanDefinitions(pack);

        // 根据集合反射创建对象，将@Value属性注入
        reflectBeanObjectByDefinition(beanDefinitionSet);

        // 将对象进行注入
        AutowiredBeanObjectByDefinition(beanDefinitionSet);
    }

    private  Set<BeanDefinition> getBeanDefinitions(String pack){
        Set<Class<?>> classes = MyTools.getClasses(pack);
        Iterator<Class<?>> iterator = classes.iterator();
        Set<BeanDefinition> beanDefinitionSet = new HashSet<BeanDefinition>();
        while (iterator.hasNext()){
            Class<?> clasz = iterator.next();
            Component componentAnnotation = clasz.getAnnotation(Component.class);
            if(componentAnnotation != null){
                // beanName, 如果有参数则为参数，否则类名首字母小写
                String beanName = componentAnnotation.value();
                if("".equals(beanName)){
                    beanName = getBeanNameByClass(clasz);
                }
                BeanDefinition beanDefinition = new BeanDefinition(beanName,clasz);
                beanDefinitionSet.add(beanDefinition);

                // 添加到beanNamesList中，用于统计Bean的数量
                beanNameList.add(beanName);
            }
        }
        return beanDefinitionSet;
    }

    private void reflectBeanObjectByDefinition(Set<BeanDefinition> beanDefinitionSet){
        Iterator<BeanDefinition> iterator = beanDefinitionSet.iterator();
        while (iterator.hasNext()){
            BeanDefinition beanDefinition = iterator.next();
            Class beanClass = beanDefinition.getBeanClass();
            String beanName = beanDefinition.getBeanName();
            try {
                Object object = beanClass.getConstructor().newInstance();
                // getDeclareFields 获取所有定义的数据，  区别于getFields： 获取public数据
                Field[] declareFields = beanClass.getDeclaredFields();
                for(Field declareField: declareFields){
                   Value valueAnnotation = declareField.getAnnotation(Value.class);
                   System.out.println(declareField);
                   if(valueAnnotation != null){
                       String value = valueAnnotation.value();
                       String fieldName = declareField.getName();
                       String methodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                       System.out.println(methodName);
                       // 获取set方法名
                       Method method = beanClass.getMethod(methodName,declareField.getType());
                       // 根据field对象类型，转换数据, 这块可以再细化一下
                       Object val = null;
                       switch (declareField.getType().getName()){
                           case "java.lang.Integer":
                               val = Integer.parseInt(value);
                               break;
                           case "java.lang.String":
                               val = value;
                               break;
                           case "java.lang.Float":
                               val = Float.parseFloat(value);
                               break;
                       }
                       // 调用Set方法，向属性注入数据
                       method.invoke(object,val);
                   }
                }
                IOCCACHE.put(beanName,object);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
    private String getBeanNameByClass(Class<?> clasz){
        String classAllName = clasz.getName();
        String packName = clasz.getPackageName();
        String className = classAllName.replace(packName+".","");
        return className.substring(0,1).toLowerCase() + className.substring(1);
    }

    private void AutowiredBeanObjectByDefinition(Set<BeanDefinition> beanDefinitionSet){
        Iterator<BeanDefinition> iterator = beanDefinitionSet.iterator();
        while (iterator.hasNext()) {
            BeanDefinition beanDefinition = iterator.next();
            Class beanClass = beanDefinition.getBeanClass();
            String beanName = beanDefinition.getBeanName();
            Object object = IOCCACHE.get(beanName);
            Field[] declareFields = object.getClass().getDeclaredFields();
            for (Field declareField :declareFields){
                try {
                    Autowired autowiredAnnotation = declareField.getAnnotation(Autowired.class);
                    if(autowiredAnnotation != null){
                        // 得到要注入的BeanName, 通过IOCCACHE获得BEAN
                        Qualifier qualifierAnnotation = declareField.getAnnotation(Qualifier.class);
                        String autowiredBeanName = getBeanNameByClass(declareField.getType());
                        if(qualifierAnnotation != null){
                            autowiredBeanName = qualifierAnnotation.value();
                        }
                        Object autowiredBean = IOCCACHE.get(autowiredBeanName);
                        // 获取Set方法
                        String fieldName = declareField.getName();
                        String methodName = "set"+ fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                        Method method = beanClass.getMethod(methodName, declareField.getType());
                        method.invoke(object,autowiredBean);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
