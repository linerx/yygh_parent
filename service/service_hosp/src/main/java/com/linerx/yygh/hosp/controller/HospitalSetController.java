package com.linerx.yygh.hosp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linerx.yygh.common.result.Result;
import com.linerx.yygh.hosp.service.HospitalSetService;
import com.linerx.yygh.model.hosp.HospitalSet;
import com.linerx.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.sql.Wrapper;
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
    @ApiOperation(value = "分页查询")
    @PostMapping("getPageHospital/{current}/{limit}")
    private Result getPageHospital(@PathVariable Long current,@PathVariable Long limit,@RequestBody HospitalQueryVo hospitalQueryVo){
        Page<HospitalSet> page = new Page<>(current,limit);
        //构造查询条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper();
        String hosname = hospitalQueryVo.getHosname();
        String hoscode = hospitalQueryVo.getHoscode();
        if (StringUtils.isNotBlank(hosname)){
            wrapper.like("hosname",hospitalQueryVo.getHosname());
        }
        if (StringUtils.isNotBlank(hoscode)){
            wrapper.eq("hoscode",hospitalQueryVo.getHoscode());
        }
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page,wrapper);
        return Result.ok(hospitalSetPage);

    }
}
