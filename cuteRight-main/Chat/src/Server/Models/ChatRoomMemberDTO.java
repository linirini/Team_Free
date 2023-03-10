package Server.Models;


import org.json.simple.JSONObject;

//현재 방에서 학생들의 목록을 보여줄때 사용
public class ChatRoomMemberDTO {

	private String room_id;
	private String std_id;
	private String member;
	private String name;
	private String d_job;
	private String state;
	
	public ChatRoomMemberDTO(String std_id, String room_id, String member, String name, String d_job, String state) {
		this.std_id = std_id;
		this.room_id = room_id;
		this.member = member;
		this.name = name;
		this.d_job = d_job;
		this.state = state;
	}
	
	
	public ChatRoomMemberDTO() {
		// TODO Auto-generated constructor stub
	}


	public String getStd_id() {
		return std_id;
	}
	
	public void setStd_id(String std_id) {
		this.std_id = std_id;
	}
	
	public String getRoom_id() {
		return room_id;
	}
	
	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}
	
	public String getMember() {
		return member;
	}
	
	public void setMember(String member) {
		this.member = member;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getD_job() {
		return d_job;
	}
	
	public void setD_job(String d_job) {
		this.d_job = d_job;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("std_id", this.std_id);
		obj.put("room_id", this.room_id);
		obj.put("member", this.member);
		obj.put("name", this.name);
		obj.put("d_job", this.d_job);
		obj.put("state", this.state);
		return obj;
	}

	public String toJSONString() {
		return toJSONObject().toJSONString();
	}
}
