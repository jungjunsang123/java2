package org.kosta.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kosta.model.BoardDAO;
import org.kosta.model.PostVO;


public class ListController  implements Controller {
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {						
		ArrayList<PostVO> list
		=BoardDAO.getInstance().getPostingList();		
		request.setAttribute("list", list);
		request.setAttribute("url", "/board/list.jsp");		
		return "/template/layout.jsp";
	}
}







