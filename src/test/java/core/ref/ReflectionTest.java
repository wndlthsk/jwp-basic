package core.ref;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.model.Question;
import next.model.User;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            logger.debug(String.valueOf(field));
        }

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> c : constructors) {
            logger.debug(String.valueOf(c));
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            logger.debug(String.valueOf(m));
        }
    }
    
    @Test
    public void newInstanceWithConstructorArgs() throws Exception {
        Class<User> clazz = User.class;
        logger.debug(clazz.getName());

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        User user = (User) constructors[0].newInstance("userId", "password", "name", "email");
        logger.debug(user.toString());
    }
    
    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student student = new Student();
        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "주한");

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.setInt(student, 14);

        logger.debug("Name: {}, Age: {}", student.getName(), student.getAge());
    }
}
