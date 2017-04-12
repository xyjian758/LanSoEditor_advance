package com.lansosdk.videoeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import android.graphics.Bitmap;
import android.util.Log;

public class AVDecoder {
	
	/**
	 * 
	 * @param filepath
	 * @return
	 */
		public static native long  decoderInit(String filepath);
		/**
		 * 解码一帧, 发送上去.  seekUS大于等于0, 说明要seek, 
		 * 注意:如果您设置了seek大于等于0, 因为视频编码原理是基于IDR刷新帧的, seek时会选择在你设置时间的最近前一个IDR刷新帧的位置,请注意!
		 *  
		 *  
		 * 这里只seek一次开始解码, 解码后直接把数据发送上去. 用decoderIsEnd来判断当前是否已经解码好.
		 * 
		 * 建议:如果您的需求每次都解码同一个视频,视频总帧数在20帧以下,并每帧的字节不是很大, 建议一次解码后, 用list保存起来,不用每次都解码同一个视频.
		 * 
		 * @param handle  当前文件的句柄,
		 * 
		 * @param seekUs  是否要seek, 大于等于0说明要seek, 
		 * 
		 * @param out  输出.
		 * 
		 * @return  返回当前当前帧的时间戳,单位us
		 */
		public static native long decoderFrame(long handle,long seekUs,int[] out);
		
		/**
		 * 释放当前解码器.
		 * 
		 * @param handle
		 * @return
		 */
		public static native int decoderRelease(long handle);
		/**
		 * 解码是否到文件尾.
		 * @param handle
		 * @return
		 */
		public static native boolean decoderIsEnd(long handle);
		
		/**
		 * 代码测试.
		 *
		 *测试证明, 是可以多个线程同时解码的.
		 *private void testDecoder()
		{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					testDecoder2("/sdcard/480x480.mp4","/sdcard/480x480.yuv");
				}
			}).start();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					testDecoder2("/sdcard/ping20s.mp4","/sdcard/ping20s.yuv");
				}
			}).start();
		}
		private void testDecoder2(String src,String dst)
		{
			   long decoderHandler;
			   IntBuffer  mGLRgbBuffer;
			   String gifPath=src;
			   MediaInfo  gifInfo=new MediaInfo(gifPath);
			       if(gifInfo.prepare())
			       {
			    	   decoderHandler=AVDecoder.decoderInit(gifPath);
			    	   FileWriteUtls  write=new FileWriteUtls(dst);
			    	   mGLRgbBuffer = IntBuffer.allocate(gifInfo.vWidth * gifInfo.vHeight);
			    	   while(AVDecoder.decoderIsEnd(decoderHandler)==false)
			    	   {
			    			mGLRgbBuffer.position(0);
		    				AVDecoder.decoderFrame(decoderHandler, -1, mGLRgbBuffer.array());
		    				mGLRgbBuffer.position(0);
		    				write.writeFile(mGLRgbBuffer);
			    	   }
			    	   write.closeWriteFile();
			    	   Log.i(TAG,"write closeEEEE!");
			       }
			}
		 */
}
