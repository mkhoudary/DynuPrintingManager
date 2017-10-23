package ps.purelogic.printserver.printer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.print.PrinterJob;
import javax.print.PrintService;
import java.util.List;
import java.util.ArrayList;

public final class PrintUtility {

    /**
     * Retrieve a Print Service with a name containing the specified PrinterName; will return null if not found.
     * 
     * @return
     */
    public static PrintService findPrintService(String printerName) {

        printerName = printerName.toLowerCase();

        PrintService service = null;

        // Get array of all print services
        PrintService[] services = PrinterJob.lookupPrintServices();

        // Retrieve a print service from the array
        for (int index = 0; service == null && index < services.length; index++) {

            if (services[index].getName().toLowerCase().equals(printerName.toLowerCase())) {
                service = services[index];
            }
        }

        // Return the print service
        return service;
    }

    /**
     * Retrieves a List of Printer Service Names.
     * 
     * @return List
     */
    public static List<String> getPrinterServiceNameList() {

        // get list of all print services
        PrintService[] services = PrinterJob.lookupPrintServices();
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < services.length; i++) {
            list.add(services[i].getName());
        }

        return list;
    }

    /**
     * Utility class; no construction!
     */
     private PrintUtility() {}
}