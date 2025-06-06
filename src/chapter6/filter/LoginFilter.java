package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.User;
//仕様追加④ログインフィルター
//ユーザー編集画面とつぶやき編集画面に対して行うフィルター
@WebFilter(urlPatterns = {"/setting","/edit"})
public class LoginFilter implements Filter{

	public void doFilter(ServletRequest httpRequest, ServletResponse httpResponse,
			FilterChain chain) throws IOException, ServletException {

		//引数で渡された型の変換
		HttpServletRequest request = (HttpServletRequest)httpRequest;
		HttpServletResponse response = (HttpServletResponse)httpResponse;

		//ログインしている場合、リクエストの画面へ遷移
		User user = (User) request.getSession().getAttribute("loginUser");
	    if (user != null) {
			chain.doFilter(request, response);
		}else {
			//ログインしていない場合、ログイン画面へ遷移しエラーメッセージ表示
			List<String> errorMessages = new ArrayList<>();
	        errorMessages.add("ログインしてください");

	        HttpSession session = request.getSession();
	        session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./login");
			return;
		}
	}

		@Override
		public void init(FilterConfig config) {
		}

		@Override
		public void destroy() {
		}


}
