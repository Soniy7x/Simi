package io.simi.norm;

/**
 * -------------------------------
 * 		        LEVEL
 * -------------------------------
 *
 * createTime: 2015-04-12
 * updateTime: 2015-04-12
 *
 */
public enum LEVEL {

    CUSTOMER(0),
    VERBOSE(1),
    INFO(2),
    WARNING(3),
    ERROR(4);

    private int code;
    private LEVEL(int code) {
        this.code = code;
    }
}
