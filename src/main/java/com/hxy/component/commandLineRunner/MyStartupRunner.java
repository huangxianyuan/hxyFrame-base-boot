package com.hxy.component.commandLineRunner;

import com.hxy.modules.common.cache.CodeCache;
import com.hxy.modules.common.common.Constant;
import com.hxy.modules.common.utils.RedisUtil;
import com.hxy.modules.common.utils.StringUtils;
import com.hxy.modules.sys.dao.CodeDao;
import com.hxy.modules.sys.entity.CodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类的功能描述.
 * 服务启动执行
 * @Auther hxy
 * @Date 2017/11/14
 */
@Component
public class MyStartupRunner implements CommandLineRunner {

    @Autowired
    private CodeDao codeDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("服务启动执行服务启动执行服务启动执行服务启动执行");
        CodeCache();
    }

    /**
     * 缓存全部数据字典
     */
    public void CodeCache(){
        List<CodeEntity> allCode = codeDao.queryAllCode();
        Map<String,Map<String,Object>> allMap = new HashMap<>();
        if(allCode != null && allCode.size()>0){
            Map<String,Object> attrMap =null;
            for (CodeEntity code:allCode){
                attrMap= new HashMap<>();
                attrMap.put("id",code.getId());
                attrMap.put("value",code.getValue());
                attrMap.put("childs",code.getChilds());
                attrMap.put("mark",code.getMark());
                attrMap.put("name",code.getName());
                allMap.put(code.getMark(),attrMap);
            }
        }
        for (String key:allMap.keySet()){
            //父字典
            Map<String, Object> parentMap = allMap.get(key);
            String childStr = (String) parentMap.get("childs");
            if(StringUtils.isEmpty(childStr)){
                continue;
            }
            String[] split = childStr.split(",");
            List<Map<String, Object>> childMaps = new ArrayList<>();

            for (String str:split){
                //子字典
                Map<String, Object> childMap = allMap.get(str);
                childMaps.add(childMap);
            }
            //将所有子字典设置到父级字典
            parentMap.put("childList",childMaps);
        }
        CodeCache.put(Constant.CODE_CACHE,allMap);
        try {
            redisUtil.setObject(Constant.CODE_CACHE,allMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
