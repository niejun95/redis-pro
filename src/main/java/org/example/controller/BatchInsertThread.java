package org.example.controller;

import org.example.entities.Account;
import org.example.mapper.AccountMapper;

/**
 * @ClassName BatchInsertThread
 * @Author niejun
 * @Date 2022/6/21
 * @Description:
 * @Version 1.0
 **/
public class BatchInsertThread implements Runnable{

    public AccountMapper accountMapper;

    public int index;

    public BatchInsertThread(int index, AccountMapper accountMapper) {
        this.index = index;
        this.accountMapper = accountMapper;
    }

    @Override
    public void run() {
        Account account = new Account();
        account.setId(index);
        account.setName("ryan" + index);
        account.setMoney((100 + index));
        accountMapper.insertSingle(account);
    }
}
