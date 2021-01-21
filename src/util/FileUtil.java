package util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileUtil {

	private static final String THUMBNAIL_PATH = "/Thumb/";
	private static final int MAX_THUMB_SIZE = 100;		
	private static final int MAX_PROFILE_THUMB_SIZE = 100;
	
	public static boolean deleteFile(String filePath) {
		
		boolean bDelete = false;
		
		try {
			
			File file = new File(filePath);		
			
			boolean isExist = file.exists();
			
			if( isExist ) {
				
				file.setExecutable(true);
				
				bDelete = file.delete();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bDelete;
	}
	
	public static String copyFile(String orgPath, String dstPath, String fileName) {
		boolean bCopyed = false;
		// 폴더가 있는지..
		File f = new File(dstPath); 
		
		if(!f.exists()){
			f.mkdirs();
		}

		// 복사 원본 파일
		String orgFileName = orgPath + "\\" + fileName;
		File orgFile = new File(orgFileName);			 
		//System.out.println("orgFile = " + orgFile);
		
		// 복사 사본 파일
		String copyFileName = dstPath + "\\" + fileName;
		File newFile = new File(copyFileName);
		//System.out.println("newFile = " + copyFileName);
		
		int i = 0;		
		String rename = fileName;
		while (newFile.exists()) {
			rename = rename.replace(i == 0 ? "." : i + ".", "" + (i + 1) + ".");
			i++;
			copyFileName = dstPath + "\\" + rename;
//			//System.out.println("rename  = " + copyFileName);
			newFile = new File(copyFileName);
		}
//		//System.out.println("copyFileName = " + copyFileName);
		
		newFile.setExecutable(true);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		byte[] buf = new byte[1024 * 2];
		int len = -1;		 

		try {
		    bis = new BufferedInputStream(new FileInputStream(orgFile));
		    bos = new BufferedOutputStream(new FileOutputStream(newFile));		 

		    while((len = bis.read(buf, 0, buf.length)) != -1) {
		        bos.write(buf, 0, len);
		        bos.flush();
		    }
		    
		    bCopyed = true;
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		} finally {
		    // 파일 자원 해제
		    if(bos != null) try { bos.close(); } catch(Exception e) {}
		    if(bis != null) try { bis.close(); } catch(Exception e) {}
		}

		return copyFileName;
	}
	
	public static void createProfileThumbnail(String uploadPath, String uploadThumbPath, String orgFileName) {
		createThumbnail(uploadPath, uploadThumbPath, orgFileName, MAX_PROFILE_THUMB_SIZE);
	}
	
	public static void createThumbnail(String uploadPath, String uploadThumbPath, String orgFileName) {
		createThumbnail(uploadPath, uploadThumbPath, orgFileName, MAX_THUMB_SIZE);
	}
		
	public static void createThumbnail(String uploadPath, String uploadThumbPath, String orgFileName, int maxSize) {
		final String type = "image/jpg";
		
		final String orgPath = uploadPath + "/" + orgFileName;
		final String thumbPath = uploadThumbPath;
		
		//System.out.println("orgPath = " + orgPath);
		//System.out.println("thumbPath = " + thumbPath);
		
		File f = new File(thumbPath); 
		
		if(!f.exists()){
			f.mkdirs();
		}
		
//		//System.out.println("createThumbnail " + orgPath);
		
		try {
			BufferedInputStream streamFile = new BufferedInputStream(new FileInputStream(orgPath));
			
			String thumbFileName = thumbPath + "/" + orgFileName;
//			//System.out.println("thumbFileName = " + thumbFileName);
			File file = new File(thumbFileName);
			file.setExecutable(true);
			BufferedImage bi = ImageIO.read(streamFile);
			
			
			int width = bi.getWidth();
			int height = bi.getHeight();
			
			// 가로와 세로중 큰것을 200으로 맞추고 그 비율만큼 다른하나 길이도 줄이기
			if (width > height)//썸네일이미지의 크기를 조정하기 위해
			{			
				if ( width > maxSize) {
					height = maxSize * height / width;
					width = maxSize;					
				}
			}
			else
			{
				if ( height > maxSize) {
					width = maxSize * width / height;
					height = maxSize;					
				}
			}
			//System.out.println("width " + width);
			//System.out.println("height " + height);
			
			BufferedImage bufferIm = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Image atemp = bi.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
			Graphics2D g2 = bufferIm.createGraphics();
			g2.drawImage(atemp, 0,0, width, height,null);
			ImageIO.write(bufferIm, type, file);
		} catch ( Exception e ) {
		}
	}
}
