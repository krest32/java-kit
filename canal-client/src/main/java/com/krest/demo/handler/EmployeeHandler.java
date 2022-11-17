package com.krest.demo.handler;

import com.krest.demo.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

@CanalTable("employee")
@Component
@Slf4j
public class EmployeeHandler implements EntryHandler<Employee> {

    //把数据往Redis同步
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void insert(Employee employee) {
        redisTemplate.opsForValue().set("EMP:" + employee.getId(), employee);
    }

    @Override
    public void delete(Employee employee) {
        redisTemplate.delete("EMP:" + employee.getId());
    }

    @Override
    public void update(Employee before, Employee after) {
        redisTemplate.opsForValue().set("EMP:" + after.getId(), after);
    }
}

