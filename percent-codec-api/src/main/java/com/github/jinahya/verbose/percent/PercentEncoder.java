package com.github.jinahya.verbose.percent;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * An interface for percent-encoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface PercentEncoder {

    /**
     * Encodes given octet and put encoded characters to specified byte buffer.
     *
     * @param decoded the octet to encode
     * @param encoded the byte buffer to which encoded characters are put.
     */
    void encodeOctet(int decoded, ByteBuffer encoded);

    /**
     * Encodes all remaining bytes from given input buffer and put result to
     * specified output buffer.
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
     * Encodes all remaining bytes of given input buffer and returns a byte
     * buffer containing the result.
     *
     * @param decoded the input byte buffer
     * @return a new byte buffer containing encoded bytes.
     */
    default ByteBuffer encode(final ByteBuffer decoded) {
        final ByteBuffer encoded = ByteBuffer.allocate(decoded.remaining() * 3);
        encode(decoded, encoded);
        encoded.flip();
        return encoded;
    }

    /**
     * Encodes given string using specified character set to obtain the bytes.
     *
     * @param decoded the string to encode
     * @param charset the character set to encode the input string to byte
     * array.
     *
     * @return encoded String
     */
    default String encode(final String decoded, final Charset charset) {
        final byte[] decodedBytes = decoded.getBytes(charset);
        System.out.println("decodedBytes.length: " + decodedBytes.length);
        final byte[] encodedBytes = new byte[decodedBytes.length * 3];
        System.out.println("encodedBytes.length: " + encodedBytes.length);
        final ByteBuffer decodedBuffer = ByteBuffer.wrap(decodedBytes);
        final ByteBuffer encodedBuffer = ByteBuffer.wrap(encodedBytes);
        encode(decodedBuffer, encodedBuffer);
        return new String(encodedBytes, 0, encodedBuffer.position(), US_ASCII);
    }

    default String encode(final String decoded) {
        return encode(decoded, UTF_8);
    }
}
