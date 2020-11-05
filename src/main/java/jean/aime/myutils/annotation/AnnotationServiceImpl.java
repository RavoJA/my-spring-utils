package jean.aime.myutils.annotation;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Jean Aimé
 * Set uppercate the first letter of the sintence
 */
@Service
public class AnnotationServiceImpl {

    @PersistenceContext
    EntityManager vEntityManager;

    public <T> void makeUpperCase(T targetEntity, Class<?> aClass) throws IllegalAccessException, NoSuchFieldException {
        for (Field field : aClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Uppercase.class)) {
                if (field.getType().equals(String.class)) {

                    Uppercase annotation = field.getAnnotation(Uppercase.class);
                    String sourceFieldName = annotation.target().trim();
                    Field sourceField = aClass.getDeclaredField(sourceFieldName);
                    sourceField.setAccessible(true);
                    String targetFieldValue = (String) sourceField.get(targetEntity);


                    String result = makeStringUppercase(targetFieldValue);
                    field.set(targetEntity, result);

                } else {
                    throw new IllegalArgumentException("UpperCase annotation cannot be applied for the given type " + field.getType().getName());
                }
            }
        }
    }


    @Transactional
    public <T> List<T> checkUniqValue(T targetEntity, Class<?> aClass) throws IllegalAccessException {
        List<T> finalResult = new ArrayList<>();
        for (Field field : aClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(UniqueValue.class)) {
                if (field.getType().equals(String.class)) {
                    String fieldValue = (String) field.get(targetEntity);
                    String vQuery = String.format("select record from %s record where record.%s=:pValue", aClass.getSimpleName(), field.getName());
                    Optional<?> result = vEntityManager.unwrap(Session.class).createQuery(vQuery, aClass)
                            .setParameter("pValue", fieldValue)
                            .stream().findFirst();
                    result.ifPresent(o -> finalResult.add((T) o));
                } else {
                    throw new IllegalArgumentException("Annotation unique check cannot be applied for the given type " + field.getType().getName());
                }
            }
        }
        return finalResult;
    }

    private String makeStringUppercase(final String targetStringValue) {
        String code = targetStringValue.replaceAll(" ", "_").toUpperCase();
        code = code.replaceAll("[ÙÚÛûù]", "U");
        code = code.replaceAll("[ÌÍÎÏïî]", "I");
        code = code.replaceAll("[ÈÉÊËëêèé]", "E");
        code = code.replaceAll("[ÒÓÔÕÖØôö]", "O");
        code = code.replaceAll("[ÀÁÂÃÄÅàâä]", "A");
        code = code.replaceAll("[Çç]", "C");
        code = code.replaceAll("[',-]", "_");
        code = code.replaceAll("[()$&~@%µ§*#°]", "_");
        return code;
    }
}
