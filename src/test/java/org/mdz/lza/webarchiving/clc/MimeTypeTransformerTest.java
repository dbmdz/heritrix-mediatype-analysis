package org.mdz.lza.webarchiving.clc;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by MBITZL on 12.11.2015.
 */
public class MimeTypeTransformerTest {

    @Test
    public void shouldExtractMimetypeForFoundFile() {
        String source = "2014-03-17T15:22:16.679Z   200       " +
                "4851 http://ajax.googleapis.com/robots.txt EP " +
                "http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js text/plain #001 " +
                "20140317152216646+32 sha1:HC3C4E7J27T5MKC4XPZDNA6RV76FE2F3 - -";
        MimeTypeTransformer converter = new MimeTypeTransformer();
        assertThat(converter.transform(source).getMimeType(), is("text/plain"));
    }

    @Test
    public void shouldExtractMimetypeForFileNotFound() {
        String source = "2014-03-17T15:22:19.612Z   404        213 http://www.sadmoon.de/text/javascript EX http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js text/html #002 20140317152219586+26 sha1:OS2OPUB7C7MDGVSKUJAFMHKHY22G7NYZ - -";
        MimeTypeTransformer converter = new MimeTypeTransformer();
        assertThat(converter.transform(source).getMimeType(), is("text/html"));
    }

}