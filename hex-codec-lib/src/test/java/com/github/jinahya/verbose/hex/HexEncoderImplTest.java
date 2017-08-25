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
package com.github.jinahya.verbose.hex;

import com.google.common.io.BaseEncoding;
import static java.lang.invoke.MethodHandles.lookup;
import static java.nio.ByteBuffer.wrap;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * A class testing {@link HexEncoderImpl}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexEncoderImplTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    /**
     * Tests encoding using {@code RFC4648} test vectors.
     */
    @Test
    public void encodeVerboseCompareRfc4648() {
        Rfc4648TestVectors.base16((d, e) -> assertEquals( // <1>
                new HexEncoderImpl().encode(d, US_ASCII), e)
        );
    }

    /**
     * Test encoding by comparing the result to the result of
     * {@link Hex#encode(byte[])}.
     */
    @Test(invocationCount = 128)
    public void encodeVerboseEncodeCommons() {
        final byte[] created; // <1>
        {
            created = new byte[current().nextInt(1024)];
            current().nextBytes(created);
        }
        final byte[] encoded1; // <2>
        {
            encoded1 = new String(new Hex().encode(created))
                    .toUpperCase().getBytes(US_ASCII);
        }
        final byte[] encoded2; // <3>
        {
            encoded2 = new byte[created.length << 1];
            new HexEncoderImpl().encode(wrap(created), wrap(encoded2));
        }
        assertEquals(encoded2, encoded1); // <4>
    }

    /**
     * Cross-checks {@link HexEncoderImpl} against {@link Hex}.
     *
     * @throws DecoderException if a decoding exception occurs
     */
    @Test(invocationCount = 128)
    public void encodeVerboseDecodeCommons() throws DecoderException {
        final byte[] created; // <1>
        {
            created = new byte[current().nextInt(1024)];
            current().nextBytes(created);
        }
        final byte[] encoded; // <2>
        {
            encoded = new byte[created.length << 1];
            new HexEncoderImpl().encode(wrap(created), wrap(encoded));
        }
        final byte[] decoded; // <3>
        {
            decoded = new Hex().decode(encoded);

        }
        assertEquals(decoded, created); // <4>
    }

    /**
     * Test encoding by comparing the result to the result of
     * {@link BaseEncoding#encode(byte[])}.
     */
    @Test(invocationCount = 128)
    public void encodeVerboseEncodeGuava() {
        final byte[] created; // <1>
        {
            created = new byte[current().nextInt(1024)];
            current().nextBytes(created);
        }
        final byte[] encoded1; // <2>
        {
            encoded1 = BaseEncoding.base16().encode(created).getBytes();
        }
        final byte[] encoded2; // <3>
        {
            encoded2 = new byte[created.length << 1];
            new HexEncoderImpl().encode(wrap(created), wrap(encoded2));
        }
        assertEquals(encoded2, encoded1); // <4>
    }

    /**
     * Cross-checks {@link HexEncoderImpl} against {@link BaseEncoding}.
     */
    @Test(invocationCount = 128)
    public void encodeVerboseDecodeGuava() {
        final byte[] created; // <1>
        {
            created = new byte[current().nextInt(1024)];
            current().nextBytes(created);
        }
        final byte[] encoded; // <2>
        {
            encoded = new byte[created.length << 1];
            new HexEncoderImpl().encode(wrap(created), wrap(encoded));
        }
        final byte[] decoded; // <3>
        {
            decoded = BaseEncoding.base16().decode(new String(encoded));
        }
        assertEquals(decoded, created); // <4>
    }
}
