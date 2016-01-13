package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * And interface for hex encoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@FunctionalInterface
public interface HexEncoder {

    /**
     * Encodes given octet and puts those encoded hex characters to specified
     * byte buffer.
     *
     * @param decoded the octet to encode
     * @param encoded the byte buffer to which encoded characters are put.
     */
    void encodeOctet(int decoded, ByteBuffer encoded);

    /**
     * Encodes all remaining bytes from given input byte buffer and puts results
     * to specified output byte buffer.
     *
     * @param decoded the input byte buffer
     * @param encoded the output byte buffer
     */
    default void encode(final ByteBuffer decoded, final ByteBuffer encoded) {
        while (decoded.hasRemaining()) {
            encodeOctet(decoded.get(), encoded);
        }
    }

    /**
     * Encodes all remaining bytes from given byte buffer and returns a byte
     * buffer containing the result.
     *
     * @param decoded the byte buffer containing bytes to encode
     * @return a byte buffer containing encoded bytes.
     */
    default ByteBuffer encode(final ByteBuffer decoded) {
        final ByteBuffer encoded = ByteBuffer.allocate(decoded.remaining() * 2);
        encode(decoded, encoded);
        encoded.flip();
        return encoded;
    }

    /**
     * Encodes given string using specified charset to get a byte array from the
     * string.
     *
     * @param decoded the string to encoding
     * @param charset the charset to get a byte array from the string.
     * @return encoded string.
     */
    default String encode(final String decoded, final Charset charset) {
        final byte[] decodedBytes = decoded.getBytes(charset);
        final byte[] encodedBytes = new byte[decodedBytes.length << 1];
        encode(ByteBuffer.wrap(decodedBytes), ByteBuffer.wrap(encodedBytes));
        return new String(encodedBytes, US_ASCII);
    }

    default String encode(final String decoded) {
        return encode(decoded, UTF_8);
    }
}
