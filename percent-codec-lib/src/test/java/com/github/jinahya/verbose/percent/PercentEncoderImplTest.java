/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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
package com.github.jinahya.verbose.percent;

import com.buck.common.codec.Codec;
import com.buck.common.codec.CodecDecoder;
import static com.github.jinahya.verbose.percent.UrlCodecConverter.EXAMPLE_DECODED;
import static com.github.jinahya.verbose.percent.UrlCodecConverter.EXAMPLE_ENCODED;
import static com.github.jinahya.verbose.percent.UrlCodecConverter.toPercentEncoded;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.copyOf;
import static java.util.concurrent.ThreadLocalRandom.current;
import javax.inject.Inject;
import static org.apache.commons.lang3.RandomStringUtils.random;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Guice(modules = PercentEncoderImplModule.class)
public class PercentEncoderImplTest {

    @Test
    public void testExample() throws UnsupportedEncodingException {
        final String expected = toPercentEncoded(EXAMPLE_ENCODED);
        final String actual = encoder.encode(EXAMPLE_DECODED);
        assertEquals(actual, expected);
    }

    @Test(invocationCount = 128)
    public void testEncodingAgainstURLEncoder()
            throws UnsupportedEncodingException {
        final Charset charset = UTF_8;
        final String decoded = random(current().nextInt(128));
        final String expected = toPercentEncoded(
                URLEncoder.encode(decoded, charset.name()));
        final String actual = encoder.encode(decoded, charset);
        assertEquals(actual, expected);
    }

    @Test(invocationCount = 1)
    public void encodeVerboseDecodeRbuck() {
        final byte[] created = new byte[current().nextInt(128)];
        current().nextBytes(created);
        final ByteBuffer encoded = allocate(created.length * 3);
        final int count = encoder.encode(wrap(created), encoded);
        final Codec codec = Codec.forName("percent-encoded");
        final CodecDecoder decoder = codec.newDecoder();
        final byte[] decoded = decoder.decode(
                copyOf(encoded.array(), encoded.position()));
        assertEquals(decoded, created);
    }

    private transient final Logger logger = getLogger(getClass());

    @Inject
    private PercentEncoder encoder;
}
