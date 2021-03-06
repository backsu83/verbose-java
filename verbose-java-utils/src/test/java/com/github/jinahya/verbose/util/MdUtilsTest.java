/*
 * Copyright 2016 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jinahya.verbose.util;

import static com.github.jinahya.verbose.util.MdUtils.digest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.Channels.newChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import static java.nio.file.Files.delete;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.WRITE;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * A class for testing {@link MdUtils}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MdUtilsTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    @DataProvider
    Object[][] algorithms() {
        return new Object[][]{{"MD5"}, {"SHA-1"}, {"SHA-256"}};
    }

    @Test(dataProvider = "algorithms", invocationCount = 128)
    public static void digestStream(final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        final byte[] bytes = new byte[current().nextInt(1024)];
        current().nextBytes(bytes);
        try (InputStream stream = new ByteArrayInputStream(bytes)) {
            final byte[] digest = digest(
                    algorithm, stream, new byte[current().nextInt(1, 128)]);
        }
    }

    @Test(dataProvider = "algorithms", invocationCount = 128)
    public static void digestFile(final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        final File file = File.createTempFile("tmp", null);
        try (OutputStream s = new FileOutputStream(file)) {
            final byte[] b = new byte[current().nextInt(1024)];
            current().nextBytes(b);
            s.write(b);
            s.flush();
        }
        final byte[] digest = digest(
                algorithm, file, new byte[current().nextInt(1, 128)]);
        file.delete();
    }

    @Test(dataProvider = "algorithms", invocationCount = 128)
    public static void digestChannel(final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        final byte[] bytes = new byte[current().nextInt(1024)];
        current().nextBytes(bytes);
        try (ReadableByteChannel channel
                = newChannel(new ByteArrayInputStream(bytes))) {
            final byte[] digest = digest(
                    algorithm, channel, allocate(current().nextInt(1, 128)));
        }
    }

    @Test(dataProvider = "algorithms", invocationCount = 128)
    public static void digestPath(final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        final Path path = Files.createTempFile(null, null);
        try (FileChannel c = FileChannel.open(path, WRITE)) { // <2>
            final ByteBuffer b = allocate(current().nextInt(1024));
            current().nextBytes(b.array());
            for (; b.hasRemaining(); c.write(b));
            c.force(false);
        }
        final byte[] digest = digest(
                algorithm, path, allocate(current().nextInt(1, 128)));
        delete(path);
    }
}
