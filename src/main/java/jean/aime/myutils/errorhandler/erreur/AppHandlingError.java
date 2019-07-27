package jean.aime.myutils.errorhandler.erreur;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class AppHandlingError {
    private HttpStatus status;
    private String message;
    private List<String> listErrors;

    public AppHandlingError(HttpStatus status, String message, List<String> listErrors) {
        this.status = status;
        this.message = message;
        this.listErrors = listErrors;
    }

    public AppHandlingError(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        this.listErrors = Collections.singletonList(error);
    }


}
