package Server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class MessagePacker {
    private JSONObject body;

    public MessagePacker(int std_id, int room_id, String message) {
        body = new JSONObject();
        body.put("std_id", String.valueOf(std_id));
        body.put("room_id", String.valueOf(room_id));
        body.put("message", message);
    }

    public MessagePacker(byte[] data) throws Exception {
        JSONParser parser = new JSONParser();
        body = (JSONObject) parser.parse(new String(data, StandardCharsets.UTF_8));
    }

    public static MessagePacker unpack(InputStream is) throws Exception {
        byte[] sizeBuf = new byte[2];
        int readByCount = is.read(sizeBuf,0, sizeBuf.length);
        if(readByCount == -1) throw new IOException();
        int size = byteArrayToInt(sizeBuf, 2);

        // Message 내용을 담을 버퍼
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] receiveData = new byte[size];
        int read;

        // 버퍼 안의 데이터를 다 읽을 때까지 반복문을 돌린다.
        while((read = is.read(receiveData,0,receiveData.length))!=-1) {
            buffer.write(receiveData,0,read);
            size = size - read;
            if (size<=0) {
                break;
            }
        }

        byte[] data = buffer.toByteArray();
        buffer.flush();
        buffer.close();

        MessagePacker packet = new MessagePacker(data);
        return packet;
    }

    public byte[] getPacket() {
        byte[] jsonData = body.toJSONString().getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[2 + jsonData.length];
        byte[] dataSize = intToByteArray(jsonData.length, 2);
        for(int i=0; i<2; i++) {
            data[i] = dataSize[i];
        }
        for(int i=0; i< jsonData.length; i++) {
            data[2+i] = jsonData[i];
        }

        return data;
    }

    public String getMessage() { return (String) body.get("message"); }
    public int getStdId() { return Integer.parseInt((String) body.get("std_id")); }
    public int getRoomId() { return Integer.parseInt((String) body.get("room_id")); }

    private static byte[] intToByteArray(int value, int lengthDiv) {
        byte[] byteArray = new byte[lengthDiv];
        if (lengthDiv == 2){
            byteArray[0] = (byte) value;
            byteArray[1] = (byte) (value >>> 8);
        }else if (lengthDiv == 4){
            byteArray[0] = (byte)(value >> 24);
            byteArray[1] = (byte)(value >> 16);
            byteArray[2] = (byte)(value >> 8);
            byteArray[3] = (byte)(value);
        }
        return byteArray;
    }

    public static int byteArrayToInt(byte[] b, int lengthDiv) {
        int byteInt = 0;
        if (lengthDiv == 2) {
            byteInt = ((b[1] & 0xFF) << 8) | (b[0] & 0xFF);
        } else if (lengthDiv == 4) {
            byteInt = b[0] & 0xFF |
                    (b[1] & 0xFF) << 8 |
                    (b[2] & 0xFF) << 16 |
                    (b[3] & 0xFF) << 24;
        }
        return byteInt;
    }

    public String toString() { return body.toJSONString(); }
}
