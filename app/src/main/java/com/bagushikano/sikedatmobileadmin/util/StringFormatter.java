package com.bagushikano.sikedatmobileadmin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class StringFormatter {
    public static String sentenceCaseAndAddSpace(String string) {
        string = string.replace("-", " ");
        String[] split = string.split(" ");
        for (int i=0; i<split.length; i++) {

        }
        return string;
    }


    public static String formatNamaWithGelar(String nama, String gelarDepan, String gelarBelakang) {
        String namaFormated = nama;
        if (gelarDepan != null) {
            if (gelarDepan.length() != 0) {
                namaFormated = String.format("%s %s",
                        gelarDepan, nama
                );
            }
        }
        if (gelarBelakang != null) {
            if (gelarBelakang.length() != 0) {
                namaFormated = String.format("%s, %s",
                        namaFormated,
                        gelarBelakang
                );
            }
        }
        return namaFormated;
    }
}
