package Server.Models;

import org.json.simple.JSONObject;

//현재 방에서 학생들의 목록을 보여줄때 사용
public class ChatRoomInfoDTO{

	private int room_id;
	private String title;
	private int limit_person;
	private int cur_person;
	private int leader_id;
	
	public ChatRoomInfoDTO(int room_id, String title, int limit_person, int cur_person, int leader_id) {
		this.room_id = room_id;
		this.title = title;
		this.limit_person = limit_person;
		this.cur_person = cur_person;
		this.leader_id = leader_id;
	}
	
	
	public ChatRoomInfoDTO() {
	}
	
	
	public int getRoom_id() {
		return room_id;
	}
	
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getLimit_person() {
		return limit_person;
	}
	
	public void setLimit_person(int limit_person) {
		this.limit_person = limit_person;
	}
	
	public int getCur_person() {
		return cur_person;
	}
	
	public void setCur_person(int cur_person) {
		this.cur_person = cur_person;
	}
	
	public int getLeader_id() {
		return leader_id;
	}
	
	public void setLeader_id(int leader_id) {
		this.leader_id = leader_id;
	}

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("room_id", this.room_id);
		obj.put("title", this.title);
		obj.put("limit_person", this.limit_person);
		obj.put("cur_person", this.cur_person);
		obj.put("leader_id", this.leader_id);
		return obj;
	}

	public String toJSONString() {
		return toJSONObject().toJSONString();
	}
}
