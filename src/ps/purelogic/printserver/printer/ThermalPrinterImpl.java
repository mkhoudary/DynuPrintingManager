package ps.purelogic.printserver.printer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import ps.purelogic.printserver.Tafqeet;
import static ps.purelogic.printserver.printer.PrintingConstants.DATE_FORMATTER;
import static ps.purelogic.printserver.printer.PrintingConstants.D_LINE;
import static ps.purelogic.printserver.printer.PrintingConstants.D_LINE_CONTENT;
import static ps.purelogic.printserver.printer.PrintingConstants.FOURTY_SPACE;
import static ps.purelogic.printserver.printer.PrintingConstants.U_LINE;

/**
 *
 * @author m.elkhoudary
 */
public class ThermalPrinterImpl {


    private static PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

    public static void initialize() {
        
    }

    public static void printEmptySample(
            String documentTitle,
            String username,
            String printerName) throws Exception {

        double paperWidth = 3;//3.25
        double paperHeight = 3.69;//11.69
        double leftMargin = 0.12;
        double rightMargin = 0.10;
        double topMargin = 0.12;
        double bottomMargin = 0.01;

        Paper paper = new Paper();
        paper.setSize(paperWidth * 200, paperHeight * 200);
        paper.setImageableArea(leftMargin * 200, topMargin,
                (paperWidth - leftMargin - rightMargin) * 200,
                (paperHeight - topMargin - bottomMargin) * 200);

        aset.add(OrientationRequested.PORTRAIT);

        PrintService printService = PrintUtility.findPrintService(printerName);

        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintService(printService);

        Printable printable = new EmptyPaperPrint(documentTitle,
                username
        );

        PageFormat format = new PageFormat();
        format.setPaper(paper);

        format = printerJob.validatePage(format);

        printerJob.setPrintable(printable, format);
        printerJob.print(aset);
    }

    public static String completeWithEmpty(String text) {
        if (text.length() > 40) {
            return text.substring(0, 40);
        }

        int deduction = occurancies(text, "لا") + occurancies(text, "لأ") + occurancies(text, "لإ");

        return text + FOURTY_SPACE.substring(0, 40 - text.length() + deduction) + "\n";
    }

    public static String completeWithUnderscores(String text) {
        if (text.length() > 40) {
            return text.substring(0, 40);
        }

        int deduction = occurancies(text, "لا") + occurancies(text, "لأ") + occurancies(text, "لإ");

        return text + D_LINE_CONTENT.substring(0, 40 - text.length() + deduction) + "\n";
    }

    public static String completeUptoSpecific(String text, int upto) {
        int deduction = occurancies(text, "لا") + occurancies(text, "لأ") + occurancies(text, "لإ");

        return text + FOURTY_SPACE.substring(0, (upto - text.length() + deduction) > 0 ? (upto - text.length() + deduction) : 0);
    }

    public static String completeUptoSpecificFromStart(String text, int upto) {
        int deduction = occurancies(text, "لا") + occurancies(text, "لأ") + occurancies(text, "لإ");

        return FOURTY_SPACE.substring(0, (upto - text.length() + deduction) > 0 ? (upto - text.length() + deduction) : 0) + text;
    }

    public static String centerWithEmpty(String text) {
        int deduction = occurancies(text, "لا") + occurancies(text, "لأ") + occurancies(text, "لإ");

        int spaces = (40 - text.length() + deduction) / 2;

        return FOURTY_SPACE.substring(0, spaces) + text + FOURTY_SPACE.substring(0, spaces) + "\n";
    }

