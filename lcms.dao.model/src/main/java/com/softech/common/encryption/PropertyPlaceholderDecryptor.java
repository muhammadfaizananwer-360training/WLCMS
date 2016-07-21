package com.softech.common.encryption;

import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Created by abdul.wahid on 6/13/2016.
 */

public class PropertyPlaceholderDecryptor extends PropertyPlaceholderConfigurer {

    private String secretKey;

    @Override
    protected String convertPropertyValue(String orgVal) {
        if (orgVal.startsWith("!!!")) {
            //encrypted text. need to decrypt.
            try {
                String val = EncryptionUtil.decrypt(secretKey, orgVal.substring(3));
                return val;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return orgVal;
        }
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public static void main(String[] args)  {

        System.out.println("STEPTS");
        System.out.println("--------");
        System.out.println("1: Generate secret key.");
        System.out.println("2: Replace org.springframework.beans.factory.config.PropertyPlaceholderConfigurer");
        System.out.println("   with com.softech.common.encryption.PropertyPlaceholderDecryptor");
        System.out.println("   in spring configuration. e.g. mvc-dispatcher-servlet.xml\n");
        System.out.println("3: Set generated key to com.softech.common.encryption.PropertyPlaceholderDecryptor.secretKey.");
        System.out.println("   <property name=\"secretKey\" value=\"{GENERATED KEY}\"/>\n");
        System.out.println("4: Encrypt the values whatever required in .properties file.");
        System.out.println("5: Replace plane values with their encryption in .properties file.");
        System.out.println("***********************************************************************");

        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("\n\n\nCOMMANDS");
            System.out.println("----------");
            System.out.println("gen:    To generate secret key.");
            System.out.println("enc:    To encrypt.");
            System.out.println("dec:    To decrypt.");
            System.out.println("exit:   To terminate.");
            System.out.println("==========================================\n\n\n");
            String cmd = null;
            cmd = scanner.nextLine();
            if (cmd.equalsIgnoreCase("exit")) {
                return;
            } else if (!StringUtil.equalsAny(cmd, true, "gen", "enc", "dec")) {
                System.out.println("Bad Command!");
                continue;
            }

            if (cmd.equalsIgnoreCase("gen")) {
                try {
                    String generatedKey = EncryptionUtil.generateKey();
                    System.out.println("GENERATED KEY");
                    System.out.println("-------------");
                    System.out.println(generatedKey + "\n");
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("KEY GENERATING FAILED!");
                    e.printStackTrace();
                    System.out.println("-------------\n");
                }

            } else {
                System.out.println("Enter secret key. (leave blank to go to main menu)");
                String key = scanner.nextLine();
                if (!key.trim().isEmpty()) {
                    System.out.println("Enter inputs. (leave blank to print result)");
                    List<String> list = new ArrayList<>();
                    while (true) {
                        String input = scanner.nextLine();
                        if (input.trim().isEmpty()) {
                            System.out.println("RESULT");
                            System.out.println("---------");
                            for(String item : list) {
                                System.out.println(item);
                            }
                            break;
                        } else if (cmd.equalsIgnoreCase("enc")) {
                            try {
                                list.add("!!!" + EncryptionUtil.encrypt(key,input));
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("ENCRYPTION FAILED! (you can continue entering inputs)");
                                e.printStackTrace();
                            }
                        }  else if (cmd.equalsIgnoreCase("dec")) {
                            try {
                                if(input.startsWith("!!!")) {
                                    input = input.substring(3);
                                }
                                list.add(EncryptionUtil.decrypt(key,input));
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("DECRYPTION FAILED! (you can continue entering inputs)");
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

     }
}
