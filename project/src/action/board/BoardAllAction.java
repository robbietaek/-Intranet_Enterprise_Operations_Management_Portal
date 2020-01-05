package action.board;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import action.ActionForward;
import model.Board;
import model.BoardDao;
import model.Commute;
import model.CommuteDao;
import model.Flow;
import model.FlowDao;
import model.Member;
import model.MemberDao;
import model.ProcessDao;
import model.Reply;
import model.ReplyDao;
import model.Vacation;
import model.VacationDao;
import model.Vote;
import model.VoteDao;
import model.Voted;
import model.VotedDao;

public class BoardAllAction {
	protected String login;
	protected String id;
	protected int boardtype;
	// 1�� : ��������
	// 2�� : �� ����
	// 3�� : ���ߺμ�
	// 4�� : ȸ��μ�
	// 5�� : �����μ�
	// 6�� : �����μ�
	// 7�� : ����
	// 8�� : �� ���� üũ����Ʈ
	// 9�� : ȸ��ҽ�

	public ActionForward decorator(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		return new ActionForward();
	}

	public ActionForward signin(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		return new ActionForward();
	}

	public ActionForward signup(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		return new ActionForward();
	}

	public ActionForward login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String msg = "�ش� id�� �������� �ʽ��ϴ�.";
		String url = "sign-in.do";
		Member mem = new MemberDao().selectOne(id);
		if (mem != null && mem.getMembertype() == 1) {
			if (pass.equals(mem.getPass())) {
				request.getSession().setAttribute("login", id);
				msg = mem.getName() + "���� �α��� �ϼ̽��ϴ�.";
				url = "index.do";
			} else {
				msg = "��й�ȣ�� Ʋ���ϴ�.";
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward pwsearchform(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ActionForward();
	}

	public ActionForward pwsearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		MemberDao dao = new MemberDao();
		String url = "pwsearchForm.do";
		String msg = "�ش������� �����ϴ�.";

		String id = request.getParameter("id");
		String tel = request.getParameter("tel");
		String hiredate = request.getParameter("hiredate");
		
		Member m = new Member();
		
		if (dao.pwSearch(id, tel, hiredate)>0) {
			m = dao.selectOne(id);
			return new ActionForward(false, "pwshowForm.do?id="+m.getId());
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");

	}
	
	public ActionForward pwshowform(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String id = request.getParameter("id");
		
		Member m = new MemberDao().selectOne(id);
		
		request.setAttribute("m", m);
		
		return new ActionForward();
	}

	public ActionForward logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}
		String msg = login + "���� �α׾ƿ��ϼ̽��ϴ�.";
		String url = "sign-in.do";
		request.getSession().invalidate();
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward regist(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		Member mem = new Member();
		mem.setId(request.getParameter("id"));
		mem.setName(request.getParameter("name"));
		mem.setDept(request.getParameter("dept"));
		mem.setPosition(request.getParameter("position"));
		mem.setTel(request.getParameter("tel"));
		mem.setBirthday(request.getParameter("birthday"));
		mem.setHiredate(request.getParameter("hiredate"));
		mem.setPass(request.getParameter("pass"));
		mem.setPicture(request.getParameter("picture"));
		mem.setMembertype(2);
		MemberDao mdao = new MemberDao();
		String msg = "ȸ������ ����";
		String url = "sign-up.do";
		if (mdao.insert(mem) > 0) {
			msg = "ȸ�����Խ�û �Ϸ�. �λ��������� ������ ��ٸ�����.";
			url = "sign-in.do";
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward pictureform(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		return new ActionForward();
	}

	public ActionForward picture(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String path = request.getServletContext().getRealPath("/") + "dist/picture";
		String fname = null;
		try {
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}
			MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
			fname = multi.getFilesystemName("picture");
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("fname", fname);
		return new ActionForward();
	}

	public ActionForward index(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		// �������� 3��
		BoardDao dao = new BoardDao();
		int limit = 3;
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (NumberFormatException e) {
		}
		String column = request.getParameter("column");
		String find = request.getParameter("find");
		if (column != null && column.trim().equals(""))
			column = null;
		if (find != null && find.trim().equals(""))
			find = null;
		if (column == null || find == null) {
			column = null;
			find = null;
		}
		boardtype = 1;
		int boardcnt = dao.boardCount(column, find, boardtype, null, null);
		List<Board> list = dao.list(pageNum, limit, column, find, boardtype, null, null);
		int maxpage = (int) ((double) boardcnt / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage)
			endpage = maxpage;
		int boardnum = boardcnt - (pageNum - 1) * limit;
		request.setAttribute("boardcnt", boardcnt);
		request.setAttribute("list", list);
		request.setAttribute("maxpage", maxpage);
		request.setAttribute("startpage", startpage);
		request.setAttribute("endpage", endpage);
		request.setAttribute("boardnum", boardnum);
		request.setAttribute("pageNum", pageNum);
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("today", today);

		// �����
		FlowDao fdao = new FlowDao();
		List<Flow> flow = fdao.list();
		request.setAttribute("flow", flow);

		// ������
		MemberDao mdao = new MemberDao();
		request.setAttribute("birthday", mdao.list());

		// ȸ�� �ҽ�
		boardtype = 9;
		List<Board> blist = dao.todaylist(boardtype);
		request.setAttribute("blist", blist);

		// �̹����� ����
		VotedDao vdao = new VotedDao();
		List<Voted> v = vdao.list();
		List<Member> mem = mdao.list();
		List<Member> recvmem = new ArrayList<>();
		for (int i = 0; i < v.size(); i++) {
			for (int j = 0; j < mem.size(); j++) {
				if (v.get(i).getId().equals(mem.get(j).getId())) {
					recvmem.add(mdao.selectOne(mem.get(j).getId()));
				}
			}
		}
		request.setAttribute("recvmem", recvmem);

		return new ActionForward();
	}

	public ActionForward editprofileform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		MemberDao dao = new MemberDao();
		Member check = dao.selectOne(id);
		String rid = check.getId();
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals(rid)) {
			request.setAttribute("msg", "���θ� �� �� �ֽ��ϴ�");
			request.setAttribute("url", "editprofileForm.do?id=" + login);
			return new ActionForward(false, "../alert.jsp");
		}

		Member m = new MemberDao().selectOne(id);
		request.setAttribute("m", m);
		return new ActionForward();
	}

	public ActionForward editprofile(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		MemberDao dao = new MemberDao();
		Member check = dao.selectOne(id);
		String rid = check.getId();
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals(rid)) {
			request.setAttribute("msg", "���θ� �� �� �ֽ��ϴ�");
			request.setAttribute("url", "editprofileForm.do?id=" + login);
			return new ActionForward(false, "../alert.jsp");
		}

		Member mem = new Member();
		mem.setId(request.getParameter("id"));
		mem.setPass(request.getParameter("newpass"));
		mem.setTel(request.getParameter("tel"));
		mem.setPicture(request.getParameter("picture"));

		Member m = dao.selectOne(request.getParameter("id"));
		String realpass = m.getPass();
		String inputpass = request.getParameter("pass");

		String msg = "��й�ȣ�� Ʋ�Ƚ��ϴ�.";
		String url = "editprofileForm.do?id=" + request.getParameter("id");

		if (realpass.equals(inputpass)) {
			dao.update(mem);
			msg = "����Ǿ����ϴ�.";
			url = "index.do";
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward iddelete(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}
		String id = request.getParameter("id");
		MemberDao dao = new MemberDao();
		Member mem = dao.selectOne(id); // db ���� ��ȸ
		String msg = "���� ����";
		String url = "pmForm.do";
		// Ż�� ��� ����
		if (dao.delete(id) > 0) {
			msg = id + " ����ڸ� ���� Ż�� ����";
			url = "pmForm.do";
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward pmForm(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do?id=" + login);
			return new ActionForward(false, "../alert.jsp");
		}

		MemberDao mdao = new MemberDao();

		int count1 = mdao.count("���ߺμ�");
		int count2 = mdao.count("ȸ��μ�");
		int count3 = mdao.count("�����μ�");
		int count4 = mdao.count("�����μ�");
		request.setAttribute("count1", count1);
		request.setAttribute("count2", count2);
		request.setAttribute("count3", count3);
		request.setAttribute("count4", count4);
		request.setAttribute("list", mdao.submitlist());

		VoteDao vdao = new VoteDao();
		List<Vote> v = vdao.list();
		List<Member> mem = mdao.list();
		List<Member> recvmem = new ArrayList<>();
		for (int i = 0; i < v.size(); i++) {
			for (int j = 0; j < mem.size(); j++) {
				if (v.get(i).getVotedid().equals(mem.get(j).getId())) {
					recvmem.add(mdao.selectOne(mem.get(j).getId()));
				}
			}
		}

		request.setAttribute("recvmem", recvmem);

		List<Vacation> vacation = new VacationDao().selectAdmin();
		request.setAttribute("vacation", vacation);

		return new ActionForward();
	}

	public ActionForward registsubmit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}
		Member mem = new Member();
		MemberDao mdao = new MemberDao();

		String rid = request.getParameter("id");

		String msg = "ó�� ����";
		String url = "pmForm.do";

		if (mdao.submit(rid) > 0) {
			msg = "ȸ������ ���οϷ�";
			url = "pmForm.do";
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward vacationsubmit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}
		String msg = "������ ���еǾ����ϴ�.";
		String url = "pmForm.do";

		int num = Integer.parseInt(request.getParameter("num"));
		VacationDao dao = new VacationDao();
		if (dao.update(num)) {
			msg = "������ ���Ǿ����ϴ�.";
			url = "pmForm.do";
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward registdeny(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}
		String did = request.getParameter("id");
		MemberDao mdao = new MemberDao();
		String msg = "ó�� ����";
		String url = "pmForm.do";
		if (mdao.deny(did) > 0) {
			msg = "�ݷ��Ϸ�";
			url = "pmForm.do";
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward pmlist1form(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}

		MemberDao dao = new MemberDao();
		request.setAttribute("list", dao.list1());
		return new ActionForward();

	}

	public ActionForward pmlist2form(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}

		MemberDao dao = new MemberDao();
		request.setAttribute("list", dao.list2());
		return new ActionForward();

	}

	public ActionForward pmlist3form(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}

		MemberDao dao = new MemberDao();
		request.setAttribute("list", dao.list3());
		return new ActionForward();

	}

