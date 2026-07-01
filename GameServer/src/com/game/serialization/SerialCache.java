/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.serialization;

import java.util.ArrayList;

/**
 * 
 * @author zhaibiao
 * @param <E> 
 */
public class SerialCache<E> extends ArrayList<E> {

    int index = 0;

    public final int getInt() {

        return Integer.parseInt((String) get(index++));
    }


    public final short getShort() {

        return Short.parseShort((String) get(index++));
    }


    public final byte getByte() {

        return Byte.parseByte((String) get(index++));
    }


    public final long getLong() {

        return Long.parseLong((String) get(index++));
    }


    public final float getFloat() {

        return Float.parseFloat((String) get(index++));
    }


    public final double getDouble() {

        return Double.parseDouble((String) get(index++));
    }


    public final String getString() {

        return (String) get(index++);
    }


    public final char getChar() {

        return (char) Integer.parseInt((String) get(index++));
    }


    public final boolean getBoolean() {

        return Boolean.parseBoolean((String) get(index++));
    }


//    public final Object getObject(Class clazz) {
//
//        String str = (String) get(index++);
//        if (Serialize.class.isAssignableFrom(clazz) && str != null) {
//            try {
//                Constructor<?>[] temp = clazz.getDeclaredConstructors();
//                for (int i = 0; i < temp.length; i++) {
//                    if (temp[i].getParameterTypes().length == 0) {
//                        temp[i].setAccessible(true);
//                        return ((Serialize) temp[i].newInstance()).deserial(str);
//                    }
//                }
//                throw new RuntimeException("need none params constructor...");
//                // return ((Serialize) clazz.newInstance()).deserial(str);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        return null;
//    }

//    public final byte[] getByteArray() {
//        String str = (String) get(index++);
//        if (str != null) {
//            try {
//                return Base64.decode(str);sh
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        return null;
//    }


    public final String skip() {

        return (String) get(index++);
    }


    public final boolean hasNext() {
        if (index < size()) {
            return true;
        }
        return false;
    }

    public final boolean isLastIndex() {
        if (index == size()) {
            return true;
        }
        return false;
    }


//    public final List getList(Class clazz) {
//
//        String str = (String) get(index++);
//        if (str != null) {
//            try {
//                List<String> elems = SerialTool.deserial(str);
//                List result = new ArrayList();
//                for (String s : elems) {
//                    if (s == null) {
//                        result.add(s);
//                    } else if (Integer.class.isAssignableFrom(clazz)) {
//                        result.add(Integer.valueOf(s));
//                    } else if (String.class.isAssignableFrom(clazz)) {
//                        result.add(s);
//                    } else if (Long.class.isAssignableFrom(clazz)) {
//                        result.add(Long.valueOf(s));
//                    } else if (Float.class.isAssignableFrom(clazz)) {
//                        result.add(Float.valueOf(s));
//                    } else if (Double.class.isAssignableFrom(clazz)) {
//                        result.add(Double.valueOf(s));
//                    } else if (Short.class.isAssignableFrom(clazz)) {
//                        result.add(Short.valueOf(s));
//                    } else if (Byte.class.isAssignableFrom(clazz)) {
//                        result.add(Byte.valueOf(s));
//                    } else if (Character.class.isAssignableFrom(clazz)) {
//                        result.add(Character.valueOf(s.charAt(0)));
//                    } else if (byte[].class.isAssignableFrom(clazz)) {
//                        result.add(Base64.decode(s));
//                    } else if (Serialize.class.isAssignableFrom(clazz)) {
//                        result.add(((Serialize) clazz.newInstance()).deserial(s));
//                    } else {
//                        throw new Exception("deserial to list not support " + clazz);
//                    }
//                }
//                return result;
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//        }
//        return null;
//    }


//    public final Object getArray(Class clazz) {
//        if (byte[].class.isAssignableFrom(clazz)) {// byte[]�
//            return getByteArray();
//        }
//        Object result = null;
//        String str = (String) get(index++);
//
//        if (str != null) {
//            try {
//                List<String> elems = SerialTool.deserial(str);
//                if (int[].class.isAssignableFrom(clazz)) {
//                    int[] tmp = new int[elems.size()];
//                    for (int i = 0, len = elems.size(); i < len; i++) {
//                        tmp[i] = Integer.valueOf(elems.get(i));
//                    }
//                    result = tmp;
//                } else if (String[].class.isAssignableFrom(clazz)) {
//                    String[] tmp = new String[elems.size()];
//                    for (int i = 0, len = elems.size(); i < len; i++) {
//                        tmp[i] = elems.get(i);
//                    }
//                    result = tmp;
//                } else if (short[].class.isAssignableFrom(clazz)) {
//                    short[] tmp = new short[elems.size()];
//                    for (int i = 0, len = elems.size(); i < len; i++) {
//                        tmp[i] = Short.valueOf(elems.get(i));
//                    }
//                    result = tmp;
//                } else if (long[].class.isAssignableFrom(clazz)) {
//                    long[] tmp = new long[elems.size()];
//                    for (int i = 0, len = elems.size(); i < len; i++) {
//                        tmp[i] = Long.valueOf(elems.get(i));
//                    }
//                    result = tmp;
//                } else if (float[].class.isAssignableFrom(clazz)) {
//                    float[] tmp = new float[elems.size()];
//                    for (int i = 0, len = elems.size(); i < len; i++) {
//                        tmp[i] = Float.valueOf(elems.get(i));
//                    }
//                    result = tmp;
//                } else if (double[].class.isAssignableFrom(clazz)) {
//                    double[] tmp = new double[elems.size()];
//                    for (int i = 0, len = elems.size(); i < len; i++) {
//                        tmp[i] = Double.valueOf(elems.get(i));
//                    }
//                    result = tmp;
//                } else if (char[].class.isAssignableFrom(clazz)) {
//                    char[] tmp = new char[elems.size()];
//                    for (int i = 0, len = elems.size(); i < len; i++) {
//                        tmp[i] = (char) Integer.parseInt(elems.get(i));
//                    }
//                    result = tmp;
//                } else if (boolean[].class.isAssignableFrom(clazz)) {
//                    boolean[] tmp = new boolean[elems.size()];
//                    for (int i = 0, len = elems.size(); i < len; i++) {
//                        tmp[i] = Boolean.parseBoolean(elems.get(i));
//                    }
//                    result = tmp;
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//        }
//        return result;
//    }
}
