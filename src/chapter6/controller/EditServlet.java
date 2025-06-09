package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })

public class EditServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	//仕様追加②-1  つぶやきの編集
	//編集ボタン押下後 つぶやきを取得しedit.jspに画面遷移
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();

		//jspから渡ってきたmessage.idを取得
		String id = request.getParameter("id");

		//idが数字以外またはnullの場合、
		//トップ画面に遷移し、「不正なパラメータが入力されました」とエラーメッセージを表示
		//if 正規構文
		if (id == null || !id.matches("^[1-9]\\d*$")) {
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		//MessageServiceにid渡す、戻り値は該当のMessage (message)
		Message message = new MessageService().selectEdit(id);

		//MessageDaoから戻ってきた値がnull（idが存在しない数字だった）場合、
		//トップ画面に遷移し、「不正なパラメータが入力されました」とエラーメッセージを表示
		if (message == null) {
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		//MessageServiceから戻ったMessage (message)をセット
		request.setAttribute("message", message);

		//メッセージをedit.jspに渡す p28
		request.getRequestDispatcher("edit.jsp").forward(request, response);
	}

	//仕様追加②-2  つぶやきの編集
	//つぶやき編集後、更新ボタン押下後
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<String> errorMessages = new ArrayList<String>();
		//入力されたテキスト内容を取得
		String text = request.getParameter("text");
		Message message = new Message();
		//Message型messageにセット
		message.setText(text);
		//エラー確認、入力内容を保持させるためメッセージをセットしjspへ渡す
		if (!isValid(text, errorMessages)) {
			request.setAttribute("errorMessages", errorMessages);
			request.setAttribute("message", message);
			request.getRequestDispatcher("edit.jsp").forward(request, response);
			return;
		}
		//入力内容にエラーがない場合、ID、UserIdもセットしMessageServiceへ渡す
		message.setId(Integer.parseInt(request.getParameter("id")));
		message.setUserId(message.getId());
		new MessageService().update(message);
		response.sendRedirect("./");
	}

	private boolean isValid(String text, List<String> errorMessages) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		if (StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}

		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}
