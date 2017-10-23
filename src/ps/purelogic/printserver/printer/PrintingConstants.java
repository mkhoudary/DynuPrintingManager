/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ps.purelogic.printserver.printer;

import java.text.SimpleDateFormat;

/**
 *
 * @author m.elkhoudary
 */
public interface PrintingConstants {

    SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat();

    String D_LINE = "DLINE";

    String U_LINE = "ULINE";

    String D_LINE_CONTENT = "_________________________________________";

    String U_LINE_CONTENT = "¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯";

    String FOURTY_SPACE = "                                                            ";
    
}
