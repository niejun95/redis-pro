package org.example.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entities.Account;
import org.example.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TestDBController
 * @Author niejun
 * @Date 2022/6/15
 * @Description: 数据库 测试
 * @Version 1.0
 **/
@RestController
@RequestMapping("/db")
public class TestDBController {
    public static final Logger log = LogManager.getLogger(TestDBController.class);

    public static ThreadPoolExecutor executors;

    static {
        int processors = Runtime.getRuntime().availableProcessors();
        executors = new ThreadPoolExecutor(processors, processors, 30, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100));
    }

    @Autowired
    private AccountMapper accountMapper;

    @RequestMapping("/userQ/{name}")
    public String queryUserInfoByName(@PathVariable String name) {
        log.info("name=" + name);
        Account account = accountMapper.queryAccountInfoByName(name);
        return JSONObject.toJSONString(account);
    }


    @RequestMapping("/batchInsert")
    public void batchInsert() {
        long startTime = System.currentTimeMillis();
        log.info("批量插入 开始时间戳：" + startTime);
        for (int i = 200; i < 250; i++) {
            BatchInsertThread thread = new BatchInsertThread(i, accountMapper);
            executors.submit(thread);
        }
        long endTime = System.currentTimeMillis();
        log.info("批量插入 结束时间戳：" + endTime);
        log.info("总耗时:" + (endTime - startTime));
    }

    @RequestMapping("/manyInsert")
    public void manyInsert() {
        long startTime = System.currentTimeMillis();
        log.info("many insert 开始时间戳：" + startTime);
        for (int index = 300; index < 350; index++) {
            Account account = new Account();
            account.setId(index);
            account.setName("ryan" + index);
            account.setMoney((100 + index));
            accountMapper.insertSingle(account);
        }
        long endTime = System.currentTimeMillis();
        log.info("many insert 结束时间戳：" + endTime);
        log.info("总耗时:" + (endTime - startTime));
    }

    @RequestMapping("/foreachInsert")
    public void foreachInsert() {
        long startTime = System.currentTimeMillis();
        log.info("many insert 开始时间戳：" + startTime);
        List<Account> list = new ArrayList<>();
        for (int index = 300; index < 350; index++) {
            Account account = new Account();
            account.setId(index);
            account.setName("ryan" + index);
            account.setMoney((100 + index));
            list.add(account);
        }
        accountMapper.insertMany(list);
        long endTime = System.currentTimeMillis();
        log.info("many insert 结束时间戳：" + endTime);
        log.info("总耗时:" + (endTime - startTime));
    }

    @RequestMapping("/deleteForDruidSQlWall")
    public Object delete() {
        log.info("测试 SQL 防火墙，配置了不允许删除");
        return accountMapper.deleteAccount(2);
    }
}
