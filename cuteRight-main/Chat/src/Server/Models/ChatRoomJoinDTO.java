package Server.Models;


import org.json.simple.JSONObject;

//현재 방에서 학생들의 목록을 보여줄때 사용
public class ChatRoomJoinDTO {

	private int member_id;
	private int room_id;
	private int std_id;
	private int member;
	
	public ChatRoomJoinDTO(int member_id, int room_id, int std_id, int member) {
		this.member_id = member_id;
		this.std_id = std_id;
		this.room_id = room_id;
		this.member = member;
	}
	

	public int getMember_id() {
		return member_id;
	}
	
	public void setJoin_id(int member_id) {
		this.member_id = member_id;
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
	
	public int getMember() {
		return member;
	}
	
	public void setMember(int member) {
		this.member = member;
	}

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("member_id", this.member_id);
		obj.put("room_id", this.room_id);
		obj.put("std_id", this.std_id);
		obj.put("member", this.member);
		return obj;
	}

	public String toJSONString() {
		return toJSONObject().toJSONString();
	}
}