    public static int occurancies(String str, String findStr) {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {

            lastIndex = str.indexOf(findStr, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return count;
    }

    static class EmptyPaperPrint implements Printable {

        private final String greetings = "ون موبايل - شركة القرن";
        private final String documentTitle;
        private final String byUser;

        private int lineCount;

        private JTextArea textarea;

        private final int perPage = 40;

        public EmptyPaperPrint(String documentTitle,
                String byUser) {
            this.documentTitle = documentTitle;
            this.byUser = byUser;

            initialize();
        }

        private void initialize() {
            DATE_FORMATTER.applyPattern("dd/MM/yyyy HH:mm:ss");

            final String LF = "\n";// text string to output

            textarea = new JTextArea(10, 40);

            textarea.append(D_LINE + "\n");

            textarea.append(centerWithEmpty(documentTitle));

            textarea.append(D_LINE + "\n");
            textarea.append(completeWithEmpty(completeUptoSpecific("الوقت :", 14) + DATE_FORMATTER.format(new Date(System.currentTimeMillis()))));
            textarea.append(U_LINE + "\n");

            textarea.append(completeUptoSpecific("الصنف", 22));
            textarea.append(completeUptoSpecific("كمية", 9));
            textarea.append(completeUptoSpecific("السعر", 9));
            textarea.append(LF);

            textarea.append(D_LINE + "\n");

            textarea.append(completeUptoSpecific("صنف تجريبي", 22));
            textarea.append(completeUptoSpecific("2", 9));
            textarea.append(completeUptoSpecific("150", 9));
            textarea.append(LF);

            textarea.append(U_LINE + "\n");

            textarea.append(completeWithEmpty("فقط " + Tafqeet.doTafqeet(new BigDecimal("150")) + " لا غير"));

            textarea.append(U_LINE + "\n");

            textarea.append(completeWithEmpty("محل النصر" + " - بواسطة " + byUser));

            textarea.append(U_LINE + "\n");
            textarea.append(centerWithEmpty("للتواصل والاستفسار: 2877610"));
            textarea.append(U_LINE + "\n");

            textarea.append(centerWithEmpty(greetings));
            textarea.append(DATE_FORMATTER.format(new Date()) + LF);
            textarea.setEditable(false);

            textarea.append(U_LINE + "\n");

            lineCount = textarea.getLineCount();
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
                throws PrinterException {
            Graphics2D g2d = (Graphics2D) graphics;

            if (pageIndex > Math.floor(lineCount / perPage)) {
                return Printable.NO_SUCH_PAGE;
            }

            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            Image image = new ImageIcon("d:/logo2.png").getImage();

            if (image.getHeight(null) <= 0) {
                throw new RuntimeException();
            }

            g2d.drawImage(image, 1, 0, null);

            Font font = new Font("Free 3 of 9 Extended", Font.PLAIN, 32);
            g2d.setFont(font);

            g2d.drawString("*" + 1234 + "*", 1, 37);

            font = new Font("Kawkab Mono", Font.BOLD, 7);
            g2d.setFont(font);
            g2d.drawString(String.valueOf(1234), 1, 46);
            int overallHeight = image.getHeight(null) + 2;

            String strText;
            int lineStart;
            int lineEnd;

            int startingLines = perPage;
            int lineNumber = pageIndex * perPage;

            while (startingLines != 0) {
                try {
                    lineStart = textarea.getLineStartOffset(lineNumber);
                    lineEnd = textarea.getLineEndOffset(lineNumber);
                    strText = textarea.getText(lineStart, lineEnd - lineStart);
                } catch (Exception exception) {
                    break;
                }

                overallHeight += 18;

                if (strText.contains("DLINE")) {
                    overallHeight -= 8;
                    strText = D_LINE_CONTENT;
                } else if (strText.contains("ULINE")) {
                    overallHeight -= 8;
                    strText = D_LINE_CONTENT;
                }

                g2d.drawString(strText, 1, overallHeight);
                lineNumber = lineNumber + 1;

                startingLines--;
            }

            if (startingLines == perPage) {
                return Printable.NO_SUCH_PAGE;
            }

            return Printable.PAGE_EXISTS;
        }
    }

    public static void main(String[] args) throws Exception {
        initialize();

        printEmptySample("تجريب", "mkhoudary", "BIXOLON-1");

        //WebSocketsOperator.initializeOperator(8123);
    }
}
