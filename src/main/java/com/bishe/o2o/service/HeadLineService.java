package com.bishe.o2o.service;

import java.io.IOException;
import java.util.List;

import com.bishe.o2o.dto.HeadLineExecution;
import com.bishe.o2o.dto.ImageHolder;
import com.bishe.o2o.entity.HeadLine;

public interface HeadLineService {
	public static final String HLLISTKEY = "headlinelist";

	/**
	 * 根据传入的条件返回指定的头条列表
	 * 
	 * @param headLineCondition
	 * @return
	 * @throws IOException
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition);

	/**
	 * 添加头条信息，并存储头条图片
	 * 
	 * @param headLine
	 * @param thumbnail
	 * @return
	 */
	HeadLineExecution addHeadLine(HeadLine headLine, ImageHolder thumbnail);

	/**
	 * 修改头条信息
	 * 
	 * @param headLine
	 * @param thumbnail
	 * @return
	 */
	HeadLineExecution modifyHeadLine(HeadLine headLine, ImageHolder thumbnail);

	/**
	 * 删除单条头条
	 * 
	 * @param headLineId
	 * @return
	 */
	HeadLineExecution removeHeadLine(long headLineId);

	/**
	 * 批量删除头条
	 * 
	 * @param headLineIdList
	 * @return
	 */
	HeadLineExecution removeHeadLineList(List<Long> headLineIdList);
}
