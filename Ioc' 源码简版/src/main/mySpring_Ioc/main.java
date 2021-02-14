import logic.MyAnnotationConfigApplicationContext;

import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
       MyAnnotationConfigApplicationContext applicationContext = new MyAnnotationConfigApplicationContext("entities");
        String[] beanNameList = applicationContext.getBeanNameList();
        for (String beanName: beanNameList){
            System.out.println(beanName);
            System.out.println(applicationContext.getBean(beanName));
        }
    }
}
