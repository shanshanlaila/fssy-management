package com.fssy.shareholder.management.controller.system.sso;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.sso.SsoKey;
import com.fssy.shareholder.management.service.system.sso.SsoKeyService;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TerryZeng
 * @since 2022-11-27
 */
@Controller
@RequestMapping("/system/sso/")
public class SsoKeyController {

    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥

    @Autowired
    private SsoKeyService ssoKeyService;

    @RequiredLog("单点登录")
    @RequiresPermissions("system:sso:index")
    @GetMapping("index")
    public String ssoKeyIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "system/sso/sso-key-list";
    }

    /**
     * 获取单点登录密钥对
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getObjects(HttpServletRequest request) {

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        if (!ObjectUtils.isEmpty(request.getParameter("publicKey"))) {
            params.put("publicKey", request.getParameter("publicKey"));
        }

        Page<SsoKey> ssoKeyPage = ssoKeyService.
                findSsoKeyDataPerPageByParams(params);
        if (ssoKeyPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查到任何数据");
        } else {
            result.put("code", 0);
            result.put("count", ssoKeyPage.getTotal());
            result.put("data", ssoKeyPage.getRecords());
        }
        return result;
    }

    /**
     * 以主键删除密钥对，重新生成密钥对
     *
     * @param id
     * @return true/false
     */

    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult resetSsoKey(@PathVariable(value = "id") Integer id, String[] args) throws Exception {
        boolean resultDelete = ssoKeyService.deleteSsoKeyById(id);
        genKeyPair();
        SsoKey ssoKey = new SsoKey();
        ssoKey.setPrivateKey(keyMap.get(1));
        ssoKey.setPublicKey(keyMap.get(0));
        ssoKeyService.insertSsoKey(ssoKey);

        return SysResult.build(200, "重新生成密钥对成功，请复制新的公钥至主系统,以便实现单点登录");
    }


    /**
     * 密钥对生成方法
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
//        System.out.println("publicKeyString = " + publicKeyString);
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
//        System.out.println("privateKeyString = " + privateKeyString);
        keyMap.put(0, publicKeyString);  //0表示公钥
        keyMap.put(1, privateKeyString);  //1表示私钥
    }

}
