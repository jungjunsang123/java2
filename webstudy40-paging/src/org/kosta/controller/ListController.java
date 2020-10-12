package org.kosta.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kosta.model.BoardDAO;
import org.kosta.model.ListVO;
import org.kosta.model.PagingBean;
import org.kosta.model.PostVO;

import javafx.scene.control.ListView;


public class ListController  implements Controller {
   @Override
   public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {            
      int totalPostCount= BoardDAO.getInstance().getTotalPostCount();
      String pageNo = request.getParameter("pageNo");
      PagingBean pagingBean = null;
      if(pageNo==null) {
         pagingBean = new PagingBean(totalPostCount);
      }else
         pagingBean = new PagingBean(totalPostCount,Integer.parseInt(pageNo));
      ArrayList<PostVO> list=BoardDAO.getInstance().getPostingList(pagingBean);
      ListVO listVO = new ListVO(list, pagingBean);
      request.setAttribute("lvo", listVO);
      request.setAttribute("url", "/board/list.jsp");      
      return "/template/layout.jsp";
   }
}






