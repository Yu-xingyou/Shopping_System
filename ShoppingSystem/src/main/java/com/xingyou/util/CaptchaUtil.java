package com.xingyou.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

/**
 * 图形验证码生成工具类
 * 生成包含干扰线、噪点和旋转字符的验证码图片
 */
@Slf4j
public class CaptchaUtil {
    
    /**
     * 验证码字符集（去除易混淆的字符：0/O, 1/I/L）
     */
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    
    /**
     * 验证码图片宽度
     */
    private static final int WIDTH = 120;
    
    /**
     * 验证码图片高度
     */
    private static final int HEIGHT = 40;
    
    /**
     * 验证码长度（4位字符）
     */
    private static final int CODE_LENGTH = 4;
    
    /**
     * 干扰线数量
     */
    private static final int LINE_COUNT = 5;
    
    /**
     * 噪点数量
     */
    private static final int DOT_COUNT = 50;
    
    /**
     * 生成验证码
     * 
     * @return CaptchaResult 包含Base64编码的图片和验证码文本
     */
    public static CaptchaResult generateCaptcha() {
        try {
            // 创建图片
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            
            // 设置抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // 设置背景色（浅灰色）
            g.setColor(new Color(240, 240, 240));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            
            // 生成随机验证码
            StringBuilder code = new StringBuilder();
            Random random = new Random();
            
            // 绘制字符
            for (int i = 0; i < CODE_LENGTH; i++) {
                char ch = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
                code.append(ch);
                
                // 随机颜色（深色）
                g.setColor(new Color(
                    30 + random.nextInt(100),
                    30 + random.nextInt(100),
                    30 + random.nextInt(100)
                ));
                
                // 随机字体大小（24-32）
                int fontSize = 24 + random.nextInt(8);
                g.setFont(new Font("Arial", Font.BOLD, fontSize));
                
                // 随机旋转角度（-30度到30度）
                double angle = (random.nextDouble() - 0.5) * 0.6;
                
                // 计算字符位置
                int x = WIDTH / CODE_LENGTH * i + 15;
                int y = HEIGHT / 2 + fontSize / 3;
                
                // 应用旋转并绘制字符
                g.rotate(angle, x, y);
                g.drawString(String.valueOf(ch), x, y);
                g.rotate(-angle, x, y);
            }
            
            // 添加干扰线
            for (int i = 0; i < LINE_COUNT; i++) {
                g.setColor(new Color(
                    150 + random.nextInt(100),
                    150 + random.nextInt(100),
                    150 + random.nextInt(100)
                ));
                
                int x1 = random.nextInt(WIDTH);
                int y1 = random.nextInt(HEIGHT);
                int x2 = random.nextInt(WIDTH);
                int y2 = random.nextInt(HEIGHT);
                
                g.drawLine(x1, y1, x2, y2);
            }
            
            // 添加噪点
            for (int i = 0; i < DOT_COUNT; i++) {
                g.setColor(new Color(
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256)
                ));
                
                int x = random.nextInt(WIDTH);
                int y = random.nextInt(HEIGHT);
                g.fillRect(x, y, 2, 2);
            }
            
            g.dispose();
            
            // 转换为Base64编码
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "JPEG", baos);
            byte[] imageBytes = baos.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            
            log.debug("生成验证码成功 - code: {}", code.toString());
            
            return new CaptchaResult(base64Image, code.toString());
            
        } catch (Exception e) {
            log.error("生成验证码失败", e);
            throw new RuntimeException("生成验证码失败", e);
        }
    }
    
    /**
     * 验证码结果类
     */
    public static class CaptchaResult {
        /**
         * Base64编码的图片（不含data:image前缀）
         */
        private String imageBase64;
        
        /**
         * 验证码文本（用于验证）
         */
        private String code;
        
        public CaptchaResult(String imageBase64, String code) {
            this.imageBase64 = imageBase64;
            this.code = code;
        }
        
        public String getImageBase64() {
            return imageBase64;
        }
        
        public String getCode() {
            return code;
        }
    }
}
