package com.example.fruugo.api;

import com.example.fruugo.dataobject.Link;
import com.example.fruugo.dataobject.User;
import com.example.fruugo.model.Result;
import com.example.fruugo.po.AddLinkPO;
import com.example.fruugo.po.EditLinkPO;
import com.example.fruugo.po.EditLinksPO;
import com.example.fruugo.service.LinkService;
import com.example.fruugo.service.UserService;
import com.example.fruugo.vo.LinkListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户登录API
 * 注解@RequestMapping设定了这个Controller类所有接口的前置路径/api/user，该前置路径会和下面每一个接口的路径拼接
 * 例如下面登录接口标注的是@GetMapping("/login")，那么登录接口的实际路径是：/api/user/login
 */
@Tag(name = "UserAPI", description = "fruugo项目")
@RestController
@RequestMapping("/api/user")
public class UserAPI {

	/**
	 * session的字段名
	 */
	public static final String SESSION_NAME = "userInfo";

	@Autowired
	private UserService userService;

	@Autowired
	private LinkService linkService;

	/**
	 * 用户注册
	 *
	 * @param user   传入注册用户信息
	 * @param errors Validation的校验错误存放对象
	 * @return 注册结果
	 */
	@Operation(summary = "注册")
	@PostMapping("/register")
	public Result<User> register(@RequestBody @Valid User user, BindingResult errors) {
		Result<User> result;
		// 如果校验有错，返回注册失败以及错误信息
		if (errors.hasErrors()) {
			result = new Result<>();
			result.setResultFailed(errors.getFieldError().getDefaultMessage());
			return result;
		}
		// 调用注册服务
		result = userService.register(user);
		return result;
	}

	/**
	 * 用户登录
	 *
	 * @param user    传入登录用户信息
	 * @param errors  Validation的校验错误存放对象
	 * @param request 请求对象，用于操作session
	 * @return 登录结果
	 */
	@Operation(summary = "登录")
	@PostMapping("/login")
	public Result<User> login(@RequestBody @Valid User user, BindingResult errors, HttpServletRequest request) {
		Result<User> result;
		// 如果校验有错，返回登录失败以及错误信息
		if (errors.hasErrors()) {
			result = new Result<>();
			result.setResultFailed(errors.getFieldError().getDefaultMessage());
			return result;
		}
		// 调用登录服务
		result = userService.login(user);
		// 如果登录成功，则设定session
		if (result.isSuccess()) {
			request.getSession().setAttribute(SESSION_NAME, result.getData());
		}
		return result;
	}

	/**
	 * 判断用户是否登录
	 *
	 * @param request 请求对象，从中获取session里面的用户信息以判断用户是否登录
	 * @return 结果对象，已经登录则结果为成功，且数据体为用户信息；否则结果为失败，数据体为空
	 */
	@Operation(summary = "判断是否登录")
	@GetMapping("/is-login")
	public Result<User> isLogin(HttpServletRequest request) {
		// 传入session到用户服务层
		return userService.isLogin(request.getSession());
	}

//	/**
//	 * 用户信息修改
//	 *
//	 * @param user    修改后用户信息对象
//	 * @param request 请求对象，用于操作session
//	 * @return 修改结果
//	 */
//	@Operation()
//	@PatchMapping("/update")
//	public Result<User> update(@RequestBody User user, HttpServletRequest request) throws Exception {
//		Result<User> result = new Result<>();
//		HttpSession session = request.getSession();
//		// 检查session中的用户（即当前登录用户）是否和当前被修改用户一致
//		User sessionUser = (User) session.getAttribute(SESSION_NAME);
//		if (sessionUser.getId() != user.getId().intValue()) {
//			result.setResultFailed("当前登录用户和被修改用户不一致，终止！");
//			return result;
//		}
//		result = userService.update(user);
//		// 修改成功则刷新session信息
//		if (result.isSuccess()) {
//			session.setAttribute(SESSION_NAME, result.getData());
//		}
//		return result;
//	}

	/**
	 * 用户登出
	 *
	 * @param request 请求，用于操作session
	 * @return 结果对象
	 */
	@Operation(summary = "登出")
	@GetMapping("/logout")
	public Result<Void> logout(HttpServletRequest request) {
		Result<Void> result = new Result<>();
		// 用户登出很简单，就是把session里面的用户信息设为null即可
		request.getSession().setAttribute(SESSION_NAME, null);
		result.setResultSuccess("User logs out successfully!");
		return result;
	}

	@Operation(summary = "新增跳转链接")
	@PostMapping("/addLinks")
	public Result<LinkListVO> addLinks(@RequestBody @Valid AddLinkPO po, HttpServletRequest request) {
		Result<LinkListVO> result = new Result<>();
		if (Objects.isNull(po) || po.getLinks() == null) {
			result.setResultFailed("The input link is empty, please reenter it!");
		} else {
			List<String> linkList = po.getLinks();
			List<Link> list = new ArrayList<>(po.getLinks().size());
			for (String link : linkList) {
				Link realLink = new Link();
				realLink.setLink(link);
				realLink.setStatus(1);
				list.add(realLink);
			}
			result = linkService.insertLink(list);
		}
		return result;

	}

	@Operation(summary = "查询所有有效跳转链接")
	@GetMapping("/queryLinks")
	public Result<LinkListVO> getAll() {
		Result<LinkListVO> result = new Result<>();
		result = linkService.getAllLinks();
		return result;
	}


	@Operation(summary = "编辑跳转链接")
	@PostMapping("/editLinks")
	public Result<LinkListVO> updateLinks(@RequestBody @Valid EditLinksPO pos, HttpServletRequest request) {
		Result<LinkListVO> result = new Result<>();
		if (Objects.isNull(pos) || pos.getPoList() == null) {
			result.setResultFailed("The input link is empty, please reenter it!");
			return result;
		} else {
			List<EditLinkPO> poList = pos.getPoList();
			List<Link> list = new ArrayList<>(poList.size());
			for (EditLinkPO po : poList) {
				Link reaLink = new Link();
				reaLink.setId(po.getId());
				reaLink.setLink(po.getLink());
				list.add(reaLink);
			}
			result = linkService.updateLinks(list);
			return result;
		}

	}

	@Operation(summary = "删除跳转链接")
	@PostMapping("/deleteLinks")
	public Result<LinkListVO> deleteLinks(@RequestParam @Valid String idList, HttpServletRequest request) {
		Result<LinkListVO> result = new Result<>();
		String regex = "\\d+";
		Pattern pattern = Pattern.compile(regex);
		if (Objects.isNull(idList)) {
			result.setResultFailed("Please enter the link id that needs to be deleted");
			return result;
		} else if (!pattern.matcher(idList).find()) {
			result.setResultFailed("Please enter the correct link id");
			return result;
		} else {
			String[] ids = idList.split(",");
			List<Link> list = new ArrayList<>();
			for (String id : ids) {
				Link realLink = new Link();
				realLink.setId(Long.valueOf(id));
				list.add(realLink);
			}
			result = linkService.deleteLinks(list);
		}
		return result;


	}
}