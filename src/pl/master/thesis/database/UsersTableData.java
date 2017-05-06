package pl.master.thesis.database;

public class UsersTableData {

	private String userName;
	private String password;
	private String question;
	private String answer;
	private int userId;

	public UsersTableData setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public UsersTableData setPassword(String password) {
		this.password = password;
		return this;
	}

	public UsersTableData setQuestion(String question) {
		this.question = question;
		return this;
	}

	public UsersTableData setAnswer(String answer) {
		this.answer = answer;
		return this;
	}

	public UsersTableData setUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}

	public int getUserId() {
		return userId;
	}

}
