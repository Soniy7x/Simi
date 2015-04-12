package io.simi.exception;

import java.io.IOException;

/**
 * -------------------------------
 * 		ByteNotFoundException
 * -------------------------------
 *
 * createTime: 2015-04-12
 * updateTime: 2015-04-12
 *
 */
public class ByteNotFoundException extends IOException {

    public ByteNotFoundException() {
        super();
    }

    public ByteNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public ByteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ByteNotFoundException(Throwable cause) {
        super(cause);
    }
}
