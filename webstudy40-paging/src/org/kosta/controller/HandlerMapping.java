package org.kosta.controller;

public class HandlerMapping {
	private static HandlerMapping instance=new HandlerMapping();
	private HandlerMapping() {}
	public static HandlerMapping getInstance() {
		return instance;
	}
	public Controller create(String command) {
		Controller controller=null;
		if(command.equals("List")){
			controller=new ListController();
		}else if(command.equals("Login")){
			controller=new LoginController();
		}else if(command.equals("Logout")){
			controller=new LogoutController();
		}else if(command.equals("PostDetail")){
			controller=new PostDetailController();
		}else if (command.equals("WritePostForm")) {
			controller = new WritePostFormController();
		}else if (command.equals("WritePost")) {
			controller = new WritePostController();
		}else if(command.equals("PostDetailNoHits")){
			controller=new PostDetailNoHitsController();
		}else if(command.equals("DeletePost")){
			controller=new DeletePostController();
		}else if(command.equals("UpdatePostForm")){
			controller=new UpdatePostFormController();
		}else if(command.equals("UpdatePost")){
			controller=new UpdatePostController();
		} 
		return controller; 
	}
}






















