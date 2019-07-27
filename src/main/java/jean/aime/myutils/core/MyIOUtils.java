package jean.aime.myutils.core;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface MyIOUtils {

    static void getFileDocument(String filename, HttpServletResponse response) throws IOException {
        InputStream inputStream = new FileInputStream(new File(filename));
        int nRead;
        while ((nRead = inputStream.read()) != -1) {
            response.getWriter().write(nRead);
        }
    }


}
