/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ps.purelogic.printserver.printer;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author m.elkhoudary
 */
public class ThermalPrintingManager {
    
    private static Map<String, String> properties;

    public static void initialize() throws IOException, FontFormatException {
        Properties config = new Properties();
        config.load(new FileInputStream(new File("config.properties")));
        
        properties = new HashMap<>();
        
        properties.put("paper-width", "3");
        properties.put("paper-height", "3.69");
        properties.put("paper-left-margin", "0.12");
        properties.put("paper-right-margin", "0.10");
        properties.put("paper-top-margin", "0.12");
        properties.put("paper-bottom-margin", "0.01");
        properties.put("paper-size-pixels-factor", "200");
        
        config.entrySet().stream().forEach((configuration) -> {
            properties.put(configuration.getKey().toString(), configuration.getValue().toString());
        });
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        if (StringUtils.isNotBlank(properties.get("mono-font-path"))) {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(properties.get("mono-font-path"))));
        }
        
        if (StringUtils.isNotBlank(properties.get("mono-barcode-font-path"))) {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(properties.get("mono-barcode-font-path"))));
        }
    }
    
    public static void main(String[] args) throws IOException, FontFormatException {
        initialize();
    }

}
