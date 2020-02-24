package com.bishe.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.bishe.o2o.util.PathUtil;
import com.bishe.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	//ch处理用户传过来的文件流
	private static String basePath=PathUtil.getImgBasePath();
		private static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
		private static final Random r=new Random();
		
		/**
		 * 将CommonsMultipartFile转换成File类
		 * 
		 * @param cFile
		 * @return
		 */
		public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
			File newFile = new File(cFile.getOriginalFilename());
			try {
				cFile.transferTo(newFile);
			} catch (IllegalStateException e) {
				//logger.error(e.toString());
				e.printStackTrace();
			} catch (IOException e) {
				//logger.error(e.toString());
				e.printStackTrace();
			}
			return newFile;
		}

		/**chul处理缩略图，并返回新生产图片的相对值路径
		 * @param thumbnail
		 * @param targetAddr
		 * @return
		 */
		public static String generateThumbnail(ImageHolder thumbnail,String targetAddr){
			
		//s使用随机名
		String realFileName=getRandomFileName();
		//h获取扩展名
		String extension=getFileExtension(thumbnail.getImageName());
		//luj路径不存在时候，创建路径
		makeDirPath(targetAddr);
		//x相对路径
		String relativeAddr=targetAddr+realFileName+extension;
		File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
		try{
			Thumbnails.of(thumbnail.getImage()).size(200, 200)
			
			.outputQuality(0.8f).toFile(dest);
		}catch(IOException e){
			e.printStackTrace();
		}
		return relativeAddr;
	}
		
		/**
		 * 处理详情图，并返回新生成图片的相对值路径
		 * 
		 * @param thumbnail
		 * @param targetAddr
		 * @return
		 */
		public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
			// 获取不重复的随机名
			String realFileName = getRandomFileName();
			// 获取文件的扩展名如png,jpg等
			String extension = getFileExtension(thumbnail.getImageName());
			// 如果目标路径不存在，则自动创建
			makeDirPath(targetAddr);
			// 获取文件存储的相对路径(带文件名)
			String relativeAddr = targetAddr + realFileName + extension;
			//logger.debug("current relativeAddr is :" + relativeAddr);
			// 获取文件要保存到的目标路径
			File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
			//logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
			// 调用Thumbnails生成带有水印的图片
			
			try {
				System.out.println(basePath);
				Thumbnails.of(thumbnail.getImage()).size(337, 640)
						
						.outputQuality(0.9f).toFile(dest);
				System.out.println(basePath);
				
			} catch (IOException e) {
				//logger.error(e.toString());
				
				throw new RuntimeException("创建缩图片失败：" + e.toString());
			}
			// 返回图片相对路径地址
			return relativeAddr;
		}	
	/**c创建目标路径所涉及到的目录，
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
			String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;
			File dirPath=new File(realFileParentPath);
			if(!dirPath.exists()){
			dirPath.mkdirs();
		}
	}

	/**huo获取输入文件流的扩展名
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fileName) {
			// TODO Auto-generated method stub
		
		//String originalFileName=cFile.getName();
			return fileName.substring(fileName.lastIndexOf("."));
		}

	/**生成随机文件名，当前年月日小时分钟秒+五位随机数
	 * @return
	 */
	public static String getRandomFileName() {
		// 获取随机的五位数
		int rannum=r.nextInt(89999)+10000;
		String nowTimeStr=sDateFormat.format(new Date());
	
		return nowTimeStr+rannum;
	}
	public static void main(String[] args) throws IOException{
		System.out.println(basePath);
		Thumbnails.of(new File("D:/image/11.png"))
		.size(200,200).watermark(Positions.BOTTOM_RIGHT,//0.25为水印透明度
				ImageIO.read(new File(basePath+"/2.png")),0.25f).outputQuality(0.8f)
		.toFile("D:/image/11new.png");
	}
	
	/**
	 * storePath是文件的路径还是目录的路径， 如果storePath是文件路径则删除该文件，
	 * 如果storePath是目录路径则删除该目录下的所有文件
	 * 
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		if (fileOrPath.exists()) {
			if (fileOrPath.isDirectory()) {
				File files[] = fileOrPath.listFiles();//是目录就遍历删除
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();//是文件就直接删除
		}
	}
	

}
