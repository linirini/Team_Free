package Client.Models;

import org.json.simple.JSONObject;

//현재 방에서 학생들의 목록을 보여줄때 사용
public class UsersDTO{ 

	private int std_id;
	private String name;
	private String d_job;
	private int state;
	private String pwd;
	
	public UsersDTO(int std_id, String name, String d_job, int state, String pwd) {
		this.std_id = std_id;
		this.name = name;
		this.d_job = d_job;
		this.state=state;
		this.pwd = pwd;
	}
	
	
	public UsersDTO() {
		
	}
	
	public int getStd_id() {
		return std_id;
	}
	
	public void setStd_id(int std_id) {
		this.std_id = std_id;
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
	
	public String getPwd() {
		return pwd;
	}
	
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getState() {
		return state;
	}
	   
	public void setState(int state) {
	    this.state = state;
	}

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("std_id", this.std_id);
		obj.put("name", this.name);
		obj.put("d_job", this.d_job);
		obj.put("state", this.state);
		obj.put("pwd", this.pwd);
		return obj;
	}

	public String toJSONString() {
		return toJSONObject().toJSONString();
	}
}
