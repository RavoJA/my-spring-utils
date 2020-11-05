package jean.aime.myutils.errorhandler.erreur;

import jean.aime.myutils.errorhandler.AppResponseEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@ControllerAdvice
public class ErreurControlleur {
    private List<String> errors = new ArrayList<>();
    private AppHandlingError error = new AppHandlingError();

    @ExceptionHandler({AppResponseEntityException.class, Exception.class})
    public ResponseEntity<AppHandlingError> handleResponseError(HttpServletRequest req, Exception ex) {
        log.error("Request response entity: {} with error {} ", req.getRequestURL(), ex);
        log.error("", ex.getCause());
        errors.add(ex.getLocalizedMessage());
        AppHandlingError error = new AppHandlingError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<AppHandlingError> violationInegrity(DataIntegrityViolationException ex) {
        log.info("constraint error handling ");
        error.setMessage("This record already exist");
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setListErrors(Collections.singletonList(ex.getLocalizedMessage()));
        return new ResponseEntity<>(error, error.getStatus());
    }

}




