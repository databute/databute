/*
 * MIT License
 *
 * Copyright (c) 2019 - 2021 databute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package databute.network.message;

public enum MessageCode {

    REGISTER(0),
    CLUSTER_NODE_NOTIFICATION(1),
    BUCKET_NOTIFICATION(2),
    GET_ENTRY(3),
    SET_ENTRY(4),
    UPDATE_ENTRY(5),
    DELETE_ENTRY(6),
    ENTRY_OPERATION_SUCCESS(7),
    ENTRY_OPERATION_FAIL(8),
    EXPIRE_ENTRY(9),
    HANDSHAKE_REQUEST(100),
    HANDSHAKE_RESPONSE(101);

    public static MessageCode of(int value) {
        for (MessageCode messageCode : values()) {
            if (messageCode.value() == value) {
                return messageCode;
            }
        }
        throw new IllegalArgumentException("not found: " + value);
    }

    private final int value;

    MessageCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
