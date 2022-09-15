package com.fssy.shareholder.management.controller.tool;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author QinHui
 * @title: QrCodeController
 * @description:
 * @date 2021/11/6
 */
@Controller
@RequestMapping("/system/tool/qrCode")
public class QrCodeController {
    @GetMapping("/index")
    public void get(@RequestParam(name = "width", defaultValue = "100", required = false) int width,
                    @RequestParam(name = "height", defaultValue = "100", required = false) int height,
                    @RequestParam(name = "format", defaultValue = "png", required = false) String format,
                    @RequestParam(name = "content", defaultValue = "content") String content, HttpServletResponse response) throws Exception {

        ServletOutputStream out = response.getOutputStream();
        Map<EncodeHintType, Object> config = new HashMap<>();
        config.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        config.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        config.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, config);
        MatrixToImageWriter.writeToStream(bitMatrix, format, out);
    }

    @GetMapping("/barCode")
    public void getBarCode(@RequestParam(name = "width", defaultValue = "100", required = false) int width,
                           @RequestParam(name = "height", defaultValue = "100", required = false) int height,
                           @RequestParam(name = "format", defaultValue = "png", required = false) String format,
                           @RequestParam(name = "content", defaultValue = "content") String content, HttpServletResponse response) throws Exception {
        HashMap<EncodeHintType, Object> code = new HashMap<>(); //条形码参数
        code.put(EncodeHintType.CHARACTER_SET, "utf-8");//设置编码
        code.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//条形码的容错率，分为L,M,Q,H
        code.put(EncodeHintType.MARGIN, 0);//条形码的边框宽度
        BufferedImage i = MatrixToImageWriter.toBufferedImage(new Code128Writer().encode(content, BarcodeFormat.CODE_128, width, height, code));//生成二维码的图片
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(i, format, out);
    }


}
