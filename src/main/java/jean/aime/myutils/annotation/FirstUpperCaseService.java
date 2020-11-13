package jean.aime.myutils.annotation;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * @author Jean Aimé
 * Set uppercate the first letter of the sintence
 */
@Service
public class FirstUpperCaseService {
    public <T> void setFirstUpperCase(T targetString, Class<?> aClass) throws IllegalAccessException {
        for (Field field : aClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(FirstUppercase.class)) {
                if (field.getType().equals(String.class)) {
                    String fieldValue = (String) field.get(targetString);
                    String result = fieldValue.substring(0, 1).toUpperCase() + fieldValue.substring(1).toLowerCase();
                    field.set(targetString, result);
                } else {
                    throw new IllegalArgumentException("FirstUpperCase annotation cannot be applied for the given type " + field.getType().getName());
                }
            }
        }
    }


}
