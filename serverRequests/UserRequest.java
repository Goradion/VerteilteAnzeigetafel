package serverRequests;

public abstract class UserRequest extends ServerRequest{

	private static final long serialVersionUID = 1L;
	int userID;
	
	public UserRequest(int userID) {
		super();
		this.userID = userID;
	}
	
	public int getUserID(){
		return userID;
	}
	@Override
	public boolean wantsAnswer(){
		return true;
	}
	
}
