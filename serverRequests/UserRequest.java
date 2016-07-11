package serverRequests;

public abstract class UserRequest extends ServerRequest{

	private static final long serialVersionUID = 1L;
	int userID;
	/**
	 * Construcs a UserRequest.
	 * @param userID
	 */
	public UserRequest(int userID) {
		super();
		this.userID = userID;
	}
	/**
	 * Returns the ID of the user who is sending the Request.
	 * @return
	 */
	public int getUserID(){
		return userID;
	}
	@Override
	public boolean wantsAnswer(){
		return true;
	}
	
}
