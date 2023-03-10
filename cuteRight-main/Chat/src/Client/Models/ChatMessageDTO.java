package Client.Models;

import org.json.simple.JSONObject;

public class ChatMessageDTO {

	int std_id;
	int room_id;
	String name;
	String message;
	String time;
	
	public ChatMessageDTO(int std_id, int room_id, String name, String message) {
		this.std_id = std_id;
		this.room_id = room_id;
		this.name=name;
		this.message = message;
		this.time = null;
	}
	
	public ChatMessageDTO(int std_id, int room_id, String name, String message, String time) {
		this.std_id = std_id;
		this.room_id = room_id;
		this.name=name;
		this.message = message;
		this.time = time;
	}
	
	public ChatMessageDTO() {
		this.std_id = 0;
		this.room_id = 0;
		this.message = null;
		this.time = null;
		
	}
	
	public int getStd_id() {
		return std_id;
	}
	
	public void setStd_id(int std_id) {
		this.std_id = std_id;
	}
	
	public int getRoom_id() {
		return room_id;
	}
	
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("std_id", this.std_id);
		obj.put("room_id", this.room_id);
		obj.put("name", this.name);
		obj.put("message", this.message);
		obj.put("time", this.time);
		return obj;
	}

	public String toJSONString() {
		return toJSONObject().toJSONString();
	}
}
