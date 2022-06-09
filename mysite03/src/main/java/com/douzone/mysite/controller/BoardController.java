package com.douzone.mysite.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.douzone.mysite.repository.BoardRepository;
import com.douzone.mysite.service.BoardService;
import com.douzone.mysite.vo.BoardVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@RequestMapping("")
	public String index(Model model, HttpServletRequest request) {
		
		String pageNum = request.getParameter("p");
		String kwd = request.getParameter("kwd");
		
		if(pageNum == null) {
			pageNum = "1";
		}
		int limit = 5;
		
		HashMap<String, Integer> pages = new HashMap<String, Integer>();
		int currentPage = Integer.parseInt(pageNum);
		
		int startPage = limit * ((int)Math.ceil((double)currentPage / limit)-1)+1;
		if(startPage < 1) {
			startPage = 1;
		}
		
		int totalPage = (new BoardRepository().countBoard() % limit) == 0 ? new BoardRepository().countBoard()/limit : new BoardRepository().countBoard()/limit+1;
		int lastPage = startPage + (limit - 1) > totalPage ? totalPage : startPage + (limit-1);
		int prevPage = currentPage - 1 < 0 ? 1 : currentPage - 1;
		int nextPage = currentPage + 1 > totalPage ? totalPage : currentPage + 1;
		
		pages.put("currentPage", currentPage);	
		pages.put("startPage", startPage);		
		pages.put("totalPage", totalPage);		
		pages.put("lastPage", lastPage);		
		pages.put("prevPage", prevPage);		
		pages.put("nextPage", nextPage);		
		
		int totalBoard = new BoardRepository().countBoard();	
		request.setAttribute("totalBoard", totalBoard);
		
		List<BoardVo> list = boardService.getMessageList(currentPage, kwd);
		model.addAttribute("list", list);
		model.addAttribute("pages", pages);
		
//		request.setAttribute("list", list);
//		request.setAttribute("pages", pages);
		
		return "board/index";
	}
}
