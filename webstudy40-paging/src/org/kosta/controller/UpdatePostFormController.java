package org.kosta.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.kosta.model.BoardDAO;
import org.kosta.model.PostVO;

public class UpdatePostFormController implements Controller {
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session=request.getSession(false);
		if(session==null||session.getAttribute("mvo")==null){
			return "redirect:index.jsp";
		}
		
		PostVO vo = BoardDAO.getInstance().getPostingByNo(request.getParameter("no"));		
		request.setAttribute( "pvo", vo);
		request.setAttribute("url", "/board/update.jsp");
		return "/template/layout.jsp";
	}
}