	public ActionForward pmlist4form(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}

		MemberDao dao = new MemberDao();
		request.setAttribute("list", dao.list4());
		return new ActionForward();

	}

	public ActionForward adminform(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do?id=" + login);
			return new ActionForward(false, "../alert.jsp");
		}
		MemberDao mdao = new MemberDao();
		int count1 = mdao.count("���ߺμ�");
		int count2 = mdao.count("ȸ��μ�");
		int count3 = mdao.count("�����μ�");
		int count4 = mdao.count("�����μ�");
		request.setAttribute("count1", count1);
		request.setAttribute("count2", count2);
		request.setAttribute("count3", count3);
		request.setAttribute("count4", count4);

		FlowDao fdao = new FlowDao();
		List<Flow> flow = fdao.list();
		request.setAttribute("flow", flow);

		return new ActionForward();
	}

	public ActionForward adminwrite(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String msg = "��Ͻ���";
		String url = "adminForm.do";

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		try {
			File f = new File(path);
			if (!f.exists())
				f.mkdirs();
			MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
			Board b = new Board();
			BoardDao bdao = new BoardDao();
			Date d = new Date();
			int num = bdao.maxnum();
			b.setId(login);
			b.setBoardtype(9);
			b.setSubject(multi.getParameter("subject"));
			b.setContent(multi.getParameter("content"));
			b.setFile1(multi.getFilesystemName("file1"));
			b.setSubtype(null);
			b.setDept(null);
			b.setNum(++num);
			b.setGrp(num);
			b.setRegdate(d);
			if (bdao.insert(b)) {
				msg = "ȸ�� �ҽ� ��� ����";
				url = "adminForm.do";
			}
		} catch (IOException e) {
			throw new ServletException(e);
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward admintextdelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String msg = "��������";
		String url = "adminForm.do";

		BoardDao dao = new BoardDao();

		if (dao.admindelete()) {
			msg = "���� ����";
			url = "adminForm.do";
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward flow(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}
		Flow f = new Flow();
		FlowDao dao = new FlowDao();
		String msg = "������ �ֽ��ϴ�.";
		String url = "adminForm.do";
		int num = dao.maxnum();
		try {
			f.setNum(++num);
			f.setJan(Integer.parseInt(request.getParameter("jan")));
			f.setFeb(Integer.parseInt(request.getParameter("feb")));
			f.setMar(Integer.parseInt(request.getParameter("mar")));
			f.setApr(Integer.parseInt(request.getParameter("apr")));
			f.setMay(Integer.parseInt(request.getParameter("may")));
			f.setJun(Integer.parseInt(request.getParameter("jun")));
			f.setJul(Integer.parseInt(request.getParameter("jul")));
			f.setAug(Integer.parseInt(request.getParameter("aug")));
			f.setSep(Integer.parseInt(request.getParameter("sep")));
			f.setOct(Integer.parseInt(request.getParameter("oct")));
			f.setNov(Integer.parseInt(request.getParameter("nov")));
			f.setDece(Integer.parseInt(request.getParameter("dece")));
			if (dao.insert(f)) {
				msg = "������Ʈ ����";
				url = "adminForm.do";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("flow", f);
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);

		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward flowdelete(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}

		FlowDao dao = new FlowDao();
		String msg = "������ �ֽ��ϴ�.";
		String url = "adminForm.do";
		try {
			if (dao.delete()) {
				msg = "���� ����";
				url = "adminForm.do";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);

		return new ActionForward(false, "../alert.jsp");
	}

	//////////////////////////////////////////// vote

	public ActionForward voteinsert(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String msg = "��ǥ ����";

		Vote v = new Vote();
		VoteDao dao = new VoteDao();

		v.setId(login);
		v.setVotedid(request.getParameter("votedid"));
		if (dao.check(v.getId()) > 0) {
			msg = "��ǥ�� �� ���� �����մϴ�.";

		} else {
			if (dao.insert(v)) {
				msg = "��ǥ �Ϸ�";
			}
		}
		request.setAttribute("msg", msg);
		return new ActionForward(false, "../alertclose.jsp");

	}

	public ActionForward votedelete(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String msg = "�ʱ�ȭ ����";
		String url = "pmForm.do";

		Vote v = new Vote();
		VoteDao dao = new VoteDao();
		VotedDao vdao = new VotedDao();
		if (dao.delete() && vdao.delete()) {
			msg = "�ʱ�ȭ ����.";
			url = "pmForm.do";
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");

	}

	public ActionForward votelistform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		MemberDao dao = new MemberDao();
		List<Member> m = dao.list();

		request.setAttribute("m", m);
		return new ActionForward();
	}

	public ActionForward votedmember(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}
		String msg = "�������� ����";
		String url = "pmForm.do";

		id = request.getParameter("votedid");
		VotedDao dao = new VotedDao();

		if (dao.insert(id)) {
			msg = "�������� �Ϸ�";
			url = "pmForm.do";
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);

		return new ActionForward(false, "../alert.jsp");

	}

	//////////////////////////////////////////// ��������

	public ActionForward imgupload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String path = request.getServletContext().getRealPath("/") + "dist/imgfile";
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
		String fileName = multi.getFilesystemName("upload");
		request.setAttribute("fileName", fileName);
		request.setAttribute("CKEditorFuncNum", request.getParameter("CKEditorFuncNum"));
		return new ActionForward(false, "ckeditor.jsp");
	}

	public ActionForward ckeditor(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return new ActionForward();
	}

	public ActionForward noticeform(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		BoardDao dao = new BoardDao();
		int limit = 10;
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (NumberFormatException e) {
		}
		String column = request.getParameter("column");
		String find = request.getParameter("find");
		if (column != null && column.trim().equals(""))
			column = null;
		if (find != null && find.trim().equals(""))
			find = null;
		if (column == null || find == null) {
			column = null;
			find = null;
		}
		boardtype = 1;
		int boardcnt = dao.boardCount(column, find, boardtype, null, null);
		List<Board> list = dao.list(pageNum, limit, column, find, boardtype, null, null);
		int maxpage = (int) ((double) boardcnt / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage)
			endpage = maxpage;
		int boardnum = boardcnt - (pageNum - 1) * limit;
		request.setAttribute("boardcnt", boardcnt);
		request.setAttribute("list", list);
		request.setAttribute("maxpage", maxpage);
		request.setAttribute("startpage", startpage);
		request.setAttribute("endpage", endpage);
		request.setAttribute("boardnum", boardnum);
		request.setAttribute("pageNum", pageNum);
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("today", today);
		return new ActionForward();

	}

	public ActionForward noticewriteform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "noticeForm.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");

		return new ActionForward();
	}

	public ActionForward noticewrite(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "noticeForm.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String msg = "�������� ��� ����";
		String url = "noticeForm.do";
		String path = request.getServletContext().getRealPath("/") + "dist/file";

		try {
			File f = new File(path);
			if (!f.exists())
				f.mkdirs();
			MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
			Board b = new Board();
			BoardDao dao = new BoardDao();
			Date d = new Date();
			int num = dao.maxnum();
			b.setId(login);
			b.setBoardtype(1);
			b.setSubject(multi.getParameter("subject"));
			b.setContent(multi.getParameter("content"));
			b.setFile1(multi.getFilesystemName("file1"));
			b.setSubtype(null);
			b.setDept(null);
			b.setNum(++num);
			b.setGrp(num);
			b.setRegdate(d);
			if (dao.insert(b)) {
				msg = "�������� ��� ����";
				url = "noticeForm.do";
			}
		} catch (IOException e) {
			throw new ServletException(e);
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward noticetextform(HttpServletRequest request, HttpServletResponse response) {

		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 1;
		Board b = dao.selectOne(num, boardtype);
		if (request.getRequestURI().contains("/noticetextForm.do")) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);
		return new ActionForward();
	}

	public ActionForward noticeeditform(HttpServletRequest request, HttpServletResponse response) {

		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "noticeForm.do");
			return new ActionForward(false, "../alert.jsp");
		}

		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 1;
		Board b = dao.selectOne(num, boardtype);
		if (request.getRequestURI().contains("/noticetextForm.do")) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);
		return new ActionForward();
	}

	public ActionForward noticeedit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "noticeForm.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
		Board board = new Board();
		BoardDao dao = new BoardDao();
		boardtype = 1;
		board.setBoardtype(boardtype);
		board.setNum(Integer.parseInt(multi.getParameter("num")));
		board.setSubject(multi.getParameter("subject"));
		board.setContent(multi.getParameter("content"));
		board.setFile1(multi.getFilesystemName("file1"));
		// ÷������ ������ �ȵ� ���
		if (board.getFile1() == null || board.getFile1().equals("")) {
			board.setFile1(multi.getParameter("file2"));
		}

		Board dbBoard = dao.selectOne(board.getNum(), boardtype);
		String msg = "";
		String url = "noticeeditForm.do?num=" + board.getNum();
		if (login.equals(dbBoard.getId())) {
			if (dao.update(board)) {
				msg = "�Խù� ���� �Ϸ�";
				url = "noticetextForm.do?num=" + board.getNum();
			} else {
				msg = "�Խù� ���� ����";
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward noticedelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}
		int num = Integer.parseInt(request.getParameter("num"));
		String msg = "������ �ƴմϴ�.";
		String url = "grp1textForm.do?num=" + num;
		BoardDao dao = new BoardDao();
		boardtype = 1;
		Board board = dao.selectOne(num, boardtype);
		if (board == null) {
			msg = "���� �Խñ��Դϴ�.";
			url = "noticeForm.do";
		} else {
			if (login.equals(board.getId())) {
				if (dao.delete(num)) {
					msg = "�Խñ� ���� ����";
					url = "noticeForm.do";
				} else {
					msg = "�Խñ� ���� ����";
					url = "noticetextForm.do?num=" + num;
				}
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	/////////////////////////////////////////// commute

	public ActionForward emplistform(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		id = request.getParameter("id");
		CommuteDao dao = new CommuteDao();
		int limit = 10;
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (NumberFormatException e) {
		}
		String column = request.getParameter("column");
		String find = request.getParameter("find");
		if (column != null && column.trim().equals(""))
			column = null;
		if (find != null && find.trim().equals(""))
			find = null;
		if (column == null || find == null) {
			column = null;
			find = null;
		}
		int boardcnt = dao.commuteCount(column, find, id);
		List<Commute> list = dao.list(pageNum, limit, column, find, id);
		int maxpage = (int) ((double) boardcnt / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage)
			endpage = maxpage;
		int boardnum = boardcnt - (pageNum - 1) * limit;
		request.setAttribute("id", id);
		request.setAttribute("boardcnt", boardcnt);
		request.setAttribute("list", list);
		request.setAttribute("maxpage", maxpage);
		request.setAttribute("startpage", startpage);
		request.setAttribute("endpage", endpage);
		request.setAttribute("boardnum", boardnum);
		request.setAttribute("pageNum", pageNum);
		return new ActionForward();

	}

	public ActionForward empinwrite(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		Commute b = new Commute();
		CommuteDao dao = new CommuteDao();
		Date d = new Date();
		int num = dao.maxnum();
		b.setId(login);
		b.setNum(++num);
		b.setGrp(num);
		b.setEmpin(d);
		dao.insert(b);

		request.setAttribute("msg", "����� �Ϸ�Ǿ����ϴ�..");
		request.setAttribute("url", "index.do");

		return new ActionForward(false, "../alert.jsp");

	}

	public ActionForward empoutwriteform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		return new ActionForward();
	}

	public ActionForward empoutwrite(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String msg = "��� ��� ����";
		String url = "empoutForm.do";
		String path = request.getServletContext().getRealPath("/") + "dist/file";

		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		Commute b = new Commute();
		CommuteDao dao = new CommuteDao();
		Date d = new Date();
		int num = dao.maxnum();
		b.setId(login);
		b.setSubject(request.getParameter("subject"));
		b.setContent(request.getParameter("content"));
		b.setNum(++num);
		b.setGrp(num);
		b.setEmpout(d);
		if (dao.insert(b)) {
			msg = "��� ��� ����";
			url = "empoutForm.do";
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alertclose.jsp");
	}

	/////////////////////////////////////////// message

	public ActionForward sendmessageform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		return new ActionForward();
	}

	public ActionForward sendmessage(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String msg = "���� ������ ����";
		String url = "sendmessageForm.do";
		String path = request.getServletContext().getRealPath("/") + "dist/file";

		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		Board b = new Board();
		BoardDao dao = new BoardDao();
		Date d = new Date();
		int num = dao.maxnum();
		b.setId(login);
		b.setBoardtype(7);
		b.setSubject(request.getParameter("subject"));
		b.setContent(request.getParameter("content"));
		b.setNum(++num);
		b.setGrp(num);
		b.setRegdate(d);
		b.setSendid(login);
		b.setRecvid(request.getParameter("recvid"));
		if (dao.messageinsert(b)) {
			msg = "���������� ����";
		}

		request.setAttribute("msg", msg);
		return new ActionForward(false, "../alertclose.jsp");
	}

	public ActionForward messageboxform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		BoardDao dao = new BoardDao();
		int limit = 10;
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (NumberFormatException e) {
		}
		String column = request.getParameter("column");
		String find = request.getParameter("find");
		if (column != null && column.trim().equals(""))
			column = null;
		if (find != null && find.trim().equals(""))
			find = null;
		if (column == null || find == null) {
			column = null;
			find = null;
		}
		boardtype = 7;
		int boardcnt = dao.messageboardCount(column, find, boardtype, login);
		List<Board> list = dao.messagelist(pageNum, limit, column, find, boardtype, login);
		int maxpage = (int) ((double) boardcnt / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage)
			endpage = maxpage;
		int boardnum = boardcnt - (pageNum - 1) * limit;
		request.setAttribute("boardcnt", boardcnt);
		request.setAttribute("list", list);
		request.setAttribute("maxpage", maxpage);
		request.setAttribute("startpage", startpage);
		request.setAttribute("endpage", endpage);
		request.setAttribute("boardnum", boardnum);
		request.setAttribute("pageNum", pageNum);
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("today", today);
		return new ActionForward();

	}

	public ActionForward sentmessageboxform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		BoardDao dao = new BoardDao();
		int limit = 10;
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (NumberFormatException e) {
		}
		String column = request.getParameter("column");
		String find = request.getParameter("find");
		if (column != null && column.trim().equals(""))
			column = null;
		if (find != null && find.trim().equals(""))
			find = null;
		if (column == null || find == null) {
			column = null;
			find = null;
		}
		boardtype = 7;
		int boardcnt = dao.sentmessageboardCount(column, find, boardtype, login);
		List<Board> list = dao.sentmessagelist(pageNum, limit, column, find, boardtype, login);
		int maxpage = (int) ((double) boardcnt / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage)
			endpage = maxpage;
		int boardnum = boardcnt - (pageNum - 1) * limit;
		request.setAttribute("boardcnt", boardcnt);
		request.setAttribute("list", list);
		request.setAttribute("maxpage", maxpage);
		request.setAttribute("startpage", startpage);
		request.setAttribute("endpage", endpage);
		request.setAttribute("boardnum", boardnum);
		request.setAttribute("pageNum", pageNum);
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("today", today);
		return new ActionForward();

	}

	public ActionForward messagetextform(HttpServletRequest request, HttpServletResponse response) {

		login = (String) request.getSession().getAttribute("login");
		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 7;
		Board check = dao.selectOne(num, boardtype);
		String rid = check.getRecvid();
		String mid = check.getSendid();
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			return new ActionForward(false, "../alertclose.jsp");
		} else if (login.equals(mid)) {
		}
		else if (!login.equals(rid)) {
			request.setAttribute("msg", "���ο��� �� ������ �ƴմϴ�.");
			request.setAttribute("url", "messagebox.do?id=" + login);
			return new ActionForward(false, "../alert.jsp");
		}

		Board b = dao.selectOne(num, boardtype);
		if (request.getRequestURI().contains("messagetextForm.do?id=" + login)) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);
		return new ActionForward();
	}

	public ActionForward sendto1form(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao dao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(dao.selectOne(login).getPosition().equals("�̻�") || dao.selectOne(login).getPosition().equals("����")
				|| dao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		return new ActionForward();
	}

	public ActionForward sendto2form(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao dao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(dao.selectOne(login).getPosition().equals("�̻�") || dao.selectOne(login).getPosition().equals("����")
				|| dao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}
		return new ActionForward();
	}

	public ActionForward sendto3form(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao dao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(dao.selectOne(login).getPosition().equals("�̻�") || dao.selectOne(login).getPosition().equals("����")
				|| dao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}
		return new ActionForward();
	}

	public ActionForward sendto4form(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao dao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(dao.selectOne(login).getPosition().equals("�̻�") || dao.selectOne(login).getPosition().equals("����")
				|| dao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}
		return new ActionForward();
	}

	public ActionForward sendto1(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao ckdao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(ckdao.selectOne(login).getPosition().equals("�̻�")
				|| ckdao.selectOne(login).getPosition().equals("����")
				|| ckdao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		String msg = "���� ������ ����";
		String path = request.getServletContext().getRealPath("/") + "dist/file";

		MemberDao mdao = new MemberDao();
		List<Member> m = mdao.list1();
		BoardDao dao = new BoardDao();
		Date d = new Date();

		for (int i = 0; i < m.size(); i++) {
			Board b = new Board();
			int num = dao.maxnum();
			b.setId(login);
			b.setBoardtype(7);
			b.setSubject(request.getParameter("subject"));
			b.setContent(request.getParameter("content"));
			b.setNum(++num);
			b.setGrp(num);
			b.setRegdate(d);
			b.setSendid(login);
			b.setRecvid(m.get(i).getId());
			b.setDept("���ߺμ�");
			if (dao.messageinsert(b)) {
				msg = "���ߺμ� ���������� ����";
			}

		}
		request.setAttribute("msg", msg);
		return new ActionForward(false, "../alertclose.jsp");
	}

	public ActionForward sendto2(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao ckdao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(ckdao.selectOne(login).getPosition().equals("�̻�")
				|| ckdao.selectOne(login).getPosition().equals("����")
				|| ckdao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		String msg = "���� ������ ����";
		String path = request.getServletContext().getRealPath("/") + "dist/file";

		MemberDao mdao = new MemberDao();
		List<Member> m = mdao.list2();
		BoardDao dao = new BoardDao();
		Date d = new Date();

		for (int i = 0; i < m.size(); i++) {
			Board b = new Board();
			int num = dao.maxnum();
			b.setId(login);
			b.setBoardtype(7);
			b.setSubject(request.getParameter("subject"));
			b.setContent(request.getParameter("content"));
			b.setNum(++num);
			b.setGrp(num);
			b.setRegdate(d);
			b.setSendid(login);
			b.setRecvid(m.get(i).getId());
			b.setDept("ȸ��μ�");
			if (dao.messageinsert(b)) {
				msg = "ȸ��μ� ���������� ����";
			}

		}
		request.setAttribute("msg", msg);
		return new ActionForward(false, "../alertclose.jsp");
	}

	public ActionForward sendto3(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao ckdao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(ckdao.selectOne(login).getPosition().equals("�̻�")
				|| ckdao.selectOne(login).getPosition().equals("����")
				|| ckdao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		String msg = "���� ������ ����";
		String path = request.getServletContext().getRealPath("/") + "dist/file";

		MemberDao mdao = new MemberDao();
		List<Member> m = mdao.list3();
		BoardDao dao = new BoardDao();
		Date d = new Date();

		for (int i = 0; i < m.size(); i++) {
			Board b = new Board();
			int num = dao.maxnum();
			b.setId(login);
			b.setBoardtype(7);
			b.setSubject(request.getParameter("subject"));
			b.setContent(request.getParameter("content"));
			b.setNum(++num);
			b.setGrp(num);
			b.setRegdate(d);
			b.setSendid(login);
			b.setRecvid(m.get(i).getId());
			b.setDept("�����μ�");
			if (dao.messageinsert(b)) {
				msg = "�����μ� ���������� ����";
			}

		}
		request.setAttribute("msg", msg);
		return new ActionForward(false, "../alertclose.jsp");
	}

	public ActionForward sendto4(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao ckdao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(ckdao.selectOne(login).getPosition().equals("�̻�")
				|| ckdao.selectOne(login).getPosition().equals("����")
				|| ckdao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		String msg = "���� ������ ����";
		String path = request.getServletContext().getRealPath("/") + "dist/file";

		MemberDao mdao = new MemberDao();
		List<Member> m = mdao.list4();
		BoardDao dao = new BoardDao();
		Date d = new Date();

		for (int i = 0; i < m.size(); i++) {
			Board b = new Board();
			int num = dao.maxnum();
			b.setId(login);
			b.setBoardtype(7);
			b.setSubject(request.getParameter("subject"));
			b.setContent(request.getParameter("content"));
			b.setNum(++num);
			b.setGrp(num);
			b.setRegdate(d);
			b.setSendid(login);
			b.setRecvid(m.get(i).getId());
			b.setDept("�����μ�");
			if (dao.messageinsert(b)) {
				msg = "�����μ� ���������� ����";
			}

		}
		request.setAttribute("msg", msg);
		return new ActionForward(false, "../alertclose.jsp");
	}

	//////////////////////////////////////////// vacation

	public ActionForward vacationform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}
		return new ActionForward();
	}

	public ActionForward vacationwrite(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String msg = "�ް���û ����";

		Vacation v = new Vacation();
		VacationDao dao = new VacationDao();
		MemberDao mdao = new MemberDao();
		int num = dao.maxnum();
		v.setId(login);
		v.setDept(mdao.selectOne(login).getDept());
		v.setNum(++num);
		v.setGrp(num);
		v.setStartdate(request.getParameter("startdate"));
		v.setEnddate(request.getParameter("startdate"));
		v.setVacationtype(request.getParameter("vacationtype"));
		v.setState(2);

		if (dao.insert(v)) {
			msg = "�ް���û ����, �λ��������� ������ ��ٸ�����.";
		}

		request.setAttribute("msg", msg);
		return new ActionForward(false, "../alertclose.jsp");
	}

	public ActionForward vacationdeny(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals("admin")) {
			request.setAttribute("msg", "�����ڸ� ������ �ŷ� �Դϴ�.");
			request.setAttribute("url", "myworkForm.do");
			return new ActionForward(false, "../alert.jsp");
		}
		int num = Integer.parseInt(request.getParameter("num"));
		VacationDao dao = new VacationDao();
		String msg = "ó�� ����";
		String url = "pmForm.do";
		if (dao.deny(num) > 0) {
			msg = "�ݷ��Ϸ�";
			url = "pmForm.do";
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward vacationlistform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		int limitV = 10;
		int pageNumV = 1;
		try {
			pageNumV = Integer.parseInt(request.getParameter("pageNumV"));
		} catch (NumberFormatException e) {
		}
		String columnV = request.getParameter("columnV");
		String findV = request.getParameter("findV");
		if (columnV != null && columnV.trim().equals(""))
			columnV = null;
		if (findV != null && findV.trim().equals(""))
			findV = null;
		if (columnV == null || findV == null) {
			columnV = null;
			findV = null;
		}
		VacationDao vdao = new VacationDao();
		int state = 1;
		id = login;
		String dept = null;
		int vacationcnt = vdao.vacationCount(columnV, findV, state, id, dept);
		List<Vacation> vlist = vdao.list(pageNumV, limitV, columnV, findV, state, id, dept);
		int maxpageV = (int) ((double) vacationcnt / limitV + 0.95);
		int startpageV = ((int) (pageNumV / 10.0 + 0.9) - 1) * 10 + 1;
		int endpageV = startpageV + 9;
		if (endpageV > maxpageV)
			endpageV = maxpageV;
		int boardnumV = vacationcnt - (pageNumV - 1) * limitV;

		request.setAttribute("maxpageV", maxpageV);
		request.setAttribute("startpageV", startpageV);
		request.setAttribute("endpageV", endpageV);
		request.setAttribute("boardnumV", boardnumV);
		request.setAttribute("pageNumV", pageNumV);
		String todayV = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("todayV", todayV);
		request.setAttribute("vacationcnt", vacationcnt);
		request.setAttribute("vlist", vlist);

		return new ActionForward();
	}
	//////////////////////////////////////////// mywork

	public ActionForward myworkform(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		String id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals(id)) {
			request.setAttribute("msg", "������ �Խ����� �ƴմϴ�.");
			request.setAttribute("url", "myworkForm.do?id=" + login);
			return new ActionForward(false, "../alert.jsp");
		}

		BoardDao dao = new BoardDao();
		int limit = 10;
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (NumberFormatException e) {
		}
		String column = request.getParameter("column");
		String find = request.getParameter("find");
		if (column != null && column.trim().equals(""))
			column = null;
		if (find != null && find.trim().equals(""))
			find = null;
		if (column == null || find == null) {
			column = null;
			find = null;
		}
		boardtype = 2;
		int boardcnt = dao.boardCount(column, find, boardtype, login, null);
		List<Board> list = dao.list(pageNum, limit, column, find, boardtype, login, null);
		int maxpage = (int) ((double) boardcnt / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage)
			endpage = maxpage;
		int boardnum = boardcnt - (pageNum - 1) * limit;
		request.setAttribute("boardcnt", boardcnt);
		request.setAttribute("list", list);
		request.setAttribute("maxpage", maxpage);
		request.setAttribute("startpage", startpage);
		request.setAttribute("endpage", endpage);
		request.setAttribute("boardnum", boardnum);
		request.setAttribute("pageNum", pageNum);
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("today", today);

		boardtype = 8;
		List<Board> checklist = dao.list(pageNum, limit, column, find, boardtype, login, null);
		request.setAttribute("checklist", checklist);
		System.out.println(checklist);

		return new ActionForward();

	}

	public ActionForward myworkwriteform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");

		return new ActionForward();
	}

	public ActionForward myworkwrite(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String msg = "�Խñ� ��� ����";
		String url = "myworkForm.do?id=" + login;
		String path = request.getServletContext().getRealPath("/") + "dist/file";

		try {
			File f = new File(path);
			if (!f.exists())
				f.mkdirs();
			MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
			Board b = new Board();
			BoardDao dao = new BoardDao();
			Date d = new Date();
			int num = dao.maxnum();
			b.setId(login);
			b.setBoardtype(2);
			b.setSubject(multi.getParameter("subject"));
			b.setContent(multi.getParameter("content"));
			b.setFile1(multi.getFilesystemName("file1"));
			b.setSubtype(multi.getParameter("subtype"));
			b.setDept(null);
			b.setNum(++num);
			b.setGrp(num);
			b.setRegdate(d);
			if (dao.insert(b)) {
				msg = "�Խñ� ��� ����";
				url = "myworkForm.do?id=" + login;
			}
		} catch (IOException e) {
			throw new ServletException(e);
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward myworktextform(HttpServletRequest request, HttpServletResponse response) {

		login = (String) request.getSession().getAttribute("login");
		String id = request.getParameter("id");
		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 2;
		Board check = dao.selectOne(num, boardtype);
		String rid = check.getId();
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals(id) || !login.equals(rid)) {
			request.setAttribute("msg", "������ �Խñ��� �ƴմϴ�.");
			request.setAttribute("url", "myworkForm.do?id=" + login);
			return new ActionForward(false, "../alert.jsp");
		}

		Board b = dao.selectOne(num, boardtype);
		if (request.getRequestURI().contains("myworktextForm.do?id=" + login)) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);
		return new ActionForward();
	}

	public ActionForward myworkeditform(HttpServletRequest request, HttpServletResponse response) {

		login = (String) request.getSession().getAttribute("login");
		String id = request.getParameter("id");
		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 2;
		Board check = dao.selectOne(num, boardtype);
		String rid = check.getId();
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals(id) || !login.equals(rid)) {
			request.setAttribute("msg", "������ �Խñ��� �ƴմϴ�.");
			request.setAttribute("url", "myworkForm.do?id" + login);
			return new ActionForward(false, "../alert.jsp");
		}

		Board b = dao.selectOne(num, boardtype);
		if (request.getRequestURI().contains("/myworktextForm.do")) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);
		return new ActionForward();
	}

	public ActionForward myworkedit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
		Board board = new Board();
		BoardDao dao = new BoardDao();
		boardtype = 2;
		board.setBoardtype(boardtype);
		board.setNum(Integer.parseInt(multi.getParameter("num")));
		board.setSubtype(multi.getParameter("subtype"));
		board.setSubject(multi.getParameter("subject"));
		board.setContent(multi.getParameter("content"));
		board.setFile1(multi.getFilesystemName("file1"));
		// ÷������ ������ �ȵ� ���
		if (board.getFile1() == null || board.getFile1().equals("")) {
			board.setFile1(multi.getParameter("file2"));
		}

		Board dbBoard = dao.selectOne(board.getNum(), boardtype);
		String msg = "";
		String url = "myworkeditForm.do?num=" + board.getNum();
		if (login.equals(dbBoard.getId())) {
			if (dao.update(board)) {
				msg = "�Խù� ���� �Ϸ�";
				url = "myworktextForm.do?num=" + board.getNum();
			} else {
				msg = "�Խù� ���� ����";
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward myworkdelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		login = (String) request.getSession().getAttribute("login");
		String id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!login.equals(id)) {
			request.setAttribute("msg", "������ �Խ����� �ƴմϴ�.");
			request.setAttribute("url", "myworkForm.do?id=" + login);
			return new ActionForward(false, "../alert.jsp");
		}
		int num = Integer.parseInt(request.getParameter("num"));
		String msg = "������ �ƴմϴ�.";
		String url = "myworktextForm.do?num=" + num;
		BoardDao dao = new BoardDao();
		boardtype = 2;
		Board board = dao.selectOne(num, boardtype);
		if (board == null) {
			msg = "���� �Խñ��Դϴ�.";
			url = "myworkForm.do";
		} else {
			if (login.equals(board.getId())) {
				if (dao.delete(num)) {
					msg = "�Խñ� ���� ����";
					url = "myworkForm.do?id=" + login;
				} else {
					msg = "�Խñ� ���� ����";
					url = "myworktextForm.do?num=" + num;
				}
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward myworkchecklistwrite(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		String msg = "üũ����Ʈ ��� ����";
		String url = "myworkForm.do?id=" + login;

		Board b = new Board();
		BoardDao dao = new BoardDao();
		Date d = new Date();
		int num = dao.maxnum();
		b.setId(login);
		b.setBoardtype(8);
		b.setSubject(null);
		b.setContent(request.getParameter("content"));
		b.setFile1(null);
		b.setSubtype(null);
		b.setDept(null);
		b.setNum(++num);
		b.setGrp(num);
		b.setRegdate(d);
		if (dao.insert(b)) {
			msg = "üũ����Ʈ ��� ����";
			url = "myworkForm.do?id=" + login;
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward myworkchecklistdelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}
		int num = Integer.parseInt(request.getParameter("num"));
		String msg = "������ �ƴմϴ�.";
		String url = "myworkForm.do?id=" + login;
		BoardDao dao = new BoardDao();
		boardtype = 8;
		Board board = dao.selectOne(num, boardtype);
		if (board == null) {
			msg = "���� �Խñ��Դϴ�.";
			url = "myworkForm.do?id=" + login;
		} else {
			if (login.equals(board.getId())) {
				if (dao.delete(num)) {
					msg = "�Խñ� ���� ����";
					url = "myworkForm.do?id=" + login;
				} else {
					msg = "�Խñ� ���� ����";
					url = "myworkForm.?id=" + login;
				}
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	///////////////////////////////////////////////////// grp1

	public ActionForward grp1replywrite(HttpServletRequest request, HttpServletResponse response) throws IOException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		Reply r = new Reply();
		ReplyDao dao = new ReplyDao();
		int replynum = dao.maxnum();
		Date d = new Date();
		int pN = Integer.parseInt(request.getParameter("pageNum"));
		r.setNum(Integer.parseInt(request.getParameter("num")));
		r.setId(login);
		r.setReplynum(++replynum);
		r.setContent(request.getParameter("content"));
		r.setRegdate(d);

		String msg = "��� �ۼ��� �����߽��ϴ�.";
		String url = "grp1textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
				+ Integer.parseInt(request.getParameter("pageNum"));
		if (dao.insert(r)) {
			msg = "��� �ޱ� ����";
			url = "grp1textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
					+ Integer.parseInt(request.getParameter("pageNum"));
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);

		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp1replydelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		ReplyDao dao = new ReplyDao();
		int replynum = Integer.parseInt(request.getParameter("replynum"));
		Reply r = dao.selectOne(replynum);
		String msg = "������ �ƴմϴ�.";
		String url = "grp1textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
				+ Integer.parseInt(request.getParameter("pageNum"));

		if (login.equals(r.getId())) {
			dao.delete(replynum);
			msg = "���� ����.";
			url = "grp1textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
					+ Integer.parseInt(request.getParameter("pageNum"));
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp1form(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("���ߺμ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		BoardDao dao = new BoardDao();
		int limit = 10;
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (NumberFormatException e) {
		}
		String column = request.getParameter("column");
		String find = request.getParameter("find");
		if (column != null && column.trim().equals(""))
			column = null;
		if (find != null && find.trim().equals(""))
			find = null;
		if (column == null || find == null) {
			column = null;
			find = null;
		}
		boardtype = 3;
		String dept = "���ߺμ�";
		int boardcnt = dao.boardCount(column, find, boardtype, null, dept);
		List<Board> list = dao.list(pageNum, limit, column, find, boardtype, null, dept);
		int maxpage = (int) ((double) boardcnt / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage)
			endpage = maxpage;
		int boardnum = boardcnt - (pageNum - 1) * limit;

		request.setAttribute("boardcnt", boardcnt);
		request.setAttribute("list", list);
		request.setAttribute("maxpage", maxpage);
		request.setAttribute("startpage", startpage);
		request.setAttribute("endpage", endpage);
		request.setAttribute("boardnum", boardnum);
		request.setAttribute("pageNum", pageNum);
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("today", today);

		/////////////////////////////////////////////////////////////////////////

		int limitV = 10;
		int pageNumV = 1;
		try {
			pageNumV = Integer.parseInt(request.getParameter("pageNumV"));
		} catch (NumberFormatException e) {
		}
		String columnV = request.getParameter("columnV");
		String findV = request.getParameter("findV");
		if (columnV != null && columnV.trim().equals(""))
			columnV = null;
		if (findV != null && findV.trim().equals(""))
			findV = null;
		if (columnV == null || findV == null) {
			columnV = null;
			findV = null;
		}
		VacationDao vdao = new VacationDao();
		int state = 1;
		id = null;
		dept = "���ߺμ�";
		int vacationcnt = vdao.vacationCount(columnV, findV, state, id, dept);
		List<Vacation> vlist = vdao.list(pageNumV, limitV, columnV, findV, state, id, dept);
		int maxpageV = (int) ((double) vacationcnt / limitV + 0.95);
		int startpageV = ((int) (pageNumV / 10.0 + 0.9) - 1) * 10 + 1;
		int endpageV = startpageV + 9;
		if (endpageV > maxpageV)
			endpageV = maxpageV;
		int boardnumV = vacationcnt - (pageNumV - 1) * limitV;

		request.setAttribute("maxpageV", maxpageV);
		request.setAttribute("startpageV", startpageV);
		request.setAttribute("endpageV", endpageV);
		request.setAttribute("boardnumV", boardnumV);
		request.setAttribute("pageNumV", pageNumV);
		String todayV = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("todayV", todayV);
		request.setAttribute("vacationcnt", vacationcnt);
		request.setAttribute("vlist", vlist);

		MemberDao mdao = new MemberDao();
		request.setAttribute("member", mdao.selectOne(login));

		ProcessDao pdao = new ProcessDao();
		List<model.Process> plist = pdao.list(boardtype);
		request.setAttribute("plist", plist);

		return new ActionForward();

	}

	public ActionForward grp1writeform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("���ߺμ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");

		return new ActionForward();
	}

	public ActionForward grp1write(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("���ߺμ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		String msg = "�Խñ� ��� ����";
		String url = "grp1Form.do";
		String path = request.getServletContext().getRealPath("/") + "dist/file";

		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		try {
			File f = new File(path);
			if (!f.exists())
				f.mkdirs();
			MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
			Board b = new Board();
			BoardDao dao = new BoardDao();
			Date d = new Date();
			int num = dao.maxnum();
			String dept = "���ߺμ�";
			boardtype = 3;
			b.setId(login);
			b.setBoardtype(boardtype);
			b.setSubject(multi.getParameter("subject"));
			b.setContent(multi.getParameter("content"));
			b.setFile1(multi.getFilesystemName("file1"));
			b.setSubtype(multi.getParameter("subtype"));
			b.setDept(dept);
			b.setNum(++num);
			b.setGrp(num);
			b.setRegdate(d);
			if (dao.insert(b)) {
				msg = "�Խñ� ��� ����";
				url = "grp1Form.do";
			}
		} catch (IOException e) {
			throw new ServletException(e);
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp1textform(HttpServletRequest request, HttpServletResponse response) {

		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("���ߺμ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 3;
		Board b = dao.selectOne(num, boardtype);

		if (request.getRequestURI().contains("grp1textForm.do?id=" + login)) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);

		ReplyDao rdao = new ReplyDao();
		List<Reply> replylist = rdao.list(Integer.parseInt(request.getParameter("num")));
		request.setAttribute("replylist", replylist);

		return new ActionForward();
	}

	public ActionForward grp1editform(HttpServletRequest request, HttpServletResponse response) {

		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("���ߺμ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}
		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 3;
		Board b = dao.selectOne(num, boardtype);
		if (request.getRequestURI().contains("/grp1textForm.do")) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);
		return new ActionForward();
	}

	public ActionForward grp1edit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("���ߺμ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
		Board board = new Board();
		BoardDao dao = new BoardDao();
		boardtype = 3;
		board.setBoardtype(boardtype);
		board.setNum(Integer.parseInt(multi.getParameter("num")));
		board.setSubtype(multi.getParameter("subtype"));
		board.setSubject(multi.getParameter("subject"));
		board.setContent(multi.getParameter("content"));
		board.setFile1(multi.getFilesystemName("file1"));
		// ÷������ ������ �ȵ� ���
		if (board.getFile1() == null || board.getFile1().equals("")) {
			board.setFile1(multi.getParameter("file2"));
		}
		int pageNum = Integer.parseInt(multi.getParameter("pageNum"));
		Board dbBoard = dao.selectOne(board.getNum(), boardtype);
		String msg = "���θ� ������ �� �ֽ��ϴ�.";
		String url = "grp1editForm.do?num=" + board.getNum();
		if (login.equals(dbBoard.getId())) {
			if (dao.update(board)) {
				msg = "�Խù� ���� �Ϸ�";
				url = "grp1textForm.do?num=" + board.getNum() + "&pageNum=" + pageNum;
			} else {
				msg = "�Խù� ���� ����";
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp1delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("���ߺμ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}
		int num = Integer.parseInt(request.getParameter("num"));
		String msg = "������ �ƴմϴ�.";
		String url = "grp1textForm.do?num=" + num + "&pageNum=" + Integer.parseInt(request.getParameter("pageNum"));
		BoardDao dao = new BoardDao();
		boardtype = 3;
		Board board = dao.selectOne(num, boardtype);
		if (board == null) {
			msg = "���� �Խñ��Դϴ�.";
			url = "grp1Form.do";
		} else {
			if (login.equals(board.getId())) {
				if (dao.delete(num)) {
					msg = "�Խñ� ���� ����";
					url = "grp1Form.do";
				} else {
					msg = "�Խñ� ���� ����";
					url = "grp1textForm.do?num=" + num + "&pageNum="
							+ Integer.parseInt(request.getParameter("pageNum"));
				}
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp1processwrite(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao ckdao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(ckdao.selectOne(login).getPosition().equals("�̻�")
				|| ckdao.selectOne(login).getPosition().equals("����")
				|| ckdao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		String msg = "������Ʈ ����";
		String url = "grp1Form.do";

		model.Process p = new model.Process();
		ProcessDao pdao = new ProcessDao();

		int num = pdao.maxnum();

		p.setNum(++num);
		p.setGrptype(3);
		p.setDept(Integer.parseInt(request.getParameter("dept")));
		p.setDeadline(Integer.parseInt(request.getParameter("deadline")));

		if (pdao.insert(p)) {
			msg = "������Ʈ ����";
			url = "grp1Form.do";
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp1processdelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao ckdao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(ckdao.selectOne(login).getPosition().equals("�̻�")
				|| ckdao.selectOne(login).getPosition().equals("����")
				|| ckdao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		String msg = "�ʱ�ȭ ����";
		String url = "grp1Form.do";

		ProcessDao pdao = new ProcessDao();

		if (pdao.delete(3)) {
			msg = "�ʱ�ȭ ����";
			url = "grp1Form.do";
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	//////////////////////////////////////////////////// grp2
	public ActionForward grp2replywrite(HttpServletRequest request, HttpServletResponse response) throws IOException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		Reply r = new Reply();
		ReplyDao dao = new ReplyDao();
		int replynum = dao.maxnum();
		Date d = new Date();
		int pN = Integer.parseInt(request.getParameter("pageNum"));
		r.setNum(Integer.parseInt(request.getParameter("num")));
		r.setId(login);
		r.setReplynum(++replynum);
		r.setContent(request.getParameter("content"));
		r.setRegdate(d);

		String msg = "��� �ۼ��� �����߽��ϴ�.";
		String url = "grp2textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
				+ Integer.parseInt(request.getParameter("pageNum"));
		if (dao.insert(r)) {
			msg = "��� �ޱ� ����";
			url = "grp2textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
					+ Integer.parseInt(request.getParameter("pageNum"));
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);

		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp2replydelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		ReplyDao dao = new ReplyDao();
		int replynum = Integer.parseInt(request.getParameter("replynum"));
		Reply r = dao.selectOne(replynum);
		String msg = "������ �ƴմϴ�.";
		String url = "grp2textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
				+ Integer.parseInt(request.getParameter("pageNum"));

		if (login.equals(r.getId())) {
			dao.delete(replynum);
			msg = "���� ����.";
			url = "grp2textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
					+ Integer.parseInt(request.getParameter("pageNum"));
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp2form(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("ȸ��μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		BoardDao dao = new BoardDao();
		int limit = 10;
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (NumberFormatException e) {
		}
		String column = request.getParameter("column");
		String find = request.getParameter("find");
		if (column != null && column.trim().equals(""))
			column = null;
		if (find != null && find.trim().equals(""))
			find = null;
		if (column == null || find == null) {
			column = null;
			find = null;
		}
		boardtype = 4;
		String dept = "ȸ��μ�";
		int boardcnt = dao.boardCount(column, find, boardtype, null, dept);
		List<Board> list = dao.list(pageNum, limit, column, find, boardtype, null, dept);
		int maxpage = (int) ((double) boardcnt / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage)
			endpage = maxpage;
		int boardnum = boardcnt - (pageNum - 1) * limit;

		request.setAttribute("boardcnt", boardcnt);
		request.setAttribute("list", list);
		request.setAttribute("maxpage", maxpage);
		request.setAttribute("startpage", startpage);
		request.setAttribute("endpage", endpage);
		request.setAttribute("boardnum", boardnum);
		request.setAttribute("pageNum", pageNum);
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("today", today);

		/////////////////////////////////////////////////////////////////

		int limitV = 10;
		int pageNumV = 1;
		try {
			pageNumV = Integer.parseInt(request.getParameter("pageNumV"));
		} catch (NumberFormatException e) {
		}
		String columnV = request.getParameter("columnV");
		String findV = request.getParameter("findV");
		if (columnV != null && columnV.trim().equals(""))
			columnV = null;
		if (findV != null && findV.trim().equals(""))
			findV = null;
		if (columnV == null || findV == null) {
			columnV = null;
			findV = null;
		}
		VacationDao vdao = new VacationDao();
		int state = 1;
		id = null;
		dept = "ȸ��μ�";
		int vacationcnt = vdao.vacationCount(columnV, findV, state, id, dept);
		List<Vacation> vlist = vdao.list(pageNumV, limitV, columnV, findV, state, id, dept);
		int maxpageV = (int) ((double) vacationcnt / limitV + 0.95);
		int startpageV = ((int) (pageNumV / 10.0 + 0.9) - 1) * 10 + 1;
		int endpageV = startpageV + 9;
		if (endpageV > maxpageV)
			endpageV = maxpageV;
		int boardnumV = vacationcnt - (pageNumV - 1) * limitV;

		request.setAttribute("maxpageV", maxpageV);
		request.setAttribute("startpageV", startpageV);
		request.setAttribute("endpageV", endpageV);
		request.setAttribute("boardnumV", boardnumV);
		request.setAttribute("pageNumV", pageNumV);
		String todayV = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("todayV", todayV);
		request.setAttribute("vacationcnt", vacationcnt);
		request.setAttribute("vlist", vlist);

		MemberDao mdao = new MemberDao();
		request.setAttribute("member", mdao.selectOne(login));

		ProcessDao pdao = new ProcessDao();
		List<model.Process> plist = pdao.list(boardtype);
		request.setAttribute("plist", plist);

		return new ActionForward();

	}

	public ActionForward grp2writeform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("ȸ��μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");

		return new ActionForward();
	}

	public ActionForward grp2write(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("ȸ��μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		String msg = "�Խñ� ��� ����";
		String url = "grp2Form.do";
		String path = request.getServletContext().getRealPath("/") + "dist/file";

		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		try {
			File f = new File(path);
			if (!f.exists())
				f.mkdirs();
			MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
			Board b = new Board();
			BoardDao dao = new BoardDao();
			Date d = new Date();
			int num = dao.maxnum();
			String dept = "ȸ��μ�";
			boardtype = 4;
			b.setId(login);
			b.setBoardtype(boardtype);
			b.setSubject(multi.getParameter("subject"));
			b.setContent(multi.getParameter("content"));
			b.setFile1(multi.getFilesystemName("file1"));
			b.setSubtype(multi.getParameter("subtype"));
			b.setDept(dept);
			b.setNum(++num);
			b.setGrp(num);
			b.setRegdate(d);
			if (dao.insert(b)) {
				msg = "�Խñ� ��� ����";
				url = "grp2Form.do";
			}
		} catch (IOException e) {
			throw new ServletException(e);
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp2textform(HttpServletRequest request, HttpServletResponse response) {

		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("ȸ��μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 4;
		Board b = dao.selectOne(num, boardtype);

		if (request.getRequestURI().contains("grp2textForm.do?id=" + login)) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);

		ReplyDao rdao = new ReplyDao();
		List<Reply> replylist = rdao.list(Integer.parseInt(request.getParameter("num")));
		request.setAttribute("replylist", replylist);

		return new ActionForward();
	}

	public ActionForward grp2editform(HttpServletRequest request, HttpServletResponse response) {

		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("ȸ��μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 4;
		Board b = dao.selectOne(num, boardtype);
		if (request.getRequestURI().contains("/grp2textForm.do")) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);
		return new ActionForward();
	}

	public ActionForward grp2edit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("ȸ��μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
		Board board = new Board();
		BoardDao dao = new BoardDao();
		boardtype = 4;
		board.setBoardtype(boardtype);
		board.setNum(Integer.parseInt(multi.getParameter("num")));
		board.setSubtype(multi.getParameter("subtype"));
		board.setSubject(multi.getParameter("subject"));
		board.setContent(multi.getParameter("content"));
		board.setFile1(multi.getFilesystemName("file1"));
		// ÷������ ������ �ȵ� ���
		if (board.getFile1() == null || board.getFile1().equals("")) {
			board.setFile1(multi.getParameter("file2"));
		}
		int pageNum = Integer.parseInt(multi.getParameter("pageNum"));
		Board dbBoard = dao.selectOne(board.getNum(), boardtype);
		String msg = "���θ� ������ �� �ֽ��ϴ�.";
		String url = "grp2editForm.do?num=" + board.getNum();
		if (login.equals(dbBoard.getId())) {
			if (dao.update(board)) {
				msg = "�Խù� ���� �Ϸ�";
				url = "grp2textForm.do?num=" + board.getNum() + "&pageNum=" + pageNum;
			} else {
				msg = "�Խù� ���� ����";
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp2delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("ȸ��μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}
		int num = Integer.parseInt(request.getParameter("num"));
		String msg = "������ �ƴմϴ�.";
		String url = "grp2textForm.do?num=" + num + "&pageNum=" + Integer.parseInt(request.getParameter("pageNum"));
		BoardDao dao = new BoardDao();
		boardtype = 4;
		Board board = dao.selectOne(num, boardtype);
		if (board == null) {
			msg = "���� �Խñ��Դϴ�.";
			url = "grp2Form.do";
		} else {
			if (login.equals(board.getId())) {
				if (dao.delete(num)) {
					msg = "�Խñ� ���� ����";
					url = "grp2Form.do";
				} else {
					msg = "�Խñ� ���� ����";
					url = "grp2textForm.do?num=" + num + "&pageNum="
							+ Integer.parseInt(request.getParameter("pageNum"));
				}
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp2processwrite(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao ckdao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(ckdao.selectOne(login).getPosition().equals("�̻�")
				|| ckdao.selectOne(login).getPosition().equals("����")
				|| ckdao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		String msg = "������Ʈ ����";
		String url = "grp2Form.do";

		model.Process p = new model.Process();
		ProcessDao pdao = new ProcessDao();

		int num = pdao.maxnum();

		p.setNum(++num);
		p.setGrptype(4);
		p.setDept(Integer.parseInt(request.getParameter("dept")));
		p.setDeadline(Integer.parseInt(request.getParameter("deadline")));

		if (pdao.insert(p)) {
			msg = "������Ʈ ����";
			url = "grp2Form.do";
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp2processdelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao ckdao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(ckdao.selectOne(login).getPosition().equals("�̻�")
				|| ckdao.selectOne(login).getPosition().equals("����")
				|| ckdao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		String msg = "�ʱ�ȭ ����";
		String url = "grp2Form.do";

		ProcessDao pdao = new ProcessDao();

		if (pdao.delete(4)) {
			msg = "�ʱ�ȭ ����";
			url = "grp2Form.do";
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	//////////////////////////////////////////////////// grp3

	public ActionForward grp3replywrite(HttpServletRequest request, HttpServletResponse response) throws IOException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		Reply r = new Reply();
		ReplyDao dao = new ReplyDao();
		int replynum = dao.maxnum();
		Date d = new Date();
		int pN = Integer.parseInt(request.getParameter("pageNum"));
		r.setNum(Integer.parseInt(request.getParameter("num")));
		r.setId(login);
		r.setReplynum(++replynum);
		r.setContent(request.getParameter("content"));
		r.setRegdate(d);

		String msg = "��� �ۼ��� �����߽��ϴ�.";
		String url = "grp3textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
				+ Integer.parseInt(request.getParameter("pageNum"));
		if (dao.insert(r)) {
			msg = "��� �ޱ� ����";
			url = "grp3textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
					+ Integer.parseInt(request.getParameter("pageNum"));
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);

		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp3replydelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		ReplyDao dao = new ReplyDao();
		int replynum = Integer.parseInt(request.getParameter("replynum"));
		Reply r = dao.selectOne(replynum);
		String msg = "������ �ƴմϴ�.";
		String url = "grp3textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
				+ Integer.parseInt(request.getParameter("pageNum"));

		if (login.equals(r.getId())) {
			dao.delete(replynum);
			msg = "���� ����.";
			url = "grp3textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
					+ Integer.parseInt(request.getParameter("pageNum"));
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp3form(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		BoardDao dao = new BoardDao();
		int limit = 10;
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (NumberFormatException e) {
		}
		String column = request.getParameter("column");
		String find = request.getParameter("find");
		if (column != null && column.trim().equals(""))
			column = null;
		if (find != null && find.trim().equals(""))
			find = null;
		if (column == null || find == null) {
			column = null;
			find = null;
		}
		boardtype = 5;
		String dept = "�����μ�";
		int boardcnt = dao.boardCount(column, find, boardtype, null, dept);
		List<Board> list = dao.list(pageNum, limit, column, find, boardtype, null, dept);
		int maxpage = (int) ((double) boardcnt / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage)
			endpage = maxpage;
		int boardnum = boardcnt - (pageNum - 1) * limit;

		request.setAttribute("boardcnt", boardcnt);
		request.setAttribute("list", list);
		request.setAttribute("maxpage", maxpage);
		request.setAttribute("startpage", startpage);
		request.setAttribute("endpage", endpage);
		request.setAttribute("boardnum", boardnum);
		request.setAttribute("pageNum", pageNum);
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("today", today);

		////////////////////////////////////////////////////////////////////////
		int limitV = 10;
		int pageNumV = 1;
		try {
			pageNumV = Integer.parseInt(request.getParameter("pageNumV"));
		} catch (NumberFormatException e) {
		}
		String columnV = request.getParameter("columnV");
		String findV = request.getParameter("findV");
		if (columnV != null && columnV.trim().equals(""))
			columnV = null;
		if (findV != null && findV.trim().equals(""))
			findV = null;
		if (columnV == null || findV == null) {
			columnV = null;
			findV = null;
		}
		VacationDao vdao = new VacationDao();
		int state = 1;
		id = null;
		dept = "�����μ�";
		int vacationcnt = vdao.vacationCount(columnV, findV, state, id, dept);
		List<Vacation> vlist = vdao.list(pageNumV, limitV, columnV, findV, state, id, dept);
		int maxpageV = (int) ((double) vacationcnt / limitV + 0.95);
		int startpageV = ((int) (pageNumV / 10.0 + 0.9) - 1) * 10 + 1;
		int endpageV = startpageV + 9;
		if (endpageV > maxpageV)
			endpageV = maxpageV;
		int boardnumV = vacationcnt - (pageNumV - 1) * limitV;

		request.setAttribute("maxpageV", maxpageV);
		request.setAttribute("startpageV", startpageV);
		request.setAttribute("endpageV", endpageV);
		request.setAttribute("boardnumV", boardnumV);
		request.setAttribute("pageNumV", pageNumV);
		String todayV = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("todayV", todayV);
		request.setAttribute("vacationcnt", vacationcnt);
		request.setAttribute("vlist", vlist);

		MemberDao mdao = new MemberDao();
		request.setAttribute("member", mdao.selectOne(login));

		ProcessDao pdao = new ProcessDao();
		List<model.Process> plist = pdao.list(boardtype);
		request.setAttribute("plist", plist);

		return new ActionForward();

	}

	public ActionForward grp3writeform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");

		return new ActionForward();
	}

	public ActionForward grp3write(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		String msg = "�Խñ� ��� ����";
		String url = "grp3Form.do";
		String path = request.getServletContext().getRealPath("/") + "dist/file";

		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		try {
			File f = new File(path);
			if (!f.exists())
				f.mkdirs();
			MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
			Board b = new Board();
			BoardDao dao = new BoardDao();
			Date d = new Date();
			int num = dao.maxnum();
			String dept = "�����μ�";
			boardtype = 5;
			b.setId(login);
			b.setBoardtype(boardtype);
			b.setSubject(multi.getParameter("subject"));
			b.setContent(multi.getParameter("content"));
			b.setFile1(multi.getFilesystemName("file1"));
			b.setSubtype(multi.getParameter("subtype"));
			b.setDept(dept);
			b.setNum(++num);
			b.setGrp(num);
			b.setRegdate(d);
			if (dao.insert(b)) {
				msg = "�Խñ� ��� ����";
				url = "grp3Form.do";
			}
		} catch (IOException e) {
			throw new ServletException(e);
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp3textform(HttpServletRequest request, HttpServletResponse response) {

		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 5;
		Board b = dao.selectOne(num, boardtype);

		if (request.getRequestURI().contains("grp3textForm.do?id=" + login)) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);

		ReplyDao rdao = new ReplyDao();
		List<Reply> replylist = rdao.list(Integer.parseInt(request.getParameter("num")));
		request.setAttribute("replylist", replylist);

		return new ActionForward();
	}

	public ActionForward grp3editform(HttpServletRequest request, HttpServletResponse response) {

		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 5;
		Board b = dao.selectOne(num, boardtype);
		if (request.getRequestURI().contains("/grp3textForm.do")) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);
		return new ActionForward();
	}

	public ActionForward grp3edit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
		Board board = new Board();
		BoardDao dao = new BoardDao();
		boardtype = 5;
		board.setBoardtype(boardtype);
		board.setNum(Integer.parseInt(multi.getParameter("num")));
		board.setSubtype(multi.getParameter("subtype"));
		board.setSubject(multi.getParameter("subject"));
		board.setContent(multi.getParameter("content"));
		board.setFile1(multi.getFilesystemName("file1"));
		// ÷������ ������ �ȵ� ���
		if (board.getFile1() == null || board.getFile1().equals("")) {
			board.setFile1(multi.getParameter("file2"));
		}
		int pageNum = Integer.parseInt(multi.getParameter("pageNum"));
		Board dbBoard = dao.selectOne(board.getNum(), boardtype);
		String msg = "���θ� ������ �� �ֽ��ϴ�.";
		String url = "grp3editForm.do?num=" + board.getNum();
		if (login.equals(dbBoard.getId())) {
			if (dao.update(board)) {
				msg = "�Խù� ���� �Ϸ�";
				url = "grp3textForm.do?num=" + board.getNum() + "&pageNum=" + pageNum;
			} else {
				msg = "�Խù� ���� ����";
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp3delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}
		int num = Integer.parseInt(request.getParameter("num"));
		String msg = "������ �ƴմϴ�.";
		String url = "grp3textForm.do?num=" + num + "&pageNum=" + Integer.parseInt(request.getParameter("pageNum"));
		BoardDao dao = new BoardDao();
		boardtype = 5;
		Board board = dao.selectOne(num, boardtype);
		if (board == null) {
			msg = "���� �Խñ��Դϴ�.";
			url = "grp3Form.do";
		} else {
			if (login.equals(board.getId())) {
				if (dao.delete(num)) {
					msg = "�Խñ� ���� ����";
					url = "grp3Form.do";
				} else {
					msg = "�Խñ� ���� ����";
					url = "grp3textForm.do?num=" + num + "&pageNum="
							+ Integer.parseInt(request.getParameter("pageNum"));
				}
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp3processwrite(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao ckdao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(ckdao.selectOne(login).getPosition().equals("�̻�")
				|| ckdao.selectOne(login).getPosition().equals("����")
				|| ckdao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		String msg = "������Ʈ ����";
		String url = "grp3Form.do";

		model.Process p = new model.Process();
		ProcessDao pdao = new ProcessDao();

		int num = pdao.maxnum();

		p.setNum(++num);
		p.setGrptype(5);
		p.setDept(Integer.parseInt(request.getParameter("dept")));
		p.setDeadline(Integer.parseInt(request.getParameter("deadline")));

		if (pdao.insert(p)) {
			msg = "������Ʈ ����";
			url = "grp3Form.do";
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp3processdelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao ckdao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(ckdao.selectOne(login).getPosition().equals("�̻�")
				|| ckdao.selectOne(login).getPosition().equals("����")
				|| ckdao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		String msg = "�ʱ�ȭ ����";
		String url = "grp3Form.do";

		ProcessDao pdao = new ProcessDao();

		if (pdao.delete(5)) {
			msg = "�ʱ�ȭ ����";
			url = "grp3Form.do";
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	//////////////////////////////////////////////////// grp4

	public ActionForward grp4replywrite(HttpServletRequest request, HttpServletResponse response) throws IOException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		Reply r = new Reply();
		ReplyDao dao = new ReplyDao();
		int replynum = dao.maxnum();
		Date d = new Date();
		int pN = Integer.parseInt(request.getParameter("pageNum"));
		r.setNum(Integer.parseInt(request.getParameter("num")));
		r.setId(login);
		r.setReplynum(++replynum);
		r.setContent(request.getParameter("content"));
		r.setRegdate(d);

		String msg = "��� �ۼ��� �����߽��ϴ�.";
		String url = "grp4textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
				+ Integer.parseInt(request.getParameter("pageNum"));
		if (dao.insert(r)) {
			msg = "��� �ޱ� ����";
			url = "grp4textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
					+ Integer.parseInt(request.getParameter("pageNum"));
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);

		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp4replydelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		ReplyDao dao = new ReplyDao();
		int replynum = Integer.parseInt(request.getParameter("replynum"));
		Reply r = dao.selectOne(replynum);
		String msg = "������ �ƴմϴ�.";
		String url = "grp4textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
				+ Integer.parseInt(request.getParameter("pageNum"));

		if (login.equals(r.getId())) {
			dao.delete(replynum);
			msg = "���� ����.";
			url = "grp4textForm.do?num=" + Integer.parseInt(request.getParameter("num")) + "&pageNum="
					+ Integer.parseInt(request.getParameter("pageNum"));
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp4form(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		BoardDao dao = new BoardDao();
		int limit = 10;
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (NumberFormatException e) {
		}
		String column = request.getParameter("column");
		String find = request.getParameter("find");
		if (column != null && column.trim().equals(""))
			column = null;
		if (find != null && find.trim().equals(""))
			find = null;
		if (column == null || find == null) {
			column = null;
			find = null;
		}
		boardtype = 6;
		String dept = "�����μ�";
		int boardcnt = dao.boardCount(column, find, boardtype, null, dept);
		List<Board> list = dao.list(pageNum, limit, column, find, boardtype, null, dept);
		int maxpage = (int) ((double) boardcnt / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage)
			endpage = maxpage;
		int boardnum = boardcnt - (pageNum - 1) * limit;

		request.setAttribute("boardcnt", boardcnt);
		request.setAttribute("list", list);
		request.setAttribute("maxpage", maxpage);
		request.setAttribute("startpage", startpage);
		request.setAttribute("endpage", endpage);
		request.setAttribute("boardnum", boardnum);
		request.setAttribute("pageNum", pageNum);
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("today", today);

		/////////////////////////////////////////////////////////////

		int limitV = 10;
		int pageNumV = 1;
		try {
			pageNumV = Integer.parseInt(request.getParameter("pageNumV"));
		} catch (NumberFormatException e) {
		}
		String columnV = request.getParameter("columnV");
		String findV = request.getParameter("findV");
		if (columnV != null && columnV.trim().equals(""))
			columnV = null;
		if (findV != null && findV.trim().equals(""))
			findV = null;
		if (columnV == null || findV == null) {
			columnV = null;
			findV = null;
		}
		VacationDao vdao = new VacationDao();
		int state = 1;
		id = null;
		dept = "�����μ�";
		int vacationcnt = vdao.vacationCount(columnV, findV, state, id, dept);
		List<Vacation> vlist = vdao.list(pageNumV, limitV, columnV, findV, state, id, dept);
		int maxpageV = (int) ((double) vacationcnt / limitV + 0.95);
		int startpageV = ((int) (pageNumV / 10.0 + 0.9) - 1) * 10 + 1;
		int endpageV = startpageV + 9;
		if (endpageV > maxpageV)
			endpageV = maxpageV;
		int boardnumV = vacationcnt - (pageNumV - 1) * limitV;

		request.setAttribute("maxpageV", maxpageV);
		request.setAttribute("startpageV", startpageV);
		request.setAttribute("endpageV", endpageV);
		request.setAttribute("boardnumV", boardnumV);
		request.setAttribute("pageNumV", pageNumV);
		String todayV = new SimpleDateFormat("yyyyMMdd").format(new Date());
		request.setAttribute("todayV", todayV);
		request.setAttribute("vacationcnt", vacationcnt);
		request.setAttribute("vlist", vlist);

		MemberDao mdao = new MemberDao();
		request.setAttribute("member", mdao.selectOne(login));

		ProcessDao pdao = new ProcessDao();
		List<model.Process> plist = pdao.list(boardtype);
		request.setAttribute("plist", plist);

		return new ActionForward();

	}

	public ActionForward grp4writeform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");

		return new ActionForward();
	}

	public ActionForward grp4write(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		String msg = "�Խñ� ��� ����";
		String url = "grp4Form.do";
		String path = request.getServletContext().getRealPath("/") + "dist/file";

		login = (String) request.getSession().getAttribute("login");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		}

		try {
			File f = new File(path);
			if (!f.exists())
				f.mkdirs();
			MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
			Board b = new Board();
			BoardDao dao = new BoardDao();
			Date d = new Date();
			int num = dao.maxnum();
			String dept = "�����μ�";
			boardtype = 6;
			b.setId(login);
			b.setBoardtype(boardtype);
			b.setSubject(multi.getParameter("subject"));
			b.setContent(multi.getParameter("content"));
			b.setFile1(multi.getFilesystemName("file1"));
			b.setSubtype(multi.getParameter("subtype"));
			b.setDept(dept);
			b.setNum(++num);
			b.setGrp(num);
			b.setRegdate(d);
			if (dao.insert(b)) {
				msg = "�Խñ� ��� ����";
				url = "grp4Form.do";
			}
		} catch (IOException e) {
			throw new ServletException(e);
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp4textform(HttpServletRequest request, HttpServletResponse response) {

		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 6;
		Board b = dao.selectOne(num, boardtype);

		if (request.getRequestURI().contains("grp4textForm.do?id=" + login)) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);

		ReplyDao rdao = new ReplyDao();
		List<Reply> replylist = rdao.list(Integer.parseInt(request.getParameter("num")));
		request.setAttribute("replylist", replylist);

		return new ActionForward();
	}

	public ActionForward grp4editform(HttpServletRequest request, HttpServletResponse response) {

		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		int num = Integer.parseInt(request.getParameter("num"));
		BoardDao dao = new BoardDao();
		boardtype = 6;
		Board b = dao.selectOne(num, boardtype);
		if (request.getRequestURI().contains("/grp4textForm.do")) {
			dao.readcntadd(num);
		}
		request.setAttribute("b", b);
		return new ActionForward();
	}

	public ActionForward grp4edit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}

		String path = request.getServletContext().getRealPath("/") + "dist/file";
		MultipartRequest multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
		Board board = new Board();
		BoardDao dao = new BoardDao();
		boardtype = 6;
		board.setBoardtype(boardtype);
		board.setNum(Integer.parseInt(multi.getParameter("num")));
		board.setSubtype(multi.getParameter("subtype"));
		board.setSubject(multi.getParameter("subject"));
		board.setContent(multi.getParameter("content"));
		board.setFile1(multi.getFilesystemName("file1"));
		// ÷������ ������ �ȵ� ���
		if (board.getFile1() == null || board.getFile1().equals("")) {
			board.setFile1(multi.getParameter("file2"));
		}
		int pageNum = Integer.parseInt(multi.getParameter("pageNum"));
		Board dbBoard = dao.selectOne(board.getNum(), boardtype);
		String msg = "���θ� ������ �� �ֽ��ϴ�.";
		String url = "grp4editForm.do?num=" + board.getNum();
		if (login.equals(dbBoard.getId())) {
			if (dao.update(board)) {
				msg = "�Խù� ���� �Ϸ�";
				url = "grp4textForm.do?num=" + board.getNum() + "&pageNum=" + pageNum;
			} else {
				msg = "�Խù� ���� ����";
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp4delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		MemberDao checkDao = new MemberDao();
		String checkDept = checkDao.selectOne(login).getDept();
		login = (String) request.getSession().getAttribute("login");
		id = request.getParameter("id");
		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α��� �� �ŷ��ϼ���");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!checkDept.equals("�����μ�")) {
			if (login.equals("admin")) {
			} else {
				request.setAttribute("msg", "�μ����� ���ٰ����մϴ�.");
				request.setAttribute("url", "index.do");
				return new ActionForward(false, "../alert.jsp");
			}
		}
		int num = Integer.parseInt(request.getParameter("num"));
		String msg = "������ �ƴմϴ�.";
		String url = "grp4textForm.do?num=" + num + "&pageNum=" + Integer.parseInt(request.getParameter("pageNum"));
		BoardDao dao = new BoardDao();
		boardtype = 6;
		Board board = dao.selectOne(num, boardtype);
		if (board == null) {
			msg = "���� �Խñ��Դϴ�.";
			url = "grp4Form.do";
		} else {
			if (login.equals(board.getId())) {
				if (dao.delete(num)) {
					msg = "�Խñ� ���� ����";
					url = "grp4Form.do";
				} else {
					msg = "�Խñ� ���� ����";
					url = "grp4textForm.do?num=" + num + "&pageNum="
							+ Integer.parseInt(request.getParameter("pageNum"));
				}
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp4processwrite(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao ckdao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(ckdao.selectOne(login).getPosition().equals("�̻�")
				|| ckdao.selectOne(login).getPosition().equals("����")
				|| ckdao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		String msg = "������Ʈ ����";
		String url = "grp4Form.do";

		model.Process p = new model.Process();
		ProcessDao pdao = new ProcessDao();

		int num = pdao.maxnum();

		p.setNum(++num);
		p.setGrptype(6);
		p.setDept(Integer.parseInt(request.getParameter("dept")));
		p.setDeadline(Integer.parseInt(request.getParameter("deadline")));

		if (pdao.insert(p)) {
			msg = "������Ʈ ����";
			url = "grp4Form.do";
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}

	public ActionForward grp4processdelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		login = (String) request.getSession().getAttribute("login");
		MemberDao ckdao = new MemberDao();

		if (login == null || login.trim().equals("")) {
			request.setAttribute("msg", "�α����� �ʿ��մϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "../alert.jsp");
		} else if (!(ckdao.selectOne(login).getPosition().equals("�̻�")
				|| ckdao.selectOne(login).getPosition().equals("����")
				|| ckdao.selectOne(login).getPosition().equals("����"))) {
			request.setAttribute("msg", "����� �� �ִ� ������ �ƴմϴ�.");
			request.setAttribute("url", "sign-in.do");
			return new ActionForward(false, "index.do");
		}

		String msg = "�ʱ�ȭ ����";
		String url = "grp4Form.do";

		ProcessDao pdao = new ProcessDao();

		if (pdao.delete(6)) {
			msg = "�ʱ�ȭ ����";
			url = "grp4Form.do";
		}

		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return new ActionForward(false, "../alert.jsp");
	}
}
