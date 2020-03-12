package jean.aime.myutils.core;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author JEAN-AIME
 * @version 1.0
 * @since java 1.8
 */

public final class GeneralService {

    static String EN_DATE_FORMAT = "yyyy/MM/dd";
    static String FR_DATE_FORMAT = "dd/MM/yyyy";
    static String DATE_FORMAT = "MMMM/yyyy";

    /**
     * @param pDateString date au format String et en format Francais
     * @return une instqnce de date suivant une daste String en parametre
     */
    public static Date changeDateFrToEn(String pDateString) throws Exception {
        Date date;
        try {
            SimpleDateFormat dt = new SimpleDateFormat(FR_DATE_FORMAT);
            if (pDateString.isEmpty()) {
                return new Date();
            }
            date = dt.parse(pDateString);
        } catch (Exception e) {
            throw new Exception("Veuillez entrer une date au format  30/01/2018, vous avez entre la date " + pDateString);
        }
        return date;
    }

    /**
     * Convert english date format to french date format DD/MM/YYY
     *
     * @param pDateEng date au format par defaut
     * @return un String representant un date qui correspond a la date envoye en parametre
     */
    public static String changeEnTofr(Date pDateEng) throws Exception {
        String date;
        try {
            SimpleDateFormat dt = new SimpleDateFormat(FR_DATE_FORMAT);
            date = dt.format(pDateEng);
        } catch (Exception e) {
            throw new Exception("Format de date incorect, vous avez entre la date " + pDateEng);
        }
        return date;
    }

    /**
     * Cette fonction a pour but de recuperer l'age d'une p[ersonne a partir d'une date mise en parametre en
     * se basant sur la date actuelle
     *
     * @param pDateNaissance la date de naissance de la personne
     * @return l'age correspondant
     */
    public static Integer getAgeFromDateNaissance(LocalDate pDateNaissance) throws Exception {
        int currentYear = LocalDate.now().getYear();
        int dateNaissYear = pDateNaissance.getYear();
        int current;

        if (currentYear < dateNaissYear) {
            throw new Exception("Veuillez saisir une date inferieur à la date actuelle");
        } else {
            current = currentYear - dateNaissYear;
        }
        return current;
    }

    /**
     * Cette fonction est pour verifier si la personne est majeur on non
     *
     * @param pDateDelivrance date de delivrance de la CIN
     * @param pDateNaissance  la date de naissance
     * @return verifier la difference entre les deux dates entree est superieur à 17
     * @throws Exception une exception est lancé si la conversion de la date echoue
     */
    public static Integer checkDateDelivranceFromDateNaissance(LocalDate pDateNaissance, LocalDate pDateDelivrance) throws Exception {
        int dateDeliv = pDateDelivrance.getYear();
        int dateNaissYear = pDateNaissance.getYear();
        int age;
        if (dateDeliv < dateNaissYear) {
            throw new Exception("La date de delivrance doit etre superieur a la date de naissance");
        } else {
            age = dateDeliv - dateNaissYear;
        }
        return age;
    }

    /**
     * @param totalPage valeur de la page au total en entier
     * @return un tableau des pages
     */
    public static int[] nbPages(int totalPage) {
        int[] nb_page = new int[totalPage];
        for (int i = 0; i < nb_page.length; i++) {
            nb_page[i] = i + 1;
        }
        return nb_page;
    }

    /**
     * @param page le numero de la page courante
     * @return valeur de la page courante
     */
    public static int pageCourante(int page) {
        int p = page - 1;
        if (p <= 0) {
            p = 0;
        }
        return p;
    }

    /**
     * Cette fonction a pour objectif d`afficher une heure superieur à 24:00,
     * ainsi on poura avoir une date comme 75:14 en valeur de retour
     *
     * @param pMinute la valeur de la minute
     * @return une valeur en format string d'une heure provenant d'une valeur entier de la minute
     */
    public static String convertMinuteToHiur(Integer pMinute) {
        int h = pMinute / 60;
        int min = pMinute % 60;
        return h + ":" + min;
    }

    /**
     * Effectuer une generation de sequence d'un numero pour obtenir une
     * numerotation incrementé en gardant une specific format comme 0001
     *
     * @param nombre le le nombre a incremente
     * @return String valeur retourne
     */

    public static String genereateSequentiel(String nombre) {
        String output;
        System.out.println("eto nyu valeurt " + nombre + " lengt ");
        switch (nombre.length()) {
            case 1:
                output = "000" + nombre;
                break;
            case 2:
                output = "00" + nombre;
                break;
            case 3:
                output = "0" + nombre;
                break;
            default:
                output = "" + nombre;
                break;
        }
        return output;
    }

    /**
     * Convert String date to LocalDate
     * if the conversion throw error, the result will be the Local date now value
     *
     * @param sDate     string date value
     * @param separator string date sperator, possible value (- or /)
     */

    public static LocalDate stringDateToLocaldate(String sDate, char separator) {
        // PATTERN IS THE FORMAT OF sDate
        LocalDate localDate;
        try {
            if (sDate.isEmpty()) {
                localDate = LocalDate.now();
            } else {
                localDate = LocalDate.parse(sDate, setFormarFR(separator));
            }
        } catch (Exception e) {
            localDate = LocalDate.now();
            e.printStackTrace();
        }
        return localDate;
    }


    /**
     * Convert String date to LocalDate
     * if the conversion throw error, the result will be now value
     *
     * @param sDate     string date value
     * @param separator string date sperator, possible value (- or /)
     */

    public static LocalDate stringDateToLocaldateEN(String sDate, char separator) {
        // PATTERN IS THE FORMAT OF sDate
        LocalDate localDate;
        try {
            if (sDate.isEmpty()) {
                localDate = LocalDate.now();
            } else {
                localDate = LocalDate.parse(sDate, setFormarEN(separator));
            }
        } catch (Exception e) {
            localDate = LocalDate.now();
            e.printStackTrace();
        }
        return localDate;
    }

    /**
     * Convert an english date format to string date format
     */
    public static String localDateToStringDate(LocalDate localDate, char separator) {
        String date;
        try {
            date = localDate.format(setFormarFR(separator));
        } catch (Exception e) {
            date = LocalDate.now().format(setFormarFR(separator));
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Convert an english date format to string date format
     */
    public static String localDateToStringDateEN(LocalDate localDate, char separator) {
        String date;
        try {
            date = localDate.format(setFormarEN(separator));
        } catch (Exception e) {
            date = LocalDate.now().format(setFormarEN(separator));
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Change format of date from String date
     */
    public static DateTimeFormatter setFormarFR(char separator) {
        String format = "dd/MM/yyyy";
        if (separator == '/' || separator == '-') {
            format = "dd" + separator + "MM" + separator + "yyyy";
        }
        return DateTimeFormatter.ofPattern(format);
    }

    /**
     * Change format of date from String date
     */
    public static DateTimeFormatter setFormarEN(char separator) {
        String format = "MM/dd/yyyy";
        if (separator == '/' || separator == '-') {
            format = "MM" + separator + "dd" + separator + "yyyy";
        }
        return DateTimeFormatter.ofPattern(format);
    }

    public static LocalDate dateToLocalDate(Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }

    public static Date localDateToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

}
