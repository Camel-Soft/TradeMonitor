package com.camelsoft.trademonitor._presentation.utils.scan;

import com.google.zxing.FormatException;

public class EanUpcCheckDigit
{
    private String checkDigit = "?";

    public EanUpcCheckDigit (final String shortScan) throws FormatException {
        // EAN13
        if (shortScan.length() == 12) { checkDigit = calcCheckDigit(shortScan); }
        // UPC-A
        if (shortScan.length() == 11) { checkDigit = calcCheckDigit(shortScan); }
        // EAN8 or UPC-E
        if (shortScan.length() == 7)
        {
            // UPC-E
            if (shortScan.substring(0,1).equals("0") || shortScan.substring(0,1).equals("1"))
            { checkDigit = calcCheckDigit(UpcE_to_UpcA(shortScan)); }
            else
                // EAN8
                if (!shortScan.substring(0,1).equals("0") && !shortScan.substring(0,1).equals("1"))
                { checkDigit = calcCheckDigit(shortScan); }
        }
    }

    // Вычисление контрольной суммы у сканкодов EAN13, EAN8, UPC-A (UPC-E сначала надо привести к виду UPC-A)
    private String calcCheckDigit (CharSequence s) throws FormatException
    {
        int length = s.length();
        int sum = 0;
        for (int i = length - 1; i >= 0; i -= 2) {
            int digit = s.charAt(i) - '0';
            if (digit < 0 || digit > 9) {
                throw FormatException.getFormatInstance();
            }
            sum += digit;
        }
        sum *= 3;
        for (int i = length - 2; i >= 0; i -= 2) {
            int digit = s.charAt(i) - '0';
            if (digit < 0 || digit > 9) {
                throw FormatException.getFormatInstance();
            }
            sum += digit;
        }
        return Integer.toString((1000 - sum) % 10);
    }

    // Конвертирует Upc-E в Upc-A
    private String UpcE_to_UpcA (String data)
    {
        switch (data.charAt(6))
        {
            case '0':
            case '1':
            case '2': {
                data = data.substring(0, 3) + "0000" + data.charAt(6) + data.substring(3, 6);
                break;
            }
            case '3': {
                data = data.substring(0, 4) + "00000" + data.substring(4, 6);
                break;
            }
            case '4': {
                data = data.substring(0, 5) + "00000" + data.charAt(5);
                break;
            }
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                data = data.substring(0, 6) + "0000" + data.charAt(6);
                break;
            }
        }
        return data;
    }

    public String getCheckDigit ()
    {
        return checkDigit;
    }
}