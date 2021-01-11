package correcter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        menu();

    }
    static void menu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print ("Write a mode: ");
        String menu = scanner.next();
        switch (menu) {
            case "encode"://"encode"
                //encode();
                encodeHamming();
                break;
            case "send"://"send"
                //send();
                sendHamming();
                break;
            case "decode":
                //decode();
                decodeHamming();
                break;
            default:
        }
    }
    static void encodeHamming() throws IOException {
        File sendFile = new File("send.txt");
        String encodeFile = "";
        try (Scanner inputFile = new Scanner(sendFile)) {
            while (inputFile.hasNextLine()) {
                encodeFile = encodeFile.concat(inputFile.nextLine());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] byteSendFile = encodeFile.getBytes();
        String[] binaryStringSend = new String[byteSendFile.length];
        String[] hexStringSend = new String[byteSendFile.length];
        for (int i = 0; i < byteSendFile.length; i++) {
            binaryStringSend[i] = Integer.toBinaryString(byteSendFile[i]);
            hexStringSend[i] = Integer.toHexString(byteSendFile[i]);
            if (binaryStringSend[i].length() == 7) {
                binaryStringSend[i] = "0".concat(binaryStringSend[i]);
            } else if (binaryStringSend[i].length() == 6) {
                binaryStringSend[i] = "00".concat(binaryStringSend[i]);
            }
        }
       // System.out.println(Arrays.toString(binaryStringSend));
       // System.out.println(Arrays.toString(hexStringSend));

        StringBuilder forExpand = new StringBuilder();
        for (String val: binaryStringSend) {
            forExpand.append(val);
        }
        String expandString = String.valueOf(forExpand);
       // System.out.println(forExpand + "\n" + forExpand.length());
        String[] expand = new String[binaryStringSend.length * 2];
        int index = 0;
        for (int i = 0; i < expand.length; i++) {
            expand[i] = "";
            for (int j = 0; j < 8; j++) {
                if (j == 0 || j == 1 || j == 7 || j == 3) {
                    expand[i] = expand[i].concat(".");
                } else {
                    if (index < expandString.length()) {
                        expand[i] = expand[i].concat(String.valueOf(expandString.charAt(index)));
                        index++;
                    }

                }
            }
        }
        System.out.println(Arrays.toString(expand));

        String[] parity = new String[expand.length];
        for (int i = 0; i < parity.length; i++) {
            parity[i] = "";
            for (int j = 0; j < 8; j++) {
                if (j == 0) {
                    parity[i] = parity[i].concat(String.valueOf(unSignificant1(expand[i])));
                } else if (j == 1) {
                    parity[i] = parity[i].concat(String.valueOf(unSignificant2(expand[i])));
                } else if (j == 3) {
                    parity[i] = parity[i].concat(String.valueOf(unSignificant4(expand[i])));
                } else if (j == 7) {
                    parity[i] = parity[i].concat("0");
                } else {
                    parity[i] = parity[i].concat(String.valueOf(expand[i].charAt(j)));
                }

            }
        }
        System.out.println(Arrays.toString(parity));
        File encodedTxt = new File("encoded.txt");
        byte[] two = new byte[parity.length];
        int[] two2 = new int[parity.length];
        String[] hexOut = new String[parity.length];

        for (int i = 0; i < two2.length; i++) {
            //two[i] =   Byte.parseByte(parity[i], 2);
            two2[i] = Integer.parseInt(parity[i],2); ////Перевод в десятичную систему кодированной строки
            two[i] = (byte) two2[i]; //приведение цифры к байтам

        }
        System.out.println(Integer.parseInt(parity[0], 2));
        //запись в файл для передачи
        try {
            //FileWriter outNewStr = new FileWriter(encodedTxt, true);
            OutputStream out = new FileOutputStream(encodedTxt);
            out.write(two);
            //outNewStr.write(newOut2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(two));
        System.out.println(encodeFile);
        System.out.println(Arrays.toString(hexOut));

    }
    static char unSignificant1(String str) {
        int[] encode = new int[str.length()];
        int sum = 0;
        for (int i = 0; i < encode.length; i++) {
            if (i == 2 || i == 4 || i == 6) {
                encode[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
                sum += encode[i];
            }

        }
        return sum % 2 == 0 ? '0': '1';
    }
    static char unSignificant2(String str) {
        int[] encode = new int[str.length()];
        int sum = 0;
        for (int i = 0; i < encode.length; i++) {
            if (i == 2 || i == 5 || i == 6) {
                encode[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
                sum += encode[i];
            }

        }
        return sum % 2 == 0 ? '0': '1';
    }
    static char unSignificant4(String str) {
        int[] encode = new int[str.length()];
        int sum = 0;
        for (int i = 0; i < encode.length; i++) {
            if (i == 4 || i == 5 || i == 6) {
                encode[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
                sum += encode[i];
            }

        }
        return sum % 2 == 0 ? '0': '1';
    }

    static void sendHamming() throws IOException {
        try {
            InputStream encode = new FileInputStream("encoded.txt");

            byte[] inputEncode = encode.readAllBytes();
            //System.out.println(Arrays.toString(inputEncode));
            String[] clearEncode = new String[inputEncode.length];
            for (int i = 0; i < clearEncode.length; i++) {
                //clearEncode[i] = String.format("%8s", Integer.toBinaryString(inputEncode[i]).replace(' ', '0'));
                clearEncode[i] = Integer.toBinaryString(inputEncode[i]);
                if (clearEncode[i].length() < 8) {
                    while (clearEncode[i].length() < 8) {
                        clearEncode[i] = "0".concat(clearEncode[i]);
                    }

                    //clearEncode[i] = String.format("%8s", clearEncode[i].replaceAll("\\s+", "0*"));
                } else {
                    clearEncode[i] = clearEncode[i].substring(clearEncode[i].length() - 8);
                }
            }
            //System.out.println(Arrays.toString(clearEncode));
            //printStringArray(clearEncode);
            //clearEncode[0].charAt(1) = 2;

            Random random = new Random();
            String[] errorCode = new String[clearEncode.length];
            for (int i = 0; i < errorCode.length; i++) {
                int x = random.nextInt(8);
                if (clearEncode[i].charAt(x) == '1') {
                    errorCode[i] = clearEncode[i].substring(0, x) + '0' + clearEncode[i].substring(x + 1);
                } else {
                    errorCode[i] = clearEncode[i].substring(0, x) + '1' + clearEncode[i].substring(x + 1);
                }

            }
            //System.out.println(Arrays.toString(errorCode));
            int[] two2 = new int[errorCode.length];
            byte[] two = new byte[errorCode.length];

            for (int i = 0; i < two2.length; i++) {
                //two[i] =   Byte.parseByte(parity[i], 2);
                two2[i] = Integer.parseInt(errorCode[i],2); ////Перевод в десятичную систему кодированной строки
                two[i] = (byte) two2[i]; //приведение цифры к байтам
            }
            // System.out.println(Arrays.toString(two));
            try {
                //FileWriter outNewStr = new FileWriter(encodedTxt, true);
                File received = new File("received.txt");
                OutputStream out = new FileOutputStream(received);
                out.write(two);
                //outNewStr.write(newOut2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //System.out.println(Arrays.toString(errorCode));
            System.out.println(Arrays.toString(clearEncode));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }




    }

    static void decodeHamming() throws IOException {
        sendHamming();
        InputStream received = new FileInputStream("received.txt");
        byte[] receivBytes = received.readAllBytes();
        // System.out.println(Arrays.toString(receivBytes));
        String[] receivString = new String[receivBytes.length];
        for (int i = 0; i < receivString.length; i++) {
            receivString[i] = Integer.toBinaryString(receivBytes[i]);
            if (receivString[i].length() < 8) {
                while (receivString[i].length() < 8) {
                    receivString[i] = "0".concat(receivString[i]);
                }
            } else {
                receivString[i] = receivString[i].substring(receivString[i].length() - 8);
            }
        }
        //System.out.println(Arrays.toString(receivString));
        //System.out.print("]");
        int[][] receiveDigit = new int[receivString.length][8];
        for (int i = 0; i < receiveDigit.length; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 7) {
                    receiveDigit[i][j] = 0;
                } else {
                    receiveDigit[i][j] = Integer.parseInt(String.valueOf(receivString[i].charAt(j)));
                }

               // System.out.print(receiveDigit[i][j]);
            }
            //System.out.print(", ");
        }
        for (int i = 0; i < receiveDigit.length; i++) {
           int x1 = receiveDigit[i][2] + receiveDigit[i][4] + receiveDigit[i][6];
           int x2 = receiveDigit[i][2] + receiveDigit[i][5] + receiveDigit[i][6];
           int x4 = receiveDigit[i][4] + receiveDigit[i][5] + receiveDigit[i][6];

            int index1 = (x1 % 2 == 0 && receiveDigit[i][0] == 0 || x1 % 2 != 0 && receiveDigit[i][0] == 1) ? 0 : 1;
            int index2 = (x2 % 2 == 0 && receiveDigit[i][1] == 0 || x2 % 2 != 0 && receiveDigit[i][1] == 1) ? 0 : 2;
            int index4 = (x4 % 2 == 0 && receiveDigit[i][3] == 0 || x4 % 2 != 0 && receiveDigit[i][3] == 1) ? 0 : 4;
            if (index1 + index2 + index4 > 0) {
                if (receiveDigit[i][index1 + index2 + index4 - 1] == 1) {
                    receiveDigit[i][index1 + index2 + index4 - 1] = 0;

                } else {
                    receiveDigit[i][index1 + index2 + index4 - 1] = 1;
                }
            }

        }
      //  System.out.print("\n[");
        for (int i = 0; i < receiveDigit.length; i++) {
            for (int j = 0; j < 8 ; j++) {
              //  System.out.print(receiveDigit[i][j]);
            }
           // System.out.print(", ");
        }
        int[][] clearIntBinary = new int[receiveDigit.length/2][8];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < receiveDigit.length; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 2 || j == 4 || j == 5 || j == 6) {
                    stringBuilder.append(receiveDigit[i][j]);
                }
            }
        }


        String[] decodeString = new String[stringBuilder.length() / 8];
        String dec = String.valueOf(stringBuilder);
        int index = 0;
        for (int i = 0; i < decodeString.length; i++) {
            decodeString[i] = "";
            for (int j = 0; j < 8; j++) {
                // if (index < stringBuilder.length()) {
                decodeString[i] = decodeString[i].concat(String.valueOf(dec.charAt(index)));
                index++;
                //}

            }
            // System.out.print(decodeString[i] + ", ");
        }
        //System.out.println(Arrays.toString(decodeString));
        File decodedTXT = new File("decoded.txt");
        //получение массива байт из нового кодирования

        byte[] two = new byte[decodeString.length];
        int[] two2 = new int[decodeString.length];
        for (int i = 0; i < two2.length; i++) {
            //two[i] =   Byte.parseByte(parity[i], 2);
            two2[i] = Integer.parseInt(decodeString[i],2); ////Перевод в десятичную систему кодированной строки
            two[i] = (byte) two2[i]; //приведение цифры к байтам
        }
        //запись в файл для передачи
        try {
            //FileWriter outNewStr = new FileWriter(encodedTxt, true);
            OutputStream out = new FileOutputStream(decodedTXT);
            out.write(two);
            //outNewStr.write(newOut2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



       /*
        */

    }

    static void encode() throws IOException {
        File sendFile = new File("send.txt");
        String encodeFile = "";
        try (Scanner inputFile = new Scanner(sendFile)) {
            while (inputFile.hasNextLine()) {
                  encodeFile = encodeFile.concat(inputFile.nextLine());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] byteSendFile = encodeFile.getBytes();
        String[] binaryStringSend = new String[byteSendFile.length];
        String[] hexStringSend = new String[byteSendFile.length];
        for (int i = 0; i < byteSendFile.length; i++) {
            binaryStringSend[i] = Integer.toBinaryString(byteSendFile[i]);
            hexStringSend[i] = Integer.toHexString(byteSendFile[i]);
            if (binaryStringSend[i].length() == 7) {
                binaryStringSend[i] = "0".concat(binaryStringSend[i]);
            } else if (binaryStringSend[i].length() == 6) {
                binaryStringSend[i] = "00".concat(binaryStringSend[i]);
            }
        }
        //готовим строку для символьного считывания
        StringBuilder forExpand = new StringBuilder();
        for (String val: binaryStringSend) {
            forExpand.append(val);
        }
        int charIndex = 0;

        int lengthExpand = 0;
        if (forExpand.length() % 3 != 0) {
            lengthExpand = 1 + forExpand.length() / 3;
        } else {
            lengthExpand = forExpand.length() / 3;
        }
        String[] expand = new String[lengthExpand];
        String[] parity = new String[expand.length];
        for (int i = 0; i < expand.length; i++) {
            expand[i] = "";
            // запись кодированного массива
            for (int j = 0; j < 4; j++) {
                if (charIndex < forExpand.length()) {
                    if (j < 3) {
                        expand[i] = expand[i].concat(String.valueOf(forExpand.charAt(charIndex)) + String.valueOf(forExpand.charAt(charIndex)));
                        charIndex++;
                        //System.out.print(expand[i] + " ");
                        parity[i] = expand[i];

                    } else {
                        expand[i] = expand[i].concat("..");
                        //System.out.print(expand[i] + " ");
                        if ("111100..".equals(expand[i]) || "110011..".equals(expand[i]) || "001111..".equals(expand[i])
                                || "000000..".equals(expand[i])) {
                            parity[i] = parity[i].concat("00");
                        } else {
                            parity[i] = parity[i].concat("11");
                        }
                    }
                } else {
                    if (j < 3) {
                        expand[i] = expand[i].concat("..");
                    } else {
                        expand[i] = expand[i].concat("..");
                        if ("111100..".equals(expand[i]) || "110011..".equals(expand[i]) || "001111..".equals(expand[i])
                            || "000000..".equals(expand[i])) {
                            parity[i] = parity[i].concat("00");
                        } else if ("0011....".equals(expand[i])  ) {
                            parity[i] = parity[i].concat("0011");
                        } else if ("000011..".equals(expand[i])) {
                            parity[i] = parity[i].concat("11");
                        }
                    }
                }
            }//System.out.println();// encode
        }
        //int decNumber = Integer.parseInt(String.valueOf(sourceNumber), sourceRadix);///Перевод в десятичную систему
        //String targetNumber = Integer.toString(decNumber, targetRadix); ///Перевод в любую систему из десятичной
        int[] hexView = new int[expand.length];
        String[] hexOut = new String[hexView.length];

        for (int i = 0; i < hexView.length; i++) {
            hexView[i] = Integer.parseInt(parity[i],2);//Перевод в десятичную систему
            hexOut[i] = Integer.toHexString(hexView[i]).toUpperCase();//Перевод в шестнадцатеричную систему исчисления

        }

        File encodedTxt = new File("encoded.txt");
        //получение массива байт из нового кодирования

        byte[] two = new byte[parity.length];
        int[] two2 = new int[parity.length];
        for (int i = 0; i < two2.length; i++) {
            //two[i] =   Byte.parseByte(parity[i], 2);
            two2[i] = Integer.parseInt(parity[i],2); ////Перевод в десятичную систему кодированной строки
            two[i] = (byte) two2[i]; //приведение цифры к байтам
        }
        //запись в файл для передачи
        try {
            //FileWriter outNewStr = new FileWriter(encodedTxt, true);
            OutputStream out = new FileOutputStream(encodedTxt);
            out.write(two);
            //outNewStr.write(newOut2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(Arrays.toString(expand));
        System.out.println(Arrays.toString(parity) + "\n" + parity.length);

        decode();
        send();

    }
    static void printStringArray(String[] array) {
        for (String val: array) {
            System.out.print(val + " ");
        }
        System.out.println();
    }
    static void send() throws IOException {


        try {
            InputStream encode = new FileInputStream("encoded.txt");

            byte[] inputEncode = encode.readAllBytes();
            //System.out.println(Arrays.toString(inputEncode));
            String[] clearEncode = new String[inputEncode.length];
            for (int i = 0; i < clearEncode.length; i++) {
                //clearEncode[i] = String.format("%8s", Integer.toBinaryString(inputEncode[i]).replace(' ', '0'));
                clearEncode[i] = Integer.toBinaryString(inputEncode[i]);
                if (clearEncode[i].length() < 8) {
                    while (clearEncode[i].length() < 8) {
                        clearEncode[i] = "0".concat(clearEncode[i]);
                    }

                    //clearEncode[i] = String.format("%8s", clearEncode[i].replaceAll("\\s+", "0*"));
                } else {
                    clearEncode[i] = clearEncode[i].substring(clearEncode[i].length() - 8);
                }
            }
            //System.out.println(Arrays.toString(clearEncode));
            //printStringArray(clearEncode);
            //clearEncode[0].charAt(1) = 2;

            Random random = new Random();
            String[] errorCode = new String[clearEncode.length];
            for (int i = 0; i < errorCode.length; i++) {
                int x = random.nextInt(8);
                if (clearEncode[i].charAt(x) == '1') {
                    errorCode[i] = clearEncode[i].substring(0, x) + '0' + clearEncode[i].substring(x + 1);
                } else {
                    errorCode[i] = clearEncode[i].substring(0, x) + '1' + clearEncode[i].substring(x + 1);
                }

            }
            //System.out.println(Arrays.toString(errorCode));
            byte[] two = new byte[errorCode.length];
            int[] two2 = new int[errorCode.length];
            for (int i = 0; i < two2.length; i++) {
                //two[i] =   Byte.parseByte(parity[i], 2);
                two2[i] = Integer.parseInt(errorCode[i],2); ////Перевод в десятичную систему кодированной строки
                two[i] = (byte) two2[i]; //приведение цифры к байтам
            }
           // System.out.println(Arrays.toString(two));
            try {
                //FileWriter outNewStr = new FileWriter(encodedTxt, true);
                File received = new File("received.txt");
                OutputStream out = new FileOutputStream(received);
                out.write(two);
                //outNewStr.write(newOut2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }





    }
    static void decode() throws IOException {
        InputStream received = new FileInputStream("received.txt");
        byte[] receivBytes = received.readAllBytes();
       // System.out.println(Arrays.toString(receivBytes));
        String[] receivString = new String[receivBytes.length];
        for (int i = 0; i < receivString.length; i++) {
            receivString[i] = Integer.toBinaryString(receivBytes[i]);
            if (receivString[i].length() < 8) {
                while (receivString[i].length() < 8) {
                    receivString[i] = "0".concat(receivString[i]);
                }
            } else {
                receivString[i] = receivString[i].substring(receivString[i].length() - 8);
            }
        }

        String[] clearReceive = new String[receivString.length + 3];
        int lengthRecStr = 0;
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();
        for (int i = 0; i < receivString.length ; i++) {
            for (int j = 0; j < 6; j += 2) {
             if (receivString[i].charAt(j) == receivString[i].charAt(j + 1)) {
                    stringBuilder.append(receivString[i].charAt(j));
                    stringBuilder1.append(receivString[i].charAt(j));

                } else /* if (receivString[i].charAt(j) == '0' )*/ {
                    stringBuilder.append(errorDecode(receivString[i]));
                    stringBuilder1.append('E');

                }


            }

        }
       // printSend();
       // System.out.print("    ");
       /* System.out.println("Error   " + stringBuilder1);

        System.out.println("Clean   " + stringBuilder);

        System.out.println("receive" + Arrays.toString(receivString));*/
        String[] decodeString = new String[stringBuilder.length() / 8];
        String dec = String.valueOf(stringBuilder);
        int index = 0;
        for (int i = 0; i < decodeString.length; i++) {
            decodeString[i] = "";
            for (int j = 0; j < 8; j++) {
               // if (index < stringBuilder.length()) {
                    decodeString[i] = decodeString[i].concat(String.valueOf(dec.charAt(index)));
                    index++;
                //}

            }
           // System.out.print(decodeString[i] + ", ");
        }
        //System.out.println(Arrays.toString(decodeString));
        File decodedTXT = new File("decoded.txt");
        //получение массива байт из нового кодирования

        byte[] two = new byte[decodeString.length];
        int[] two2 = new int[decodeString.length];
        for (int i = 0; i < two2.length; i++) {
            //two[i] =   Byte.parseByte(parity[i], 2);
            two2[i] = Integer.parseInt(decodeString[i],2); ////Перевод в десятичную систему кодированной строки
            two[i] = (byte) two2[i]; //приведение цифры к байтам
        }
        //запись в файл для передачи
        try {
            //FileWriter outNewStr = new FileWriter(encodedTxt, true);
            OutputStream out = new FileOutputStream(decodedTXT);
            out.write(two);
            //outNewStr.write(newOut2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }
    static char errorDecode(String str) {
        int[] decode = new int[str.length()];
        int sum = 0;
        for (int i = 0; i < decode.length; i++) {
            decode[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
            if (i < 6) {
                sum += decode[i];
            }

        }

        if ((sum == 5 || sum == 1) && str.charAt(7) == '0') {
            return '0';
        } else if ((sum == 5 || sum == 1) && str.charAt(7) == '1') {
            return '1';
        } else if (sum == 3 && str.charAt(7) == '1') {
            return '0';
        } else {
            return '1';
        }



    }
    static void errorEncoder() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        //char[] newWord = line.toCharArray();
        //char[] newWord3 = new char[newWord.length * 3];
        StringBuilder newWord = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            newWord = newWord.append(line.substring(i, i + 1) + line.substring(i, i + 1) + line.substring(i, i + 1));
        }
        System.out.println(line);
        System.out.println(newWord);
        Random random = new Random();
        String word = String.valueOf(newWord);
        char[] sentenses = word.toCharArray();
        for (int j = 0; j < sentenses.length; j += 3) {
            int a = random.nextInt(3) + j;
            char b = (char) (random.nextInt(122 - 97) + 97);

            if (a < sentenses.length) {
                sentenses[a] = b;
            }
        }
        for (char val : sentenses) {
            System.out.print(val);
        }


        System.out.println("\n" + line);
    }
    static void hex() {
        File sampleFile = new File("send.txt");
        byte[] content = new byte[] {'J', 'a', 'v', 'a'};
        String hexFile = "Simple text";
        

        try {
            OutputStream outputStream = new FileOutputStream(sampleFile, true);
            outputStream.write(content);
            outputStream.close();
            System.out.println(sampleFile);
            System.out.println(outputStream.toString());
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }
    static void old1_5() {
        Scanner scanner = new Scanner(System.in);
        char[] sentenses = scanner.nextLine().toCharArray();

        Random random = new Random();

        for (int j = 0; j < sentenses.length; j+= 3) {
            int a =  random.nextInt(3) + j;
            char b = (char) (random.nextInt(122 - 97) + 97);

            if (a < sentenses.length) {
                sentenses[a] = b;
            }

        }
        for (char val : sentenses) {
            System.out.print(val);
        }
        System.out.print(" ");

    }
    static void stage3() throws IOException {
        File bitLevelFile = new File("send.txt");
        StringBuilder receiv = new StringBuilder();
        try (Scanner input = new Scanner(bitLevelFile)) {
            while (input.hasNext()) {
                //System.out.print("\n" + input.nextLine() + " ");
                receiv.append(input.nextLine());

            }
            String receiv2 = String.valueOf(receiv);
            byte[] rec = receiv2.getBytes();
            StringBuilder newrec = new StringBuilder();
            StringBuilder newrec2  = new StringBuilder();
            String[] binary = new String[rec.length];
            String[] binary2 = new String[rec.length];
            String[] hex = new String[rec.length];


            for (byte val: rec) {
                newrec.append(Integer.toHexString(val));
                newrec2.append(Integer.toBinaryString(val));

            }
            for (int i = 0; i < rec.length; i++) {
                binary[i] = Integer.toBinaryString(rec[i]);
                hex[i] = Integer.toHexString(rec[i]);

            }
            String errorNew = "0123456789abcdef";
            Random random = new Random();

            String[] hex2 = new String[hex.length];

            for (int i = 0; i < hex.length; i++) {
                hex2[i] = hex[i].substring(0,1) + errorNew.charAt(random.nextInt(errorNew.length()));
                int rnd = random.nextInt(binary[i].length());
                for (int j = 0; j < binary[i].length(); j++) {

                    if (rnd == j) {
                        if (binary[i].charAt(j) == '0') {
                            binary2[i] += String.valueOf('1');
                        } else {
                            binary2[i] += String.valueOf('0');
                        }
                    } else {
                        binary2[i] += String.valueOf(binary[i].charAt(j));
                    }

                }
                binary2[i] = binary2[i].replaceFirst("null", "");

            }
            //System.out.println(Arrays.toString(hex2));

            byte[] outBinaryByte = new byte[binary.length];
            byte[] outHexByte = new byte[hex.length];

            for (int i = 0; i < binary.length; i++) {
                outBinaryByte[i] = Byte.parseByte(binary2[i],2);
                outHexByte[i] = Byte.parseByte(hex2[i], 16);
            }


            // System.out.println(receiv);
            //System.out.println(newrec);
            //System.out.println(newrec2);
            //

            // System.out.println(Arrays.toString(hex));
            //     System.out.println(Arrays.toString(hex2));
            //System.out.println(Arrays.toString(outBinaryByte));
            //       System.out.println(Arrays.toString(rec));

            //        System.out.println(Arrays.toString(outHexByte));
                     System.out.println(Arrays.toString(binary));
            System.out.println(Arrays.toString(hex));

            String testBinary = new String(outBinaryByte);
            String testHex = new String(outHexByte);

            String[] outHexToBinary = new String[outHexByte.length];

            for (int i = 0; i < outHexToBinary.length; i++) {
                outHexToBinary[i] = Integer.toBinaryString(outHexByte[i]);
            }

            File outlFile = new File("received.txt");
            OutputStream run = new FileOutputStream(outlFile);
            run.write(outBinaryByte);


        } catch (FileNotFoundException e) {
            System.out.println("No file found: ");
        }
    }
    static void printSend() {
        File sendFile = new File("send.txt");
        String encodeFile = "";
        try (Scanner inputFile = new Scanner(sendFile)) {
            while (inputFile.hasNextLine()) {
                encodeFile = encodeFile.concat(inputFile.nextLine());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] byteSendFile = encodeFile.getBytes();
        String[] binaryStringSend = new String[byteSendFile.length];
        String[] hexStringSend = new String[byteSendFile.length];
        for (int i = 0; i < byteSendFile.length; i++) {
            binaryStringSend[i] = Integer.toBinaryString(byteSendFile[i]);
            hexStringSend[i] = Integer.toHexString(byteSendFile[i]);
            if (binaryStringSend[i].length() == 7) {
                binaryStringSend[i] = "0".concat(binaryStringSend[i]);
            } else if (binaryStringSend[i].length() == 6) {
                binaryStringSend[i] = "00".concat(binaryStringSend[i]);
            }
        }
        System.out.println("Send" + Arrays.toString(binaryStringSend));
        StringBuilder forExpand = new StringBuilder();
        for (String val: binaryStringSend) {
            forExpand.append(val);
        }
        //System.out.println("Send    " + forExpand + " " + forExpand.length()) ;
    }
}
    //Very important text
//        Scanner scanner = new Scanner(System.in);
//        String line = scanner.nextLine();
//
//        char[] charLine = line.toCharArray();
//        FileWriter fileWriter = new FileWriter(bitLevelFile);
//        fileWriter.write(line);
//
//        byte[] near = line.getBytes();
//        run.write(near);
//        //System.out.println(Arrays.toString(near));
/////////////////\\\\\\\\\\
/*          String s = "Hello World!";
System.out.println("Source: " + s);
byte bytes[] = s.getBytes(StandardCharsets.UTF_8);
StringBuilder sb = new StringBuilder();
for (byte b : bytes) sb.append(String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0'));
String tmp = sb.toString();
System.out.println("Bites: " + tmp);
String str[] =  tmp.split("(?<=\\G.{8})");
byte out[] = new byte[str.length];
for (int i=0; i<str.length; i++) out[i] = Byte.parseByte(str[i], 2);
String test = new String(out, StandardCharsets.UTF_8);
System.out.println("Decoded: " + test);
System.out.println("Equals: " + s.equals(test));

 */// System.out.println(Arrays.toString(rec));
  /*          Random random = new Random();
            String errorHex = String.valueOf(newrec);
            char[] errorHexChar = errorHex.toCharArray();
            String[] outHex = new String[errorHexChar.length];
            for (int j = 0; j < newrec.length(); j+=2) {


                    int a = random.nextInt(2) + j;
                    char b = (char) (random.nextInt(102 - 98) + 98);



                if (a < newrec.length()) {
                    errorHexChar[a] = b;
                }


            }
            for (int i = 0; i < newrec.length(); i++) {
                for (int j = 0; j < 2; j++) {
                    outHex[i] = String.valueOf(errorHexChar[j]) + String.valueOf(errorHexChar[j+1]);
                }

            }

            System.out.println(Arrays.toString(outHex));
            byte out[] = new byte[outHex.length];
            for (int i=0; i< outHex.length; i++){
                out[i] = Byte.parseByte(outHex[i],16);
            }
            String test = new String(out,StandardCharsets.UTF_8);
            System.out.println(test);
            System.out.println(Arrays.toString(errorHexChar));

           /* byte out[] = new byte[errorHexChar.length];
            for (int i = 0; i <errorHexChar.length; i++) {
                out[i] = Byte.parseByte(String.valueOf(errorHexChar[i]), 16);
            }
            String test = new String(out, StandardCharsets.UTF_8);*/
//System.out.println(Arrays.toString(out));
// System.out.println(test);