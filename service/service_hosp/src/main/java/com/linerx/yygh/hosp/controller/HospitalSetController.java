package com.linerx.yygh.hosp.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.linerx.yygh.common.result.Result;
import com.linerx.yygh.hosp.service.HospitalSetService;
import com.linerx.yygh.model.hosp.HospitalSet;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
    private Result findAll(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    @ApiOperation(value = "删除医院设置")
    @DeleteMapping("{id}")
    private Result deleteHosp(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
}
