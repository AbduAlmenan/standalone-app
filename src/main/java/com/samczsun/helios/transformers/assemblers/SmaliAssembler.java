/*
 * Copyright 2015 Sam Sun <me@samczsun.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.samczsun.helios.transformers.assemblers;

import com.samczsun.helios.Settings;
import com.samczsun.helios.handler.ExceptionHandler;
import com.samczsun.helios.transformers.converters.Converter;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.nio.file.Files;

public class SmaliAssembler extends Assembler {

    SmaliAssembler() {
        super("smali-assembler", "Smali Assembler");
    }

    @Override
    public byte[] assemble(String name, String contents) {
        try {
            File tempDir = Files.createTempDirectory("smali").toFile();
            File tempSmaliFolder = new File(tempDir, "smalifolder");
            tempSmaliFolder.mkdir();

            File tempSmali = new File(tempDir, "temp.smali");
            File tempDex = new File(tempDir, "temp.dex");
            File tempJar = new File(tempDir, "temp.jar");
            File tempJarFolder = new File(tempDir, "temp-jar");

            FileUtils.write(tempSmali, contents, "UTF-8", false);
            try {
                org.jf.smali.main.main(
                        new String[]{tempSmaliFolder.getAbsolutePath(), "-o", tempDex.getAbsolutePath()});
            } catch (Exception e) {
                ExceptionHandler.handle(e);
            }


            if (Settings.APK_CONVERSION.get().asString().equals(Converter.ENJARIFY.getId())) {
                Converter.ENJARIFY.convert(tempDex, tempJar);
            } else if (Settings.APK_CONVERSION.get().asString().equals(Converter.DEX2JAR.getId())) {
                Converter.DEX2JAR.convert(tempDex, tempJar);
            }
            ZipUtil.unpack(tempJar, tempJarFolder);

            File outputClass = null;
            boolean found = false;
            File current = tempJarFolder;
            try {
                while (!found) {
                    File f = current.listFiles()[0];
                    if (f.isDirectory()) current = f;
                    else {
                        outputClass = f;
                        found = true;
                    }

                }

                return org.apache.commons.io.FileUtils.readFileToByteArray(outputClass);
            } catch (java.lang.NullPointerException e) {

            }
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }
        return null;
    }
}
