package com.bishe.o2o.web.superadmin;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bishe.o2o.dto.ConstantForSuperAdmin;
import com.bishe.o2o.dto.HeadLineExecution;
import com.bishe.o2o.dto.ImageHolder;
import com.bishe.o2o.entity.HeadLine;
import com.bishe.o2o.enums.HeadLineStateEnum;
import com.bishe.o2o.service.HeadLineService;
import com.bishe.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class HeadLineController {
	@Autowired
	private HeadLineService headLineService;

	/**
	 * 根据查询条件分页获取头条列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listheadlines", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listHeadLines(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<HeadLine> list = new ArrayList<HeadLine>();
		try {
			// 若传入的查询条件里有可用状态，则依据可用状态检索
			Integer enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
			HeadLine headLine = new HeadLine();
			if (enableStatus > -1) {
				headLine.setEnableStatus(enableStatus);
			}
			list = headLineService.getHeadLineList(headLine);
			modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, list);
			modelMap.put(ConstantForSuperAdmin.TOTAL, list.size());
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	/**
	 * 添加头条信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addheadline", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addHeadLine(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		HeadLine headLine = null;
		// 接收并转化相应的参数，包括头条信息以及图片信息
		String headLineStr = HttpServletRequestUtil.getString(request, "headLineStr");
		ImageHolder thumbnail = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			headLine = mapper.readValue(headLineStr, HeadLine.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		// 咱们的请求中都带有multi字样，因此没法过滤，只是用来拦截外部非图片流的处理，
		// 里边有缩略图的空值判断，请放心使用
		try {
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, "headTitleManagementAdd_lineImg");
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		// 空值判断
		if (headLine != null && thumbnail != null) {
			try {
				// decode可能有中文的地方
				headLine.setLineName(
						(headLine.getLineName() == null) ? null : URLDecoder.decode(headLine.getLineName(), "UTF-8"));
				headLine.setLineLink(
						(headLine.getLineLink() == null) ? null : URLDecoder.decode(headLine.getLineLink(), "UTF-8"));
				// 添加头条信息
				HeadLineExecution ae = headLineService.addHeadLine(headLine, thumbnail);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}

	/**
	 * 修改头条信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyheadline", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyHeadLine(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		HeadLine headLine = null;
		// 接收并转化相应的参数，包括头条信息以及图片信息
		String headLineStr = HttpServletRequestUtil.getString(request, "headLineStr");
		ImageHolder thumbnail = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			headLine = mapper.readValue(headLineStr, HeadLine.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		// 咱们的请求中都带有multi字样，因此没法过滤，只是用来拦截外部非图片流的处理，
		// 里边有缩略图的空值判断，请放心使用
		try {
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, "headTitleManagementEdit_lineImg");
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (headLine != null && headLine.getLineId() != null) {
			try {
				// decode可能有中文的地方
				headLine.setLineName(
						(headLine.getLineName() == null) ? null : URLDecoder.decode(headLine.getLineName(), "UTF-8"));
				headLine.setLineLink(
						(headLine.getLineLink() == null) ? null : URLDecoder.decode(headLine.getLineLink(), "UTF-8"));
				// 修改头条信息
				HeadLineExecution ae = headLineService.modifyHeadLine(headLine, thumbnail);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}

	/**
	 * 删除单个头条信息
	 * 
	 * @param headLineId
	 * @return
	 */
	@RequestMapping(value = "/removeheadline", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeHeadLine(Long headLineId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 空值判断
		if (headLineId != null && headLineId > 0) {
			try {
				// 根据传入的Id删除对应的头条
				HeadLineExecution ae = headLineService.removeHeadLine(headLineId);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}

	/**
	 * 批量删除头条信息
	 * 
	 * @param headLineIdListStr
	 * @return
	 */
	@RequestMapping(value = "/removeheadlines", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeHeadLines(String headLineIdListStr) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
		List<Long> headLineIdList = null;
		try {
			// 将前端传入的Id列表字符串转换成List<Integer>
			headLineIdList = mapper.readValue(headLineIdListStr, javaType);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		// 空值判断
		if (headLineIdList != null && headLineIdList.size() > 0) {
			try {
				// 根据传入的头条Id列表批量删除头条信息
				HeadLineExecution ae = headLineService.removeHeadLineList(headLineIdList);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}

	/**
	 * 图片预处理
	 * 
	 * @param request
	 * @param thumbnail
	 * @param productImgList
	 * @return
	 * @throws IOException
	 */
	private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, String flowName)
			throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 取出缩略图并构建ImageHolder对象
		CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile(flowName);
		if (thumbnailFile != null) {
			thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
		}
		return thumbnail;
	}

}
