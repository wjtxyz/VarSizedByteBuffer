package com.ainirobot.bitmapmatrixtest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class VarSizedByteBuffer {
    private ByteBuffer byteBuffer;

    public VarSizedByteBuffer(int initialCapacity) {
        this(ByteBuffer.allocate(initialCapacity).order(ByteOrder.nativeOrder()));
    }

    public VarSizedByteBuffer(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    ByteBuffer ensureCapacity(int capacity) {
        if (capacity <= byteBuffer.capacity()) {
            return byteBuffer;
        } else {
            int realCapacity = byteBuffer.capacity();
            while ((realCapacity = 2 * realCapacity + 1) < capacity) ;
            return byteBuffer = ByteBuffer.allocate(realCapacity).order(ByteOrder.nativeOrder()).put((ByteBuffer) byteBuffer.flip());
        }
    }

    public int getInt() {
        return byteBuffer.getInt();
    }

    public short getShort() {
        return byteBuffer.getShort();
    }

    public char getChar() {
        return byteBuffer.getChar();
    }

    public float getFloat() {
        return byteBuffer.getFloat();
    }

    public double getDouble() {
        return byteBuffer.getDouble();
    }

    public VarSizedByteBuffer writeInt(int value) {
        ensureCapacity(byteBuffer.position() + 4).putInt(value);
        return this;
    }

    public VarSizedByteBuffer writeShort(short value) {
        ensureCapacity(byteBuffer.position() + 2).putShort(value);
        return this;
    }

    public VarSizedByteBuffer writeDouble(double value) {
        ensureCapacity(byteBuffer.position() + 8).putDouble(value);
        return this;
    }

    public VarSizedByteBuffer writeFloat(float value) {
        ensureCapacity(byteBuffer.position() + 4).putFloat(value);
        return this;
    }

    public VarSizedByteBuffer writeChar(char value) {
        ensureCapacity(byteBuffer.position() + 2).putChar(value);
        return this;
    }

    private VarSizedByteBuffer put(ByteBuffer s) {
        ensureCapacity(byteBuffer.position() + s.remaining()).put(s);
        return this;
    }

    private VarSizedByteBuffer put(byte[] s) {
        ensureCapacity(byteBuffer.position() + s.length).put(s);
        return this;
    }

    private VarSizedByteBuffer put(byte s) {
        ensureCapacity(byteBuffer.position() + 1).put(s);
        return this;
    }

    public VarSizedByteBuffer get(ByteBuffer s) {
        final int oldLimit = byteBuffer.limit();
        try {
            s.put((ByteBuffer) byteBuffer.limit(Math.min(s.remaining(), byteBuffer.remaining())));
        } finally {
            byteBuffer.limit(oldLimit);
        }
        return this;
    }


    public VarSizedByteBuffer write(CharSequence s) {
        if (s != null) {
            final ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(s));
            writeInt(byteBuffer.remaining());
            put(byteBuffer);
        } else {
            writeInt(-1);
        }
        return this;
    }

    public CharSequence readCharSequence() {
        final int len = byteBuffer.getInt();
        if (len >= 0) {
            int oldLimit = byteBuffer.limit();
            try {
                return StandardCharsets.UTF_8.decode((ByteBuffer) byteBuffer.limit(byteBuffer.position() + len));
            } finally {
                byteBuffer.limit(oldLimit);
            }
        } else {
            return null;
        }
    }

    public VarSizedByteBuffer write(byte[] s) {
        if (s != null) {
            writeInt(s.length);
            put(s);
        } else {
            writeInt(-1);
        }
        return this;
    }

    public byte[] readByteArray() {
        final int len = byteBuffer.getInt();
        if (len >= 0) {
            final byte[] bytes = new byte[len];
            byteBuffer.get(bytes);
            return bytes;
        } else {
            return null;
        }
    }

    public VarSizedByteBuffer write(short[] s) {
        if (s != null) {
            writeInt(s.length);
            for (short value : s) {
                writeShort(value);
            }
        } else {
            writeInt(-1);
        }
        return this;
    }

    public short[] readShortArray() {
        final int len = byteBuffer.getInt();
        if (len >= 0) {
            final short[] shorts = new short[len];
            for (int i = 0; i < len; i++) {
                shorts[i] = byteBuffer.getShort();
            }
            return shorts;
        } else {
            return null;
        }
    }

    public VarSizedByteBuffer write(int[] s) {
        if (s != null) {
            writeInt(s.length);
            for (int value : s) {
                writeInt(value);
            }
        } else {
            writeInt(-1);
        }
        return this;
    }

    public int[] readIntArray() {
        final int len = byteBuffer.getInt();
        if (len >= 0) {
            final int[] ints = new int[len];
            for (int i = 0; i < len; i++) {
                ints[i] = byteBuffer.getInt();
            }
            return ints;
        } else {
            return null;
        }
    }

    public VarSizedByteBuffer write(double[] s) {
        if (s != null) {
            writeInt(s.length);
            for (double value : s) {
                writeDouble(value);
            }
        } else {
            writeInt(-1);
        }
        return this;
    }

    public double[] readDoubleArray() {
        final int len = byteBuffer.getInt();
        if (len >= 0) {
            final double[] doubles = new double[len];
            for (int i = 0; i < len; i++) {
                doubles[i] = byteBuffer.getDouble();
            }
            return doubles;
        } else {
            return null;
        }
    }

    public VarSizedByteBuffer write(char[] s) {
        if (s != null) {
            writeInt(s.length);
            for (char value : s) {
                writeChar(value);
            }
        } else {
            writeInt(-1);
        }
        return this;
    }

    public char[] readCharArray() {
        final int len = byteBuffer.getInt();
        if (len >= 0) {
            char[] chars = new char[len];
            for (int i = 0; i < len; i++) {
                chars[i] = byteBuffer.getChar();
            }
            return chars;
        } else {
            return null;
        }
    }

    public VarSizedByteBuffer write(CharSequence[] s) {
        if (s != null) {
            writeInt(s.length);
            for (CharSequence value : s) {
                write(value);
            }
        } else {
            writeInt(-1);
        }
        return this;
    }

    public CharSequence[] readCharSequenceArray() {
        final int len = byteBuffer.getInt();
        if (len >= 0) {
            final CharSequence[] strings = new CharSequence[len];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = readCharSequence();
            }
            return strings;
        } else {
            return null;
        }
    }

    public VarSizedByteBuffer write(byte[][] s) {
        if (s != null) {
            writeInt(s.length);
            for (byte[] value : s) {
                write(s);
            }
        } else {
            writeInt(-1);
        }
        return this;
    }

    public byte[][] readByteArrayArray() {
        final int len = byteBuffer.getInt();
        if (len >= 0) {
            final byte[][] bytes = new byte[len][];
            for (int i = 0; i < len; i++) {
                bytes[i] = readByteArray();
            }
            return bytes;
        } else {
            return null;
        }
    }

    public ByteBuffer toByteBuffer() {
        return (ByteBuffer) byteBuffer.flip();
    }


    public VarSizedByteBuffer writeMap(Map<CharSequence, CharSequence> map) {
        if (map != null) {
            Set<Map.Entry<CharSequence, CharSequence>> entries = map.entrySet();
            writeInt(entries.size());
            for (Map.Entry<CharSequence, CharSequence> entry : entries) {
                write(entry.getKey());
                write(entry.getValue());
            }
        } else {
            writeInt(-1);
        }
        return this;
    }

    public Map<CharSequence, CharSequence> readMap() {
        final int len = byteBuffer.getInt();
        if (len >= 0) {
            HashMap<CharSequence, CharSequence> map = new HashMap<>();
            for (int i = 0; i < len; i++) {
                map.put(readCharSequence(), readCharSequence());
            }
            return map;
        } else {
            return null;
        }
    }
}
